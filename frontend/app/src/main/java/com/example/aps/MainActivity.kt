package com.example.aps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aps.Screens.LoginScreen
import com.example.aps.Screens.Navgation
import com.example.aps.ui.theme.APSTheme
import com.example.aps.DarkTheme.SettingsViewModel
import com.example.aps.data.repository.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var onNavigateToHome: (() -> Unit)? = null

    private val settingsViewModel: SettingsViewModel by viewModels()
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            val isDarkMode by settingsViewModel.getDarkModeState(applicationContext).collectAsState(initial = false)

            APSTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                var textSizeFactor by remember { mutableStateOf(1.0f) }

                onNavigateToHome = remember {
                    {
                        navController.navigate("nav_graph") {
                            popUpTo("login_screen") { inclusive = true }
                        }
                    }
                }

                MainContent(
                    auth = auth,
                    googleSignInClient = googleSignInClient,
                    navController = navController,
                    activity = this,
                    textSizeFactor = textSizeFactor,
                    onTextSizeChange = { newSize -> textSizeFactor = newSize },
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                auth.signInWithCredential(GoogleAuthProvider.getCredential(account.idToken!!, null))
                    .addOnCompleteListener(this) { firebaseTask -> // <-- Renomeado para firebaseTask para evitar conflito com 'task'
                        if (firebaseTask.isSuccessful) {
                            val user = auth.currentUser
                            if (user != null) {
                                Log.i("SupabaseSync", "--- INFORMAÇÕES DO USUÁRIO PARA SUPABASE ---")
                                Log.i("SupabaseSync", "UID: ${user.uid}")
                                Log.i("SupabaseSync", "Nome: ${user.displayName}")
                                Log.i("SupabaseSync", "Email: ${user.email}")
                                Log.i("SupabaseSync", "Photo URL: ${user.photoUrl?.toString()}")
                                Log.i("SupabaseSync", "---------------------------------------------")

                                // 🔑 AGORA COM LOG DETALHADO DO RESULTADO DA API
                                lifecycleScope.launch {
                                    val syncResult = userRepository.syncUserWithSupabase(user)
                                    syncResult.onSuccess { success ->
                                        if (success) {
                                            Log.d("MainActivity", "POST API: Sincronização de usuário SUCEDIDO para ${user.uid}")
                                        } else {
                                            Log.e("MainActivity", "POST API: Sincronização de usuário FALHOU")
                                        }
                                    }.onFailure { exception ->
                                        Log.e("MainActivity", "pOST API: Erro na requisição de sincronização: ${exception.message}", exception)
                                    }
                                }
                                // FIM DA INTEGRAÇÃO
                            }
                            Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                            onNavigateToHome?.invoke()
                        } else {
                            Toast.makeText(this, "Erro de login: ${firebaseTask.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } catch (e: ApiException) {
                Log.w("MainActivity", "Google sign in failed", e)
                Toast.makeText(this, "Falha no login com Google", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        internal const val RC_SIGN_IN = 9001
    }
}

@Composable
fun MainContent(
    auth: FirebaseAuth,
    googleSignInClient: GoogleSignInClient,
    navController: NavController,
    activity: ComponentActivity,
    textSizeFactor: Float,
    onTextSizeChange: (Float) -> Unit,
    settingsViewModel: SettingsViewModel
) {
    val startGoogleSignIn: () -> Unit = {
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, MainActivity.RC_SIGN_IN)
    }

    NavHost(
        navController = navController as NavHostController,
        startDestination = "login_screen"
    ) {
        composable("login_screen") {
            LoginScreen(
                navController = navController,
                onGoogleSignInClicked = startGoogleSignIn
            )
        }
        composable("nav_graph") {
            Navgation(
                textSizeFactor = textSizeFactor,
                onTextSizeChange = onTextSizeChange,
                settingsViewModel = settingsViewModel
            )
        }
    }
}