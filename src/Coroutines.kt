import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

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
        repeat(10) {
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
    val mailServer: ReceiveChannel<String> = scope.produce(capacity = 8) {
        repeat(10) { send("Mail #$it") }
        delay(100)
    }

    // Read mails
    scope.launch {
        for(mail in mailServer) {
            delay(5)
            println("Receiver 1:  $mail")
        }
    }
    scope.launch {
        for(mail in mailServer) {
            delay(10)
            println("Receiver 2:  $mail")
        }
    }
    Thread.sleep(5000)
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