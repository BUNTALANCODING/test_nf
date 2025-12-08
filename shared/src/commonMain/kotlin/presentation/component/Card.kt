package presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import business.constants.SELESAI
import business.datasource.network.main.responses.GetStepDTO
import business.datasource.network.main.responses.HistoryRampcheckDTOItem
import business.datasource.network.main.responses.IdentifyDTOItem
import business.datasource.network.main.responses.QuestionResponse
import business.datasource.network.main.responses.QuestionsItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import presentation.theme.PrimaryColor
import presentation.theme.TextGray
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.arrow_right
import rampcheck.shared.generated.resources.ic_camera


@Composable
fun InformasiRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal,
                color = TextGray
            )
        )

        Text(
            value,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )
    }
}
@Composable
fun BiayaRow(
    label: String,
    value: String,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = if(isTotal) 14.sp else 12.sp,
                fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
                color = if (isTotal) Color.Black else TextGray
            )
        )

        Text(
            "Rp",
            modifier = Modifier.width(40.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = if(isTotal) 14.sp else 12.sp,
                fontWeight = if (isTotal) FontWeight.Normal else FontWeight.Normal,
                color = if (isTotal) Color.Black else TextGray
            )
        )

        Text(
            value,
            textAlign = TextAlign.End,
            modifier = Modifier.width(80.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = if(isTotal) 14.sp else 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )
    }
}

@Composable
fun AlamatItem(
    index: Int,
    selectedOption: Int,
    name: String,
    phone: String,
    address: String,
    onOptionSelected: (Int) -> Unit,
    onUbahAlamatClicked: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(end = 16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedOption == index,
                    onClick = { onOptionSelected(index) }
                )
                Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = phone,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = address,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                Text(
                    text = "Ubah Alamat",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { onUbahAlamatClicked(index) }
                )
            }
            Spacer_4dp()

        }
    }
}

@Composable
fun EDokumenItem(
    title: String,
    iconVector : ImageVector = vectorResource(Res.drawable.arrow_right),
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color.Black
            )
            androidx.compose.material.Icon(
                imageVector = iconVector,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}
