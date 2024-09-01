package com.ebrahimamin.tictactoe.RoomFolder.RoomFiles

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ebrahimamin.tictactoe.RoomFolder.RoomFiles.User

@Dao
interface UserDao {


 @Query("SELECT EXISTS(SELECT 1 FROM user WHERE user_email = :email)")
 fun checkIfEmailExists(email: String): Boolean

 @Query("SELECT user_password FROM user WHERE user_email = :email")
 fun getPasswordByEmail(email: String): String?

 @Query("SELECT _id FROM user WHERE user_email = :email")
 fun getUserId(email: String): Int?

 @Insert(onConflict = OnConflictStrategy.REPLACE)
 fun addAccount(user: User)

 @Update
 fun update(userInfo: User)

 @Query("SELECT * FROM user WHERE user_email = :email")
 fun getUserByEmail(email: String): LiveData<User>

 @Query("SELECT * FROM user WHERE _id = :userId")
 fun getUserById(userId: Int): LiveData<User>
}