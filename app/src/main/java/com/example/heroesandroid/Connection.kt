package com.example.heroesandroid

import android.system.Os.socket
import android.util.Log
import java.io.*
import java.net.Socket


object Connection {
    private var mSocket: Socket? = null
    var mHost: String? = null
    var mPort = 0
    var inBuf: BufferedReader? = null
    var outBuf: BufferedWriter? = null

    val LOG_TAG = "SOCKET"

    // Метод открытия сокета
    @Throws(Exception::class)
    fun openConnection(): Boolean {
        // Если сокет уже открыт, то он закрывается
        var result = false
        closeConnection()
        try {
            // Создание сокета
            mSocket = Socket(mHost, mPort)
            inBuf = BufferedReader(InputStreamReader(mSocket!!.getInputStream()))
            outBuf = BufferedWriter(OutputStreamWriter(mSocket!!.getOutputStream()))
            result = true
        } catch (e: IOException) {
            Log.e("SOCKET ERROR",
                "Невозможно создать сокет: "
                        + e.message
                        + " " + mHost + " " + mPort
            )
        }
        return result
    }

    /**
     * Метод закрытия сокета
     */
    fun closeConnection() {
        if (mSocket != null && !mSocket!!.isClosed) {
            try {
                mSocket!!.close()
            } catch (e: IOException) {
                Log.e(
                    LOG_TAG, ("Ошибка при закрытии сокета :"
                            + e.message)
                )
            } finally {
                mSocket = null
            }
        }
        mSocket = null
        inBuf = null
        outBuf = null
    }

    fun isClosed(): Boolean {
        if (mSocket == null) return true
        return mSocket!!.isClosed
    }

}