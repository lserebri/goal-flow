package com.example.goalflow.data.score

import kotlinx.coroutines.flow.Flow

interface ScoreRepository {
    val getScore: Flow<Int>

    suspend fun updateScore(score: Score)
}
