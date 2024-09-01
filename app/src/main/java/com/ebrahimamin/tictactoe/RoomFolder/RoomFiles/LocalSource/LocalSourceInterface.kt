package com.iti_project.recipeapp.RoomFolder.RoomFiles.LocalSource

import androidx.lifecycle.LiveData
import com.iti_project.recipeapp.RoomFolder.RoomFiles.User

interface LocalSourceInterface {

    fun checkIfEmailExists(email: String): Boolean
    fun getPasswordByEmail(email: String): String?
    fun getUserId(email: String): Int?
    fun addAccount(user: User)

    fun updateProfile(userInfo : User)
    fun getUserByEmail(email: String): LiveData<User>
    fun getUserById(userId: Int): LiveData<User>
}