package com.example.heroesandroid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup

class FieldSelectActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_field_select)

        val radioGroup: RadioGroup = findViewById(R.id.field_selector)

        radioGroup.setOnCheckedChangeListener { _, optionId ->
            val answerIntent = Intent()
            val room = intent.extras!!.getString("room")
            answerIntent.putExtra("room", room)
            when (optionId) {
                R.id.radioBtnLeftField -> answerIntent.putExtra("field", "1")
                R.id.radioBtnRightField -> answerIntent.putExtra("field", "2")
                R.id.radioBtnAnyField -> answerIntent.putExtra("field", "3")
            }

            setResult(RESULT_OK, answerIntent)
            finish()
        }
    }


}