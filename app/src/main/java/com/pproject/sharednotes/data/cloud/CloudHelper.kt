package com.pproject.sharednotes.data.cloud

interface CloudHelper {
    suspend fun upload(fileName: String, data: ByteArray): Any
    suspend fun download(fileName: String): String
}