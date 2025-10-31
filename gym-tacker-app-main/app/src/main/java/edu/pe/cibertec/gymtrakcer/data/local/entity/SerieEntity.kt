package edu.pe.cibertec.gymtrakcer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "series",
    foreignKeys = [
        ForeignKey(
            entity = SesionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sesionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EjercicioEntity::class,
            parentColumns = ["id"],
            childColumns = ["ejercicioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("sesionId"),
        Index("ejercicioId")
    ]
)
data class SerieEntity(
    val id: Int = 0,
    val sesionId: Int,
    val ejercicioId: Int,
    val pesoKg: Double,
    val repeteciones: Int = 0,
    val orden: Int = 0,
    val repeticiones: Int
)