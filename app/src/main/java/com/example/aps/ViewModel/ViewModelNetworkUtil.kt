import android.util.Log
import com.example.aps.Model.StatusLinhas
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// Função para buscar o status
suspend fun buscarStatus(linha:String): StatusLinhas {


    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging){
            level = LogLevel.ALL
        }

    }
    val response: StatusLinhas = client.get("http://10.0.2.2:3000").body()
    client.close()
    return response

}