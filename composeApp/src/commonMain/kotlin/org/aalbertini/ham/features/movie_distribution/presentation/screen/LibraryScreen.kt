package org.aalbertini.ham.features.movie_distribution.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.action_delete
import hollywoodanimalmaster.composeapp.generated.resources.action_duplicate
import hollywoodanimalmaster.composeapp.generated.resources.action_more
import hollywoodanimalmaster.composeapp.generated.resources.library_current
import hollywoodanimalmaster.composeapp.generated.resources.library_empty_body
import hollywoodanimalmaster.composeapp.generated.resources.library_empty_import
import hollywoodanimalmaster.composeapp.generated.resources.library_empty_title
import hollywoodanimalmaster.composeapp.generated.resources.library_item_supporting
import hollywoodanimalmaster.composeapp.generated.resources.library_no_results
import hollywoodanimalmaster.composeapp.generated.resources.library_open_tag
import hollywoodanimalmaster.composeapp.generated.resources.library_search
import hollywoodanimalmaster.composeapp.generated.resources.untitled_movie
import org.aalbertini.ham.core.util.format.grouped
import org.aalbertini.ham.core.util.format.toPlainString
import org.aalbertini.ham.features.movie_distribution.domain.calculator.DistributionPlanner
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.features.movie_distribution.presentation.components.Tag
import org.aalbertini.ham.features.movie_distribution.presentation.components.titleRes
import org.jetbrains.compose.resources.stringResource

@Composable
fun LibraryScreen(
    movies: List<MovieResult>,
    currentId: String,
    onOpen: (String) -> Unit,
    onDuplicate: (String) -> Unit,
    onDelete: (String) -> Unit,
    onImport: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (movies.isEmpty()) {
        EmptyState(
            title = stringResource(Res.string.library_empty_title),
            body = stringResource(Res.string.library_empty_body),
            onImport = onImport,
            modifier = modifier,
        )
        return
    }
    var query by rememberSaveable { mutableStateOf("") }
    val filtered = remember(movies, query) {
        movies.filter { query.isBlank() || it.title.contains(query.trim(), ignoreCase = true) }
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item(key = "search") {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text(stringResource(Res.string.library_search)) },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().widthIn(max = 720.dp),
            )
        }
        if (filtered.isEmpty()) {
            item(key = "no-results") {
                Text(
                    stringResource(Res.string.library_no_results, query.trim()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(24.dp),
                )
            }
        }
        items(filtered, key = { it.id }) { movie ->
            LibraryItem(
                movie = movie,
                isCurrent = movie.id == currentId,
                onOpen = { onOpen(movie.id) },
                onDuplicate = { onDuplicate(movie.id) },
                onDelete = { onDelete(movie.id) },
                modifier = Modifier.widthIn(max = 720.dp),
            )
        }
    }
}

@Composable
private fun LibraryItem(
    movie: MovieResult,
    isCurrent: Boolean,
    onOpen: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val totalRent = remember(movie) { DistributionPlanner.plan(movie).totalRent }
    val containerColor = if (isCurrent) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerLow
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = MaterialTheme.shapes.large,
    ) {
        ListItem(
            modifier = Modifier.clickable(
                onClickLabel = stringResource(Res.string.library_current),
                role = Role.Button,
                onClick = onOpen,
            ),
            colors = ListItemDefaults.colors(containerColor = containerColor),
            leadingContent = { Icon(Icons.Outlined.Movie, contentDescription = null) },
            headlineContent = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        movie.title.ifBlank { stringResource(Res.string.untitled_movie) },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    // The tint alone doesn't say which one is open.
                    if (isCurrent) Tag(stringResource(Res.string.library_open_tag), emphasized = true)
                }
            },
            supportingContent = {
                Text(
                    stringResource(
                        Res.string.library_item_supporting,
                        movie.commercialScore.toPlainString(1),
                        stringResource(movie.method.titleRes),
                        totalRent.grouped(),
                    )
                )
            },
            trailingContent = {
                Box {
                    var menuOpen by remember { mutableStateOf(false) }
                    IconButton(onClick = { menuOpen = true }) {
                        Icon(Icons.Outlined.MoreVert, contentDescription = stringResource(Res.string.action_more))
                    }
                    DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.action_duplicate)) },
                            leadingIcon = { Icon(Icons.Outlined.ContentCopy, contentDescription = null) },
                            onClick = { menuOpen = false; onDuplicate() },
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.action_delete)) },
                            leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
                            onClick = { menuOpen = false; onDelete() },
                        )
                    }
                }
            },
        )
    }
}

@Composable
private fun EmptyState(title: String, body: String, onImport: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.widthIn(max = 360.dp),
        ) {
            Icon(
                Icons.Outlined.VideoLibrary,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(title, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            Text(
                body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            OutlinedButton(onClick = onImport) {
                Icon(Icons.Outlined.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                Text(stringResource(Res.string.library_empty_import), modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}
