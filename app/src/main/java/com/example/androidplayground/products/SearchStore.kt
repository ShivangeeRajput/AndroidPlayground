package com.example.androidplayground.products

import android.content.Context

// Storage: last search yaad rakhta hai. Interface isliye taaki test mein fake de sakein.
interface SearchStore {
    var lastQuery: String
}

class PrefsSearchStore(context: Context) : SearchStore {
    private val prefs = context.getSharedPreferences("search", Context.MODE_PRIVATE)

    override var lastQuery: String
        get() = prefs.getString("query", "") ?: ""
        set(value) = prefs.edit().putString("query", value).apply()
}
