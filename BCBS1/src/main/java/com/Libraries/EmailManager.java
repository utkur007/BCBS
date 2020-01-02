package com.Libraries;




import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pages.HomePage;


public class EmailManager {

	final static Logger logger = LogManager.getLogger(HomePage.class);

	public String toAddress = "";
	public String ccAddress = "";
	public String bccAddress = "";
	public List<String> attachmentFiles = new ArrayList<>();
	
	
	public void setToAddress(String toEmails) {
		toAddress = toEmails;
	}

	public InternetAddress[] setMultipleEmails(String emailAddress) {
		String multipleEmails[] = emailAddress.split(";");
		InternetAddress[] addresses = new InternetAddress[multipleEmails.length];
		try {
			for (int i = 0; i < multipleEmails.length; i++) {
				addresses[i] = new InternetAddress(multipleEmails[i]);
			}
		} catch (AddressException e) {
			logger.error("Adding multiple email addreses error!", e);
		}
		return addresses;
	}

	public void sendEmail() {
		List<String> attachments = new ArrayList<>();
		String emailMsgBody = "Test email by JavaMail API example." + "<br><br> Regards, <br>Test Automation Team<br>";
		setToAddress("otkuralim@gmail.com");
		sendEmail("smtp.gmail.com", "587", "or199118@gmail.com", "199118Otkur", "Test Automation", emailMsgBody,
				attachments);
		
	}
	
	public void sendEmail(List<String> attachments) {
		String emailMsgBody = "Test email by JavaMail API example." + "<br><br> Regards, <br>Test Automation Team<br>";
		setToAddress("otkuralim@gmail.com");
		sendEmail("smtp.gmail.com", "587", "or199118@gmail.com", "199118Otkur", "Test Automation", emailMsgBody,
				attachments);
	}

	public void sendEmail(String host, String port, String emailUserID, String emailUserPassword, String subject,
			String emailBody, List<String> attachments) {

		try {
			// sets SMTP server properties
			Properties prop = new Properties();
			prop.put("mail.smtp.host", host);
			prop.put("mail.smtp.port", port);
			prop.put("mail.smtp.auth", "true");
			prop.put("mail.smtp.starttls.enable", "true");
			prop.put("mail.user", emailUserID);
			prop.put("mail.password", emailUserPassword);
			logger.info("Step1> preparing email configuration...");

			// creates a new session with an authenticator
			Authenticator auth = new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(emailUserID, emailUserPassword);
				}
			};

			Session session = Session.getInstance(prop, auth);

			// Creates a new e-mail message
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(emailUserID));
			msg.addRecipients(Message.RecipientType.TO, setMultipleEmails(toAddress));
			if (!ccAddress.isEmpty() && !ccAddress.equals(null)) {
				msg.addRecipients(Message.RecipientType.CC, setMultipleEmails(ccAddress));
			}
			if (!bccAddress.isEmpty() && !bccAddress.equals(null)) {
				msg.addRecipients(Message.RecipientType.BCC, setMultipleEmails(bccAddress));
			}

			msg.setSubject(subject);
			msg.setSentDate(new Date());

			// Creates message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(emailBody, "text/html");
			// Creates multi-part
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			// Adds Attachments
			if (attachments.size() > 0 && attachments != null) {
				for (String singleAttachment : attachments) {
					MimeBodyPart attachPart = new MimeBodyPart();
					try {
						attachPart.attachFile(singleAttachment);
					} catch (Exception e) {
						logger.error("Attaching files to email failed ...");
						logger.error(e.getMessage());
					}
					multipart.addBodyPart(attachPart);
				}
			}
			logger.info("Step2> Attaching report files & error screenshots ...");

			// sets the multi-part as email's content
			msg.setContent(multipart);
			// sends email
			logger.info("Step3> Sending email in progress...");
			Transport.send(msg);
			logger.info("Step4> Sending email complete...");
		} catch (Exception e) {
			logger.error("Sending email failed...", e);
			logger.error(e.getMessage());
		}
	}

	public static void main(String[] args) {
		// option 1: with attachments
		EmailManager emailSender = new EmailManager();
		List<String> files = new ArrayList<>();
		files.add("C:\\Users\\otkuralim\\Downloads\\Module12_Project_Automation\\Module12\\target\\logs\\log4j-selenium.log");
		emailSender.sendEmail(files);

		// option2: without attachments
		EmailManager emailSender2 = new EmailManager();
		emailSender2.sendEmail();
	}

	
}
