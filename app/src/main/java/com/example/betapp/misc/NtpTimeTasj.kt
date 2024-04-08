package com.example.betapp.misc

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

import kotlinx.coroutines.Dispatchers

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/*class NtpTimeTask : Thread() {
    private val NTP_PACKET_SIZE = 48
    private val NTP_PORT = 123
    private val NTP_TIMEOUT_MS = 3000
    private val NTP_PACKET_OFFSET_TRANSMIT_TIME = 40

    private var timeResult: Date? = null

    override fun run() {
        try {
            val address = InetAddress.getByName("time.google.com")
            val socket = DatagramSocket()
            socket.soTimeout = NTP_TIMEOUT_MS

            // Prepare NTP request
            val ntpData = ByteArray(NTP_PACKET_SIZE)
            ntpData[0] = 0x1B.toByte()

            // Send NTP request
            val packet = DatagramPacket(ntpData, ntpData.size, address, NTP_PORT)
            socket.send(packet)

            // Receive NTP response
            val receivePacket = DatagramPacket(ntpData, ntpData.size)
            socket.receive(receivePacket)

            // Extract transmit time
            val transmitTime = byteArrayToLong(ntpData, NTP_PACKET_OFFSET_TRANSMIT_TIME)

            // Convert to Unix time
            val unixTime = (transmitTime - 2208988800L) * 1000

            // Set result
            timeResult = Date(unixTime)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getResult(): Date? {
        return timeResult
    }

    private fun byteArrayToLong(bytes: ByteArray, offset: Int): Long {
        var value: Long = 0
        for (i in offset until offset + 8) {
            value = (value shl 8) or (bytes[i].toLong() and 0xFF)
        }
        return value
    }
}*/


fun getCurrentTimeFromInternet(callback: (String) -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val url = URL("https://worldtimeapi.org/api/ip")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val inputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuilder()

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }

            reader.close()
            connection.disconnect()

            // Parse JSON response to extract time
            val currentTime = parseTimeFromApiResponse(response.toString())

            // Invoke callback with current time
            callback(currentTime)
        } catch (e: Exception) {
            e.printStackTrace()
            callback("") // If an error occurs, return empty string
        }
    }
}

fun parseTimeFromApiResponse(response: String): String {
    // Sample JSON response: {"datetime":"2024-03-12T14:31:23.905202+05:30","timezone":"Asia/Kolkata","utc_offset":"+05:30"}
    val jsonObject = JSONObject(response)
    val dateTimeString = jsonObject.optString("datetime")

    // Parse datetime string
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val date = parser.parse(dateTimeString)

    // Convert date to desired format
    val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return outputFormat.format(date)
}

// Usage example






