package com.example.goalflow.data.score

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ScoreRepositoryImplementation @Inject constructor(
	private val scoreDao: ScoreDao
) : ScoreRepository {
	override suspend fun getScore(): Flow<Int> = scoreDao.getScore()

	override suspend fun updateScore(score: Score) = scoreDao.updateScore(score.copy(id = 0))
}