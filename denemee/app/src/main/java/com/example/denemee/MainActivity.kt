package com.example.denemee

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val xorKey = 123  // XOR anahtarı

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buton = findViewById<Button>(R.id.buton)
        val urll = findViewById<TextView>(R.id.url)
        val yazi = findViewById<TextView>(R.id.text)

        buton.setOnClickListener {
            if (urll.text.isNotEmpty()) {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(urll.text.toString())
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        runOnUiThread {
                            yazi.text = "Hata: ${e.message}"
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            val encryptedData = response.body?.bytes() // Şifrelenmiş veriyi al

                            if (encryptedData != null) {
                                try {
                                    val encryptedDataString = encryptedData.toString(Charsets.UTF_8) // Çözümlenmemiş veri
                                    val decryptedData = xorDecrypt(encryptedData)

                                    runOnUiThread {
                                        yazi.text = "Çözümlenmemiş Veri: $encryptedDataString\nÇözülmüş Veri: $decryptedData"
                                    }
                                } catch (e: Exception) {
                                    runOnUiThread {
                                        yazi.text = "Şifre çözme hatası: ${e.message}"
                                    }
                                }
                            } else {
                                runOnUiThread {
                                    yazi.text = "Response body boş."
                                }
                            }
                        } else {
                            runOnUiThread {
                                yazi.text = "Response başarısız: ${response.code}"
                            }
                        }
                    }
                })
            }
        }
    }

    private fun xorDecrypt(encryptedData: ByteArray): String {
        return encryptedData.map { it.toInt() xor xorKey }
            .map { it.toChar() }
            .joinToString("")
    }
}
