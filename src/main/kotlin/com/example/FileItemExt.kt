package com.example

import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray
import java.io.File

suspend fun PartData.FileItem.save(
    path: String,
    fileName: String,
): String {
    val fileBytes = provider().readRemaining().readByteArray()
    val folder = File(path)
    folder.mkdirs()
    File("$path$fileName").writeBytes(fileBytes)
    return fileName
}
