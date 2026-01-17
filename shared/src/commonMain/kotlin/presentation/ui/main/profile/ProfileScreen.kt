package presentation.ui.main.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import common.auth.AuthUserProvider
import navbuss.shared.generated.resources.Res
import navbuss.shared.generated.resources.ic_change
import navbuss.shared.generated.resources.ic_login
import navbuss.shared.generated.resources.ic_logout
import navbuss.shared.generated.resources.ic_privasi
import navbuss.shared.generated.resources.ic_syarat
import org.jetbrains.compose.resources.painterResource
import androidx.compose.runtime.*
import common.profile.ProfileImage
import io.ktor.client.HttpClient

@Composable
fun ProfileScreen(
    onChangeLanguage: () -> Unit = {},
    onTerms: () -> Unit = {},
    onPrivacy: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    val bg = Color(0xFFF3F3F3)
    val card = Color.White
    val danger = Color(0xFFD84A4A)
    val dangerBg = Color(0xFFFBE3E3)

    val user by AuthUserProvider.currentUser.collectAsState()

    val userName = user?.displayName ?: "Pengguna"
    val email = user?.email ?: "-"
    val photoUrl = user?.photoUrl

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(bg),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            BrandHeader()

            ProfileCard(
                name = userName,
                email = email,
                photoUrl = photoUrl,
                containerColor = card
            )

            Spacer(Modifier.height(22.dp))

            SectionTitle("Pengaturan")
            Spacer(Modifier.height(10.dp))

            OptionCard(
                title = "Ubah Bahasa",
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.ic_change),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                },
                onClick = onChangeLanguage,
                containerColor = card
            )

            Spacer(Modifier.height(18.dp))

            SectionTitle("Lainnya")
            Spacer(Modifier.height(10.dp))

            OptionCard(
                title = "Syarat dan Ketentuan",
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.ic_syarat),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                },
                onClick = onTerms,
                containerColor = card
            )

            Spacer(Modifier.height(10.dp))

            OptionCard(
                title = "Kebijakan Privasi",
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.ic_privasi),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                },
                onClick = onPrivacy,
                containerColor = card
            )

            Spacer(Modifier.height(18.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = dangerBg,
                    contentColor = danger
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_logout),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text("Keluar", fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}



@Composable
private fun BrandHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_login),
            contentDescription = "Brand",
            modifier = Modifier
                .width(200.dp)
                .heightIn(max = 100.dp),
            contentScale = ContentScale.Fit
        )
    }
}


@Composable
private fun ProfileCard(
    name: String,
    email: String,
    photoUrl: String?,
    containerColor: Color
) {
    val context = LocalPlatformContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!photoUrl.isNullOrEmpty()) {
                ProfileImage(photoUrl = photoUrl, name = name)
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD8EEFF)),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = name.take(2).uppercase()
                    Text(initials, fontWeight = FontWeight.Bold, color = Color(0xFF2B5D87))
                }
            }

            Spacer(Modifier.width(14.dp))

            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6B6B6B)
                )
            }
        }
    }
}


//@Composable
//fun ProfileImage(photoUrl: String?, name: String) {
//    val context = LocalPlatformContext.current
//
//    Box(
//        modifier = Modifier
//            .size(64.dp)
//            .clip(CircleShape)
//            .background(Color(0xFFD8EEFF)),
//        contentAlignment = Alignment.Center
//    ) {
//        if (!photoUrl.isNullOrEmpty()) {
//            AsyncImage(
//                model = ImageRequest.Builder(context)
//                    .data(photoUrl)
//                    .crossfade(true)
//                    .listener(
//                        onError = { _, result ->
//                            println("🚨 Coil load error: ${result.throwable.message}")
//                        },
//                        onSuccess = { _, _ ->
//                            println("✅ Foto profil berhasil dimuat")
//                        }
//                    )
//                    .build(),
//                contentDescription = "Foto Profil",
//                modifier = Modifier.matchParentSize(),
//                contentScale = ContentScale.Crop
//            )
//        } else {
//            val initials = name.take(2).uppercase()
//            Text(
//                text = initials,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFF2B5D87)
//            )
//        }
//    }
//}





@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF2B2B2B)
    )
}

@Composable
private fun OptionCard(
    title: String,
    leadingIcon: @Composable () -> Unit,
    onClick: () -> Unit,
    containerColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconBadge {
                leadingIcon()
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF2B2B2B),
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFB0B0B0)
            )
        }
    }
}

@Composable
private fun IconBadge(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
