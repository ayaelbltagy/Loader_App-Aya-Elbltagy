package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        // take object from my manager to cancel open notification
        val notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.cancelNotifications()
        // fill views with value get from notification
        fileNameValue.setText(intent.getStringExtra("file"))
        statusValue.setText(intent.getStringExtra("status"))
        // handle ok button click
        button.setOnClickListener {
            finish()
        }
     }

}
