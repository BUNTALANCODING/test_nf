//package common
//
//import android.graphics.Color
//import android.webkit.WebView
//import android.webkit.WebViewClient
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.viewinterop.AndroidView
//import java.net.URLEncoder
//
//
//
//@Composable
//actual fun Base64PdfViewer(url: String, modifier: Modifier) {
//
////    val isPdf = url.endsWith(".pdf", ignoreCase = true)
////
////    if (isPdf) {
////        AndroidView(
////            modifier = modifier,
////            factory = { context ->
////                WebView(context).apply {
////                    settings.javaScriptEnabled = true
////                    settings.loadWithOverviewMode = true
////                    settings.useWideViewPort = true
////                    settings.setSupportZoom(true)
////                    settings.builtInZoomControls = true
////                    settings.displayZoomControls = false
////
////                    webViewClient = WebViewClient()
////                }
////            },
////            update = { webView ->
////                webView.loadUrl(url)
////            }
////        )
////    } else {
////        Text("File ini bukan PDF dan tidak bisa ditampilkan")
////    }
//
//    fun getViewerUrl(originalUrl: String): String {
//        return if (originalUrl.endsWith(".pdf", ignoreCase = true)) {
//            // Jika PDF, muat langsung
//            originalUrl
//        }
//        else {
//            // Untuk DOCX, PPTX, XLSX, dll., gunakan Google Docs Viewer
//            // Ini akan merender file tersebut di dalam WebView
//            "https://docs.google.com/gview?embedded=true&url=" + originalUrl
//        }
//    }
//
//    // Panggil helper function di awal
//    val viewerUrl = getViewerUrl(url)
//
//    AndroidView(
//        modifier = modifier,
//        factory = { context ->
//            WebView(context).apply {
//                // Konfigurasi dasar untuk menampilkan PDF
//                settings.javaScriptEnabled = true
//                settings.loadWithOverviewMode = true
//                settings.useWideViewPort = true
//                settings.setSupportZoom(true)
//
//                // Pastikan WebView dapat menangani URL HTTPS
//                webViewClient = WebViewClient()
//            }
//        },
//        update = { webView ->
//            // Muat URL yang sudah disesuaikan
//            webView.loadUrl(viewerUrl)
//        }
//    )
////    AndroidView(
////        modifier = modifier,
////        factory = { context ->
////            WebView(context).apply {
////                // Konfigurasi dasar untuk menampilkan PDF
////                settings.javaScriptEnabled = true
////                settings.loadWithOverviewMode = true
////                settings.useWideViewPort = true
////                settings.setSupportZoom(true)
////
////                // Pastikan WebView dapat menangani URL HTTPS
////                webViewClient = WebViewClient()
////            }
////        },
////        update = { webView ->
////            // Muat URL PDF remote secara langsung.
////            // Engine WebView akan menampilkan PDF secara default.
////            webView.loadUrl(url)
////        }
////    )
//}
//
//
//package common
//
//import android.webkit.WebResourceRequest
//import android.webkit.WebResourceResponse
//import android.webkit.WebView
//import android.webkit.WebViewClient
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.viewinterop.AndroidView
//
//
//@Composable
//actual fun Base64PdfViewer(url: String, modifier: Modifier) {
//
//    var isLoading by remember { mutableStateOf(true) }
//
//    val cleanUrl = url.replace("\\/", "/")
//
//    // Deteksi file PDF presigned URL
//    val pdfPattern = Regex("(?i)\\.pdf(\\?|$)")
//    val isPdf = pdfPattern.containsMatchIn(cleanUrl)
//
//    val viewerUrl =
//        if (isPdf) "https://docs.google.com/gview?embedded=true&url=$cleanUrl"
//        else cleanUrl
//
//    Box(modifier = modifier) {
//
//        AndroidView(
//            modifier = Modifier.fillMaxSize(),
//            factory = { context ->
//
//                WebView(context).apply {
//
//                    settings.javaScriptEnabled = true
//                    settings.loadWithOverviewMode = true
//                    settings.useWideViewPort = true
//                    settings.setSupportZoom(true)
//
//                    webViewClient = object : WebViewClient() {
//
//                        // 🔥 FIX UTAMA: block HEAD agar 400 tidak tampil
//                        override fun shouldInterceptRequest(
//                            view: WebView?,
//                            request: WebResourceRequest?
//                        ): WebResourceResponse? {
//
//                            if (request?.method.equals("HEAD", ignoreCase = true)) {
//                                // Jangan balikin halaman error
//                                return WebResourceResponse("text/plain", "utf-8", null)
//                            }
//
//                            return super.shouldInterceptRequest(view, request)
//                        }
//
//                        override fun onPageFinished(view: WebView?, url: String?) {
//                            super.onPageFinished(view, url)
//                            isLoading = false
//                        }
//                    }
//                }
//            },
//            update = { webView ->
//                webView.loadUrl(viewerUrl)
//            }
//        )
//
//        // 🔥 Loading akan hilang ketika onPageFinished terpanggil
//        if (isLoading) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
//    }
//}




package common

import android.net.http.SslError
import android.webkit.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun Base64PdfViewer(url: String, modifier: Modifier) {

    var isLoading by remember { mutableStateOf(true) }

    val cleanUrl = url.replace("\\/", "/")

    // 🔥 Perbaikan penting: deteksi PDF walau ada query param
    fun isPdfUrl(u: String): Boolean {
        return Regex("(?i)\\.pdf(\\?|$)").containsMatchIn(u)
    }

    // 🔥 getViewerUrl() tetap ada dan tetap dipakai
    fun getViewerUrl(originalUrl: String): String {
        return if (isPdfUrl(originalUrl)) {
            "https://docs.google.com/gview?embedded=true&url=$originalUrl"
        } else {
            originalUrl
        }
    }

    // URL final yang akan dimuat oleh WebView
    val viewerUrl = getViewerUrl(cleanUrl)

    Box(modifier) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->

                WebView(context).apply {

                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.useWideViewPort = true
                    settings.loadWithOverviewMode = true

                    webViewClient = object : WebViewClient() {

                        override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                        }

                        // 🔥 Satu-satunya hal yang kamu minta: BYPASS SSL
                        override fun onReceivedSslError(
                            view: WebView?,
                            handler: SslErrorHandler?,
                            error: SslError?
                        ) {
                            handler?.proceed()
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                        }
                    }

                    loadUrl(viewerUrl)
                }
            },
            update = { webView ->
                isLoading = true
                webView.loadUrl(viewerUrl)
            }
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
