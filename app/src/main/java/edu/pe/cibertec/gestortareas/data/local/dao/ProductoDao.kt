package edu.pe.cibertec.gestortareas.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import edu.pe.cibertec.gestortareas.data.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(producto: ProductoEntity): Long

    @Update
    suspend fun update(producto: ProductoEntity)

    @Delete
    suspend fun delete(producto: ProductoEntity)

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun getAllProductos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun getProductoById(id: Int): ProductoEntity?

    @Query("SELECT * FROM productos WHERE categoria = :categoria ORDER BY nombre ASC")
    fun getProductosByCategoria(categoria: String): Flow<List<ProductoEntity>>
}
