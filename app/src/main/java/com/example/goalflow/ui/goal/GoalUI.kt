package com.example.goalflow.ui.goal

import com.example.goalflow.data.goal.Goal

data class GoalUI (
    val goal: Goal,
    val isOptionsRevealed: Boolean = false
)