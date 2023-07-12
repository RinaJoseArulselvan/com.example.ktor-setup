package com.example.routes

import com.example.db.DatabaseConnection
import com.example.entities.NoteEntity
import com.example.entities.NoteRequest
import com.example.models.Note
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import org.ktorm.dsl.*

fun Route.notesRoute(){
    val db = DatabaseConnection.database
    route("/notes"){
        get{
            val notes = db.from(NoteEntity).select()
                .map {
                    val id = it[NoteEntity.id]
                    val note = it[NoteEntity.note]
                    Note(id ?:-1,note ?:"")
                }
            call.respond(notes)
        }
        post {
                val request = call.receive<NoteRequest>()
                val result = db.insert(NoteEntity){
                    set(it.note,request.note)
                }
            if (result == 1){
                call.respondText("inserted successfully")
            }else{
                call.respondText("inserted successfully")
            }
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val note = db.from(NoteEntity).select()
                .where { NoteEntity.id eq id }
                .map {
                    val id = it[NoteEntity.id]
                    val note = it[NoteEntity.note]
                    Note(id ?: -1, note ?: "")
                }.firstOrNull()
            if (note == null) {
                call.respondText("note not found with this id $id ", status = HttpStatusCode.BadRequest)
            } else {
                call.respond(note)
            }
        }
        post("/update/{id}"){
            val id = call.parameters["id"]?.toInt() ?: return@post call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val request = call.receive<NoteRequest>()
            val result = db.update(NoteEntity){
                set(it.note,request.note)
                where { it.id eq id  }
            }
            if (result == 1){
                call.respondText("updated successfully " , status = HttpStatusCode.OK)
            }else{
                call.respondText("not updated" , status= HttpStatusCode.BadRequest)
            }
        }
        delete("/{id}"){
            val id = call.parameters["id"]?.toInt() ?: return@delete call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val request = db.delete(NoteEntity){
                it.id eq id
            }
            if (request == 1){
                call.respondText("deleted successfully " , status = HttpStatusCode.OK)
            }else{
                call.respondText("not deleted" , status= HttpStatusCode.BadRequest)
            }
        }
    }
}
