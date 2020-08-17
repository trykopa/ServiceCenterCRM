package com.googe.ssadm.sc.utils;

import com.googe.ssadm.sc.entity.Client;
import com.googe.ssadm.sc.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component("simpleMailSender")
public class MailSender {
    @Value("${notification.mail.sender}")
    private String from;
    @Value("${notification.mail.name}")
    private String name;
    @Value("${notification.phone}")
    private String phone;

    @Autowired
    JavaMailSender javaMailSender;
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(MailSender.class);

    public void sendMail(Client client , Order order) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage , "utf-8");
            StringBuilder sb = new StringBuilder();
            sb.append("<h3>Hello,").append(client.getName()).append("!</h3>");
            sb.append("<p>The SC service center has received your device for repair. " +
                    "We will start diagnosing it shortly. Upon completion of the " +
                    "diagnosis, our specialist will contact you to report the results " +
                    "of the diagnosis and discuss further steps to troubleshoot the problem.</p>>" +
                    "\n" +
                    "<p>In case of questions, you can contact us by answering this letter or " +
                    "by calling our contact phone - ");
            sb.append(phone);
            sb.append("</p>");
            sb.append("Sincerely yours,<br />");
            sb.append(name);
            String htmlMsg = sb.toString();
            helper.setText(htmlMsg , true);
            helper.setTo(client.getEmail());
            helper.setFrom(from);
            helper.setSubject("SC, Order No" + order.getId() + " received");
            log.info("Sending...");
            javaMailSender.send(mimeMessage);
            log.info("Done!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
