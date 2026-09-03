package es.joshluq.pluginkit

import javax.inject.Inject

interface StringProvider {
    fun getString(): String
}

class StringProviderImpl
    @Inject
    constructor() : StringProvider {
        override fun getString(): String = "Hello from Hilt Injected Dependency!"
    }
