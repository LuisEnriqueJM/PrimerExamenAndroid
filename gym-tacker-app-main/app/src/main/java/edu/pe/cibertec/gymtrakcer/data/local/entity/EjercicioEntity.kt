package edu.pe.cibertec.gymtrakcer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ejercicios")
data class EjercicioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val nombre: String,
    val grupoMuscular: String,
    val tipo: String
)