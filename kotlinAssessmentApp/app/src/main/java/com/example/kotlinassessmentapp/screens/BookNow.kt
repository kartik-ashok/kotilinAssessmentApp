//package com.example.kotlinassessmentapp.screens
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.scrollable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ChevronLeft
//import androidx.compose.material.icons.filled.Cloud
//import androidx.compose.material.icons.filled.EnergySavingsLeaf
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material.icons.filled.PunchClock
//import androidx.compose.material.icons.filled.Save
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.Card
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.kotlinassessmentapp.R
//import com.example.kotlinassessmentapp.ui.theme.AppTypography
//
//@Composable
//fun BookNow(modifier: Modifier = Modifier) {
//    val typography = AppTypography();
//    val scroll= rememberScrollState();
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .background(color = Color.White)
//            .padding(30.dp)
//            .verticalScroll(scroll)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(460.dp)
//                .clip(RoundedCornerShape(20.dp)) // Apply rounded corners to the box
//        ) {
//            Image(
//                painter = painterResource(R.drawable.mountains),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxSize(),
//                contentScale = ContentScale.Crop
//            )
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = 15.dp, vertical = 15.dp),
//                verticalArrangement = Arrangement.SpaceBetween,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Row(
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Card(
//                        shape = CircleShape,
//                    ) {
//
//                        Icon(
//                            Icons.Filled.ChevronLeft,
//                            contentDescription = null,
//                            modifier.padding(5.dp)
//                        )
//                    }
//                    Card(
//                        shape = CircleShape,
//                    ) {
//                        Icon(
//                            Icons.Filled.EnergySavingsLeaf,
//                            contentDescription = null,
//                            modifier.padding(5.dp)
//                        )
//
//
//                    }
//                }
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(90.dp),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(15.dp),
//                        verticalArrangement = Arrangement.SpaceBetween,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 10.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text(
//                                text = "Andes Mountain",
//                                style = typography.headlineMedium.copy(
//                                    color = Color.White,
//                                    fontWeight = FontWeight.W500,
//                                    fontSize = 20.sp
//                                )
//                            )
//                            Text(text = "Price")
//                        }
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 10.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Row {
//                                Icon(Icons.Filled.LocationOn, contentDescription = null)
//                                Text(text = "South, America")
//                            }
//                            Text(text = "$230")
//                        }
//                    }
//                }
//            }
//
//
//        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//        ) {
//            Text(text = "Overview")
//            Spacer(modifier.width(15.dp))
//            Text(text = "Details")
//        }
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row {
//                Icon(Icons.Filled.PunchClock, contentDescription = null)
//                Text("8 Hours")
//
//            }
//            Row {
//                Icon(Icons.Filled.Cloud, contentDescription = null)
//                Text("16 C")
//
//            }
//            Row {
//                Icon(Icons.Filled.Star, contentDescription = null)
//                Text("4.5")
//
//            }
//        }
//
//        Text(
//            text = "If you sent individual emails to different people and included all emails in the body or headers manually, they may have become visible.If you sent individual emails to different people and included all emails in the body or headers manually, they may have become visible.If you sent individual emails to different people and included all emails in the body or headers manually, they may have become visible.If you sent individual emails to different people and included all emails in the body or headers manually, they may have become visible.",
//
//            modifier = Modifier.padding(10.dp)
//        )
//
//        TextButton(
//            onClick = { /* Handle click */ },
//            modifier = Modifier
//                .padding(10.dp)
//                .fillMaxWidth()
//                .background(color = Color.Black)
//        ) {
//            Text(text = "Confirm Booking")
//        }
//    }
//}


package com.example.kotlinassessmentapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinassessmentapp.R
import com.example.kotlinassessmentapp.ui.theme.AppTypography

@Composable
fun BookNow(modifier: Modifier = Modifier) {
    val typography = AppTypography()
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(30.dp)

    ) {
        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Leave space for fixed button
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(460.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(R.drawable.mountains),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Card(shape = CircleShape) {
                            Icon(
                                Icons.Filled.ChevronLeft,
                                contentDescription = null,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                        Card(shape = CircleShape) {
                            Icon(
                                Icons.Filled.EnergySavingsLeaf,
                                contentDescription = null,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.5f)  // 80% opacity
                        ),
                        elevation = CardDefaults.cardElevation(8.dp)

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Andes Mountain",
                                    style = typography.headlineMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.W500,
                                        fontSize = 20.sp
                                    )
                                )
                                Text(text = "Price")
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row {
                                    Icon(Icons.Filled.LocationOn, contentDescription = null)
                                    Text(text = "South, America")
                                }
                                Text(text = "$230")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Overview")
                Spacer(modifier = Modifier.width(15.dp))
                Text(text = "Details")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(Icons.Filled.PunchClock, contentDescription = null)
                    Text("8 Hours")
                }
                Row {
                    Icon(Icons.Filled.Cloud, contentDescription = null)
                    Text("16 C")
                }
                Row {
                    Icon(Icons.Filled.Star, contentDescription = null)
                    Text("4.5")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(

                text = "If you sent individual emails to different people and included all emails in the body or headers manually, they may have become visible.".repeat(
                    8
                ),
//                maxLines = 3,
//                overflow = TextOverflow.Ellipsis,
//                modifier = Modifier.padding(16.dp),
                style = TextStyle(
                    textAlign = TextAlign.Justify
                ),
            )
        }

        // Fixed Bottom Button
        TextButton(
            onClick = { /* Handle click */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(color = Color.Black, shape = RoundedCornerShape(10.dp))
        ) {
            Text(
                text = "Confirm Booking",
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}


//🔍 Without padding:
//The bottom content may be hidden behind the button.
//
//✅ With padding:
//The bottom content stays fully visible and accessible, even with the button fixed in place.
//
//Let me know if you'd like a 5-line working example to try in your project!
