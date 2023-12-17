package edu.miu.cs473de.lab6.foodiepal.data.aboutme

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AboutMeItem")
data class AboutMeItem(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var title: String,
    var description: String
)
