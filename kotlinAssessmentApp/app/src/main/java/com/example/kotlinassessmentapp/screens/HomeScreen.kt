//package com.example.kotlinassessmentapp.screens
//
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Menu
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Divider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.kotlinassessmentapp.R
//
//@Composable
//fun HomeScreen() {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = Color.White)
//            .padding(horizontal = 16.dp)
//            .verticalScroll(rememberScrollState()) // ✅ Make screen scrollable
//    ) {
//        // Header row
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp)
//                .windowInsetsPadding(WindowInsets.statusBars),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                imageVector = Icons.Default.Menu,
//                contentDescription = "Menu",
//                tint = Color(0xFF27214D),
//                modifier = Modifier.size(28.dp)
//            )
//
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Image(
//                    painter = painterResource(R.drawable.shopping_basket),
//                    contentDescription = "Basket",
//                    modifier = Modifier.size(28.dp)
//                )
//                Text(
//                    text = "My Basket",
//                    fontSize = 12.sp,
//                    color = Color(0xFF27214D)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Divider(color = Color.LightGray, thickness = 1.dp)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//            text = "No, ShopClues is not closed. It was acquired by Qoo10 in 2019 and continues to operate as part of Qoo10's global e-commerce platform...",
//            color = Color.DarkGray,
//            fontSize = 14.sp,
//            lineHeight = 20.sp
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // ✅ Wrap grid with Box and give fixed height
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .heightIn(min = 200.dp, max = 800.dp) // Adjust height as needed
//        ) {
//            ProductGrid()
//        }
//    }
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun ProductGrid() {
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(2),
//        contentPadding = PaddingValues(16.dp),
//        horizontalArrangement = Arrangement.spacedBy(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight() // ✅ Fixed crash from fillMaxSize
//    ) {
//        items(10) { index ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(0.8f)
//                    .padding(4.dp),
//                shape = RoundedCornerShape(8.dp),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(8.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Image(
//                        painter = painterResource(R.drawable.food_1),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .height(100.dp)
//                            .fillMaxWidth()
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = "Item Name $index",
//                        color = Color.Black,
//                        fontSize = 16.sp
//                    )
//
//                    Text(
//                        text = "$${(index + 1) * 10}",
//                        color = Color.DarkGray,
//                        fontSize = 14.sp
//                    )
//
//                    Text(
//                        text = "This is a sample item description that should overflow with ellipsis when too long.",
//                        fontSize = 12.sp,
//                        maxLines = 2,
//                        overflow = TextOverflow.Ellipsis,
//                        color = Color.Gray,
//                        modifier = Modifier.padding(top = 4.dp)
//                    )
//                }
//            }
//        }
//    }
//}
