package es.joshluq.pluginkit.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {
    @Serializable
    data object Home : Destination
    
    @Serializable
    data class Details(val id: String) : Destination
}
