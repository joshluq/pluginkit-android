package es.joshluq.pluginkit.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry

class ShowcaseEntryProvider(
  private val onNavigateToDetails: (String) -> Unit,
  private val onBack: () -> Unit,
) : (Destination) -> NavEntry<Destination> {

  override fun invoke(key: Destination): NavEntry<Destination> =
    when (key) {
      Destination.Home ->
        NavEntry(
          key = key,
          content = { HomeScreen(onNavigateToDetails) },
        )
      is Destination.Details ->
        NavEntry(
          key = key,
          content = { DetailsScreen(key.id, onBack) },
        )
    }
}

@Composable
fun HomeScreen(onNavigateToDetails: (String) -> Unit) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(text = "Home Screen")
    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = { onNavigateToDetails("Example ID") }) {
      Text("Go to Details")
    }
  }
}

@Composable
fun DetailsScreen(id: String, onBack: () -> Unit) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(text = "Details Screen for ID: $id")
    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = onBack) {
      Text("Back Home")
    }
  }
}
