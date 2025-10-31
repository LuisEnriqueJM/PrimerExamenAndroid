package edu.pe.cibertec.gymtrakcer.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.pe.cibertec.gymtrakcer.data.local.dao.EjercicioDao
import edu.pe.cibertec.gymtrakcer.data.local.dao.SerieDao
import edu.pe.cibertec.gymtrakcer.data.local.dao.SesionDao
import edu.pe.cibertec.gymtrakcer.data.local.entity.EjercicioEntity
import edu.pe.cibertec.gymtrakcer.data.local.entity.SerieEntity
import edu.pe.cibertec.gymtrakcer.data.local.entity.SesionEntity

@Database(
    entities = [
        EjercicioEntity::class,
        SesionEntity::class,
        SerieEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase(){
    // DAOs
    abstract fun ejercicioDao(): EjercicioDao
    abstract fun sesionDao(): SesionDao
    abstract  fun serieDao(): SerieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase (context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gym_tracker_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE=instance
                instance
            }
        }
    }


}