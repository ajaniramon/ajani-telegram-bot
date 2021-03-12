package es.heathcliff.ajanitestbot

import es.heathcliff.ajanitestbot.service.HttpService
import es.heathcliff.ajanitestbot.service.TcpService

fun main() {
    val tcpService = TcpService()
    val httpService = HttpService()

    val ajaniBot = AjaniBot(tcpService, httpService)

    ajaniBot.start()
}