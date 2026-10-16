package org.arkansasio.freeorion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.arkansasio.freeorion.api.ApiResult
import org.arkansasio.freeorion.api.FreeOrionApi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { FreeOrionHome() } }
    }
}

@Composable
private fun FreeOrionHome() {
    val api = remember { FreeOrionApi() }
    val scope = rememberCoroutineScope()
    var health by remember { mutableStateOf<ApiResult?>(null) }
    var info by remember { mutableStateOf<ApiResult?>(null) }
    var busy by remember { mutableStateOf(false) }

    fun refresh() {
        scope.launch {
            busy = true
            health = api.health()
            info = api.gameInfo()
            busy = false
        }
    }

    LaunchedEffect(Unit) { refresh() }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("FREEORION", style = MaterialTheme.typography.headlineLarge)
            Text("Galactic strategy • Android companion", style = MaterialTheme.typography.titleMedium)
            Text("Explore API status and project metadata. This companion does not currently run the native game engine.")
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("API CONNECTION", style = MaterialTheme.typography.titleMedium)
                    if (busy) CircularProgressIndicator()
                    Text(health?.let { if (it.ok) "Connected: ${it.message}" else "Offline: ${it.message}" } ?: "Checking…")
                    Button(onClick = { refresh() }, enabled = !busy) { Text("Refresh connection") }
                }
            }
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("GAME INFORMATION", style = MaterialTheme.typography.titleMedium)
                    val json = info?.body
                    Text("Name: ${json?.optString("name", "FreeOrion") ?: "FreeOrion"}")
                    Text("Genre: ${json?.optString("genre", "4X turn-based strategy") ?: "4X turn-based strategy"}")
                    Text("Integration: ${json?.optString("integration", "Companion metadata API") ?: "Companion metadata API"}")
                    info?.let { Text(if (it.ok) "Metadata: available" else "Metadata API: ${it.message}") }
                }
            }
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("NEXT STEPS", style = MaterialTheme.typography.titleMedium)
                    Text("• Run the REST service documented in android-api/server.")
                    Text("• For a real device, set the API URL to your server's HTTPS address.")
                    Text("• The separate upstream Android game export remains in the repository's Android workflow.")
                }
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}
