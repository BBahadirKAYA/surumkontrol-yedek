package com.bahadirkaya.surumkontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bahadirkaya.surumkontrol.ui.theme.SurumKontrolTheme  // ✅ DÜZELTİLDİ
import okhttp3.*
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SurumKontrolTheme {  // ✅ DÜZELTİLDİ
                Surface(modifier = Modifier.fillMaxSize()) {
                    SorguSonucuEkrani()
                }
            }
        }
    }
}

@Composable
fun SorguSonucuEkrani() {
    var sonuc by remember { mutableStateOf("Veri yükleniyor...") }

    LaunchedEffect(Unit) {
        val url = "https://script.google.com/macros/s/AKfycbx66rGZgt-mT69vb9ZEaMjCc696xnfsNmhmVIf2DXw0fYatmLWEbC2HgYPafCEgLkkaAA/exec"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                sonuc = "Hata: ${e.message}"
            }

            override fun onResponse(call: Call, response: Response) {
                sonuc = response.body?.string() ?: "Boş yanıt"
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Sunucu Yanıtı:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = sonuc, style = MaterialTheme.typography.bodyLarge)
    }
}
