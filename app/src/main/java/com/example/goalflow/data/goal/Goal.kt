package com.example.goalflow.data.goal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.goalflow.data.activity.ActivityItem

@Entity(tableName = "goals")
data class Goal(
	@PrimaryKey(autoGenerate = true)
	override val id: Int = 0,

	override val name: String,
	override val weight: Int,
) : ActivityItem
