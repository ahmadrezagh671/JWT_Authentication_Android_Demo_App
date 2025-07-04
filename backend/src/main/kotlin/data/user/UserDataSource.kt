package com.ahmadrezagh671.data.user

import com.ahmadrezagh671.data.responses.UserGlobalData
import com.ahmadrezagh671.data.responses.UserInfo
import org.bson.types.ObjectId

interface UserDataSource {
    suspend fun getUserByUsername(username: String): UserModel?
    suspend fun getUserById(objectId: ObjectId): UserModel?
    suspend fun insertUser(user: UserModel): Boolean
    suspend fun updateUserLoginInfo(id: ObjectId, appVersion:Int, device: String, loginCount: Long, lastLogin: Long):Boolean
    suspend fun setName(id: ObjectId,name : String): Boolean
    suspend fun getAllUsers(): List<UserGlobalData>
}