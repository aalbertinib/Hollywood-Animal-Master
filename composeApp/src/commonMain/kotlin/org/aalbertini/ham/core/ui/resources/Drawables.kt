package org.aalbertini.ham.core.ui.resources

import hollywoodanimalmaster.composeapp.generated.resources.Res
import hollywoodanimalmaster.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.DrawableResource

/**
 * Centralized drawable resources for the Hollywood Animal Master application.
 * 
 * This object provides type-safe access to all drawable resources using Compose Multiplatform's
 * generated resources API (1.9.0). All drawables are located in composeResources/drawable/
 * and automatically generated at build time.
 * 
 * Usage:
 * ```
 * Image(
 *     painter = painterResource(Drawables.logo),
 *     contentDescription = stringResource(Strings.contentDescriptionLogo)
 * )
 * ```
 */
object Drawables {
    val composeMultiplatform: DrawableResource get() = Res.drawable.compose_multiplatform
    
    // Add more drawable references as you add them to composeResources/drawable/
    // Example:
    // val logo: DrawableResource get() = Res.drawable.logo
    // val iconAnimal: DrawableResource get() = Res.drawable.icon_animal
    // val backgroundPattern: DrawableResource get() = Res.drawable.background_pattern
}
