package com.bahadirkaya.surumkontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bahadirkaya.surumkontrol.ui.theme.SurumKontrolTheme
import okhttp3.*
import java.io.IOException
import org.json.JSONObject
import com.bahadirkaya.surumkontrol.BuildConfig   // ✅ EKLENDİ

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SurumKontrolTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SorguSonucuEkrani()
                }
            }
        }
    }
}

@Composable
fun SorguSonucuEkrani() {
    var versionCodeFromServer by remember { mutableStateOf(-1) }
    var versionName by remember { mutableStateOf("") }
    var mesaj by remember { mutableStateOf("") }
    var updateAvailable by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val url =  "https://script.google.com/macros/s/AKfycbxSNL58f0LSeiCPJ-5bo5Ror9vgtteTqdw_lT2BZQFCuCMxRwOG4W0L0GWJGpAb3OPKew/exec"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mesaj = "Hata: ${e.message}"
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string() ?: ""
                try {
                    val json = JSONObject(responseString)
                    val dataArray = json.getJSONArray("data")
                    val item = dataArray.getJSONObject(0)

                    versionCodeFromServer = item.getInt("versionCode")
                    versionName = item.getString("versionName")
                    mesaj = item.getString("mesaj")

                    val currentVersion = BuildConfig.VERSION_CODE
                    updateAvailable = versionCodeFromServer > currentVersion
                } catch (e: Exception) {
                    mesaj = "JSON ayrıştırma hatası: ${e.message}"
                }
            }
        })
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Uygulama Sürüm: ${BuildConfig.VERSION_NAME}", style = MaterialTheme.typography.titleMedium)
        Text("Sunucu Sürüm: $versionName", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Mesaj: $mesaj", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(12.dp))
        if (updateAvailable) {
            Text("🔔 Yeni güncelleme mevcut!", color = MaterialTheme.colorScheme.primary)
        } else {
            Text("Uygulama güncel.", color = MaterialTheme.colorScheme.secondary)
        }
    }
}
