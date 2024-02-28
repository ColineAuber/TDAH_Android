package com.example.tdah

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.tdah.ui.theme.green
import com.example.tdah.ui.theme.red
import com.example.tdah.ui.theme.theme
import java.io.File


@Composable
fun Refus_collecte(navController: NavController, idFile : File) {

    Surface(
    color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly){
                Box(
                    modifier = Modifier
                        .size(400.dp, 60.dp)
                        .background(color = theme),

                    ) {
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Etes vous certain?\n" +
                            "Vous ne pourrez pas utiliser l’application",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical= 5.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column {
                    Button(
                        onClick = {
                            File(idFile, "autorisation.txt").writeText("1");
                            navController.navigate("Accord_collecte") },

                        colors = ButtonDefaults.buttonColors(green)
                    ) {
                        Text(
                            text = "J’autorise\n" + "la collecte",
                            fontSize = 10.sp
                        )
                    }
                }
                Column {
                    Text(
                        text = "",
                        modifier = Modifier.padding(start = 10.dp)

                    )
                }
                Column {
                    Button(
                        onClick = { navController.navigate("Bienvenue") },
                        colors = ButtonDefaults.buttonColors(red)
                    ) {
                        Text(
                            text = "Je refuse\n" + "la collecte",
                            fontSize = 10.sp
                        )
                    }
                }

            }
        }
    }
}