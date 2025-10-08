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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun DetailNewsScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Detail",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
    ) {
        DetailNewsContent(
            state = state,
            events = events,
        )

    }
}

@Composable
private fun DetailNewsContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
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

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        HeadlineDetailSection()
        Spacer_16dp()
        ListNewsDetailSection(newsItem)

    }

}

@Composable
fun HeadlineDetailSection() {
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
            "orem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eu turpis molestie, dictum est a, mattis tellus. Sed dignissim, metus nec fringilla accumsan, risus sem sollicitudin lacus, ut interdum tellus elit sed risus. Maecenas eget condimentum velit, sit amet feugiat lectus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Praesent auctor purus luctus enim egestas, ac scelerisque ante pulvinar. Donec ut rhoncus ex. Suspendisse ac rhoncus nisl, eu tempor urna. Curabitur vel bibendum lorem. Morbi convallis convallis diam sit amet lacinia. Aliquam in elementum tellus.\n" +
                    "              Curabitur tempor quis eros tempus lacinia. Nam bibendum pellentesque quam a convallis. Sed ut vulputate nisi. Integer in felis sed leo vestibulum venenatis. Suspendisse quis arcu sem. Aenean feugiat ex eu vestibulum vestibulum. Morbi a eleifend magna. Nam metus lacus, porttitor eu mauris a, blandit ultrices nibh. Mauris sit amet magna non ligula vestibulum eleifend. Nulla varius volutpat turpis sed lacinia. Nam eget mi in purus lobortis eleifend. Sed nec ante dictum sem condimentum ullamcorper quis venenatis nisi. Proin vitae facilisis nisi, ac posuere leo.\n" +
                    "              Nam pulvinar blandit velit, id condimentum diam faucibus at. Aliquam lacus nisi, sollicitudin at nisi nec, fermentum congue felis. Quisque mauris dolor, fringilla sed tincidunt ac, finibus non odio. Sed vitae mauris nec ante pretium finibus. Donec nisl neque, pharetra ac elit eu, faucibus aliquam ligula. Nullam dictum, tellus tincidunt tempor laoreet, nibh elit sollicitudin felis, eget feugiat sapien diam nec nisl. Aenean gravida turpis nisi, consequat dictum risus dapibus a. Duis felis ante, varius in neque eu, tempor suscipit sem. Maecenas ullamcorper gravida sem sit amet cursus. Etiam pulvinar purus vitae justo pharetra consequat. Mauris id mi ut arcu feugiat maximus. Mauris consequat tellus id tempus aliquet",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal
            ),
        )
    }
}


@Composable
fun ListNewsDetailSection(newsItem: List<NewsItem>) {

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            "Berita Lainnya",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        LazyColumn(modifier = Modifier.fillMaxWidth().height(200.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            itemsIndexed(newsItem){ index, item ->
                ListNewsCard(item)
            }
        }
    }
}

@Composable
fun ListNewsCard(item: NewsItem) {
    Row(modifier = Modifier.fillMaxWidth().height(87.dp)) {
        Image(
            painter = painterResource(item.imageUrl),
            modifier = Modifier.width(174.dp).height(87.dp),
            contentDescription = null
        )
        Spacer_4dp()
        Column {
            Text(
                item.date,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Normal
                )
            )
            Text(
                item.title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            item.subtitle?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                "Baca Selengkapnya",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                )
            )
        }

    }
}
