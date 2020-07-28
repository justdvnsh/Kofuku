package divyansh.tech.kofuku.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kofuku.R
import divyansh.tech.kofuku.ui.viewmodels.SplashViewModel

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkIfUserIsAuthenticated()
    }

    private fun checkIfUserIsAuthenticated() {
        viewModel.checkIfUserIsAuthenticated()
        viewModel.isUserAuthenticatedLiveData?.observe(this, Observer {
            if (it.email == null) {
                Log.i("SPLASH", it.toString() + it.name)
            }
            if (!it.isAuthenticated!!) startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
            else getUserFromDatabase(it.email!!)
        })
    }

    private fun getUserFromDatabase(email: String?) {
        viewModel.setEmail(email!!)
        viewModel.userLiveData?.observe(this, Observer {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.putExtra(getString(R.string.userArgument), it)
            startActivity(intent)
            finish()
        })
    }
}