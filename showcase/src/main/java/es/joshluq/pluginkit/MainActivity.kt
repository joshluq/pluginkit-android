package es.joshluq.pluginkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import es.joshluq.pluginkit.ui.theme.PluginkitTheme
import javax.inject.Inject
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.ui.NavDisplay
import es.joshluq.pluginkit.navigation.Destination
import es.joshluq.pluginkit.navigation.ShowcaseEntryProvider

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var stringProvider: StringProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            PluginkitTheme {
                val backstack = remember { mutableStateListOf<Destination>(Destination.Home) }
                val entryProvider = remember {
                    ShowcaseEntryProvider(
                        onNavigateToDetails = { id -> backstack.add(Destination.Details(id)) },
                        onBack = { if (backstack.size > 1) backstack.removeAt(backstack.size - 1) }
                    )
                }

                NavDisplay(
                    backStack = backstack,
                    entryProvider = entryProvider,
                    onBack = { if (backstack.size > 1) backstack.removeAt(backstack.size - 1) },
                    popTransitionSpec = {
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                    },
                    predictivePopTransitionSpec = {
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                    }
                )
            }
        }
    }
}