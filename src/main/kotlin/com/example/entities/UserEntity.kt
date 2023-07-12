package com.example.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object UserEntity :Table<Nothing>(tableName = "users"){
    val id = int(name = "id").primaryKey()
    val username = varchar(name = "username")
    val password = varchar(name = "password")
}