package com.ahmadrezagh671

import com.ahmadrezagh671.data.user.MongoUserDataSource
import com.ahmadrezagh671.plugins.configureMonitoring
import com.ahmadrezagh671.plugins.configureRouting
import com.ahmadrezagh671.plugins.configureSecurity
import com.ahmadrezagh671.plugins.configureSerialization
import com.ahmadrezagh671.security.hashing.SHA256HashingService
import com.ahmadrezagh671.security.token.JwtTokenService
import com.ahmadrezagh671.security.token.TokenConfig
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.ktor.server.application.*
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val mongoPassword = System.getenv("MONGO_PASSWORD")
    val dbName = "ktorAuthUsers"
    val mongoUsername = "username"

    val connectionString = "mongodb+srv://$mongoUsername:$mongoPassword@ktor.aoiik5r.mongodb.net/$dbName?retryWrites=true&w=majority&appName=ktor"

    val db = MongoClient
        .create(connectionString)
        .getDatabase(dbName)

    val userDataSource = MongoUserDataSource(db)
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365.days.toLong(DurationUnit.MILLISECONDS),
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    configureSerialization()
    configureMonitoring()
    configureSecurity(tokenConfig)
    configureRouting(userDataSource, hashingService, tokenService, tokenConfig)
}
