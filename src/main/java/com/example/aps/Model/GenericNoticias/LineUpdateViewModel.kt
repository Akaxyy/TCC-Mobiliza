package com.example.aps.Model.GenericNoticias

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime



@RequiresApi(Build.VERSION_CODES.O)
class LineUpdateViewModel(private val repository: LineUpdateRepository) : ViewModel() {

    private val _state = MutableStateFlow(LineUpdatesState())
    val state: StateFlow<LineUpdatesState> = _state

    private val SAO_PAULO_ZONE = ZoneId.of("America/Sao_Paulo")

    fun fetchUpdates(lineId: Int) {
        if (_state.value.isLoading) return

        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val updates = repository.getUpdatesForLine(lineId)

                val now = ZonedDateTime.now(SAO_PAULO_ZONE)

                val threeDaysAgoStart = now.minusDays(2)
                    .toLocalDate()
                    .atStartOfDay(SAO_PAULO_ZONE)

                val filteredUpdates = updates.filter { update ->
                    try {

                        val updateTime = OffsetDateTime.parse(update.timestamp)
                            .toInstant()
                            .atZone(SAO_PAULO_ZONE)

                        updateTime.isAfter(threeDaysAgoStart) || updateTime.isEqual(threeDaysAgoStart)
                    } catch (e: Exception) {
                        false
                    }
                }

                // 3. Agrupa e ordena
                val updatesGrouped = groupByDay(filteredUpdates)

                _state.update {
                    it.copy(
                        updatesByDay = updatesGrouped,
                        legend = StatusType.entries.toList(),
                        isLoading = false,
                        error = null
                    )
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Erro ao carregar status da linha: ${e.localizedMessage}"
                    )
                }
            }
        }
    }


    private fun groupByDay(updates: List<StatusUpdate>): List<UpdateSection> {
        val now = ZonedDateTime.now(SAO_PAULO_ZONE).toLocalDate()

        return updates
            .groupBy { update ->

                val updateDate = OffsetDateTime.parse(update.timestamp)
                    .atZoneSameInstant(SAO_PAULO_ZONE)
                    .toLocalDate()

                when {
                    updateDate.isEqual(now) -> "Hoje"
                    updateDate.isEqual(now.minusDays(1)) -> "Ontem"
                    updateDate.isEqual(now.minusDays(2)) -> "Anteontem"
                    else -> "Outros"
                }
            }
            .filter { it.key != "Outros" }
            .map { (day, list) ->

                val sortedList = list.sortedByDescending {
                    OffsetDateTime.parse(it.timestamp).toInstant().toEpochMilli()
                }
                UpdateSection(day, sortedList)
            }
            .sortedBy {
                when (it.title) {
                    "Hoje" -> 1
                    "Ontem" -> 2
                    "Anteontem" -> 3
                    else -> 99
                }
            }
    }
}