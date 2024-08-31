package com.pproject.sharednotes.data.cloud

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
            client.files()
                .uploadBuilder("/${fileName}") //Path in the user's Dropbox to save the file.
                .withMode(WriteMode.OVERWRITE) //always overwrite existing file
                .uploadAndFinish(inputStream)
        }

    override suspend fun download(fileName: String) = withContext(Dispatchers.IO) {
        val outputStream: OutputStream = ByteArrayOutputStream()
        client.files().download("/${fileName}")
            .download(outputStream)
        outputStream.toString()
    }
}