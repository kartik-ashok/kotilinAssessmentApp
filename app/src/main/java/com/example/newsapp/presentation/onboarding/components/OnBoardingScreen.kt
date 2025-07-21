package com.example.newsapp.presentation.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.newsapp.presentation.onboarding.Dimensions.MediumPadding1
import com.example.newsapp.presentation.onboarding.Dimensions.MediumPadding2
import com.example.newsapp.R
import com.example.newsapp.presentation.onboarding.Page
import com.example.newsapp.presentation.onboarding.common.NewsButton
import com.example.newsapp.presentation.onboarding.pages
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@Composable
fun OnBoardingScreen(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(initialPage = 0) { pages.size }
    val coroutineScope = rememberCoroutineScope()
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = true // Set false if using dark theme
    val systemBarColor = MaterialTheme.colorScheme.background

    SideEffect {
        systemUiController.setSystemBarsColor(
            color =systemBarColor,
            darkIcons = useDarkIcons
        )
    }

    // Auto-scroll every 3 seconds
//    LaunchedEffect(pagerState.currentPage) {
//        delay(3000)
//        val nextPage = (pagerState.currentPage + 1) % pages.size
//        pagerState.animateScrollToPage(nextPage)
//    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Limit pager height with weight
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f) // Take available height, but not infinite
                .fillMaxWidth()
        ) { pageIndex ->
            val page = pages[pageIndex]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f),
                    painter = painterResource(id = page.imageId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(MediumPadding1))
                Text(
                    text = page.title,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(horizontal = MediumPadding2)
                )
                Text(
                    text = page.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(horizontal = MediumPadding2)
                )
                Spacer(modifier = Modifier.weight(3f)) // Pushes the next item to the bottom

                NewsButton(
                    modifier = Modifier.align(Alignment.End),
                    text = if (pagerState.currentPage == pages.lastIndex) "Get Started" else "Next",
                    onClick = {
                        coroutineScope.launch {
                            if (pagerState.currentPage < pages.lastIndex) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            } else {
                                // TODO: Navigate to Home or Login screen
                            }
                        }
                    },
                )
                Spacer(modifier = Modifier.weight(1f)) // Pushes the next item to the bottom
                PageIndicator(
                    pagesSize = pages.size,
                    selectedPage = pagerState.currentPage,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

    }
}

