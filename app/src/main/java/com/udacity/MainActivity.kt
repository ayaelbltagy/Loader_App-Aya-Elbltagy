package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var selectedURL: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)



        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            if (selectedURL != null) {
                if (isOnline(this)) {
                    custom_button.buttonState = ButtonState.Loading
                    download()
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.check_connection),
                        Toast.LENGTH_LONG
                    ).show()
                }

            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.please_select_file),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id != null) {
                val query = DownloadManager.Query()
                val manager = context!!.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val cursor: Cursor = manager.query(query)
                if (cursor.moveToFirst()) {
                    if (cursor.getCount() > 0) {
                        var downloadStatus: String
                        val status: Int =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            //  on success
                            downloadStatus = "SUCCESSFUL"
                        } else {
                            //   on failed.
                            downloadStatus = "FAILED"

                        }
                        // trigger button
                        custom_button.buttonState = ButtonState.Completed

                        // take object from my manager
                        val notificationManager =
                            getSystemService(NotificationManager::class.java) as NotificationManager
                        notificationManager.sendNotification(
                            "Project 3",
                            context,
                            selectedURL.toString(),
                            downloadStatus
                        )
                        // call my channel
                        createChannel(CHANNEL_ID.toString(), getString(R.string.channel_name))
                    }
                }
            } else {
                Toast.makeText(context, resources.getString(R.string.failed), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(selectedURL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                //To Store file in External Public Directory
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "udacityFile")

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.

    }

    companion object {
        private const val glideURL = "https://github.com/bumptech/glide"
        private const val udacityURL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
        private const val retrofitURL = "https://github.com/square/retrofit"
        const val CHANNEL_ID = 0
    }

    // create my channel
    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationChannel.description = "Udacity"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)


        }
    }

    // radio button click
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton && view.isChecked) {

            when (view.getId()) {
                R.id.radio_glide ->
                    selectedURL = glideURL
                R.id.radio_load_app ->
                    selectedURL = udacityURL
                R.id.radio_retrofit ->
                    selectedURL = retrofitURL
            }

            view.text.toString()
        }
    }

    // to check connection
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }


}
