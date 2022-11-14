package com.example.heroesandroid

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    companion object{
        const val REQUEST_CHOOSE_BOT = 0
        const val REQUEST_CHOOSE_ROOM = 1
        const val REQUEST_CHOOSE_FIELD = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mBtnConnect: Button = findViewById(R.id.button)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        mBtnConnect.setOnClickListener {
            // For emulator 127.0.0.1 = 10.0.2.2
            Connection.mHost = findViewById<EditText>(R.id.editTextTextPersonName).text.toString()
            findViewById<EditText>(R.id.editTextTextPersonName).setText(Connection.mHost)
            Connection.mPort = 8081
            val thread = Thread {
                val res = Connection.openConnection()
                if (res) {
                    val intent = Intent(this@MainActivity, RoomSelectActivity::class.java)
                    startActivityForResult(intent, REQUEST_CHOOSE_ROOM)
                }
            }
            thread.start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CHOOSE_ROOM -> {
                    val room = data?.getStringExtra("room")
                    if (room != null) {
                        val intent = Intent(this@MainActivity, FieldSelectActivity::class.java)
                        intent.putExtra("room", room)
                        startActivityForResult(intent, REQUEST_CHOOSE_FIELD)
                    }
                }
                REQUEST_CHOOSE_FIELD -> {
                    val field = data?.getStringExtra("field")
                    val room = data?.getStringExtra("room")
                    if (field != null && room != null) {
                        val intent = Intent(this@MainActivity, ChangeActivity::class.java)
                        intent.putExtra("room", room)
                        intent.putExtra("field", field)
                        startActivityForResult(intent, REQUEST_CHOOSE_BOT)
                    }
                }
                REQUEST_CHOOSE_BOT -> {
                    val botName = data?.getStringExtra("botname")
                    val field = data?.getStringExtra("field")
                    val room = data?.getStringExtra("room")
                    if (botName != null && field != null && room != null) {
                        toBoard(botName, room, field)
                    }
                }
            }
        }
    }

    private fun toBoard(botName: String, room: String, field: String) {
        val intent = Intent(this@MainActivity, WarActivity::class.java)
        intent.putExtra("botname", botName)
        intent.putExtra("room", room)
        intent.putExtra("field", field)
        startActivity(intent)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}