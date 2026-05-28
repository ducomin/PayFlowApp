package br.com.payflowapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.payflowapplication.navigation.PayFlowNavGraph
import br.com.payflowapplication.ui.theme.PayFlowTheme
import br.com.payflowapplication.viewmodels.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // ThemeViewModel é injetado pelo Hilt com escopo de Activity.
    // Porque a Activity sobrevive a recomposições, o estado do tema
    // é a fonte de verdade para toda a árvore de navegação.
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            // Coleta o StateFlow — qualquer mudança re-compõe o PayFlowTheme
            // e reflete para TODAS as telas instantaneamente.
            val darkTheme by themeViewModel.isDarkTheme.collectAsStateWithLifecycle()

            PayFlowTheme(darkTheme = darkTheme) {

                PayFlowNavGraph(
                    darkTheme = darkTheme,
                    onThemeChange = { enabled ->
                        // Persiste no DataStore E atualiza o StateFlow
                        themeViewModel.setDarkTheme(enabled)
                    }
                )
            }
        }
    }
}