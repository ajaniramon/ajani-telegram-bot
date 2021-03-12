package es.heathcliff.ajanitestbot

import es.heathcliff.ajanitestbot.service.HttpService
import es.heathcliff.ajanitestbot.service.TcpService
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
        runApi()
        runBot()
}

fun Application.module() {
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
    }
}

fun runApi() {
    embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
        module()
    }.start(wait = false)
}

fun runBot() {
    val tcpService = TcpService()
    val httpService = HttpService()

    val ajaniBot = AjaniBot(tcpService, httpService)

    ajaniBot.start()
}