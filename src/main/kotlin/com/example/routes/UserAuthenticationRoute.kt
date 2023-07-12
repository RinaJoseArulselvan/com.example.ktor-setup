package com.example.routes

import com.example.db.DatabaseConnection
import com.example.entities.UserEntity
import com.example.models.User
import com.example.models.UserCredentials
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt

fun Route.userAuthenticRoute(){
    val db = DatabaseConnection.database
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))
    route("/register"){
        post {
            val request = call.receive<UserCredentials>()

            if(!request.isValidCredentials()){
                call.respondText("username should be grater than 3 and password should be greater than 6",status = HttpStatusCode.BadRequest)
                return@post
            }
            val username = request.username.lowercase()
            val password = request.hashedPassword()
            val user =  db.from(UserEntity).select()
                    .where {
                        UserEntity.username eq username
                    }.map {
                        it[UserEntity.username]
                    }.firstOrNull()
            if (user!= null){
                call.respondText("This username already exists" , status = HttpStatusCode.BadRequest)
                return@post
            }
                val result = db.insert(UserEntity) {
                    set(it.username, username)
                    set(it.password, password)
                }
                if (result == 1) {
                    call.respondText("registered successfully")
                } else {
                    call.respondText(" registration unsuccessful")
                }

        }
        post("/login"){
            val request = call.receive<UserCredentials>()

            if(!request.isValidCredentials()){
                call.respondText("username should be grater than 3 and password should be greater than 6",status = HttpStatusCode.BadRequest)
                return@post
            }
            val username = request.username.lowercase()
            val password = request.password
            val result = db .from(UserEntity).select()
                .where {
                    UserEntity.username eq username

                }.map {
                    val id = it[UserEntity.id]!!
                    val username = it[UserEntity.username]!!
                    val password = it[UserEntity.password]!!
                    User(id,username,password)
                }.firstOrNull()
            if(result == null){
                call.respondText("Invalid Username or password " , status = HttpStatusCode.BadRequest)
                return@post
            }

            val doesPasswordMatch = BCrypt.checkpw(password,result.password)

            if(!doesPasswordMatch){
                call.respondText("Invalid Username or password " , status = HttpStatusCode.BadRequest)
                return@post
            }
            val token = tokenManager.generateJWTToken(result)
            call.respondText("token : $token")


        }

        authenticate {
            get("/me"){
                val principle = call.principal<JWTPrincipal>()
                val username = principle!!.payload.getClaim("username").asString()
                val userId = principle!!.payload.getClaim("userId").asInt()
                call.respondText ( "Hello $username with $userId")
            }
        }
    }
}