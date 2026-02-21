package lserebri.goalflow.data.level

import kotlin.math.pow

object LevelCalculator {
	private const val BASE_XP = 100
	private const val GROWTH_RATE = 1.5

	fun xpForLevel(level: Int): Int {
		if (level <= 1) return 0
		return (BASE_XP * GROWTH_RATE.pow(level - 1)).toInt()
	}

	fun calculateXpGain(minutes: Int, weight: Int): Int {
		return ((minutes.toFloat() / 60f) * weight).toInt()
	}

	fun getLevelInfo(currentLevel: Int, currentXp: Int): LevelInfo {
		val xpNeeded = xpForLevel(currentLevel + 1)
		val progress = if (xpNeeded > 0) {
			(currentXp.toFloat() / xpNeeded).coerceIn(0f, 1f)
		} else {
			0f
		}
		return LevelInfo(level = currentLevel, currentLevelProgress = progress)
	}

	fun applyXpChange(
		currentLevel: Int,
		currentXp: Int,
		xpDelta: Int,
		isGain: Boolean
	): Pair<Int, Int> {
		var level = currentLevel
		var xp = currentXp

		if (isGain) {
			xp += xpDelta
			while (xp >= xpForLevel(level + 1)) {
				xp -= xpForLevel(level + 1)
				level++
			}
		} else {
			xp -= xpDelta
			while (xp < 0 && level > 1) {
				level--
				xp += xpForLevel(level + 1)
			}
			if (level == 1 && xp < 0) {
				xp = 0
			}
		}

		return Pair(level, xp)
	}
}

data class LevelInfo(
	val level: Int,
	val currentLevelProgress: Float,
)