package mail

class Mailbox(private val address: String, private val mailServer: MailServer) {
    val mails = mailServer.getMails(address)
}