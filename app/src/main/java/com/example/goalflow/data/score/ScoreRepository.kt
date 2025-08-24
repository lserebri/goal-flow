package com.example.goalflow.data.score

import kotlinx.coroutines.flow.Flow

interface ScoreRepository {
	suspend fun getScore(): Flow<Int>

	suspend fun updateScore(score: Score)
}
