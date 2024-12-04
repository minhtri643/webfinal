package Mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MailUtilLocal {

    // Tạo Logger để ghi lỗi
    private static final Logger logger = Logger.getLogger(MailUtilLocal.class.getName());

    static {
        // Cấu hình ConsoleHandler để ghi log ra console
        ConsoleHandler consoleHandler = new ConsoleHandler();
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.ALL); // Chế độ ghi tất cả log
        consoleHandler.setLevel(Level.ALL);
    }

    public static void sendMail(String to, String from,
                                String subject, String body, boolean bodyIsHTML) {
        try {
            // 1 - get a mail session
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587"); // Sử dụng port 587 với STARTTLS
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            // Tạo session với thông tin xác thực
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("laptrinhwebcodientu@gmail.com", "xzhz dlmd ukpc udrh");
                }
            });

            session.setDebug(true); // Giúp debug

            // 2 - create a message
            Message message = new MimeMessage(session);
            message.setSubject(subject);
            if (bodyIsHTML) {
                message.setContent(body, "text/html");
            } else {
                message.setText(body);
            }

            // 3 - address the message
            Address fromAddress = new InternetAddress(from);
            Address toAddress = new InternetAddress(to);
            message.setFrom(fromAddress);
            message.setRecipient(Message.RecipientType.TO, toAddress);

            // 4 - send the message
            Transport transport = session.getTransport("smtp");
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            // Log khi gửi thành công
            logger.info("Email sent successfully to " + to);
        } catch (MessagingException e) {
            // Log lỗi khi có exception xảy ra
            logger.log(Level.SEVERE, "Error sending email: " + e.getMessage(), e);
        } catch (Exception e) {
            // Catch all other exceptions
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
        }
    }
}
