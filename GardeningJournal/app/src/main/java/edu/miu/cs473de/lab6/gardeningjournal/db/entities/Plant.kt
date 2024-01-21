package edu.miu.cs473de.lab6.gardeningjournal.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Plant",
    indices = [
        Index(value = ["id"], unique = true)
    ]
)
data class Plant(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var name: String,
    var type: String,
    var wateringFrequency: Int,
    var plantingDate: String
)
