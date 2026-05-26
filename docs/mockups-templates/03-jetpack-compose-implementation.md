# 🚀 PayFlow - Implementação Jetpack Compose

Exemplos de código Jetpack Compose baseados nos mockups criados. Pronto para ser usado como referência durante o desenvolvimento.

---

## 📦 Setup Inicial

### build.gradle.kts (App)

```gradle
dependencies {
    // Jetpack Compose
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.compose.foundation:foundation:1.6.0")
    implementation("androidx.activity:activity-compose:1.8.0")
    
    // Jetpack Navigation
    implementation("androidx.navigation:navigation-compose:2.7.4")
    
    // Jetpack ViewModel + Hilt
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    
    // Room
    implementation("androidx.room:room-runtime:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    
    // Retrofit + OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
}
```

---

## 🎨 Design System & Theme

### Color.kt

```kotlin
package com.payflow.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// PayFlow Colors
val Indigo500 = Color(0xFF6366F1)
val Cyan400 = Color(0xFF06B6D4)
val Pink500 = Color(0xFFEC4899)
val Red = Color(0xFFEF4444)
val Green = Color(0xFF10B981)
val White = Color(0xFFFFFFFF)
val Gray100 = Color(0xFFF3F4F6)
val Gray200 = Color(0xFFE5E7EB)
val Gray300 = Color(0xFFD1D5DB)
val Gray400 = Color(0xFF9CA3AF)
val Gray600 = Color(0xFF4B5563)
val Gray800 = Color(0xFF1F2937)
val Gray900 = Color(0xFF111827)

val lightColorScheme = lightColorScheme(
    primary = Indigo500,
    secondary = Cyan400,
    tertiary = Pink500,
    error = Red,
    background = White,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onError = White,
    onBackground = Gray800,
    onSurface = Gray800,
    surfaceVariant = Gray100,
    outline = Gray300
)

val darkColorScheme = darkColorScheme(
    primary = Color(0xFFA5B4FC),
    secondary = Cyan400,
    tertiary = Pink500,
    error = Red,
    background = Gray900,
    surface = Gray800,
    onPrimary = Gray900,
    onSecondary = Gray900,
    onTertiary = Gray900,
    onError = Gray900,
    onBackground = Color(0xFFF3F4F6),
    onSurface = Color(0xFFF3F4F6),
    surfaceVariant = Gray800,
    outline = Gray600
)
```

### Type.kt

```kotlin
package com.payflow.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val RobotoFontFamily = FontFamily(
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

val typography = Typography(
    displayLarge = TextStyle(
        fontFamily = RobotoFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 57.sp,
        lineHeight = 64.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = RobotoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    titleLarge = TextStyle(
        fontFamily = RobotoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = RobotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RobotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = RobotoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
)
```

### Theme.kt

```kotlin
package com.payflow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun PayFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkColorScheme else lightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = {
            Surface(content = content)
        }
    )
}
```

---

## 🧩 Componentes Reutilizáveis

### PrimaryButton.kt

```kotlin
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = Color(0xFFD1D5DB),
            disabledContentColor = Color(0xFF9CA3AF)
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 3.dp,
            pressedElevation = 6.dp
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
```

### SubscriptionCard.kt

```kotlin
@Composable
fun SubscriptionCard(
    name: String,
    category: String,
    dueDate: String,
    value: Double,
    icon: String,
    categoryColor: Color = Color(0xFF06B6D4),
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.marginBottom(8.dp)
                ) {
                    Text(
                        text = icon,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.marginEnd(8.dp)
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = categoryColor,
                    modifier = Modifier.marginBottom(8.dp)
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(4.dp, 2.dp)
                    )
                }

                Text(
                    text = dueDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF6B7280)
                )
            }

            Text(
                text = "R$ %.2f".format(value),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End
            )
        }
    }
}
```

### NotificationItem.kt

```kotlin
@Composable
fun NotificationItem(
    title: String,
    message: String,
    icon: String,
    iconBackgroundColor: Color = Color(0xFF06B6D4),
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, iconBackgroundColor),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = iconBackgroundColor,
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = icon,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.marginTop(4.dp)
                )
            }
        }
    }
}
```

### PayFlowBottomNavigation.kt (Versão 1)

```kotlin
@Composable
fun PayFlowBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    notificationCount: Int = 0
) {
    NavigationBar(
        modifier = Modifier.height(80.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            BottomNavItem("home", "🏠", "Home"),
            BottomNavItem("history", "📜", "Histórico"),
            BottomNavItem("notifications", "🔔", "Notif."),
            BottomNavItem("profile", "👤", "Perfil")
        )

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Box {
                        Text(item.icon, fontSize = 24.sp)
                        if (item.route == "notifications" && notificationCount > 0) {
                            Badge(
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Text(notificationCount.toString())
                            }
                        }
                    }
                },
                label = {
                    Text(
                        item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) }
            )
        }
    }
}

data class BottomNavItem(val route: String, val icon: String, val label: String)
```

### PayFlowNavigationDrawer.kt (Versão 2)

