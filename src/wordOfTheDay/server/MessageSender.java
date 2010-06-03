package wordOfTheDay.server;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MessageSender {
	public static boolean sendMessage(String from, String topic, String text,
			String to, String smtp) {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtp);
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(true);
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			InternetAddress[] sendTo = new InternetAddress[1];
			sendTo[0] = new InternetAddress(to);
			message.addRecipients(javax.mail.Message.RecipientType.TO, sendTo);
			message.setSubject(topic);
			message.setText(text);
			Transport.send(message);
		} catch (javax.mail.MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
