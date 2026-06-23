package com.raban.etabli.tex.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class TeXCommand(
    val command: String,
    val name: String,
    val preview: String,
    val category: String,
    val pkg: String?,
    val usage: String?,
)

data class TeXSnippet(
    val title: String,
    val category: String,
    val code: String,
)

data class TeXDictionary(
    val version: Int,
    val categories: List<String>,
    val entries: List<TeXCommand>,
    val snippets: List<TeXSnippet>,
) {
    companion object {
        fun loadFromAssets(ctx: Context): TeXDictionary {
            val raw = ctx.assets.open("Dictionary.json")
                .bufferedReader().use { it.readText() }
            val root = JSONObject(raw)
            return TeXDictionary(
                version = root.optInt("version", 1),
                categories = root.getJSONArray("categories").toStringList(),
                entries = root.getJSONArray("entries").mapItems { o ->
                    TeXCommand(
                        command = o.getString("command"),
                        name = o.getString("name"),
                        preview = o.getString("preview"),
                        category = o.getString("category"),
                        pkg = o.optString("package", "").ifBlank { null },
                        usage = o.optString("usage", "").ifBlank { null },
                    )
                },
                snippets = root.getJSONArray("snippets").mapItems { o ->
                    TeXSnippet(
                        title = o.getString("title"),
                        category = o.getString("category"),
                        code = o.getString("code"),
                    )
                },
            )
        }
    }
}

private fun JSONArray.toStringList(): List<String> =
    (0 until length()).map { getString(it) }

private fun <T> JSONArray.mapItems(map: (JSONObject) -> T): List<T> =
    (0 until length()).map { map(getJSONObject(it)) }
