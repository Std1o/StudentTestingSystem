package student.testing.system.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import student.testing.system.R
import java.io.InputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}