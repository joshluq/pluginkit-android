package es.joshluq.pluginkit.mylibrary

class GreetingProvider {

    companion object {
        const val GREETING = "Hello from MyLibrary using PluginKit!"
    }
    fun getGreeting(): String {
        return GREETING
    }
}
