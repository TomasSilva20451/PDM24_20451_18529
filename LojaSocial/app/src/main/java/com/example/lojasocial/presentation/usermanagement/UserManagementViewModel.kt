import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.lojasocial.domain.model.User

class UserManagementViewModel(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth // Adicione este parâmetro
) : ViewModel() {

    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> = _userList

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            try {
                val querySnapshot = firestore.collection("users").get().await()
                val users = querySnapshot.toObjects(User::class.java)
                _userList.value = users
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                // Log: Início da criação do utilizador
                Log.d("UserManagement", "Iniciando criação de utilizador com email: $email")

                // Criar utilizador no Firebase Authentication
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val newUser = result.user ?: throw Exception("Erro ao criar utilizador.")
                Log.d("UserManagement", "Utilizador criado no Firebase Authentication: ${newUser.uid}")

                // Salvar dados adicionais no Firestore
                val userData = User(
                    id = newUser.uid,
                    email = email
                )
                firestore.collection("users").document(newUser.uid).set(userData).await()
                Log.d("UserManagement", "Utilizador salvo no Firestore com ID: ${newUser.uid}")

                // Atualizar lista de utilizadores
                fetchUsers()
                Log.d("UserManagement", "Lista de utilizadores atualizada.")
            } catch (e: Exception) {
                Log.e("UserManagement", "Erro ao criar utilizador", e)
            }
        }
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch {
            try {
                firestore.collection("users").document(userId).delete().await()
                fetchUsers() // Atualizar lista após exclusão
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
