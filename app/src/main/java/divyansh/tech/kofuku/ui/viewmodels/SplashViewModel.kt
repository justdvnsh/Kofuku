package divyansh.tech.kofuku.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import divyansh.tech.kofuku.models.User
import divyansh.tech.kofuku.repositories.SplashRepository

class SplashViewModel @ViewModelInject constructor(
    private val splashRepository: SplashRepository
): ViewModel() {
    var isUserAuthenticatedLiveData: LiveData<User>? = null
    var userLiveData: LiveData<User>? = null

    fun checkIfUserIsAuthenticated() {
        isUserAuthenticatedLiveData = splashRepository.checkIfUserIsAuthenticatedInFirebase()
    }

    fun setEmail(email: String) {
        userLiveData = splashRepository.addUserToLiveData(email)
    }

}