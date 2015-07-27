/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import br.com.ezatta.mail.TesteEmail;
import static br.com.ezatta.view.EzattaMain.stage;
import br.com.ezatta.view.FXDialog;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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

    String endArquivoUpload = "";

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
            message.setSubject(txtTitulo.getText() + " - De: " + txtEmail.getText());
            message.setContent(txtMensagem.getHtmlText(), "text/html");

            //--------------------------inicio anexo------------------------------------------
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(txtMensagem.getHtmlText(), "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();

            String filename = "";
            if (!endArquivoUpload.isEmpty()) {
                System.out.println("Entrou ------------------- Erro-------------------");
                //String filename = "/home/marcelo/Imagens/Erro.png";
                filename = endArquivoUpload;
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);
                message.setContent(multipart);
            }

            Transport.send(message);
            //----------------------------fim anexo-----------------------------------------

            new FXDialog(FXDialog.Type.INFO, "Mensagem enviada com sucesso").showDialog();
            txtEmail.setText("");
            txtTitulo.setText("");
            txtMensagem.setHtmlText("Obrigado por entrar em contato. <br> Em breve retornaremos <br><br><hr> Att. Ezatta Equipamentos");
            endArquivo.setText("");

        } catch (AddressException ex) {
            Logger.getLogger(TesteEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private TextFlow txtEndereco;

    @FXML
    private WebView maps;

    Text endereco = new Text();

    //------------------inicio upload----------------------------
    @FXML
    private AnchorPane fileUpload;
    private Desktop desktop = Desktop.getDesktop();

    private FileChooser fileChooser = new FileChooser();
    private Button openButton = new Button("Anexar arquivo");
    private Text endArquivo = new Text("");

    //-----------------fim upload--------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        String mapa = "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3754.78328388787!2d-47.94678626857003!3d-19.764360064692667!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0000000000000000%3A0xebc03fe572026f9f!2sCasa+dos+Filtros!5e0!3m2!1spt-BR!2sbr!4v1422024199468\" width=\"100%\" height=\"100%\" frameborder=\"0\" style=\"border:0\"></iframe>";
        maps.getEngine().loadContent(mapa);

        Hyperlink link = new Hyperlink("ezattaequipamentos.com.br");

        String end = "\n"
                + "R. Monte Alegre, 105, São Benedito\n"
                + "Uberaba, MG\n"
                + "\n"
                + "Telefone: 34.3334.9990\n"
                + "Email: contato@ezattainteligente.com\n"
                + "Site: www.ezattaequipamentos.com.br";

        endereco.setText(end);
        txtEndereco.getChildren().add(endereco);

        link.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                WebView browser = new WebView();
                WebEngine webEngine = browser.getEngine();
                webEngine.load("http://www.oracle.com/us/products/index.html");
            }
        });

        //---------------------------------------------------------------
        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(null);
                        if (file != null) {
                            endArquivoUpload = file.getPath();
                            System.out.println("file: " + file.getPath());
                            endArquivo.setText("  " + file.getName());

                        }

                    }
                });
        HBox hbFile = new HBox();
        hbFile.getChildren().addAll(openButton, endArquivo);
        fileUpload.getChildren().add(hbFile);
    }

}
