package com.bright.sunriseset

import android.content.Context
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.LocaleList
import android.widget.Button
import com.bright.sunriseset.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale

const val LANG_ENGLISH_CODE = "en-US"
const val LANG_SIMPLE_CHINESE_CODE = "zh-Hans-CN"

class PlanetInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /**
     * Called when the activity is first created. This function initializes the activity, inflates the layout,
     * retrieves the current time, and asynchronously fetches sunrise and sunset times from an API.
     * The fetched times are then dynamically localized and displayed in Chinese on TextViews.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val changeLangToChineseButton = binding.root.findViewById<Button>(R.id.changeToChinese)

        changeLangToChineseButton.setOnClickListener { v ->
            toggleLanguage(LANG_SIMPLE_CHINESE_CODE)
        }

        val changeLangToEnglishButton = binding.root.findViewById<Button>(R.id.changeToEnglish)

        changeLangToEnglishButton.setOnClickListener { v ->
            toggleLanguage(LANG_ENGLISH_CODE)
        }

        println("Hello: ${resources.configuration.locales.toLanguageTags()}")

        // Get the current time
        val currentTime = LocalDateTime.now()

        // Asynchronously fetch sunrise and sunset times
        GlobalScope.launch(Dispatchers.Main) {
            val sunriseDeferred = async(Dispatchers.IO) { fetchTime("sunrise") }
            val sunsetDeferred = async(Dispatchers.IO) { fetchTime("sunset") }

            // Await the results of asynchronous tasks
            val sunriseTime = sunriseDeferred.await()
            val sunsetTime = sunsetDeferred.await()

            // If both sunrise and sunset times are available, localize and display them in Chinese
            if (sunriseTime != null && sunsetTime != null) {
                // Localize sunrise and sunset times
                val localizedSunrise = getLocalizedTime(sunriseTime, this@PlanetInfoActivity)
                val localizedSunset = getLocalizedTime(sunsetTime, this@PlanetInfoActivity)

                // Display localized times on TextViews
                binding.textViewSunrise.text =
                    "${getString(R.string.SunriseTime)} $localizedSunrise"
                binding.textViewSunset.text =
                    "${getString(R.string.SunsetTime)} $localizedSunset"
            }
        }
    }

    private fun toggleLanguage(code: String) {
        val config = resources.configuration
        val locale = Locale(code)
        Locale.setDefault(locale)

        // config.locale = locale // or config.setLocale(locale)
        // This overwrites the hole values of LocalList with single value,
        // so used the code below to preserve the localeList to provide existing fallbacks of system
        // by prepending the new code selection to the list
        // duplicate entry will be handled automatically

        val newLocaleListStr = "$code,${config.locales.toLanguageTags()}"
        config.setLocales(LocaleList.forLanguageTags(newLocaleListStr))

        resources.updateConfiguration(config, resources.displayMetrics)

        recreate()
    }

    /**
     * Retrieves a localized time string based on the user's preferred language.
     *
     * @param time The LocalDateTime to be formatted.
     * @param context The application context to access resources and preferences.
     * @return A string representation of the localized time.
     */
    private fun getLocalizedTime(time: LocalDateTime, context: Context): String {
        // Retrieve the user's preferred language from the device settings
        val userPreferredLanguage = Locale.getDefault().language

        // Create a SimpleDateFormat with the user's preferred language
        val sdf = SimpleDateFormat("hh:mm a", Locale(userPreferredLanguage))

        // Format the LocalDateTime into a string using the specified SimpleDateFormat
        return sdf.format(
            time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }


    // Coroutine function to fetch sunrise or sunset time from the Sunrise-Sunset API
    private suspend fun fetchTime(type: String): LocalDateTime? {
        return try {
            val apiUrl =
                URL("https://api.sunrise-sunset.org/json?lat=37.7749&lng=-122.4194&formatted=0")
            val urlConnection: HttpURLConnection = apiUrl.openConnection() as HttpURLConnection
            try {
                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val jsonResponse = JSONObject(response.toString())
                val timeUTC = jsonResponse.getJSONObject("results").getString(type)
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault())
                val dateTime = formatter.parse(timeUTC)
                LocalDateTime.ofInstant(dateTime.toInstant(), ZoneId.systemDefault())
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}