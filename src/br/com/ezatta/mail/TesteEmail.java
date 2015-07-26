/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.mail;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author marcelo
 */
public class TesteEmail {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MessagingException {
        final String username = "marceloaugusto16@gmail.com";
        final String senha = "ObrigadoSenhor33";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, senha); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("marceloaugusto16@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("marceloaugusto16@gmail.com"));
            message.setSubject("Assunto 1");
            message.setText("enviado com sucesso");
            Transport.send(message);
            
        } catch (AddressException ex) {
            Logger.getLogger(TesteEmail.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
