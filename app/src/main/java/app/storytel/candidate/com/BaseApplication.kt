package app.storytel.candidate.com

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

/**
 * We are informing the app that we are using Hilt to implement dagger
 */
@HiltAndroidApp
class BaseApplication : MultiDexApplication() {

}