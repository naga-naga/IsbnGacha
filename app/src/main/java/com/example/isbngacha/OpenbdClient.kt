package com.example.isbngacha

import android.util.Log
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject

class OpenbdClient {
    private val TAG = "OpenbdClient"

    fun fetchBookInfo(isbn: String, callback: Callback) {
        val url = "https://api.openbd.jp/v1/get?isbn=$isbn"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(callback)
    }

    fun fetchCoverImage(coverUrl: String?, callback: Callback) {
        if (coverUrl == null) {
            return
        }

        val client = OkHttpClient()
        val request = Request.Builder().url(coverUrl).build()
        client.newCall(request).enqueue(callback)
    }

    fun parseResponse(response: Response): Book {
        var book = Book("")

        response.use {
            val rawResponseBodyString = response.body!!.string()
            // openBD のレスポンスは [] で囲まれており，邪魔なので消す
            val responseJsonString =
                rawResponseBodyString.substring(1..rawResponseBodyString.length - 2)
            Log.d(TAG, responseJsonString)

            try {
                val responseJson = JSONObject(responseJsonString)
                val summaryJson = responseJson.getJSONObject("summary")
                val isbn = summaryJson.getString("isbn")
                val title = summaryJson.getString("title")
                val volume = summaryJson.getString("volume")
                val series = summaryJson.getString("series")
                val publisher = summaryJson.getString("publisher")
                val pubdate = summaryJson.getString("pubdate")
                val coverUrl = summaryJson.getString("cover")
                val author = summaryJson.getString("author")
                val lastFetchUnixTime = System.currentTimeMillis()

                book = Book(
                    isbn, title, volume, series, publisher,
                    pubdate, coverUrl, author, null, lastFetchUnixTime
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        return book
    }
}