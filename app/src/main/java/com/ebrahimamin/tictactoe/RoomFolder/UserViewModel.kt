package com.ebrahimamin.tictactoe.RoomFolder

import android.app.Application
import androidx.lifecycle.*
import com.ebrahimamin.tictactoe.RoomFolder.RoomFiles.Repo.UserRepository
import com.ebrahimamin.tictactoe.RoomFolder.RoomFiles.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val _emailExists = MutableLiveData<Boolean>()
    val emailExists: LiveData<Boolean> get() = _emailExists

    private val _password = MutableLiveData<String?>()
    val password: LiveData<String?> get() = _password

    private val _userId = MutableLiveData<Int?>()
    val userId: LiveData<Int?> get() = _userId
    private val userRepository: UserRepository = UserRepository.getInstance(application)


    fun checkIfEmailExists(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _emailExists.postValue(userRepository.checkIfEmailExists(email))
        }
    }

    fun getPasswordByEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _password.postValue(userRepository.getPasswordByEmail(email))
        }
    }

    fun checkIfEmailExistsBoolean(email: String): Boolean {
        getPasswordByEmail(email)
        return  userRepository.checkIfEmailExists(email)
    }
    fun getUserId(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _userId.postValue(userRepository.getUserId(email))
        }
    }

    fun addAccount(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.addAccount(user)
        }
    }

    //nader
    fun updateProfile(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateProfile(user)
        }
    }
    fun getUserByEmail(email: String): LiveData<User> {
        return userRepository.getUserByEmail(email)
    }

    fun getUserById(userId: Int): LiveData<User> {
        return userRepository.getUserById(userId)
    }
}