data class ConditionItem(
    val id: String,
    val title: String,
    val section: String,
    val selection: Int = 0
)
@Composable
fun ConditionCard(item: ConditionItem, events: (HomeEvent) -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp),
    ) {
        Text(
            item.title,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer_8dp()
        Divider(thickness = 1.dp, color = Color.Gray)
        Spacer_8dp()

        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
            RadioButton(
                selected = item.selection == 1,
                onClick = { events(HomeEvent.OnUpdateConditionSelection(item.id, 1)) }
            )
            Text(
                "Ya, Sesuai",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
            )
            Spacer_16dp()
            RadioButton(
                selected = item.selection == 2,
                onClick = {
                    events(HomeEvent.OnUpdateConditionSelection(item.id, 2))

                }
            )
            Text(
                "Tidak Sesuai",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
            )
        }
        AnimatedVisibility(
            visible = item.selection == 2,
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top)
        ) {

            Column(Modifier.fillMaxWidth().padding(top = 16.dp)) {


                DefaultTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    onValueChange = {},
                    placeholder = "Masukkan Kondisi Sebenarnya"
                )

                Spacer_8dp()

                DottedBorderBackground(bgColor = Color(0XFFF4F4F4), modifier = Modifier.height(70.dp).width(100.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        androidx.compose.material.Icon(
                            imageVector = vectorResource(Res.drawable.ic_camera),
                            modifier = Modifier.size(24.dp)
                                .align(Alignment.CenterHorizontally),
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Spacer_8dp()
                        Text(
                            "Foto",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color.Black
                            ),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun ConditionCardItem(item: QuestionsItem, events: (HomeEvent) -> Unit, state: HomeState, value: String, onValueChange: (String) -> Unit, onClickCamera : () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp),
    ) {
        Text(
            "Kartu Uji/ STUK : " + (item.questionName ?: "-"),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer_8dp()
        Divider(thickness = 1.dp, color = Color.Gray)
        Spacer_8dp()

        Row(verticalAlignment = Alignment.CenterVertically) {

            // ✅ SELESAI — kalau API bilang selection = 1 → YA
            RadioButton(
                selected = state.selectionKartuUji == 0,
                onClick = {
                    events(HomeEvent.OnUpdateSelectionKartuUji(0))
                }
            )
            Text("Ya, Sesuai")

            Spacer(modifier = Modifier.width(20.dp))

            // ✅ SELESAI — kalau API bilang selection = 2 → TIDAK
            RadioButton(
                selected = state.selectionKartuUji == 1,
                onClick = {
                    events(HomeEvent.OnUpdateSelectionKartuUji(1))
                }
            )
            Text("Tidak Sesuai")
        }

        AnimatedVisibility(
            visible = state.selectionKartuUji == 1,
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top)
        ) {

            Column(Modifier.fillMaxWidth().padding(top = 16.dp)) {


                DefaultTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value,
                    onValueChange = {
                        onValueChange(it)
                    },
                    placeholder = "Masukkan Kondisi Sebenarnya"
                )

                Spacer_8dp()

                DottedBorderBackground(bgColor = Color(0XFFF4F4F4), modifier = Modifier.height(70.dp).width(100.dp).clickable { onClickCamera() }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        androidx.compose.material.Icon(
                            imageVector = vectorResource(Res.drawable.ic_camera),
                            modifier = Modifier.size(24.dp)
                                .align(Alignment.CenterHorizontally),
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Spacer_8dp()
                        Text(
                            "Foto",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color.Black
                            ),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun ConditionKartuUjiCard(item: IdentifyDTOItem, events: (HomeEvent) -> Unit, state: HomeState, value: String, onValueChange: (String) -> Unit, onClickCamera : () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp),
    ) {
        Text(
            "Kartu Uji/ STUK : " + (item.answerName ?: "-"),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer_8dp()
        Divider(thickness = 1.dp, color = Color.Gray)
        Spacer_8dp()

        Row(verticalAlignment = Alignment.CenterVertically) {

            RadioButton(
                selected = state.selectionKartuUji == 0,
                onClick = {
                    events(HomeEvent.OnUpdateSelectionKartuUji(0))
                }
            )
            Text("Ya, Sesuai")

            Spacer(modifier = Modifier.width(20.dp))

            RadioButton(
                selected = state.selectionKartuUji == 1,
                onClick = {
                    events(HomeEvent.OnUpdateSelectionKartuUji(1))
                }
            )
            Text("Tidak Sesuai")
        }

        AnimatedVisibility(
            visible = state.selectionKartuUji == 1,
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top)
        ) {

            Column(Modifier.fillMaxWidth().padding(top = 16.dp)) {


                DefaultTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value,
                    onValueChange = {
                        onValueChange(it)
                    },
                    placeholder = "Masukkan Kondisi Sebenarnya"
                )

                Spacer_8dp()

                if(state.tidakSesuaiBitmap != null){
                    state.tidakSesuaiBitmap.let {
                        Image(
                            contentDescription = null,
                            modifier = Modifier.width(100.dp).height(70.dp).clickable { onClickCamera() },
                            bitmap = it
                        )
                    }
                }else{
                    DottedBorderBackground(bgColor = Color(0XFFF4F4F4), modifier = Modifier.height(70.dp).width(100.dp).clickable { onClickCamera() }) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            androidx.compose.material.Icon(
                                imageVector = vectorResource(Res.drawable.ic_camera),
                                modifier = Modifier.size(24.dp)
                                    .align(Alignment.CenterHorizontally),
                                contentDescription = null,
                                tint = Color.Black
                            )
                            Spacer_8dp()
                            Text(
                                "Foto",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color.Black
                                ),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConditionTeknisUtama(item: QuestionResponse, events: (HomeEvent) -> Unit, state: HomeState, onClickCamera : (Int) -> Unit){
    val questionId = item.question_id ?: return
    val selection = state.selectionMap[questionId] ?: 0
    val answer = state.answers.find { it.questionId == questionId }
    val bitmap = state.bitmapTidakSesuaiMap[questionId]
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp),
    ) {
        Text(
            "${(item.question_name ?: "-")} : " + (item.answer_name ?: "-"),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer_8dp()
        Divider(thickness = 1.dp, color = Color.Gray)
        Spacer_8dp()

        Row(verticalAlignment = Alignment.CenterVertically) {

            RadioButton(
                selected = selection == 0,
                onClick = {
                    events(HomeEvent.OnUpdateSelection(questionId, 0))
                }
            )
            Text("Ya, Sesuai")

            Spacer(modifier = Modifier.width(20.dp))

            RadioButton(
                selected = selection == 1,
                onClick = {
                    events(HomeEvent.OnUpdateSelection(questionId, 1))
                }
            )
            Text("Tidak Sesuai")
        }

        AnimatedVisibility(
            visible = selection == 1,
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top)
        ) {

            Column(Modifier.fillMaxWidth().padding(top = 16.dp)) {


                DefaultTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = answer?.answerCondition ?: "",
                    onValueChange = {
                        events(
                            HomeEvent.OnUpdateCondition(questionId, it)
                        )
                    },
                    placeholder = "Masukkan Kondisi Sebenarnya"
                )

                Spacer_8dp()

                if(bitmap != null){
                    bitmap.let {
                        Image(
                            contentDescription = null,
                            modifier = Modifier.width(100.dp).height(70.dp).clickable { onClickCamera(questionId) },
                            bitmap = it
                        )
                    }
                }else{
                    DottedBorderBackground(bgColor = Color(0XFFF4F4F4), modifier = Modifier.height(70.dp).width(100.dp).clickable { onClickCamera(questionId) }) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            androidx.compose.material.Icon(
                                imageVector = vectorResource(Res.drawable.ic_camera),
                                modifier = Modifier.size(24.dp)
                                    .align(Alignment.CenterHorizontally),
                                contentDescription = null,
                                tint = Color.Black
                            )
                            Spacer_8dp()
                            Text(
                                "Foto",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color.Black
                                ),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConditionTeknisPenunjang(item: QuestionResponse, events: (HomeEvent) -> Unit, state: HomeState, onClickCamera : (Int) -> Unit){
    val questionId = item.question_id ?: return
    val selection = state.selectionMapPenunjang[questionId] ?: 0
    val answer = state.answersPenunjang.find { it.questionId == questionId }
    val bitmap = state.bitmapTidakSesuaiPenunjangMap[questionId]
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp),
    ) {
        Text(
            "${(item.question_name ?: "-")} : " + (item.answer_name ?: "-"),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer_8dp()
        Divider(thickness = 1.dp, color = Color.Gray)
        Spacer_8dp()

        Row(verticalAlignment = Alignment.CenterVertically) {

            RadioButton(
                selected = selection == 0,
                onClick = {
                    events(HomeEvent.OnUpdateSelectionPenunjang(questionId, 0))
                }
            )
            Text("Ya, Sesuai")

            Spacer(modifier = Modifier.width(20.dp))

            RadioButton(
                selected = selection == 1,
                onClick = {
                    events(HomeEvent.OnUpdateSelectionPenunjang(questionId, 1))
                }
            )
            Text("Tidak Sesuai")
        }

        AnimatedVisibility(
            visible = selection == 1,
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top)
        ) {

            Column(Modifier.fillMaxWidth().padding(top = 16.dp)) {


                DefaultTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = answer?.answerCondition ?: "",
                    onValueChange = {
                        events(
                            HomeEvent.OnUpdateConditionPenunjang(questionId, it)
                        )
                    },
                    placeholder = "Masukkan Kondisi Sebenarnya"
                )

                Spacer_8dp()

                if(bitmap != null){
                    bitmap.let {
                        Image(
                            contentDescription = null,
                            modifier = Modifier.width(100.dp).height(70.dp).clickable { onClickCamera(questionId) },
                            bitmap = it
                        )
                    }
                }else{
                    DottedBorderBackground(bgColor = Color(0XFFF4F4F4), modifier = Modifier.height(70.dp).width(100.dp).clickable { onClickCamera(questionId) }) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            androidx.compose.material.Icon(
                                imageVector = vectorResource(Res.drawable.ic_camera),
                                modifier = Modifier.size(24.dp)
                                    .align(Alignment.CenterHorizontally),
                                contentDescription = null,
                                tint = Color.Black
                            )
                            Spacer_8dp()
                            Text(
                                "Foto",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color.Black
                                ),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ConditionSIMCard(item: IdentifyDTOItem, events: (HomeEvent) -> Unit, state: HomeState, value: String, onValueChange: (String) -> Unit, onClickCamera : () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp),
    ) {
        Text(
            "SIM Pengemudi : " + (item.answerName ?: "-"),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer_8dp()
        Divider(thickness = 1.dp, color = Color.Gray)
        Spacer_8dp()

        Row(verticalAlignment = Alignment.CenterVertically) {

            RadioButton(
                selected = state.selectionSIM == 0,
                onClick = {
                    events(HomeEvent.OnUpdateSelectionSIM(0))
                }
            )
            Text("Ya, Sesuai")

            Spacer(modifier = Modifier.width(20.dp))

            RadioButton(
                selected = state.selectionSIM == 1,
                onClick = {
                    events(HomeEvent.OnUpdateSelectionSIM(1))
                }
            )
            Text("Tidak Sesuai")
        }

        AnimatedVisibility(
            visible = state.selectionSIM == 1,
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top)
        ) {

            Column(Modifier.fillMaxWidth().padding(top = 16.dp)) {


                DefaultTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value,
                    onValueChange = {
                        onValueChange(it)
                    },
                    placeholder = "Masukkan Kondisi Sebenarnya"
                )

                Spacer_8dp()

                if(state.simPengemuduiTidakSesuaiBitmap != null){
                    state.simPengemuduiTidakSesuaiBitmap.let {
                        Image(
                            contentDescription = null,
                            modifier = Modifier.width(100.dp).height(70.dp).clickable { onClickCamera() },
                            bitmap = it
                        )
                    }
                }else{
                    DottedBorderBackground(bgColor = Color(0XFFF4F4F4), modifier = Modifier.height(70.dp).width(100.dp).clickable { onClickCamera() }) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            androidx.compose.material.Icon(
                                imageVector = vectorResource(Res.drawable.ic_camera),
                                modifier = Modifier.size(24.dp)
                                    .align(Alignment.CenterHorizontally),
                                contentDescription = null,
                                tint = Color.Black
                            )
                            Spacer_8dp()
                            Text(
                                "Foto",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color.Black
                                ),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }


            }
        }
    }
}

@Composable
fun ExaminationCard(item: HistoryRampcheckDTOItem,status: Int, onClick: (HistoryRampcheckDTOItem) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable {
            onClick(item)
        },
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Left Column: Nomor Kendaraan
            Column {
                Text(
                    text = if (status == SELESAI) "Nomor Berita Acara" else "Nomor Kendaraan",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.Gray
                    )
                )
                Spacer_4dp()
                Text(
                    text = item.platNumber ?: "",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Right Column: Tanggal Pemeriksaan
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (status == SELESAI) "Terbit Pada" else "Tanggal Pemeriksaan",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.Gray
                    )
                )
                Spacer_4dp()
                Text(
                    text = item.inspectionDate ?: "",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}


/*
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
}*/
