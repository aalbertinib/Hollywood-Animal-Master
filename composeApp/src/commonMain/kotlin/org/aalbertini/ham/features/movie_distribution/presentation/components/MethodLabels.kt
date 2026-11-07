package org.aalbertini.ham.features.movie_distribution.presentation.components

import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.method_custom
import hollywoodanimalmaster.composeapp.generated.resources.method_custom_desc
import hollywoodanimalmaster.composeapp.generated.resources.method_fast_decay
import hollywoodanimalmaster.composeapp.generated.resources.method_fast_decay_desc
import hollywoodanimalmaster.composeapp.generated.resources.method_steam_guide
import hollywoodanimalmaster.composeapp.generated.resources.method_steam_guide_desc
import hollywoodanimalmaster.composeapp.generated.resources.method_two_thirds
import hollywoodanimalmaster.composeapp.generated.resources.method_two_thirds_desc
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionMethod
import org.jetbrains.compose.resources.StringResource

val DistributionMethod.titleRes: StringResource
    get() = when (this) {
        DistributionMethod.STEAM_GUIDE -> Res.string.method_steam_guide
        DistributionMethod.FAST_DECAY -> Res.string.method_fast_decay
        DistributionMethod.TWO_THIRDS -> Res.string.method_two_thirds
        DistributionMethod.CUSTOM -> Res.string.method_custom
    }

/** Where the method comes from; `null` for the user's own curve. */
val DistributionMethod.sourceUrl: String?
    get() = when (this) {
        DistributionMethod.STEAM_GUIDE -> "https://steamcommunity.com/sharedfiles/filedetails/?id=3582118497"
        DistributionMethod.FAST_DECAY -> "https://steamcommunity.com/app/2680550/discussions/0/801217828677031390/"
        DistributionMethod.TWO_THIRDS -> "https://steamcommunity.com/app/2680550/discussions/0/693123052112765636/"
        DistributionMethod.CUSTOM -> null
    }

val DistributionMethod.descriptionRes: StringResource
    get() = when (this) {
        DistributionMethod.STEAM_GUIDE -> Res.string.method_steam_guide_desc
        DistributionMethod.FAST_DECAY -> Res.string.method_fast_decay_desc
        DistributionMethod.TWO_THIRDS -> Res.string.method_two_thirds_desc
        DistributionMethod.CUSTOM -> Res.string.method_custom_desc
    }
