package com.example.kotlinassessmentapp

import android.widget.CheckBox
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp


@Composable
fun CounterScreen(modifier: Modifier = Modifier) {
    var count by remember { mutableStateOf(0) }
    var count1 by rememberSaveable { mutableStateOf(0) }
    Text("$count",modifier= Modifier
            .padding(16.dp)
        ,
        color = colorResource(R.color.yellow)


    )
    Button(
        onClick ={
        count=count+1;
            print(count)
        },

        ) {

        Text("Click Here $count")
    }

}


@Composable
fun ToogleCheckBpx(modifier: Modifier = Modifier) {
    var isChecked by remember { mutableStateOf(false) }
    Checkbox(
        checked = isChecked,
        onCheckedChange = {
            isChecked=it
        }
    )
}