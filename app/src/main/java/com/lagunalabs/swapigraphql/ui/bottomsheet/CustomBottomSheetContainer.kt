package com.lagunalabs.swapigraphql.ui.bottomsheet

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.lagunalabs.swapigraphql.R

@Composable
fun CustomBottomSheetContainer(webUrl: String) {
    val scrollState = rememberScrollState()
    val isLoading = remember { mutableStateOf(true) }

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Box {
                AndroidView(factory = { context ->
                    WebView(context).apply {
                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                isLoading.value = true
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading.value = false
                            }
                        }
                        
                        loadUrl(webUrl)
                    }
                },)
                
                if (isLoading.value) {
                    val paddingXxxxxlarge = dimensionResource(id = R.dimen.padding_xxxxxlarge)
                    CircularProgressIndicator(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(paddingXxxxxlarge)
                            .align(Alignment.Center),
                        color = Color.Blue,
                    )
                }
            }
            
        }
    }
}