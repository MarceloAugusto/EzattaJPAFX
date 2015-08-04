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
import br.com.ezatta.dao.EmpresaDAO;
import br.com.ezatta.dao.UsuarioDAO;
import br.com.ezatta.mail.TesteEmail;
import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.model.EzattaUsuario;
import br.com.ezatta.util.JPAUtil;
import br.com.ezatta.util.Path;
import br.com.ezatta.view.EzattaMain;
import br.com.ezatta.view.FXDialog;
import br.com.ezatta.view.FXDialog.Type;
import br.com.ezatta.view.FormFX;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
public class LoginController implements Initializable {

    @FXML
    private PasswordField txtSenha;

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField txtUsuario;

    @FXML
    private Button btnEntrar;

    @FXML
    private AnchorPane anchor;

    /*----------------------inicio vars envio java com----------------------------------------*/
    public static Enumeration portList;
    //String portaSelecionada = "COM4";
    public static gnu.io.CommPortIdentifier portId;
    public static String messageString;
    public static String defaultPort;
    public static boolean portFound = false;
    public static char variavelChar;
    public static gnu.io.SerialPort serialPort;
    //leitura inicio
    public static InputStream entrada;
    public static Thread threadLeitura;
    public static OutputStream outputStream;
    //leitura fim
    public static boolean outputBufferEmptyFlag = false;
    public static final String CHARSET = "ASCII";
    public static boolean Escrita;
    public static OutputStream saida;
    public static String status;
    public static String efetivoDisplay;
    private String enderecoBico;
    public static String result;
    public static StringBuilder outputBf;

    public static StringBuilder outputBfj;
    public static BufferedInputStream bufferedinputStream;
    public static boolean execucaoWhile = true;
    public static String valorEfetivo = " ";
    public static int tipoBaixa = 0;
    public static boolean enchendoo;
    /*-----------------------fim vars envio java com------------------------------------------*/

    /*-----------------------------inicio bkp---------------------------------------*/
    FileChooser fileChooser = new FileChooser();
    DatabaseBackup backup = new H2DatabaseBackup();
    Calendar cal = Calendar.getInstance();
    String nome;
    private Connection conn;
    String rais = Path.workingDir + "/bkp/";
    /*---------------------------------fim bkp--------------------------------------*/

    PrincipalController principal = new PrincipalController();
    //public static EzattaEmpresa ezattaEmpresaStatic = new EzattaEmpresa();
    public static EzattaEmpresa ezattaEmpresaStatic = new EzattaEmpresa();
    public static EzattaUsuario ezattaUsuarioStatic = new EzattaUsuario();
    public static EzattaProduto ezattaProdutoStatic = new EzattaProduto();
    EmpresaDAO empresadao = new EmpresaDAO();
    UsuarioDAO usuariodao = new UsuarioDAO();

