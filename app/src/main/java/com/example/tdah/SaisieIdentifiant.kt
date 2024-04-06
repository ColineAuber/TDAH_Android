package com.example.tdah

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import com.example.tdah.ui.theme.theme
import java.io.File

@Composable
fun UsernameInputField(
    onUsernameChanged: (String) -> Unit, // Callback pour notifier les changements de l'identifiant
    onValidation: () -> Unit, // Fonction de validation à appeler lorsque l'utilisateur termine la saisie
    modifier: Modifier = Modifier // Modifier pour personnaliser l'apparence du champ d'entrée
) {
    var username by remember { mutableStateOf("") } // État pour conserver la valeur de l'identifiant

    OutlinedTextField(
        value = username,
        onValueChange = {
            username = it
            onUsernameChanged(it) // Appel du callback lorsqu'il y a un changement dans le champ d'entrée
        },
        label = { Text("Identifiant") }, // Libellé du champ d'entrée
        singleLine = true, // Option pour restreindre la saisie à une seule ligne
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), // Modifier pour définir la largeur et la marge du champ d'entrée
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text, // Type de clavier pour la saisie de texte
            imeAction = ImeAction.Done // Action du clavier lorsque l'utilisateur appuie sur "Done"
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // Appel de la fonction de validation lorsque l'utilisateur appuie sur "Done" dans le clavier
                onValidation()
            }
        ),
        isError = username.isEmpty(), // Détermine si le champ d'entrée est dans un état d'erreur
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = theme, // Couleur de bordure lorsque le champ est sélectionné
            unfocusedBorderColor = theme // Couleur de bordure lorsque le champ n'est pas sélectionné
        )
    )
}

@Composable
fun SaisieIdentifiant(navController: NavController, idFilePatient : File) {
    var enteredUsername by remember { mutableStateOf("") } // État pour conserver la valeur saisie

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Modifier pour définir la taille et la marge de la colonne
        verticalArrangement = Arrangement.Center, // Alignement vertical des éléments dans la colonne
        horizontalAlignment = Alignment.CenterHorizontally // Alignement horizontal des éléments dans la colonne
    ) {
        UsernameInputField(
            onUsernameChanged = { enteredUsername = it }, // Mise à jour de la valeur saisie
            onValidation = {
                // Logique de validation et navigation
                validateAndNavigate(navController, idFilePatient, enteredUsername)
            }
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espacement entre les éléments

        Button(
            onClick = {
                // Appel de la fonction de validation lorsque l'utilisateur clique sur le bouton "Valider"
                validateAndNavigate(navController, idFilePatient, enteredUsername)
            },
            enabled = enteredUsername.isNotEmpty() // Activation du bouton lorsque l'identifiant n'est pas vide
        ) {
            Text(text = "Valider") // Texte du bouton
        }
    }
}

// Fonction de validation et de navigation
private fun validateAndNavigate(navController: NavController, idFilePatient: File, enteredUsername: String) {
    val autorisation = idFilePatient.parentFile
    if (!autorisation.exists()) {
        autorisation.mkdirs() // Crée le répertoire et tous les répertoires parents s'ils n'existent pas
    }
    println("SaisieIdentifiant idPatient = $enteredUsername") // Affichage de l'identifiant saisi
    idFilePatient.writeText(enteredUsername) // Écriture de l'identifiant dans le fichier
    navController.navigate("Demande_collecte") // Navigation vers la destination suivante
}


