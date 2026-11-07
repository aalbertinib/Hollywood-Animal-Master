package org.aalbertini.ham.features.guide

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.guide_best_body
import hollywoodanimalmaster.composeapp.generated.resources.guide_best_title
import hollywoodanimalmaster.composeapp.generated.resources.guide_disclaimer
import hollywoodanimalmaster.composeapp.generated.resources.guide_method_custom
import hollywoodanimalmaster.composeapp.generated.resources.guide_method_fast
import hollywoodanimalmaster.composeapp.generated.resources.guide_method_steam
import hollywoodanimalmaster.composeapp.generated.resources.guide_method_two_thirds
import hollywoodanimalmaster.composeapp.generated.resources.guide_methods_title
import hollywoodanimalmaster.composeapp.generated.resources.guide_patch_0505
import hollywoodanimalmaster.composeapp.generated.resources.guide_patch_067
import hollywoodanimalmaster.composeapp.generated.resources.guide_patch_071
import hollywoodanimalmaster.composeapp.generated.resources.guide_patch_073
import hollywoodanimalmaster.composeapp.generated.resources.guide_patch_act2
import hollywoodanimalmaster.composeapp.generated.resources.guide_patch_title
import hollywoodanimalmaster.composeapp.generated.resources.guide_patch_xmas
import hollywoodanimalmaster.composeapp.generated.resources.guide_routine_title
import hollywoodanimalmaster.composeapp.generated.resources.guide_sources_title
import hollywoodanimalmaster.composeapp.generated.resources.guide_step_1
import hollywoodanimalmaster.composeapp.generated.resources.guide_step_2
import hollywoodanimalmaster.composeapp.generated.resources.guide_step_3
import hollywoodanimalmaster.composeapp.generated.resources.guide_step_4
import hollywoodanimalmaster.composeapp.generated.resources.source_attendance
import hollywoodanimalmaster.composeapp.generated.resources.source_judge_1
import hollywoodanimalmaster.composeapp.generated.resources.source_judge_2
import hollywoodanimalmaster.composeapp.generated.resources.source_news
import hollywoodanimalmaster.composeapp.generated.resources.source_nosavynada
import hollywoodanimalmaster.composeapp.generated.resources.source_someguy
import org.aalbertini.ham.core.ui.components.SectionCard
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionMethod
import org.aalbertini.ham.features.movie_distribution.presentation.components.sourceUrl
import org.aalbertini.ham.features.movie_distribution.presentation.components.titleRes
import hollywoodanimalmaster.composeapp.generated.resources.action_open_source
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private val Sources: List<Pair<StringResource, String>> = listOf(
    Res.string.source_someguy to "https://steamcommunity.com/sharedfiles/filedetails/?id=3582118497",
    Res.string.source_judge_1 to "https://steamcommunity.com/app/2680550/discussions/0/801217828677031390/",
    Res.string.source_judge_2 to "https://steamcommunity.com/app/2680550/discussions/0/562535204979238875/",
    Res.string.source_nosavynada to "https://steamcommunity.com/app/2680550/discussions/0/693123052112765636/",
    Res.string.source_attendance to "https://steamcommunity.com/app/2680550/discussions/0/759555730939319344/",
    Res.string.source_news to "https://store.steampowered.com/news/app/2680550",
)

@Composable
fun GuideScreen(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    val itemModifier = Modifier.widthIn(max = 720.dp)
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            SectionCard(stringResource(Res.string.guide_best_title), itemModifier) {
                Body(stringResource(Res.string.guide_best_body))
            }
        }
        item {
            SectionCard(stringResource(Res.string.guide_routine_title), itemModifier) {
                listOf(Res.string.guide_step_1, Res.string.guide_step_2, Res.string.guide_step_3, Res.string.guide_step_4)
                    .forEachIndexed { index, step -> Step(index + 1, stringResource(step)) }
            }
        }
        item {
            SectionCard(stringResource(Res.string.guide_methods_title), itemModifier) {
                listOf(
                    DistributionMethod.FAST_DECAY to Res.string.guide_method_fast,
                    DistributionMethod.STEAM_GUIDE to Res.string.guide_method_steam,
                    DistributionMethod.TWO_THIRDS to Res.string.guide_method_two_thirds,
                    DistributionMethod.CUSTOM to Res.string.guide_method_custom,
                ).forEach { (method, body) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            stringResource(method.titleRes),
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.weight(1f),
                        )
                        method.sourceUrl?.let { url ->
                            TextButton(onClick = { uriHandler.openUri(url) }) {
                                Icon(Icons.Outlined.Link, contentDescription = null, modifier = Modifier.size(18.dp))
                                Text(stringResource(Res.string.action_open_source), modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                    Body(stringResource(body))
                }
            }
        }
        item {
            SectionCard(stringResource(Res.string.guide_patch_title), itemModifier) {
                listOf(
                    Res.string.guide_patch_act2,
                    Res.string.guide_patch_073,
                    Res.string.guide_patch_071,
                    Res.string.guide_patch_067,
                    Res.string.guide_patch_xmas,
                    Res.string.guide_patch_0505,
                ).forEach { Body(stringResource(it)) }
            }
        }
        item {
            SectionCard(stringResource(Res.string.guide_sources_title), itemModifier) {
                Sources.forEach { (label, url) ->
                    TextButton(onClick = { uriHandler.openUri(url) }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Outlined.Link, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text(stringResource(label), modifier = Modifier.weight(1f).padding(start = 8.dp))
                    }
                }
                Text(
                    stringResource(Res.string.guide_disclaimer),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun Body(text: String) {
    Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
private fun Step(number: Int, text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(28.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    "$number",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        Text(text, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
    }
}
