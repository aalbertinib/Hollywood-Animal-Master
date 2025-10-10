package org.aalbertini.ham.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Utility functions for working with Compose Multiplatform resources.
 * 
 * Provides helper functions for common resource operations following
 * best practices and thread-safety guidelines.
 */

/**
 * Converts Dp to pixels based on current display density.
 * Useful for Canvas drawing operations.
 * 
 * @return The pixel value as Float
 */
@Composable
@ReadOnlyComposable
fun Dp.toPx(): Float {
    return with(LocalDensity.current) { this@toPx.toPx() }
}

/**
 * Converts pixels to Dp based on current display density.
 * 
 * @return The Dp value
 */
@Composable
@ReadOnlyComposable
fun Float.toDp(): Dp {
    return with(LocalDensity.current) { this@toDp.toDp() }
}

/**
 * Helper function to get a string resource.
 * Note: Compose Multiplatform's stringResource() is already safe and handles missing resources.
 * 
 * @param resource The string resource
 * @return Resolved string
 */
@Composable
fun safeStringResource(
    resource: StringResource
): String {
    return stringResource(resource)
}

/**
 * Helper function to get a formatted string resource.
 * Note: Compose Multiplatform's stringResource() is already safe and handles missing resources.
 * 
 * @param resource The string resource with format specifiers
 * @param formatArgs Arguments to format the string
 * @return Formatted string
 */
@Composable
fun safeFormattedStringResource(
    resource: StringResource,
    vararg formatArgs: Any
): String {
    return stringResource(resource, *formatArgs)
}

/**
 * Extension function to get a string resource conditionally.
 * Returns null if condition is false, making it easier to use with optional text.
 * 
 * Usage:
 * ```
 * val errorMessage = condition.takeIf { it }?.let { 
 *     stringResource(Strings.errorMessage) 
 * }
 * ```
 */
@Composable
inline fun <T> T?.ifNotNull(block: @Composable (T) -> String): String? {
    return this?.let { block(it) }
}

/**
 * Data class to hold resource state for ViewModels.
 * Since ViewModels cannot directly access @Composable functions,
 * use this to pass resource references.
 * 
 * Usage in ViewModel:
 * ```
 * sealed class UiState {
 *     data class Error(val messageResource: ResourceState.Text) : UiState()
 * }
 * 
 * // In ViewModel
 * _uiState.value = UiState.Error(
 *     ResourceState.Text.Resource(Strings.errorNetwork)
 * )
 * ```
 * 
 * Usage in UI:
 * ```
 * when (val state = viewModel.uiState) {
 *     is UiState.Error -> Text(state.messageResource.resolve())
 * }
 * ```
 */
sealed class ResourceState {
    /**
     * Represents text that can come from either a resource or a plain string.
     */
    sealed class Text : ResourceState() {
        /**
         * Text from a string resource.
         */
        data class Resource(val resource: StringResource) : Text()
        
        /**
         * Text from a plain string (e.g., from API response).
         */
        data class Plain(val text: String) : Text()
        
        /**
         * Formatted text from a resource with arguments.
         */
        data class Formatted(
            val resource: StringResource,
            val formatArgs: List<Any>
        ) : Text()
        
        /**
         * Resolves the text to a string within a @Composable context.
         */
        @Composable
        fun resolve(): String = when (this) {
            is Resource -> stringResource(resource)
            is Plain -> text
            is Formatted -> stringResource(resource, *formatArgs.toTypedArray())
        }
    }
}

/**
 * Extension function to create a ResourceState.Text from a StringResource.
 */
fun StringResource.asResourceText(): ResourceState.Text =
    ResourceState.Text.Resource(this)

/**
 * Extension function to create a ResourceState.Text from a String.
 */
fun String.asPlainText(): ResourceState.Text =
    ResourceState.Text.Plain(this)

/**
 * Extension function to create a formatted ResourceState.Text.
 */
fun StringResource.asFormattedText(vararg formatArgs: Any): ResourceState.Text =
    ResourceState.Text.Formatted(this, formatArgs.toList())

/**
 * Helper object for common resource operations that need to be thread-safe.
 */
object ResourceHelper {
    
    /**
     * Validates if a resource name follows naming conventions.
     * Resource names should use lowercase with underscores.
     * 
     * @param name The resource name to validate
     * @return true if valid, false otherwise
     */
    fun isValidResourceName(name: String): Boolean {
        return name.matches(Regex("^[a-z][a-z0-9_]*$"))
    }
    
    /**
     * Converts a camelCase name to snake_case for resource naming.
     * 
     * Example: "myResourceName" -> "my_resource_name"
     */
    fun camelToSnakeCase(camelCase: String): String {
        return camelCase
            .replace(Regex("([a-z])([A-Z])"), "$1_$2")
            .lowercase()
    }
    
    /**
     * Converts snake_case to camelCase for Kotlin property naming.
     * 
     * Example: "my_resource_name" -> "myResourceName"
     */
    fun snakeToCamelCase(snakeCase: String): String {
        return snakeCase
            .split('_')
            .mapIndexed { index, part ->
                if (index == 0) part else part.replaceFirstChar { it.uppercase() }
            }
            .joinToString("")
    }
}

/**
 * Example usage in a ViewModel showing best practices.
 */
class ExampleViewModel {
    // Store resource references, not resolved strings
    private val titleResource = Strings.appName
    private val errorResource = Strings.errorInvalidInput
    
    // Use ResourceState for dynamic messages
    fun getErrorState(code: Int): ResourceState.Text = when (code) {
        404 -> Strings.errorLoadFailed.asResourceText()
        500 -> Strings.errorNetwork.asResourceText()
        else -> "Unknown error: $code".asPlainText()
    }
    
    // For formatted messages with parameters
    fun getAnimalCountMessage(count: Int): ResourceState.Text =
        Strings.totalAnimals.asFormattedText(count)
}
