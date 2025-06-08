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

    val email = "news.app@mail.ru"
    val host = "176.124.207.251"
    val port = "8080"



    suspend fun reset(to: String, verificationCode: Int)
    {
        val props = System.getProperties()

        props["mail.smtp.host"] = "smtp.mail.ru"
        props["mail.smtp.port"] = "587"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"

        val session = Session.getInstance(props, object: Authenticator(){
            override fun getPasswordAuthentication(): PasswordAuthentication? {
                return PasswordAuthentication(email, "LycfbcGTMYuLd9qkzjSx")
            }
        })


        val htmlContent = """
    <html>
        <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
            <div style="max-width: 500px; margin: auto; background-color: #ffffff; border-radius: 8px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.05);">
                <h2 style="color: #333333; text-align: center;">Password Reset Code</h2>
                <p style="font-size: 16px; color: #555555; text-align: center;">
                    Use the following verification code to reset your password:
                </p>
                <div style="font-size: 32px; font-weight: bold; color: #007BFF; text-align: center; margin: 24px 0;">
                    $verificationCode
                </div>
                <p style="font-size: 14px; color: #888888; text-align: center;">
                    This code is valid for 10 minutes. If you didn’t request this, please ignore this email.
                </p>
                <hr style="margin-top: 30px; border: none; border-top: 1px solid #eeeeee;">
                <p style="font-size: 12px; color: #cccccc; text-align: center;">
                    © 2025 Your Company. All rights reserved.
                </p>
            </div>
        </body>
    </html>
""".trimIndent()


        val message = MimeMessage(session).apply {
            setFrom(InternetAddress(email,"News app"))
            addRecipient(Message.RecipientType.TO, InternetAddress(to))
            subject = "Reset password"
            setContent(htmlContent, "text/html; charset=utf-8")
        }

        Transport.send(message)
    }
}