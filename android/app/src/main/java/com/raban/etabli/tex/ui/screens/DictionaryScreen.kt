package com.raban.etabli.tex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raban.etabli.tex.EtabliTeXApplication
import com.raban.etabli.tex.data.TeXCommand
import com.raban.etabli.tex.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun DictionaryScreen(app: EtabliTeXApplication) {
    val t = Coder.tokens
    val scope = rememberCoroutineScope()
    val favorites by app.favorites.flow.collectAsState(initial = emptySet())
    var query by remember { mutableStateOf("") }
    var category by remember { mutableStateOf<String?>(null) }

    val filtered = remember(query, category, app.dictionary) {
        app.dictionary.entries.filter { cmd ->
            (category == null || cmd.category == category) &&
            (query.isBlank() ||
                cmd.command.contains(query, true) ||
                cmd.name.contains(query, true) ||
                cmd.preview.contains(query, true))
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(t.color.paper).padding(t.space.lg),
        verticalArrangement = Arrangement.spacedBy(t.space.md),
    ) {
        PromptHeader(listOf("dictionary", "${filtered.size} of ${app.dictionary.entries.size}"))
        TextInput(value = query, placeholder = "search commands…", onChange = { query = it })
        if (app.loadError != null) {
            MonoLabel("⚠ ${app.loadError}", color = t.color.danger)
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(t.space.sm)) {
            item {
                Chip(label = "all", selected = category == null) { category = null }
            }
            items(app.dictionary.categories) { cat ->
                Chip(label = cat.lowercase(), selected = category == cat) {
                    category = if (category == cat) null else cat
                }
            }
        }
        if (filtered.isEmpty()) {
            EmptyState("No commands match.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(t.space.sm)) {
                items(filtered, key = { it.command }) { cmd ->
                    CommandRow(
                        cmd = cmd,
                        favorited = cmd.command in favorites,
                        onToggleFav = { scope.launch { app.favorites.toggle(cmd.command) } },
                    )
                }
            }
        }
    }
}

@Composable
private fun CommandRow(cmd: TeXCommand, favorited: Boolean, onToggleFav: () -> Unit) {
    val t = Coder.tokens
    Card(title = cmd.command, icon = Icons.Default.Book) {
        Text(cmd.name, style = t.font.body)
        if (cmd.preview.isNotBlank()) {
            CodeBlock(cmd.preview)
        }
        cmd.usage?.let { MonoLabel("usage: $it", color = t.color.faint) }
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(t.space.sm),
            modifier = Modifier.fillMaxWidth()) {
            MonoLabel(cmd.category.lowercase(), color = t.color.faint)
            cmd.pkg?.let {
                MonoLabel("· $it", color = t.color.faint)
            }
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = if (favorited) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "favorite",
                tint = if (favorited) t.color.accent else t.color.faint,
                modifier = Modifier.size(20.dp).clickable(onClick = onToggleFav),
            )
        }
    }
}
