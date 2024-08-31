package com.pproject.sharednotes.data.cloud

import android.util.Log
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.oauth.DbxCredential
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.WriteMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

class DropboxHelper : CloudHelper {
    private val credentials = DbxCredential(
        "",
        0L,
        "W1cCZz8zfqoAAAAAAAAAAb0JD03SK7HSQiGjYzAiiy6S6tJpVkqEkYORiAA373QZ",
        "mirmb4ov4t44rlu",
        "7e2oc7kur4j53ir"
    )
    private val client: DbxClientV2 = DbxClientV2(
        DbxRequestConfig.newBuilder("dropbox/sharednotes").build(),
        credentials
    )

    override suspend fun upload(fileName: String, data: ByteArray): Any =
        withContext(Dispatchers.IO) {
            val inputStream: InputStream = ByteArrayInputStream(data)
            try {
                client.files()
                    .uploadBuilder("/${fileName}")
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(inputStream)
            } catch (e: Exception) {
                Log.e("CloudUploadException", "${e.message}")
            }
        }

    override suspend fun download(fileName: String) = withContext(Dispatchers.IO) {
        val outputStream: OutputStream = ByteArrayOutputStream()
        try {
            client.files().download("/${fileName}")
                .download(outputStream)
        } catch (e: Exception) {
            Log.e("CloudDownloadException", "${e.message}")
        }
        outputStream.toString()
    }
}