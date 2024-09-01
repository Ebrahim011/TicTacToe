package com.iti_project.recipeapp.RoomFolder.RoomFiles.LocalSource

import android.content.Context
import androidx.lifecycle.LiveData
import com.iti_project.recipeapp.RoomFolder.RoomFiles.RoomDataBase
import com.iti_project.recipeapp.RoomFolder.RoomFiles.User
import com.iti_project.recipeapp.RoomFolder.RoomFiles.UserDao

class LocalSource(private val userDao: UserDao) : LocalSourceInterface {

    companion object {
        @Volatile
        private var INSTANCE: LocalSource? = null

        fun getInstance(context: Context): LocalSource {
            return INSTANCE ?: synchronized(this) {
                val instance = LocalSource(RoomDataBase.getInstance(context).getUserDao())
                INSTANCE = instance
                instance
            }
        }
    }


    override fun checkIfEmailExists(email: String): Boolean {
        return userDao.checkIfEmailExists(email)
    }

    override fun getPasswordByEmail(email: String): String? {
        return userDao.getPasswordByEmail(email)
    }

    override fun getUserId(email: String): Int? {
        return userDao.getUserId(email)
    }

    override fun addAccount(user: User) {
        userDao.addAccount(user)
    }


    override fun updateProfile(userInfo: User) {
        userDao.update(userInfo)
    }

    override fun getUserByEmail(email: String): LiveData<User> {
        return userDao.getUserByEmail(email)
    }

    override fun getUserById(userId: Int): LiveData<User> {
        return userDao.getUserById(userId)
    }
}