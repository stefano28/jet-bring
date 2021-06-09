package com.example.jet_bring.ui.ispirazione

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.jet_bring.model.Ricetta
import com.example.jet_bring.ui.utils.InputText

@Composable
fun paddingBasedOnOrientation() : Int{
    if(LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE){
        return 12
    }else{
        return 6
    }

}





/**
 * Funzione che nasconde l'effetto di tocco sulla card della ricetta
 * */
inline fun Modifier.noRippleClickable(crossinline onClick: ()->Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

/**
 *
 * Funzione che ritorna una card delle ricette
 *
 * */

@Composable
fun RicetteCard (
    ricetta: Ricetta,
    navController: NavHostController,
    route: String
) {

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(
                bottom = 6.dp,
                top = 6.dp,
                start = 16.dp,
                end = 16.dp

            )
            .fillMaxWidth()
            .noRippleClickable {
                navController.navigate("$route/${ricetta.id}") {
                }
            },


        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.background


    ){
        Column() {
            ricetta.pubblicatore?.let { titolo ->

                Text(
                    text = titolo,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .wrapContentWidth(Alignment.Start)
                        .padding(start = 5.dp, end = 5.dp),
                    fontSize = 15.sp
                )

            }
            ricetta.descrizione?.let { descrizione ->

                Text(
                    text = descrizione,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp),
                    fontSize = 15.sp

                )

            }
            ricetta.titolo?.let { titolo ->

                Text(
                    text = titolo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.Start)
                        .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 3.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )

            }

            ricetta.immagine?.let { url ->

                Image(
                    painter = painterResource(id = ricetta.immagine!!),
                    modifier = Modifier
                        .fillMaxWidth(0.97F)
                        .clip(shape = RoundedCornerShape(16.dp))
                        .height(275.dp)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop,
                    contentDescription = ""
                )
            }
            AlignInRow(ricetta)

        }

        /**
         * Funzioni per disegnare elementi grafici
         * */
        Canvas(
            //Rettangolo grigio
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = paddingBasedOnOrientation().dp, top = 98.dp
                )){
            drawRect(

                color = Color.LightGray,
                size = androidx.compose.ui.geometry.Size(130F, 60F)
            )
            //Cerchio grigio con parametri del centro
            val canvasWidth = size.width
            val canvasHeight = size.height
            drawCircle(
                color = Color.LightGray,
                center = Offset(x = 136f , y = 30f),
                radius = 30.00f
            )
        }
        Text(text = "Ricetta" ,modifier = Modifier
            .fillMaxWidth()
            .padding(start = (paddingBasedOnOrientation()+4).dp, top = 99.dp),
            color = Color.Black,
            fontFamily = FontFamily.Cursive
        )

    }
}


@ExperimentalComposeUiApi
@Composable
fun AlertDialogLink(link: String, onModifiedText: (String) -> Unit, addRicettaViewModel: AddRicettaViewModel) {

    Column {
        val openDialog = remember { mutableStateOf(false)  }

        Button(onClick = {
            openDialog.value = true
        }) {
            Icon(Icons.Filled.Link, contentDescription = "", tint = MaterialTheme.colors.onBackground, modifier = Modifier.size(30.dp) )
        }

        if (openDialog.value) {

            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    openDialog.value = false
                },
                title = {
                    Text(text = "Aggiungi Link")
                },
                text = {
                    InputText(text = link, onModifiedText, modifier = Modifier.fillMaxWidth())

                },

                confirmButton = {
                    Button(

                        onClick = {
                            addRicettaViewModel.onSaveDone()
                            openDialog.value = false
                        }) {
                        Text("ANNULLA")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                        }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}



@Composable
fun AlignInRow(ricetta: Ricetta,) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val context = LocalContext.current
        val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(ricetta.sourceUrl)) }


        Button(
            onClick ={ context.startActivity(intent) },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.background,
            ),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp
            ),

            ) {
            Icon(Icons.Rounded.Link, contentDescription = "Localized description", tint = MaterialTheme.colors.onBackground)
            Text(
                text = "Vedi la ricetta",
                color = MaterialTheme.colors.onBackground
            )
        }
        Button(
            onClick = { /* Do something! */ },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.background
            ),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp
            ),
            //elevation = ButtonElevation.elevation(enabled = false, interactionSource = null )

        ) {
            Icon(Icons.Rounded.Share, contentDescription = "Localized description", tint = MaterialTheme.colors.onBackground )
            Text(
                text = "Condividi",
                color = MaterialTheme.colors.onBackground
            )
        }
        Button(onClick = { /* Do something! */ },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.background,
            ),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp
            ),
        ) {
            Icon(Icons.Rounded.Favorite, contentDescription = "Localized description", tint = MaterialTheme.colors.onBackground)
            Text(
                text = ricetta.voti.toString(),
                color = MaterialTheme.colors.onBackground
            )
        }



    }
}


