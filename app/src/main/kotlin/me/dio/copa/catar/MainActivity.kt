package me.dio.copa.catar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint
import me.dio.copa.catar.extensions.observe
import me.dio.copa.catar.notification.scheduler.extensions.NotificationsMatchesWorker
import me.dio.copa.catar.ui.theme.Copa2022Theme
import me.dio.copa.catar.viewmodel.MainUiAction
import me.dio.copa.catar.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeActions()
        setContent {
            Copa2022Theme(
            ) {
                val state by mainViewModel.state.collectAsState()
                MainScreen(matches = state.matches, mainViewModel::toggleNotification)
            }
        }
    }

    private fun observeActions(){
        mainViewModel.action.observe(this){action ->
            when(action){
                is MainUiAction.DisableNotification -> {
                    NotificationsMatchesWorker.cancel(applicationContext, action.match)
                }
                is MainUiAction.EnableNotification -> {
                    NotificationsMatchesWorker.start(applicationContext, action.match)
                }
                is MainUiAction.MatchesNotFound -> {}
                MainUiAction.Unexpected -> {}
            }
        }
    }

}