package com.ebrahimamin.tictactoe.RoomFolder.RoomFiles.Repo

import android.content.Context
import androidx.lifecycle.LiveData
import com.ebrahimamin.tictactoe.RoomFolder.RoomFiles.LocalSource.LocalSource
import com.ebrahimamin.tictactoe.RoomFolder.RoomFiles.LocalSource.LocalSourceInterface
import com.ebrahimamin.tictactoe.RoomFolder.RoomFiles.User

class UserRepository(private val localSource: LocalSourceInterface) : UserRepositoryInterface {

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(context: Context): UserRepository {
            return instance ?: synchronized(this) {
                val localSource = LocalSource.getInstance(context)
                UserRepository(localSource).also { instance = it }
            }
        }
    }

    override fun checkIfEmailExists(email: String): Boolean {
        return localSource.checkIfEmailExists(email)
    }

    override fun getPasswordByEmail(email: String): String? {
        return localSource.getPasswordByEmail(email)
    }

    override fun getUserId(email: String): Int? {
        return localSource.getUserId(email)
    }

    override fun addAccount(user: User) {
        localSource.addAccount(user)
    }



    override fun updateProfile(userInfo: User) {
        localSource.updateProfile(userInfo)
    }

    override fun getUserByEmail(email: String): LiveData<User> {
        return localSource.getUserByEmail(email)
    }

    override fun getUserById(userId: Int): LiveData<User> {
        return localSource.getUserById(userId)
    }
}