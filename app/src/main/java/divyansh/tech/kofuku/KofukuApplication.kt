package divyansh.tech.kofuku

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KofukuApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}