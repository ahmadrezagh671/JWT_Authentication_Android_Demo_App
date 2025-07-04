package com.ahmadrezagh671.data.user

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class UserModel(
    @BsonId val id: ObjectId = ObjectId(),
    val username: String,
    val password: String,
    val salt: String,

    val name:String = "",
    val banStatus: Int = 0,
    val subscription:Int = 0,
    val device: String = "",
    var appVersion:Int = 0,
    var loginCount:Long = 0L,
    val created:Long = System.currentTimeMillis(),
    var lastLogin:Long = 0L
)