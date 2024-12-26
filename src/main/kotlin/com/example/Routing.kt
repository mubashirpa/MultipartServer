package com.example

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import java.io.File

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")

        post("upload") {
            var fileName = ""
            val multipartData = call.receiveMultipart()

            multipartData.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        fileName = part.originalFileName as String

                        if (part.name == "image") {
                            part.save("build/resources/main/static/images/", fileName)
                        }
                    }

                    else -> {}
                }
                part.dispose()
            }

            call.respondText("File uploaded to 'uploads/$fileName'")
        }

        get("download") {
            val file = File("build/resources/main/static/images/")
            val image = file.listFiles()?.first()

            if (image?.exists() == true) {
                call.respondFile(image)
            } else {
                call.respondText("File not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}
