import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.collect
import mail.Mail
import mail.MailServer
import mail.Mailbox

val job = Job()
val scope = CoroutineScope(Dispatchers.Default + job)

fun main() {

    /********** Async - Await **********/
    println("--- Async - Await ---")
    scope.launch {
        val c1 = async { longRunningCalculation() }
        val c2 = async { longRunningIO() }
        println("Result: ${c1.await() + c2.await()}") // Await and print the result
    }
    Thread.sleep(3000)
    println()

    /********** Channels **********/
    println("--- Channels ---")
    val channel = Channel<Int>(5)

    // Send
    scope.launch {
        repeat(5) {
            channel.send(it)
            delay(100)
        }
        channel.close()
    }

    // Receive
    scope.launch {
        for(item in channel) {
            println("$item")
        }
    }
    Thread.sleep(3000)
    println()

    // Simulate Mail Server with produce
    val stringChannel = Channel<String>(0) // RendezvousChannel
    scope.launch {
        repeat(5) {
            delay(100)
            stringChannel.send("String #$it")
        }
    }

    // Read mails
    scope.launch {
        for(myString in stringChannel) {
            println("Receiver 1:  $myString")
        }
    }
    scope.launch {
        for(myString in stringChannel) {
            println("Receiver 2:  $myString")
        }
    }
    Thread.sleep(5000)
    println()

    /********** Flow API **********/
    println("--- Flow API ---")
    val mailServer = MailServer()
    val mailbox = Mailbox("test@mail.com", mailServer)

    // Send mail
    with(mailServer) {
        send(
            Mail("test@mail.com", "Subject 1", "Message 1")
        )
        send(
            Mail("test@mail.com", "Subject 2", "Message 2")
        )
    }

    // Show mails
    scope.launch {
        mailbox.mails.collect {
            println(it)
        }
    }
    Thread.sleep(1000)
}

suspend fun longRunningCalculation() = withContext(Dispatchers.Default) {
    var sum = 0
    (1..5).forEach {
        sum += it
        delay(100)
    }
    sum
}

suspend fun longRunningIO() = withContext(Dispatchers.IO) {
    var sum = 0
    (1..5).forEach {
        sum += it
        delay(100)
    }
    sum
}