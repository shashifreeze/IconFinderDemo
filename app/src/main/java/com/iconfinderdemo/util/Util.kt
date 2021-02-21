package com.iconfinderdemo.util

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.iconfinderdemo.R
import java.io.File

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 30f
        start()
    }
}

fun ImageView.loadImage(url: String?, progressDrawable: CircularProgressDrawable) {

   val options:RequestOptions = RequestOptions()
       .placeholder(progressDrawable)
       .error(R.mipmap.ic_launcher)

    Glide.with(this.context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

fun Context.downloadImage(imageUrl:String)
{
     var msg: String? = ""
     var lastMsg = ""
    val directory = File(Environment.DIRECTORY_PICTURES)

    if (!directory.exists()) {
        directory.mkdirs()
    }

    val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    val downloadUri = Uri.parse(imageUrl)

    val request = DownloadManager.Request(downloadUri).apply {
        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle(imageUrl.substring(imageUrl.lastIndexOf("/") + 1))
            .setDescription("")
            .setDestinationInExternalPublicDir(
                directory.toString(),
                imageUrl.substring(imageUrl.lastIndexOf("/") + 1)
            )
    }

    val downloadId = downloadManager.enqueue(request)
    val query = DownloadManager.Query().setFilterById(downloadId)
    Thread(Runnable {
        var downloading = true
        while (downloading) {
            val cursor: Cursor = downloadManager.query(query)
            cursor.moveToFirst()
            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                downloading = false
            }
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            msg = statusMessage(imageUrl, directory, status)
            if (msg != lastMsg) {
               // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                lastMsg = msg ?: ""
            }
            cursor.close()
        }
    }).start()
}

private fun statusMessage(url: String, directory: File, status: Int): String? {
    var msg = ""
    msg = when (status) {
        DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
        DownloadManager.STATUS_PAUSED -> "Paused"
        DownloadManager.STATUS_PENDING -> "Pending"
        DownloadManager.STATUS_RUNNING -> "Downloading..."
        DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
            url.lastIndexOf("/") + 1
        )
        else -> "There's nothing to download"
    }
    return msg
}
