/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import br.com.ezatta.backup.Backup;
import br.com.ezatta.backup.DatabaseBackup;
import br.com.ezatta.backup.H2DatabaseBackup;
import br.com.ezatta.backup.dao.BackupDao;
import static br.com.ezatta.controller.LoginController.ezattaEmpresaStatic;
import br.com.ezatta.mail.TesteEmail;
import br.com.ezatta.util.JPAUtil;
import br.com.ezatta.util.Path;
import br.com.ezatta.view.EzattaMain;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
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
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import org.hibernate.Session;

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class BackUpController implements Initializable {

    private Connection conn;
    Calendar cal = Calendar.getInstance();
    private BackupDao dao = new BackupDao();
    private Backup bkp;
    FileChooser fileChooser = new FileChooser();
    DatabaseBackup backup = new H2DatabaseBackup();

    @FXML
    private CheckBox chAtivar;

    @FXML
    void salvarArquivo(ActionEvent event) {

        //Set extension filter
        fileChooser.setTitle("Save backup");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIPpd backups", "*.zip");
        fileChooser.setInitialFileName(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE) + "-" + cal.get(Calendar.HOUR_OF_DAY) + ".zip");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
        File file = fileChooser.showSaveDialog(EzattaMain.stage);

        EntityManager em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        Session hibernateSession = em.unwrap(Session.class);
        hibernateSession.doWork(new org.hibernate.jdbc.Work() {
            @Override
            public void execute(Connection con) throws SQLException {
                conn = con;
            }
        });

        if (file != null) {
            try {
                backup.backupDatabase(conn, file.getAbsolutePath());
            } catch (Throwable t) {
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            System.out.println("dao.getValueBackup(): " + dao.getValueBackup());
            if (dao.getValueBackup() == 0) {
                chAtivar.setSelected(false);
            } else {
                bkp = dao.getBackup(1);
                chAtivar.setSelected(bkp.isBkp());
                System.out.println("tem registro:");
            }

        } catch (SQLException ex) {
            Logger.getLogger(BackUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void salvarAutomatico(ActionEvent event) throws SQLException {
        bkp = new Backup();
        if (dao.getValueBackup() == 0) {//add
            bkp.setId(1);
            bkp.setBkp(chAtivar.selectedProperty().getValue());
            Timestamp data = new Timestamp(System.currentTimeMillis());
            bkp.setData(data);
            dao.addBackup(bkp);
            fazerBkp();
        } else {//update
            bkp.setBkp(chAtivar.selectedProperty().getValue());
            Timestamp data = new Timestamp(System.currentTimeMillis());
            bkp.setData(data);
            bkp.setId(1);
            dao.updateBackup(bkp);
            fazerBkp();
        }
        System.out.println("atualizou " + chAtivar.selectedProperty().getValue());

    }
    String nome;

    private void fazerBkp() {
        String rais = Path.workingDir + "/bkp/";
        File diretorio = new File(rais); // ajfilho é uma pasta!  
        if (!diretorio.exists()) {
            diretorio.mkdirs(); //mkdir() cria somente um diretório, mkdirs() cria diretórios e subdiretórios.  
            System.out.println("criou");
        } else {
            System.out.println("Diretório já existente");
        }

        //Set extension filter
        fileChooser.setTitle("Save backup");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIPpd backups", "*.zip");
        fileChooser.setInitialDirectory(diretorio);
        fileChooser.setInitialFileName(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE) + ".zip");
        fileChooser.getExtensionFilters().add(extFilter);
        nome = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE) + ".zip";
        File file = new File(rais, nome);

        EntityManager em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        Session hibernateSession = em.unwrap(Session.class);
        hibernateSession.doWork(new org.hibernate.jdbc.Work() {
            @Override
            public void execute(Connection con) throws SQLException {
                conn = con;
            }
        });

        if (file != null) {
            try {
                backup.backupDatabase(conn, file.getAbsolutePath());
            } catch (Throwable t) {
            }

        }
        try {
            String endMoreName = rais.concat(nome);
            enviarBkpEmail(endMoreName);
        } catch (MessagingException ex) {
            Logger.getLogger(BackUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enviarBkpEmail(String nome) throws MessagingException {
        final String username = "marceloaugusto16@gmail.com";
        final String senha = "ObrigadoSenhor33";
        String titulo = ezattaEmpresaStatic.getLogin();
        //String titulo = ezattaUsuarioStatic.getEmpresa().getNome();//nome da empresa
        String dataCorpoMensagem = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE) + ".zip";
        String mensagem = dataCorpoMensagem; // personalizar com a data
        String endArquivoUpload = "";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        javax.mail.Session session = javax.mail.Session.getInstance(props, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, senha); //To change body of generated methods, choose Tools | Templates.
            }
        });

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("marceloaugusto16@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("marceloaugusto16@gmail.com"));
            message.setSubject(titulo);
            message.setContent(mensagem, "text/html");

            //--------------------------inicio anexo------------------------------------------
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(mensagem, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();

            String filename = "";
            //if (!endArquivoUpload.isEmpty()) {
            System.out.println("Inicio envio email");
            System.out.println("endereço arquvio: " + nome);
            filename = nome;
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            //}

            Transport.send(message);
            //----------------------------fim anexo-----------------------------------------

            //new FXDialog(FXDialog.Type.INFO, "").showDialog();
        } catch (AddressException ex) {
            Logger.getLogger(TesteEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------navega;'ao telas
    @FXML
    private Button btnVoltar;

    @FXML
    private StackPane stack;

    PrincipalController p = PrincipalController.principal;

    @FXML
    void btnVoltarTela(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/DGPrincipal.fxml"));
        } catch (Exception e) {
        }
    }

    public Node getNode(String node) {
        Node no = null;
        try {
            no = FXMLLoader.load(getClass().getResource(node));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return no;
    }

}
