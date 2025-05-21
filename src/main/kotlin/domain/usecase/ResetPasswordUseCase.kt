package domain.usecase

import io.ktor.server.sessions.Sessions
import jakarta.mail.Authenticator
import jakarta.mail.Message
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage

class ResetPasswordUseCase {

    val email = "viva.brovin@gmail.com"

    fun reset(to: String, token: String)
    {
        val props = System.getProperties()

        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.port"] = "587"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"

        val session = Session.getInstance(props, object: Authenticator(){
            override fun getPasswordAuthentication(): PasswordAuthentication? {
                return PasswordAuthentication(email, "djjs bjtp pzef flrq")
            }
        })

        val message = MimeMessage(session).apply {
            setFrom(InternetAddress())
            addRecipient(Message.RecipientType.TO, InternetAddress(to))
            subject = "Reset password"
            setText("Click to reset your password: http://192.168.0.5:8081/auth/reset-password?token=$token")
        }

        Transport.send(message)
    }
}