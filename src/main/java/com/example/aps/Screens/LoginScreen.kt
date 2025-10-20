package com.example.aps.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun LoginScreen(
    navController: NavController,
    onGoogleSignInClicked: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = "MOBILIZA",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Image(
                painter = painterResource(R.drawable.mobiliza_login),
                contentDescription = "Login graphic",
                modifier = Modifier.size(180.dp),
                contentScale = ContentScale.Fit
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Conectando passageiros",
                    fontSize = 18.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "Atualizando caminhos.",
                    fontSize = 18.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            SocialLoginButton(
                text = "Logar com Google",
                iconResId = R.drawable.google1,
                onClick = {
                    onGoogleSignInClicked()
                    Log.d("LoginScreen", "Botão Logar com Google clicado!")
                },
                isGoogleButton = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            SocialLoginButton(
                text = "Logar com Facebook",
                iconResId = R.drawable.facebook_icon,
                onClick = {
                    println("Logar com Facebook clicado!")
                }
            )
        }
    }


    // Observa o estado do user
    val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            navController.navigate("nav_graph") {

                popUpTo("login_screen") { inclusive = true }
            }
        }
    }
}

@Composable
fun SocialLoginButton(
    text: String,
    iconResId: Int,
    onClick: () -> Unit,
    isGoogleButton: Boolean = false
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.Black,
            containerColor = Color.Transparent
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = "$text icon",
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = text,
                fontSize = 16.sp
            )
        }
    }
}