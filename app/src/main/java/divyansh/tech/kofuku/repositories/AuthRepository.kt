package divyansh.tech.kofuku.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kofuku.models.User
import divyansh.tech.kofuku.utils.Constants.Companion.USERS
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) {

    private val usersRef = firebaseFirestore.collection(USERS)

    fun testIfInjected() {
        Log.i("INJECTED_FIREBASE", firebaseAuth.hashCode().toString())
    }

    fun firebaseSignInWithGoogle(authCredential: AuthCredential): MutableLiveData<User> {
        val authenticatedUserMutableLiveData: MutableLiveData<User> = MutableLiveData()
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener {authResult ->
            if (authResult.isSuccessful) {
                val isNewUser = authResult.result?.additionalUserInfo?.isNewUser
                val firebaseUser = firebaseAuth.currentUser
                firebaseUser?.let {
                    val user = User(
                        it.uid, it.displayName.toString(), it.email.toString()
                    )
                    user.isNew = isNewUser
                    authenticatedUserMutableLiveData.value = user
                }
            } else {
                Log.i("AUTHREPO", "FAILED ${authResult.exception?.message}")
            }
        }

        return authenticatedUserMutableLiveData
    }

    fun createUserInFirestoreIfNotExists(user: User): MutableLiveData<User> {
        val newUserMutableLiveData: MutableLiveData<User> = MutableLiveData()
        val uidRef: DocumentReference = usersRef.document(user.email!!)
        uidRef.get().addOnCompleteListener {task ->
            if (task.isSuccessful) {
                val document = task.result
                if (!document?.exists()!!) {
                    uidRef.set(user).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            user.isCreated = true
                            newUserMutableLiveData.value = user
                        } else {
                            Log.i("AUTHREPO", task.exception?.message)
                        }
                    }
                } else {
                    newUserMutableLiveData.value = user
                }
            } else {
                Log.i("AUTHREPO", "FAILED ${task.exception?.message}")
            }
        }

        return newUserMutableLiveData
    }

}