package com.example.crud4.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.crud4.R

@Composable
fun MyPage(navController: NavController) {
    var isClicked by remember { mutableStateOf(false) }

//    Button(
//        onClick = { isClicked = !isClicked }
//    ) {
//        Text(if (isClicked) "Clicked!" else "Click Me")
//    }
    Scaffold(
        bottomBar = {
            BottomAppBar {


            }
        }
    ) {

        innerPadding->
        Column(modifier = Modifier.padding(innerPadding)) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.LightGray)
            ) {
                Text("Bottom", modifier = Modifier.align(Alignment.BottomStart))
                Text("Center", modifier = Modifier.align(Alignment.Center))
                Text("Top", modifier = Modifier.align(Alignment.TopEnd))
            }

            Box(
                modifier = Modifier
                    .background(Color.Red)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

                Column {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background), // load drawable
                        contentDescription = "My icon",
                        modifier = Modifier.size(64.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text("hello")
                    Text("hello")
                    Text("hello")
                    Text("hello")
                    Row {
                        Text("Bye")
                        Text("Bye")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End, // mainAxisAlignment
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = {}) {
                            Text("Bye")
                        }
                        Button(onClick = {}) {
                            Text("Bye")
                        }
                        Button(onClick = {}) {
                            Text("Bye")
                        }
                    }
                }
//                Text("I am inside a Container-like Box", color = Color.White)
            }


        }
    }
}

