package org.cybersafety.ticket.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import presentation.component.Spacer_16dp
import presentation.theme.bannerFocusedIndicatorColor
import presentation.theme.bannerUnfocusedIndicatorColor

@Composable
fun AutoAdvancePager(pageItems: List<DrawableResource>, modifier: Modifier = Modifier) {
    Box(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        val pagerState = rememberPagerState(pageCount = { pageItems.size })
        val pagerIsDragged by pagerState.interactionSource.collectIsDraggedAsState()

        val pageInteractionSource = remember { MutableInteractionSource() }
        val pageIsPressed by pageInteractionSource.collectIsPressedAsState()

        // Stop auto-advancing when pager is dragged or one of the pages is pressed
        val autoAdvance = !pagerIsDragged && !pageIsPressed

        if (false) {
            LaunchedEffect(pagerState, pageInteractionSource) {
                while (true) {
                    delay(2000)
                    val nextPage = (pagerState.currentPage + 1) % pageItems.size
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }
        Column() {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) { page ->
//            AsyncImage(
//                model = imageUrls[page],
//                contentDescription = "Image $page",
//                modifier = Modifier.fillMaxSize()
//            )
                Image(
                    painter = painterResource(pageItems[page]),
                    contentDescription = "Image $page",
                    modifier =  modifier.fillMaxWidth().height(160.dp)
                        .clickable(
                            interactionSource = pageInteractionSource,
                            indication = LocalIndication.current
                        ) {
                            // Handle page click
                        }
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                )
            }

            Spacer_16dp()

            PagerIndicator(pageItems.size, pagerState.currentPage)
        }

    }
}

@Composable
fun PagerIndicator(pageCount: Int, currentPageIndex: Int, modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.Start
        ) {
            repeat(pageCount) { iteration ->
                val color = if (currentPageIndex == iteration) bannerFocusedIndicatorColor else bannerUnfocusedIndicatorColor
                val shape = if (currentPageIndex == iteration) RoundedCornerShape(8.dp) else CircleShape
                val width = if (currentPageIndex == iteration) 19.dp else 6.dp
                Box(
                    modifier = modifier
                        .padding(2.dp)
                        .clip(
                            shape
                        )
                        .background(color)
                        .width(width)
                        .height(6.dp)
                )
            }
        }
    }
}