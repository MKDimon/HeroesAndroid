package com.example.heroesandroid

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup

class RoomSelectActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_change)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val radioGroup: RadioGroup = findViewById(R.id.room_selector)

        radioGroup.setOnCheckedChangeListener { _, optionId ->
            val answerIntent = Intent()
            when (optionId) {
                R.id.radioBtnRoom1 -> answerIntent.putExtra("room", "1")
                R.id.radioBtnRoom2 -> answerIntent.putExtra("room", "2")
                R.id.radioBtnRoom3 -> answerIntent.putExtra("room", "3")
            }

            setResult(RESULT_OK, answerIntent)
            finish()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}