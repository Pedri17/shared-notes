package com.pproject.sharednotes.data.cloud

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.io.ByteArrayOutputStream
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object CloudManager {
    private val cloudHelper: CloudHelper = DropboxHelper()
    private val key = SecretKeySpec("[B@38fb3f4@[/#@]".toByteArray(), "AES")

    private fun encrypt(data: ByteArray): ByteArray {
        if (data.size < 2) return "[]".toByteArray()
        val cypher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cypher.init(Cipher.ENCRYPT_MODE, key)
        return cypher.doFinal(data)
    }

    fun decrypt(data: ByteArray): ByteArray {
        if (data.size < 2) return "[]".toByteArray()
        val cypher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cypher.init(Cipher.DECRYPT_MODE, key)
        return cypher.doFinal(data)
    }

    private suspend fun upload(fileName: String, data: ByteArray) {
        cloudHelper.upload(
            fileName, data
        )
    }

    suspend fun download(fileName: String): ByteArray {
        return cloudHelper.download(fileName)
    }

    suspend fun <T : Any> saveOnCloud(entityFlow: Flow<T>, entityName: String) {
        val entities = entityFlow.firstOrNull()
        val output = ByteArrayOutputStream()
        entities.let {
            ObjectMapper().writeValue(output, it)
            upload("${entityName}.json", encrypt(output.toByteArray()))
        }
    }

    suspend inline fun <reified T> loadFromCloud(entityName: String): List<T> {
        val cloudData: ByteArray = download("${entityName}.json")
        if (cloudData.isNotEmpty()) {
            return ObjectMapper().readValue<List<T>>(decrypt(cloudData))
        }
        return emptyList()
    }
}