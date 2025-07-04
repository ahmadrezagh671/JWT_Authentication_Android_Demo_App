package com.ahmadrezagh671.data.user

import com.ahmadrezagh671.data.responses.UserGlobalData
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId


class MongoUserDataSource(
    db: MongoDatabase
): UserDataSource {

    private val usersCollection = db.getCollection<UserModel>("Users")

    override suspend fun getUserByUsername(username: String): UserModel? {
        return usersCollection.find(Filters.eq("username", username)).firstOrNull()
    }

    override suspend fun getUserById(objectId: ObjectId): UserModel? {
        return usersCollection.find(Filters.eq("_id", objectId)).firstOrNull()
    }

    override suspend fun insertUser(user: UserModel): Boolean {
        return usersCollection.insertOne(user).wasAcknowledged()
    }

    override suspend fun updateUserLoginInfo(id: ObjectId, appVersion:Int, device: String, loginCount: Long, lastLogin: Long): Boolean {
        val query = Filters.eq("_id", id)
        val updates = Updates.combine(
            Updates.set("appVersion",appVersion),
            Updates.set("device",device),
            Updates.set("loginCount",loginCount),
            Updates.set("lastLogin",lastLogin),
        )
        val result = usersCollection.updateOne(query, updates)

        return result.wasAcknowledged()
    }

    override suspend fun setName(id: ObjectId,name : String): Boolean {
        val query = Filters.eq("_id", id)
        val updates = Updates.combine(
            Updates.set("name",name),
        )
        //val options = UpdateOptions().upsert(true)
        val result = usersCollection.updateOne(query, updates)

        return result.wasAcknowledged()
    }

    override suspend fun getAllUsers(): List<UserGlobalData> {
        val query = Filters.eq("banStatus" , 0)
        val result = usersCollection.find(query)

        val users = mutableListOf<UserGlobalData>()

        result.collect {
            users.add(UserGlobalData(
                id = it.id.toString(),
                username = it.username,
                name = it.name,
                created = it.created,
                subscription = it.subscription,
                lastLogin = it.lastLogin
            ))
        }

        return users
    }


}