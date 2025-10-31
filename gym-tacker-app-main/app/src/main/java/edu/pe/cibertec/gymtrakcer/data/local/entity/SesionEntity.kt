package edu.pe.cibertec.gymtrakcer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sesiones")
data class SesionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fecha: Long,
    val duracionMinutos: Int = 0,
    val notas: String = ""
)