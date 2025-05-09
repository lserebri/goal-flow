package com.example.goalflow.data.consumable

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consumable")
data class Consumable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val weight: Int,
)