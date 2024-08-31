package com.pproject.sharednotes.data.cloud

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.io.ByteArrayOutputStream

object CloudManager {
    private val cloudHelper: CloudHelper = DropboxHelper()
    private suspend fun upload(fileName: String, data: ByteArray) {
        cloudHelper.upload(fileName, data)
    }

    suspend fun download(fileName: String): String {
        return cloudHelper.download(fileName)
    }

    suspend fun <T : Any> saveOnCloud(entityFlow: Flow<T>, entityName: String) {
        val entities = entityFlow.firstOrNull()
        val output = ByteArrayOutputStream()
        entities.let {
            ObjectMapper().writeValue(output, it)
            upload("${entityName}.json", output.toByteArray())
        }
    }

    suspend inline fun <reified T> loadFromCloud(entityName: String): List<T> {
        val cloudData: String = download("${entityName}.json")
        if (cloudData.isNotEmpty()) {
            return ObjectMapper().readValue<List<T>>(cloudData)
        }
        return emptyList()
    }
}