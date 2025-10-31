package edu.pe.cibertec.gestortareas.data.repository

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import edu.pe.cibertec.gestortareas.model.Libro
import edu.pe.cibertec.gestortareas.model.Usuario
import kotlinx.coroutines.tasks.await

class FirebaseRepository(private val context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Configurar Google Sign-In
    fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("517705033353-e1jn9sqh0v9hejm64klolsnsjuvvg7na.apps.googleusercontent.com") // Web Client ID de Firebase Console
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, gso)
    }

    // Autenticación con Google
    suspend fun autenticarConGoogle(account: GoogleSignInAccount): Result<Usuario> {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = auth.signInWithCredential(credential).await()

            val firebaseUser = result.user ?: throw Exception("Error al obtener usuario")

            val usuario = Usuario(
                uid = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                nombre = firebaseUser.displayName ?: "",
                photoUrl = firebaseUser.photoUrl?.toString() ?: ""
            )

            // Guardar o actualizar usuario en Firestore
            firestore.collection("usuarios")
                .document(usuario.uid)
                .set(usuario)
                .await()

            Result.success(usuario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Registro con Email/Password
    suspend fun registrarUsuario(email: String, password: String, nombre: String): Result<Usuario> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Error al crear usuario")

            val usuario = Usuario(uid = uid, email = email, nombre = nombre)
            firestore.collection("usuarios").document(uid).set(usuario).await()

            Result.success(usuario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Login con Email/Password
    suspend fun iniciarSesion(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Error al iniciar sesión")
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Cerrar Sesión
    fun cerrarSesion() {
        auth.signOut()
        getGoogleSignInClient().signOut()
    }

    // Obtener Usuario Actual
    fun obtenerUsuarioActual() = auth.currentUser

    // Obtener Datos del Usuario
    suspend fun obtenerDatosUsuario(uid: String): Result<Usuario> {
        return try {
            val snapshot = firestore.collection("usuarios")
                .document(uid)
                .get()
                .await()

            val usuario = snapshot.toObject(Usuario::class.java)
                ?: throw Exception("Usuario no encontrado")

            Result.success(usuario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // FIRESTORE - LIBROS
    suspend fun guardarLibro(libro: Libro): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")
            val libroConUsuario = libro.copy(
                titulo = libro.titulo,
                precioUnitario = libro.precioUnitario,
                cantidad = libro.cantidad,
                categoria = libro.categoria
            )

            firestore.collection("usuarios")
                .document(uid)
                .collection("libros")
                .document(libro.id)
                .set(libroConUsuario)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerLibrosUsuario(): Result<List<Libro>> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")

            val snapshot = firestore.collection("usuarios")
                .document(uid)
                .collection("libros")
                .get()
                .await()

            val libros = snapshot.documents.mapNotNull { it.toObject(Libro::class.java) }
            Result.success(libros)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarLibro(libroId: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")

            firestore.collection("usuarios")
                .document(uid)
                .collection("libros")
                .document(libroId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun limpiarCarrito(): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")

            val snapshot = firestore.collection("usuarios")
                .document(uid)
                .collection("libros")
                .get()
                .await()

            snapshot.documents.forEach { doc ->
                doc.reference.delete().await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
