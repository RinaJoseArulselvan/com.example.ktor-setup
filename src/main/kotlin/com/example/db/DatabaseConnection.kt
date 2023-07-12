package com.example.db

import com.example.entities.NoteEntity
import org.ktorm.database.Database

object DatabaseConnection{
   val database = Database.connect(
        url="jdbc:mysql://localhost:3308/stock",
        driver="com.mysql.cj.jdbc.Driver",
        user="root",password ="root"
    )
//    database.insert(NoteEntity){
//        set(it.note,"mathematics")
//    }
//    database.insert(NoteEntity){
//        set(it.note,"study ktor")
//    }
//    database.insert(NoteEntity){
//        set(it.note,"wash clothes")
//    }
//
//    var rows = database.from(NoteEntity).select()
//    for (row in rows){
//        println("${row[NoteEntity.id]} , ${row[NoteEntity.note]}")
//    }
//
//    database.update(NoteEntity){
//        set(it.note,"notebook 1")
//        where { it.id.eq(1)}
//    }
//
//    database.delete(NoteEntity){
//        it.id eq 4
//    }

}
