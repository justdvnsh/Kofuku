package divyansh.tech.kofuku.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import divyansh.tech.kofuku.models.User
import divyansh.tech.kofuku.repositories.AuthRepository

class AuthViewModel @ViewModelInject constructor(
    val authRepository: AuthRepository
): ViewModel() {
    var authenticatedUserLiveData: LiveData<User>? = null
    var createdUserLiveData: LiveData<User>? = null

    fun signInWithGoogle(authCredential: AuthCredential) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(authCredential)
    }

    fun createUser(user: User) {
        createdUserLiveData = authRepository.createUserInFirestoreIfNotExists(user)
    }
}