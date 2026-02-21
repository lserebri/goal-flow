package lserebri.goalflow.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    onPrimary = Color.White,
    primaryContainer = Blue90,
    onPrimaryContainer = Blue10,
    secondary = Mint40,
    onSecondary = Color.White,
    secondaryContainer = Mint90,
    onSecondaryContainer = Mint10,
    tertiary = Cyan40,
    onTertiary = Color.White,
    tertiaryContainer = Cyan90,
    onTertiaryContainer = Cyan10,
    error = Error40,
    onError = Color.White,
    errorContainer = Error90,
    onErrorContainer = Error10,
    background = Grey99,
    onBackground = Grey10,
    surface = Grey99,
    onSurface = Grey10,
    surfaceVariant = GreyVariant90,
    onSurfaceVariant = GreyVariant30,
    outline = GreyVariant60,
    outlineVariant = GreyVariant80,
    inverseSurface = Grey20,
    inverseOnSurface = Grey90,
    inversePrimary = Blue80,
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    onPrimary = Blue20,
    primaryContainer = Blue30,
    onPrimaryContainer = Blue90,
    secondary = Mint80,
    onSecondary = Mint20,
    secondaryContainer = Mint30,
    onSecondaryContainer = Mint90,
    tertiary = Cyan80,
    onTertiary = Cyan20,
    tertiaryContainer = Cyan30,
    onTertiaryContainer = Cyan90,
    error = Error80,
    onError = Error20,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Error90,
    background = Grey10,
    onBackground = Grey90,
    surface = Grey10,
    onSurface = Grey90,
    surfaceVariant = GreyVariant30,
    onSurfaceVariant = GreyVariant80,
    outline = GreyVariant60,
    outlineVariant = GreyVariant30,
    inverseSurface = Grey90,
    inverseOnSurface = Grey20,
    inversePrimary = Blue40,
)

@Composable
fun GoalFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set to true to use Android 12+ dynamic (wallpaper-based) colors instead
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
