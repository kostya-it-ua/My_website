import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.mail.*;
import javax.mail.internet.*;

public class VisitorLoggerServlet extends HttpServlet {
    private static final String EMAIL_TO = "kostya_eu@ukr.net";
    private static final String EMAIL_FROM = "your_server@example.com";
    private static final String SMTP_HOST = "smtp.example.com";
    private static final String SMTP_USER = "your_smtp_user";
    private static final String SMTP_PASS = "your_smtp_password";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String page = request.getRequestURL().toString();
        
        String logEntry = "New Visitor\nIP: " + ip + "\nUser-Agent: " + userAgent + "\nPage: " + page + "\n";
        logToFile(logEntry);
        sendEmail(logEntry);
        sendReportToEmail(logEntry);
    }

    private void logToFile(String logEntry) {
        try (FileWriter fw = new FileWriter("visitor_logs.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(String logEntry) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_TO));
            message.setSubject("New Visitor Logged");
            message.setText(logEntry);
            
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void sendReportToEmail(String logEntry) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_TO));
            message.setSubject("Visitor Report");
            message.setText("Visitor report:\n" + logEntry);
            
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
