package edu.pe.cibertec.gestortareas.data.repository

import edu.pe.cibertec.gestortareas.data.local.dao.ProductoDao
import edu.pe.cibertec.gestortareas.data.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val productoDao: ProductoDao) {

    fun getAllProductos(): Flow<List<ProductoEntity>> {
        return productoDao.getAllProductos()
    }

    fun getProductosByCategoria(categoria: String): Flow<List<ProductoEntity>> {
        return productoDao.getProductosByCategoria(categoria)
    }

    suspend fun getProductoById(id: Int): ProductoEntity? {
        return productoDao.getProductoById(id)
    }

    suspend fun insertProducto(producto: ProductoEntity): Long {
        return productoDao.insert(producto)
    }

    suspend fun updateProducto(producto: ProductoEntity) {
        productoDao.update(producto)
    }

    suspend fun deleteProducto(producto: ProductoEntity) {
        productoDao.delete(producto)
    }
}
