package com.example.parentcoachbot

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.parentcoachbot.feature_chat.domain.util.AuthManager
import com.example.parentcoachbot.feature_chat.domain.util.Language
import com.example.parentcoachbot.feature_chat.domain.util.MyContextWrapper
import com.example.parentcoachbot.feature_chat.presentation.Navigation
import com.example.parentcoachbot.ui.theme.ParentCoachBotTheme
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    @Inject
    lateinit var authManager: AuthManager
    private var isLoggedIn = false
    private var checkAuthJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAuthJob?.cancel()

        checkAuthJob = lifecycleScope.launch {
            authManager.authenticatedRealmUser.onEach { user ->
                user?.let {
                    if (user.loggedIn) {
                        isLoggedIn = true
                    }
                }
            }.collect()
        }

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !isLoggedIn
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(screen.view, View.ROTATION_X, 0.4f, 0.0f)
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 50L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(screen.view, View.ROTATION_Y, 0.4f, 0.0f)
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 50L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }

        }


        setContent {
            ParentCoachBotTheme (darkTheme = false){
                // A surface container using the 'background' color from the theme
                val authResult: User? by authManager.authenticatedRealmUser.collectAsStateWithLifecycle()

                authResult?.let {
                    Navigation()
                }

            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val appPreferences = newBase.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val language = appPreferences.getString("default_language", Language.English.isoCode)
            ?: Language.English.isoCode



        val context = MyContextWrapper.wrap(
            newBase,
            language = language
        )
        super.attachBaseContext(context)
    }
}




