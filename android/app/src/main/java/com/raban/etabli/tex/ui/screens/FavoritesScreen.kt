package com.raban.etabli.tex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raban.etabli.tex.EtabliTeXApplication
import com.raban.etabli.tex.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(app: EtabliTeXApplication) {
    val t = Coder.tokens
    val scope = rememberCoroutineScope()
    val favorites by app.favorites.flow.collectAsState(initial = emptySet())
    val items = remember(favorites, app.dictionary) {
        app.dictionary.entries.filter { it.command in favorites }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(t.color.paper).padding(t.space.lg),
        verticalArrangement = Arrangement.spacedBy(t.space.md),
    ) {
        PromptHeader(listOf("favorites", items.size.toString()))
        if (items.isEmpty()) {
            Card(title = "empty", icon = Icons.Default.Star) {
                MonoLabel("tap the star on any command to save it here.", color = t.color.faint)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(t.space.sm)) {
                items(items, key = { it.command }) { cmd ->
                    Card(title = cmd.command, icon = Icons.Default.Star) {
                        Text(cmd.name, style = t.font.body)
                        if (cmd.preview.isNotBlank()) CodeBlock(cmd.preview)
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()) {
                            MonoLabel(cmd.category.lowercase(), color = t.color.faint)
                            Spacer(Modifier.weight(1f))
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "remove",
                                tint = t.color.accent,
                                modifier = Modifier.size(20.dp).clickable {
                                    scope.launch { app.favorites.toggle(cmd.command) }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
