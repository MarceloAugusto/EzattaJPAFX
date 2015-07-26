/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import br.com.ezatta.mail.TesteEmail;
import br.com.ezatta.view.FXDialog;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
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
 * FXML Controller class
 *
 * @author marcelo
 */
public class AjudaSuporteController implements Initializable {

    @FXML
    private TextField txtEmail;

    @FXML
    private HTMLEditor txtMensagem;

    @FXML
    private TextField txtTitulo;

    @FXML
    void envarMensagem(ActionEvent event) throws MessagingException {
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
            message.setSubject(txtTitulo.getText());
            message.setContent(txtMensagem.getHtmlText(), "text/html");
            //message.setText(txtMensagem.getHtmlText().concat(txtEmail.getText()));
            Transport.send(message);
            
            new FXDialog(FXDialog.Type.INFO, "Mensagem enviada com sucesso").showDialog();
            txtEmail.setText("");
            txtTitulo.setText("");
            txtMensagem.setHtmlText("Obrigado por entrar em contato. <br> Em breve retornaremos <br><br><hr> Att. Ezatta Equipamentos");

        } catch (AddressException ex) {
            Logger.getLogger(TesteEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
