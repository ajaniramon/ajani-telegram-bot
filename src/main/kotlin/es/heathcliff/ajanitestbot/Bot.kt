package es.heathcliff.ajanitestbot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import es.heathcliff.ajanitestbot.constant.Constants.BOT_API_KEY
import es.heathcliff.ajanitestbot.constant.Constants.HTTP_COMMAND
import es.heathcliff.ajanitestbot.constant.Constants.MAX_TELEGRAM_MESSAGE_LENGTH
import es.heathcliff.ajanitestbot.constant.Constants.REFLECT_COMMAND
import es.heathcliff.ajanitestbot.constant.Constants.START_COMMAND
import es.heathcliff.ajanitestbot.constant.Constants.TCP_COMMAND
import es.heathcliff.ajanitestbot.service.HttpService
import es.heathcliff.ajanitestbot.service.InvalidCommandUsageException
import es.heathcliff.ajanitestbot.service.TcpService
import es.heathcliff.ajanitestbot.util.toHttpCommand
import es.heathcliff.ajanitestbot.util.toTcpCommand
import org.apache.logging.log4j.LogManager.getLogger
import java.io.IOException

class AjaniBot(private val tcpService: TcpService,
               private val httpService: HttpService,
               token: String = System.getenv(BOT_API_KEY)) {
    private val logger = getLogger(this::class.java)
    private val bot: Bot = bot {
        this.token = token

        dispatch {
            message {
                logger.info("Received message: ${this.message.text}")
            }

            command(START_COMMAND) {
                bot.sendMessage(
                    chatId = message.chat.id,
                    text = "TBD"
                )
            }

            command(REFLECT_COMMAND) {
                bot.sendMessage(
                    chatId = message.chat.id,
                    text = message.text?.replace("/reflect", "") ?: ""
                )
            }

            command(TCP_COMMAND) {
                try {
                    val request = message.text!!.toTcpCommand()
                    val response = tcpService.send(request)

                    logger.info("Received response for request $request: $response")

                    bot.sendMessage(chatId = message.chat.id, text = response)
                } catch (e: IOException) {
                    logger.error(e)
                    bot.sendMessage(chatId = message.chat.id, text = "Network error.")
                } catch (e: Throwable) {
                    logger.error(e)
                    bot.sendMessage(
                        chatId = message.chat.id,
                        text = "An error ocurred. Check your syntax (or the log)."
                    )
                }
            }

            command(HTTP_COMMAND) {
                try {
                    val request = message.text!!.toHttpCommand()
                    val response = httpService.send(request)

                    logger.info("Received response for $request: $response")

                    response.chunked(MAX_TELEGRAM_MESSAGE_LENGTH).forEach {
                        bot.sendMessage(chatId = message.chat.id, text = it)
                    }

                } catch (e: Throwable) {
                    logger.error(e)
                    bot.sendMessage(
                        chatId = message.chat.id,
                        text = "An error ocurred. Check your syntax (or the log)."
                    )
                }
            }
        }
    }

    fun start() {
        bot.startPolling()
        println("[^] Bot is now ready.")
    }

    private fun stop() {
        bot.stopPolling()
        println("[^] Bot is now down.")
    }

}
