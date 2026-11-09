package com.mallet.echotiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mallet.echotiles.core.di.ServiceLocator
import com.mallet.echotiles.core.theme.AppTheme
import com.mallet.echotiles.presentation.app.AppRoot

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ServiceLocator.init(applicationContext)
        setContent {
            AppTheme {
                AppRoot()
            }
        }
    }
}
