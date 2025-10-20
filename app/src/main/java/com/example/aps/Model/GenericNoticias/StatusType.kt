import androidx.compose.ui.graphics.Color
import com.example.aps.Model.GenericNoticias.UpdateSection
import java.time.LocalDate





enum class StatusType(val description: String, val color: Color) {
    NORMAL("Tudo funcionando sem problemas", Color(0xFF00C853)), // Verde
    REDUCED_SPEED("Operando com ajustes ou velocidade menor", Color(0xFFFFC107)), // Amarelo
    SUSPENDED("Serviço suspenso ou com falhas", Color(0xFFD32F2F)), // Vermelho
    PROGRAMMED_ACTIVITY("Atividade Programada", Color(0xFFFFC107)), // Amarelo
    SERVICE_ENDED("Operação Encerrada", Color(0xFFD32F2F)) // Vermelho
}



data class LineUpdatesState(
    val updatesByDay: List<UpdateSection> = emptyList(),
    val legend: List<StatusType> = StatusType.entries.toList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

