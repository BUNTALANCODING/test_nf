package presentation.ui.main.home.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import business.core.UIComponent
import business.domain.main.NewsItem
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import presentation.component.DefaultScreenUI
import presentation.component.Spacer_16dp
import presentation.component.Spacer_4dp
import presentation.component.Spacer_8dp
import presentation.theme.PrimaryColor
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_article_img

@Composable
fun NewsScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToDetail: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Berita & Informasi",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
    ) {
        NewsContent(
            state = state,
            events = events,
            navigateToDetail = navigateToDetail
        )

    }
}

@Composable
private fun NewsContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToDetail: () -> Unit
) {

    val newsItem = listOf(
        NewsItem(
            date = "23 September 2025",
            title = "Program Pemutihan Pajak kendaraan Pemprov Banten",
            imageUrl = Res.drawable.ic_article_img,
            subtitle = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vulputate libero et velit interdum, ac aliquet odio mattis. Class aptent taciti sociosqu ad litora torquent per conubia nostraasdgjkbuq gtahtlagtyagstaskgfsa usagfusagfuaglfhasf gusgfuagfeugfegf"
        ),
        NewsItem(
            date = "23 September 2025",
            title = "Program Pemutihan Pajak kendaraan Pemprov Banten",
            imageUrl = Res.drawable.ic_article_img,
            subtitle = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vulputate libero et velit interdum, ac aliquet odio mattis. Class aptent taciti sociosqu ad litora torquent per conubia nostraasdgjkbuq gtahtlagtyagstaskgfsa usagfusagfuaglfhasf gusgfuagfeugfegf"
        ),
        NewsItem(
            date = "23 September 2025",
            title = "Program Pemutihan Pajak kendaraan Pemprov Banten",
            imageUrl = Res.drawable.ic_article_img,
            subtitle = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vulputate libero et velit interdum, ac aliquet odio mattis. Class aptent taciti sociosqu ad litora torquent per conubia nostraasdgjkbuq gtahtlagtyagstaskgfsa usagfusagfuaglfhasf gusgfuagfeugfegf"
        ),
        NewsItem(
            date = "23 September 2025",
            title = "Program Pemutihan Pajak kendaraan Pemprov Banten",
            imageUrl = Res.drawable.ic_article_img,
            subtitle = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vulputate libero et velit interdum, ac aliquet odio mattis. Class aptent taciti sociosqu ad litora torquent per conubia nostraasdgjkbuq gtahtlagtyagstaskgfsa usagfusagfuaglfhasf gusgfuagfeugfegf"
        ),
        NewsItem(
            date = "23 September 2025",
            title = "Program Pemutihan Pajak kendaraan Pemprov Banten",
            imageUrl = Res.drawable.ic_article_img,
            subtitle = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vulputate libero et velit interdum, ac aliquet odio mattis. Class aptent taciti sociosqu ad litora torquent per conubia nostraasdgjkbuq gtahtlagtyagstaskgfsa usagfusagfuaglfhasf gusgfuagfeugfegf"
        ),
        NewsItem(
            date = "23 September 2025",
            title = "Program Pemutihan Pajak kendaraan Pemprov Banten",
            imageUrl = Res.drawable.ic_article_img,
            subtitle = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vulputate libero et velit interdum, ac aliquet odio mattis. Class aptent taciti sociosqu ad litora torquent per conubia nostraasdgjkbuq gtahtlagtyagstaskgfsa usagfusagfuaglfhasf gusgfuagfeugfegf"
        ),
    )

    Column(modifier = Modifier.fillMaxSize()) {
        HeadlineSection(onCLick = navigateToDetail)
        Spacer_16dp()
        ListNewsSection(newsItem)

    }

}

@Composable
fun HeadlineSection(onCLick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Image(
            painter = painterResource(Res.drawable.ic_article_img),
            modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Spacer_8dp()
        Text(
            "23 September 2025",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Normal
            )
        )
        Spacer_4dp()
        Text(
            "Program Pemutihan Pajak Pemprov Banten",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            ),
        )
        Spacer_4dp()
        Text(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vulputate libero et velit interdum, ac aliquet odio mattis. Class aptent taciti sociosqu ad litora torquent per conubia nostraasdgjkbuq gtahtlagtyagstaskgfsa usagfusagfuaglfhasf gusgfuagfeugfegf",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal
            ),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Spacer_4dp()
        Text(
            "Baca Selengkapnya",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            ),
            modifier = Modifier.clickable {
                onCLick()
            }
        )
    }
}


@Composable
fun ListNewsSection(newsItem: List<NewsItem>) {

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            "Berita Lainnya",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        LazyColumn(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            itemsIndexed(newsItem){ index, item ->
                ListNewsCard(item)
            }
        }
    }
}