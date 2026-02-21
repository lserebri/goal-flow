package lserebri.goalflow.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircularProgressBar(
	number: Int,
	percentage: Float,
	radius: Dp = 50.dp,
	strokeWidth: Dp = 8.dp,
	animationDurationPerLevel: Int = 600
) {

	val totalTarget = number + percentage.coerceIn(0f, 1f)

	val progress = rememberProgressAnimation(
		target = totalTarget, durationPerLevel = animationDurationPerLevel
	)

	val currentLevel = progress.toInt()
	val circleProgress = ((progress % 1f) + 1f) % 1f
	val scale = rememberLevelPopAnimation(currentLevel)

	CircularProgressContent(
		level = currentLevel,
		circleProgress = circleProgress,
		scale = scale,
		radius = radius,
		strokeWidth = strokeWidth
	)
}

@Composable
private fun rememberProgressAnimation(
	target: Float,
	durationPerLevel: Int
): Float {

	val animatable = remember { Animatable(0f) }

	LaunchedEffect(target) {

		val distance = target - animatable.value

		if (distance == 0f) return@LaunchedEffect

		animatable.animateTo(
			targetValue = target,
			animationSpec = tween(
				durationMillis = (kotlin.math.abs(distance) * durationPerLevel).toInt(),
				easing = LinearEasing
			)
		)
	}

	return animatable.value
}

@Composable
private fun rememberLevelPopAnimation(
	currentLevel: Int
): Float {

	val scaleAnim = remember { Animatable(1f) }
	var previousLevel by remember { mutableStateOf(currentLevel) }

	LaunchedEffect(currentLevel) {

		if (currentLevel != previousLevel) {

			scaleAnim.snapTo(1f)

			scaleAnim.animateTo(
				1.3f,
				animationSpec = tween(100, easing = FastOutLinearInEasing)
			)

			scaleAnim.animateTo(
				1f,
				animationSpec = tween(200, easing = FastOutSlowInEasing)
			)
		}

		previousLevel = currentLevel
	}

	return scaleAnim.value
}

@Composable
private fun CircularProgressContent(
	level: Int, circleProgress: Float, scale: Float, radius: Dp, strokeWidth: Dp
) {
	Box(
		contentAlignment = Alignment.Center, modifier = Modifier.size(radius * 2)
	) {
		Canvas(modifier = Modifier.size(radius * 2)) {
			drawArc(
				color = Color.Green,
				startAngle = -90f,
				sweepAngle = 360 * circleProgress,
				useCenter = false,
				style = Stroke(
					width = strokeWidth.toPx(), cap = StrokeCap.Round
				)
			)
		}
		Text(
			text = level.toString(),
			fontWeight = FontWeight.Bold,
			fontSize = 24.sp,
			modifier = Modifier.graphicsLayer {
				scaleX = scale
				scaleY = scale
			}
		)
	}
}
