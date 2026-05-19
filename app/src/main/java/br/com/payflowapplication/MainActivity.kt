package br.com.payflowapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.payflowapplication.navigation.PayFlowNavGraph
import br.com.payflowapplication.ui.theme.PayFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PayFlowTheme {
                PayFlowNavGraph()
            }
        }
    }
}

