package com.example.heroesandroid

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup

class ChangeActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val radioGroup: RadioGroup = findViewById(R.id.radio_group)

        radioGroup.setOnCheckedChangeListener { _, optionId ->
            val answerIntent = Intent()
            val room = intent.extras!!.getString("room")
            answerIntent.putExtra("room", room)
            val field = intent.extras!!.getString("field")
            answerIntent.putExtra("field", field)
            when (optionId) {
                R.id.radio_dimon -> answerIntent.putExtra("botname", "dimon")
                R.id.radio_gleb -> answerIntent.putExtra("botname", "gleb")
                R.id.radio_nikita -> answerIntent.putExtra("botname", "nikita")
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