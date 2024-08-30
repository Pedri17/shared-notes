package com.pproject.sharednotes.data.network

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.dropbox.core.DbxException
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.oauth.DbxCredential
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.WriteMode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.pproject.sharednotes.data.db.entity.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import kotlin.coroutines.CoroutineContext


val token = "W1cCZz8zfqoAAAAAAAAAAb0JD03SK7HSQiGjYzAiiy6S6tJpVkqEkYORiAA373QZ"

val credentials = DbxCredential(
    "",
    0L,
    "W1cCZz8zfqoAAAAAAAAAAb0JD03SK7HSQiGjYzAiiy6S6tJpVkqEkYORiAA373QZ",
    "mirmb4ov4t44rlu",
    "7e2oc7kur4j53ir"
)
val client: DbxClientV2 = DbxClientV2(DbxRequestConfig("dropbox/sharednotes", "en_US"), credentials)

suspend fun upload(name: String, byteArray: ByteArray) = withContext(Dispatchers.IO) {
    val inputStream: InputStream = ByteArrayInputStream(byteArray)
    client.files()
        .uploadBuilder("/${name}") //Path in the user's Dropbox to save the file.
        .withMode(WriteMode.OVERWRITE) //always overwrite existing file
        .uploadAndFinish(inputStream)
    Log.d("Upload Status", "Success")
}

suspend fun download(path: String) = withContext(Dispatchers.IO) {
    val outputStream: OutputStream = ByteArrayOutputStream()
    client.files().download(path)
        .download(outputStream)
    Log.d("Download Status", "Success")
    outputStream.toString()
}