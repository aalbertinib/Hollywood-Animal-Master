package org.aalbertini.ham.resources

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.aalbertini.ham.ui.theme.HAMTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Example composable demonstrating best practices for using Compose Multiplatform resources.
 * 
 * This file serves as a reference for:
 * - Accessing string resources with stringResource()
 * - Using drawable resources with painterResource()
 * - Applying theme colors and dimensions
 * - Formatting strings with parameters
 * - Thread-safe resource access
 * 
 * Best Practices Demonstrated:
 * 1. Always use resource wrappers (Strings, Colors, Dimensions) for type safety
 * 2. Access resources within @Composable context
 * 3. Provide content descriptions for accessibility
 * 4. Use semantic color names from theme, not raw Colors object
 * 5. Apply consistent spacing using Dimensions
 */
@Composable
private fun ResourcesExample() {
    HAMTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimensions.screenPaddingHorizontal),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacing4)
            ) {
                // Example 1: Simple string resource
                Text(
                    text = stringResource(Strings.appName),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(Dimensions.spacing6))
                
                // Example 2: Image with content description
                Image(
                    painter = painterResource(Drawables.composeMultiplatform),
                    contentDescription = stringResource(Strings.contentDescriptionLogo),
                    modifier = Modifier.size(Dimensions.iconSizeXLarge)
                )
                
                Spacer(modifier = Modifier.height(Dimensions.spacing6))
                
                // Example 3: Formatted string resource with parameter
                val animalCount = 42
                Text(
                    text = stringResource(Strings.totalAnimals, animalCount),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(Dimensions.spacing4))
                
                // Example 4: Card with custom styling using dimensions and colors
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimensions.spacing2),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = Dimensions.elevationLevel2
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(Dimensions.cardPadding)
                    ) {
                        Text(
                            text = stringResource(Strings.parametersSectionTitle),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(Dimensions.spacing2))
                        
                        Text(
                            text = stringResource(Strings.noResults),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(Dimensions.spacing6))
                
                // Example 5: Button with string resource
                Button(
                    onClick = { /* Handle click */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.buttonHeight),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = stringResource(Strings.calculateButton),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                
                Spacer(modifier = Modifier.height(Dimensions.spacing2))
                
                // Example 6: Outlined button variant
                OutlinedButton(
                    onClick = { /* Handle click */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.buttonHeight),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = stringResource(Strings.resetButton),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                
                Spacer(modifier = Modifier.height(Dimensions.spacing4))
                
                // Example 7: Divider with custom thickness
                HorizontalDivider(
                    thickness = Dimensions.dividerThickness,
                    color = MaterialTheme.colorScheme.outline
                )
                
                Spacer(modifier = Modifier.height(Dimensions.spacing4))
                
                // Example 8: Error state with error colors
                Text(
                    text = stringResource(Strings.errorInvalidInput),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Example showing how to use resources in a ViewModel or non-Composable context.
 * 
 * Note: You cannot directly call stringResource() outside of @Composable functions.
 * Instead, pass resource references and resolve them in the UI layer.
 */
private class ResourceViewModel {
    // Store resource references, not resolved strings
    val titleResource = Strings.appName
    val errorResource = Strings.errorInvalidInput
    
    // For error messages that need to be displayed, emit resource references
    // and resolve them in the @Composable layer using ResourceState.Text
    fun getErrorMessageResource(): ResourceState.Text {
        return Strings.errorSaveFailed.asResourceText()
    }
}
