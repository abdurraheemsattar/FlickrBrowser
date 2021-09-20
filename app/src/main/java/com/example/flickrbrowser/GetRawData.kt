package com.example.flickrbrowser

import android.os.AsyncTask
import android.os.ProxyFileDescriptorCallback
import android.util.Log
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus {
    OK, IDLE, NOT_INITAILISED, FAILED_OR_EMPTY, PERMISSIONS_ERROR, ERROR
}

class GetRawData(private val listener: OnDownloadComplete) : AsyncTask<String,Void, String>() {

    private val TAG = "GetRawData"
    private var downloadstatus = DownloadStatus.IDLE


    interface OnDownloadComplete {
        fun onDownloadComplete(data: String, status: DownloadStatus)
    }




//    private var listener: MainActivity? = null
//    fun setDownloadCompletelistener(callbackObject: MainActivity){
//        listener = callbackObject
//    }

    override fun onPostExecute(result: String) {
        Log.d(TAG, "onPostExecute called")
        listener.onDownloadComplete(result, downloadstatus)

    }

    override fun doInBackground(vararg params: String?): String {
        if (params[0] == null){
            downloadstatus = DownloadStatus.NOT_INITAILISED
            return "No URL specified"
        }

        try {
            downloadstatus = DownloadStatus.OK
            return URL(params[0]).readText()
        }catch (e: Exception) {
            val errorMessage = when (e) {
                is MalformedURLException -> {
                    downloadstatus = DownloadStatus.NOT_INITAILISED
                    "doInBackground: Invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadstatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IOException reading data ${e.message}"
                }
                is SecurityException -> {
                    downloadstatus = DownloadStatus.PERMISSIONS_ERROR
                    "doInBackground: SecurityException: need permission ${e.message}"
                } else -> {
                    downloadstatus = DownloadStatus.ERROR
                    "Unknown error: ${e.message}"
                }
            }
            Log.e(TAG,errorMessage)
            return errorMessage
        }
    }
























}