    @FXML
    void entrar(ActionEvent event) throws IOException, SQLException, Exception {

        //Empresa recupera usuario e senha informado pelo usuario
        ezattaEmpresaStatic.setLogin(txtUsuario.getText());
        ezattaEmpresaStatic.setSenha(txtSenha.getText());

        //Usuario recupera usuario e senha informado pelo usuario
        ezattaUsuarioStatic.setLogin(txtUsuario.getText());
        ezattaUsuarioStatic.setSenha(txtSenha.getText());

        boolean validaLoginSistema = empresadao.getLogAndPasswordSistema(txtUsuario.getText(), txtSenha.getText());
        boolean validEmpresa = empresadao.isValidUserAndPassowrd(ezattaEmpresaStatic);
        boolean validUsuario = usuariodao.isValidUserAndPassowrd(ezattaUsuarioStatic);

        try {
            ezattaEmpresaStatic = empresadao.listUserAndPassowrd(ezattaEmpresaStatic);
        } catch (NullPointerException e) {
        }
        try {
            ezattaUsuarioStatic = usuariodao.getUserByLogAndPassword(ezattaUsuarioStatic);
        } catch (NullPointerException e) {
        }

        System.out.println("EmpresaLogada: " + ezattaEmpresaStatic.getNome());
        System.out.println("UsuarioLogada: " + ezattaUsuarioStatic.getNome());
        System.out.println("validEmpresa: " + validEmpresa);

//        Stage telaPrincipal = new Stage();
//        new FormFX<PrincipalController>("Principal.fxml", telaPrincipal, "Ezatta Inteligent Oil Supply", true);
//---------------------------------------------------descomentar valida Empresa
//        if (validEmpresa) {
//            Stage telaPrincipal = new Stage();
//            try {
//                new FormFX<PrincipalController>("Principal.fxml", telaPrincipal, "Ezatta Inteligent Oil Supply", true);
//            } catch (Exception e) {
//                new FXDialog(Type.ERROR, "Erro ao carregar a tela -> Principal").showDialog();
//            }
//            EzattaMain.stage.close();
//        } else 
//------------------------------------------------------------------------------
        if (validUsuario) {
            try {
                Stage telaPrincipal = new Stage();
                new FormFX<PrincipalController>("Principal.fxml", telaPrincipal, "Ezatta Inteligent Oil Supply", true);
            } catch (Exception e) {
                new FXDialog(Type.ERROR, "Erro ao carregar a tela -> Principal").showDialog();
            }
            EzattaMain.stage.close();
        } else {
            new FXDialog(Type.ERROR, "Usuário ou senha incorreto...").showDialog();
        }
//------------------------------------------------------------------------------

//            if (validaLoginSistema) {
//                //principal.frmEmpresa();
//                //new FrmSis(new javax.swing.JFrame(), true, this).setVisible(true);
//            } else 
//                if (validEmpresa) {
//                anchor.setVisible(false);
//                System.out.println("validEmpresa: " + validEmpresa);
//                ezattaEmpresaStatic = empresadao.listUserAndPassowrd(ezattaEmpresaStatic);
//                new LogadoEmpresa().start(new Stage());
//                EzattaMain.stage.close();
//            } else if (validUsuario) {
//                anchor.setVisible(false);
//                System.out.println("entrou usuario");
//                EzattaUsuario usuario = usuariodao.getUserByLogAndPassword(ezattaUsuarioStatic);
//                new LogadoEmpresa().start(new Stage());
//                EzattaMain.stage.close();
//            } else {
//                new FXDialog(FXDialog.Type.WARNING, "Login/Senha Inválidos!!!").showDialog();
//                return;
//            }
        //------------------------------------------------inicio bkp--------------------------------------------------
        try {
            try {
                BackupDao bkpDao = new BackupDao();
                if (bkpDao.getValueBackup() > 0) {//add
                    Backup bkp = new Backup();
                    bkp = bkpDao.getBackup(1);
                    if (bkp.isBkp()) {
                        //nome do arquivo / data atual
                        String dataAtual = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
                        System.out.println("dataAtual: " + dataAtual);
                        //listar arquivos

                        File diretorio = new File(rais);
                        File fList[] = diretorio.listFiles();
                        int numeroArquivos = diretorio.listFiles().length;

                        System.out.println("Numero de arquivos no diretorio : " + numeroArquivos);

                        Arrays.sort(fList, new Comparator<File>() {
                            public int compare(File f1, File f2) {
                                return Long.compare(f1.lastModified(), f2.lastModified());
                            }
                        });

                        //verifico quantidade e removo o ultimo
                        boolean pegar = false;
                        if (numeroArquivos >= 30) {
                            pegar = true;
                        }

                        //lista diretorio e remove se tiver mais que 30 arquivos
                        boolean verificaarquivo = false;
                        for (int i = 0; i < fList.length; i++) {
                            //verifica se tem mais de 30 itens
                            if (pegar) {
                                fList[i].delete();
                                pegar = false;
                            }

                            Date d = new Date(fList[i].lastModified());
                            Calendar calendar = dateToCalendar(d);
                            System.out.println("---------------------------------------------------------------");
                            String dataArquivo = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
                            System.out.println(fList[i].getName() + " - " + dataArquivo);

                            //verifica a data se é igual 
                            String nomeArquivo = fList[i].getName();
                            String s[] = nomeArquivo.split("\\.");
                            System.out.println("nome arquivo sem . " + s[0]);

                            Calendar calAtual = Calendar.getInstance();
                            String dataAtuals = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
                            if (s[0].equals(dataAtuals)) {
                                System.out.println("Não enviar email e não fazer bkp");
                                verificaarquivo = false;
                            } else {
                                System.out.println("Enviar email e fazer bkp");
                                verificaarquivo = true;
                            }
                        }
                        if (verificaarquivo) {
                            fazerBkp();
                        }
                        //------------------------------------------------------------

                    } else {
                        System.out.println(": " + bkp.isBkp());
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //------------------------------------------------fim bkp-----------------------------------------------------
    }

    private Calendar dateToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }

    //Convert Calendar to Date
    private Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }

    @FXML
    void cancelar(ActionEvent event) throws IOException {
        //fecha aporta
        System.out.println("port " + defaultPort + " not found.");
        serialPort.close();
        System.out.println("Porta fechada...");

        //fechar conexao
        System.out.println("Fechou");
        JPAUtil.closeManager(JPAUtil.getEntityManager());

        //fechar aplicaçao
        Platform.exit();
        System.exit(0);
        EzattaMain.stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // inicializa o Hibernate H2DB
        EntityManager manager = JPAUtil.getEntityManager();
        manager.clear();

        // abre porta serial 
        //defaultPort = "COM4";
        defaultPort = "/dev/ttyACM0";
        System.out.println("Abrindo porta serial: " + defaultPort);

        portList = gnu.io.CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (gnu.io.CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == gnu.io.CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(defaultPort)) {

                    System.out.println("Found port " + defaultPort);
                    portFound = true;
                    //Verifica se a porta esta em Uso   
                    try {
                        serialPort = (gnu.io.SerialPort) portId.open("SimpleWrite", 9600);
                        System.out.println("Abriu");
                    } catch (PortInUseException e) {
                        System.out.println("Port in use.");
                        serialPort.close();
                        new FXDialog(FXDialog.Type.ERROR, "Porta serial COM4 em uso...").showDialog();
                        //continue;
                        //------------------------------rever
                        System.out.println("port " + defaultPort + " not found.");
                        serialPort.close();
                        System.out.println("Porta fechada...");

                        //fechar conexao H2DB
                        System.out.println("Fechou H2DB");
                        JPAUtil.closeManager(JPAUtil.getEntityManager());

                        //fechar conexao Mysql
//                        System.out.println("Fechou Mysql");
//                        JPAUtilChamado.closeManager(JPAUtilChamado.getEntityManager());
                        //fechar aplicaçao
                        Platform.exit();
                        System.exit(0);
                        EzattaMain.stage.close();
                        //mensagem avisando que a porta esta em uso                        
                    }
                    //Abre a porta para utilizar-la
                    try {
                        outputStream = serialPort.getOutputStream();
                    } catch (IOException e) {
                        System.out.println("Erro na abertura da porta.");
                        serialPort.close();
                    }
                    //Setar os parametros da porta
                    try {
                        serialPort.setSerialPortParams(19200, gnu.io.SerialPort.DATABITS_8, gnu.io.SerialPort.STOPBITS_1, gnu.io.SerialPort.PARITY_NONE);
                    } catch (UnsupportedCommOperationException e) {
                        System.out.println("Erro ao setar atributos na p.");
                        serialPort.close();
                    }
                    //Deixa o fluxo limpo
                    try {
                        serialPort.notifyOnOutputEmpty(true);
                    } catch (Exception e) {
                        System.out.println("Error setting event notification");
                        System.out.println(e.toString());
                        System.exit(-1);
                    }
                    /*-------------------------inicio leitura-------------------------------*/

                    /*-------------------------fim leitura---------------------------------*/
                }
                //se a porta não estiver funcionando mostra na tela...
                if (!portFound) {
                    System.out.println("port " + defaultPort + " not found.");
                    //serialPort.close();
                    System.out.println("Porta fechada...");
                }
            }

        }
        /*---------------------------------------fim do metodo que abre a porta--------------------------------------*/
        /*---------------------------------------inicio thread leitura da porta--------------------------------------*/

        //new Thread(task).start();
    }

    private void fazerBkp() {

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
        String titulo = ezattaUsuarioStatic.getEmpresa().getNome();//nome da empresa
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

}