```kotlin
@Composable
fun PayFlowNavigationDrawer(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.75f)) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 16.dp)
                ) {
                    val items = listOf(
                        DrawerItem("home", "🏠", "Home"),
                        DrawerItem("history", "📜", "Histórico"),
                        DrawerItem("notifications", "🔔", "Notificações"),
                        DrawerItem("profile", "👤", "Perfil")
                    )

                    items.forEach { item ->
                        NavigationDrawerItem(
                            icon = {
                                Text(item.icon, fontSize = 20.sp)
                            },
                            label = {
                                Text(
                                    item.label,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            selected = currentRoute == item.route,
                            onClick = { onNavigate(item.route) },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    NavigationDrawerItem(
                        icon = { Text("⚙️", fontSize = 20.sp) },
                        label = { Text("Configurações") },
                        selected = false,
                        onClick = { /* navigate to settings */ },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    NavigationDrawerItem(
                        icon = { Text("🚪", fontSize = 20.sp) },
                        label = { Text("Sair") },
                        selected = false,
                        onClick = onLogout,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    ) {
        // Content
    }
}

data class DrawerItem(val route: String, val icon: String, val label: String)
```

### PayFlowTopAppBar.kt

```kotlin
@Composable
fun PayFlowTopAppBar(
    title: String,
    onMenuClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
    actionIcon: String = "⋮",
    showMenu: Boolean = true,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = if (showMenu) {
            {
                IconButton(onClick = onMenuClick) {
                    Text("☰", fontSize = 24.sp)
                }
            }
        } else null,
        actions = {
            IconButton(onClick = onActionClick) {
                Text(actionIcon, fontSize = 20.sp)
            }
        },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = TopAppBarDefaults.topAppBarElevation(elevation = 4.dp)
    )
}
```

### PayFlowFAB.kt

```kotlin
@Composable
fun PayFlowFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: String = "➕"
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(
            text = icon,
            fontSize = 28.sp
        )
    }
}
```

---

## 📱 Estrutura de Screens

### LoginScreen.kt

```kotlin
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoginSuccessful) {
        if (uiState.isLoginSuccessful) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    "PF",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "PayFlow",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            "Gerencie suas assinaturas",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6B7280),
            modifier = Modifier.marginTop(8.dp).marginBottom(32.dp)
        )

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .marginBottom(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(8.dp)
        )

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            modifier = Modifier
                .fillMaxWidth()
                .marginBottom(16.dp),
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(8.dp)
        )

        // Remember Me
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .marginBottom(24.dp)
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it }
            )
            Text(
                "Lembrar acesso",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.marginStart(8.dp)
            )
        }

        // Login Button
        PrimaryButton(
            text = "Entrar",
            onClick = {
                viewModel.login(email, password)
            },
            isLoading = uiState.isLoading
        )

        // Error State
        if (uiState.error != null) {
            Text(
                uiState.error ?: "",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.marginTop(16.dp)
            )
        }
    }
}
```

### HomeScreen.kt (Versão 1 - Bottom Nav)

```kotlin
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onSubscriptionClick: (String) -> Unit,
    onAddSubscriptionClick: () -> Unit,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            PayFlowBottomNavigation(
                currentRoute = currentRoute,
                onNavigate = onNavigate,
                notificationCount = uiState.notificationCount
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Olá, João! 👋",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    "Seu resumo financeiro",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.marginTop(4.dp)
                )
            }

            item {
                SummaryCard(
                    label = "Gastos esse mês",
                    value = "R$ %.2f".format(uiState.monthlyTotal),
                    backgroundColor = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Text(
                    "Assinaturas Ativas",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(uiState.subscriptions) { subscription ->
                SubscriptionCard(
                    name = subscription.name,
                    category = subscription.category,
                    dueDate = subscription.dueDate,
                    value = subscription.value,
                    icon = subscription.icon,
                    onClick = { onSubscriptionClick(subscription.id) }
                )
            }
        }
    }
}

@Composable
fun SummaryCard(
    label: String,
    value: String,
    backgroundColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                alpha = 0.9f
            )
            Text(
                value,
                style = MaterialTheme.typography.displaySmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.marginTop(8.dp)
            )
        }
    }
}
```

---

## 🏗️ ViewModel Example

### LoginViewModel.kt

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val result = authRepository.login(email, password)
                if (result.isSuccessful) {
                    userRepository.saveUser(result.data)
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            isLoginSuccessful = true
                        ) 
                    }
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = result.error
                        ) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Erro ao conectar: ${e.message}"
                    ) 
                }
            }
        }
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val error: String? = null
)
```

---

## 📚 Outras Referências

- **Material Design 3 Tokens:** `androidx.compose.material3.ColorScheme`
- **Spacing System:** Use `spacing4`, `spacing8`, `spacing16`, etc.
- **Shape System:** Use `RoundedCornerShape` com 4.dp, 8.dp, 12.dp, 16.dp, 20.dp
- **Elevation:** Use `CardDefaults.cardElevation()` com 0.dp, 2.dp, 4.dp, 6.dp, 8.dp

---

**Status:** ✅ Pronto para Implementação  
**Versão:** 1.0  
**Última Atualização:** 15 de Maio de 2026
