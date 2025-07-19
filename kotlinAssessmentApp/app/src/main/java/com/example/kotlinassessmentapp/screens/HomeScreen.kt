import android.R.attr.text
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import com.example.kotlinassessmentapp.R;
import com.example.kotlinassessmentapp.ui.theme.AppTypography

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


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val typography = AppTypography()
    var text by remember { mutableStateOf("") }
    val fruits = listOf("Apple", "Banana", "Orange", "Mango", "Strawberry")
    val scrollState = rememberScrollState()



    Column(
//        verticalArrangement = Arrangement.spacedBy(16.dp),// 16.dp space between children
        modifier = modifier
            .background(color = Color.White)
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(25.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically // optional, to vertically center
        ) {
            Text("Hello Kartik", style = typography.headlineMedium)

            Image(
                painter = painterResource(R.drawable.img),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp) // ensure small and circular
                    .clip(CircleShape)

            )
        }
        Spacer(Modifier.height(4.dp))

        Text("Explore The World", style = typography.headlineSmall)
//        Row(
//            modifier
//                .border(
//                    width = 2.dp,
//                    color = Color.Black
//                )
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween, // between children
////            verticalAlignment = Alignment.CenterVertically // align children vertically inside row
//        ) {
//            Text("Row 1", style = typography.labelMedium)
//            Icon(
//                imageVector = Icons.Filled.Agriculture,
//                contentDescription = "Home Icon",
//                tint = Color.Black// Optional: sets the color of the icon
//            )
//        }
        var text by remember { mutableStateOf("") }

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Email") },
            placeholder = { Text("example@mail.com") },
            singleLine = true,

            // ✅ 1. Change TextField height & text size
            textStyle = TextStyle(fontSize = 18.sp), // text size
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp), // adjust height

            // ✅ Rounded corners
            shape = RoundedCornerShape(16.dp), // Change radius as needed

            // ✅ 2. Change border color
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Gray

            ),

            // ✅ 3. Add leading icon (optional)
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Filled.VerifiedUser,
//                    contentDescription = "Email Icon"
//                )
//            },

            // ✅ 4. Add trailing icon (optional)
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.VerifiedUser,
                    contentDescription = "Clear Icon"
                )
            }
        )


        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()

        ) {
            Text("Popular Places")
            Text("View All")
        }

        LazyRow(
            modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp), // space between items


        ) {
            items(fruits.size) {
                Card(
                    modifier
//                        .background(Color.Black.copy(alpha = 0.5f))
//                        .height(10.dp)
//                        .width(30.dp)
//                        .padding(8.dp)
                ) {
                    Text(fruits[it], modifier.padding(10.dp))

                }
            }
        }
        Spacer(modifier.height(20.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
//                .background(Color.Red)
//                .padding(horizontal = 16.dp), // Optional: outer padding
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(10) {
                Box(
                    modifier = Modifier
                        .width(270.dp)
                        .height(450.dp)
                        .background(
                            Color.Green,
                            shape = RoundedCornerShape(16.dp)
                        )

                ) {
                    Image(
                        painter = painterResource(R.drawable.green_land),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .shadow(
                                elevation = 18.dp,                      // Z-axis height
//                                shape = RoundedCornerShape(12.dp),     // Optional: rounded corners
//                                clip = true                           // Avoid clipping shadow

                            )
                            .clip(RoundedCornerShape(16.dp)),


                        contentScale = ContentScale.Crop

                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp) // Set desired height
                            .align(Alignment.BottomCenter) // Align to bottom center
                            .padding(10.dp)
                            .clip(RoundedCornerShape(16.dp))

                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center,


                        ) {
//                        Card {
//
//                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
//                                .background(color = Color.Black.copy(alpha = 0.5f),RoundedCornerShape(16.dp))
//                                .clip(RoundedCornerShape(16.dp))

                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Row(
                                modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
//                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Mount Foji")
                                Text("USA")
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,

                                modifier = Modifier.fillMaxSize()


                            ) {
                                Icon(Icons.Filled.AddLocation, contentDescription = null)
                                Text("California,USA")
                                Row {
                                    Icon(Icons.Filled.StarRate, contentDescription = null)
                                    Text("4.8")

                                }
                            }
                        }
                    }
                }
            }
        }

//Button has its own default background and content color defined by the Material Theme, which ignores the Modifier.background(...) you applied outside the button content.
        Button(
            onClick = { /* Handle button click */ },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow,  // Background color
                contentColor = Color.Black      // Text color
            )
        ) {
            Text("Hello")
        }

        Spacer(modifier.height(20.dp))

        Button(
            onClick = { /* Handle button click */ },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow,  // Background color
                contentColor = Color.Black      // Text color
            )
        ) {
            Text("Hello")
        }
        Spacer(modifier.height(20.dp))

        Button(
            onClick = { /* Handle button click */ },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow,  // Background color
                contentColor = Color.Black      // Text color
            )
        ) {
            Text("Hello")
        }



    }
}


//| Goal                       | Use                                                            |
//| -------------------------- | -------------------------------------------------------------- |
//| Vertical layout            | `Column`                                                       |
//| Horizontal layout          | `Row`                                                          |
//| Overlapping/layout control | `Box`                                                          |
//| Just setting background    | Use `.background()` on the main layout (`Column`, `Box`, etc.) |


//| Feature                     | Included by Default | With `material-icons-extended` |
//| --------------------------- | ------------------- | ------------------------------ |
//| Basic icons (`Menu`, `Add`) | ✅ Yes               | ✅ Yes                          |
//| All Material icons          | ❌ No                | ✅ Yes                          |
//| Styles (Outlined, Rounded)  | ❌ Limited           | ✅ Full support                 |


//| Button Type         | Background | Border | Elevation | Text/Icon |
//| ------------------- | ---------- | ------ | --------- | --------- |
//| `Button`            | ✔️ Yes     | ❌ No   | ✔️        | Text/Icon |
//| `OutlinedButton`    | ❌ No       | ✔️ Yes | ❌         | Text/Icon |
//| `TextButton`        | ❌ No       | ❌ No   | ❌         | Text only |
//| `IconButton`        | ❌ No       | ❌ No   | ❌         | Icon only |
//| `ElevatedButton`    | ✔️ Yes     | ❌ No   | ✔️        | Text/Icon |
//| `FilledTonalButton` | ✔️ Light   | ❌ No   | ✔️        | Text      |
