package es.heathcliff.ajanitestbot.util

import es.heathcliff.ajanitestbot.service.InvalidCommandUsageException
import es.heathcliff.ajanitestbot.service.SendHttpCommandRequest
import es.heathcliff.ajanitestbot.service.SendTcpCommandRequest

fun String.toTcpCommand(): SendTcpCommandRequest {
    val args = this.replace("/tcp ", "").split(" ")

    return if(args.size < 3) {
        throw InvalidCommandUsageException(this)
    } else {
        SendTcpCommandRequest(args[0], args[1].toInt(), args[2])
    }
}

fun String.toHttpCommand(): SendHttpCommandRequest {
    val args = this.replace("/http ", "").split(" ")

    if(args.size < 2) {
        throw InvalidCommandUsageException(this)
    }else {
        val url = args[0]
        val method = args[1]
        val body = if(args.size == 3) args[2] else null

        return SendHttpCommandRequest(url, method, body)
    }
}