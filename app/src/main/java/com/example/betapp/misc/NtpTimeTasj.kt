package com.example.betapp.misc

import java.text.SimpleDateFormat
import java.util.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class NtpTimeTask : Thread() {
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
}

fun getCurrentTime(): String {
    val ntpTimeTask = NtpTimeTask()
    ntpTimeTask.start()
    ntpTimeTask.join() // Wait for NTP time retrieval

    val ntpTime = ntpTimeTask.getResult()
    if (ntpTime != null) {
        val parser = SimpleDateFormat("hh:mm a", Locale.getDefault())

        return parser.format(ntpTime)
    } else {
        return ""
    }
}



