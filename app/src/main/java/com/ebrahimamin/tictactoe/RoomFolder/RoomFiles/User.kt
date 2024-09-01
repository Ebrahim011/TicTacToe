package com.iti_project.recipeapp.RoomFolder.RoomFiles

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    @ColumnInfo(name = "user_email") val userEmail: String?,
    @ColumnInfo(name = "user_password") val userPassword: String?,
    @ColumnInfo(name = "user_name") val userName: String?,

) : Parcelable