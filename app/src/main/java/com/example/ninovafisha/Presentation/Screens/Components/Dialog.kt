package com.example.ninovafisha.Presentation.Screens.Components

import android.app.Dialog
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun Dialog(showConfirmationDialog:Boolean, onClick: () -> Unit, onDismissRequest: () -> Unit, onClickNo: () -> Unit, title:String, desc:String){
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text("${title} ")
        },
        text = {
            Text("${desc} ")
        },
        confirmButton = {
            TextButton(
                onClick = onClick
            ) {
                Text("Да")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onClickNo
            ) {
                Text("Нет")
            }
        }
    )
}