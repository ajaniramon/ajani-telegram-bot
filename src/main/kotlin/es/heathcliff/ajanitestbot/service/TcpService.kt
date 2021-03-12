package es.heathcliff.ajanitestbot.service

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class InvalidCommandUsageException(attemptedCommand: String): Exception("Invalid command usage: ${attemptedCommand}")

data class SendTcpCommandRequest(val host: String, val port: Int, val content: String)

class TcpService {
    fun send(request: SendTcpCommandRequest): String {
        try{
            val client = Socket(request.host, request.port)
            val writer = PrintWriter(client.getOutputStream())
            val reader = BufferedReader(InputStreamReader(client.getInputStream()))

            println("[+] Sending ${request}")

            writer.write(request.content)

            val response = reader.readLine()

            println("[+] Sent, closing.")

            client.close()
            return response
        }catch(e: Throwable) {
            println(e)
            throw e
        }
    }
}