package mail

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class MailServer {
    private val mailCollection = mutableListOf<Mail>()

    fun getMails(address: String) = flow {
        mailCollection.filter {
            it.receiver == address
        }.forEach {
            delay(300)
            emit(it)
        }
    }

    fun send(mail: Mail) {
        mailCollection.add(mail)
    }
}