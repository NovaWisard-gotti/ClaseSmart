package com.educalab.clasesmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.educalab.clasesmart.ui.navigation.ClaseSmartNavGraph
import com.educalab.clasesmart.ui.theme.ClaseSmartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = (application as ClaseSmartApplication).container
        setContent {
            ClaseSmartTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ClaseSmartNavGraph(container = container)
                }
            }
        }
    }
}
