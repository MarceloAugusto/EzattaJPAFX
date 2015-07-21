/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import br.com.ezatta.dao.EmpresaDAO;
import br.com.ezatta.dao.UsuarioDAO;
import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.model.EzattaUsuario;
import br.com.ezatta.util.JPAUtil;
import br.com.ezatta.view.EzattaMain;
import br.com.ezatta.view.FXDialog;
import br.com.ezatta.view.FXDialog.Type;
import br.com.ezatta.view.FormFX;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Enumeration;
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
import javafx.stage.Stage;
import javax.persistence.EntityManager;

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

        //fechar aplica;'ao
        Platform.exit();
        System.exit(0);
        EzattaMain.stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // inicializa o Hibernate
        EntityManager manager = JPAUtil.getEntityManager();
        manager.clear();

        // abre porta serial 
        //defaultPort = "COM4";
        defaultPort = "/dev/ttyACM0";
        System.out.println("Abrindo porta serial ttyACM0");

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
                        continue;
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

//    //thread leitura porta serial-----------------------------------------------------------------------
//    Task task = new Task<Void>() {
//        @Override
//        public void run() {
//            try {
////inicio-----------------------------------------------------------------------
//                boolean liberado = true;
//                long numBytes = 0;
//                byte[] readBuffer = new byte[1000];
//                byte[] auxBuffer = new byte[1000];
//
//                while (execucaoWhile) {
//                    entrada = null;
//                    entrada = serialPort.getInputStream();
//
//                    //joga fora primeiras menssagens de outros pedidos    
//                    if (entrada.available() > 0) {
//                        entrada.read(readBuffer);
//                    }
//
//                    int timeout = 0;
//                    numBytes = 0;
//                    while (numBytes < 24) {
//                        while (entrada.available() > 0) {
//                            numBytes = numBytes + entrada.read(readBuffer, (int) numBytes, 1);//recebe os bytes na leitura.
//                            timeout = 0;
//                        }
//
//                        Thread.sleep(1);
//
//                        timeout++;
//                        if (timeout > 25) {
//                            numBytes = 0;
//                            timeout = 0;
//                        }
//                        if (numBytes > 0) {
//                            System.out.println("numBytes: " + numBytes);
//
//                        }
//                    }
//
//                    int contAux = 0;
//                    String strCompleta = String.format("%02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x", readBuffer[0], readBuffer[1], readBuffer[2], readBuffer[3], readBuffer[4], readBuffer[5], readBuffer[6], readBuffer[7], readBuffer[8], readBuffer[9], readBuffer[10], readBuffer[11], readBuffer[12], readBuffer[13], readBuffer[14], readBuffer[15], readBuffer[16], readBuffer[17], readBuffer[18], readBuffer[19], readBuffer[20], readBuffer[21], readBuffer[22], readBuffer[23], readBuffer[24]);
//                    System.out.println("string completa: " + strCompleta);
//                    if (liberado || (readBuffer[15 + contAux] == (byte) 0x4C)) {
//                        String volumeTratado = String.format("%02x%02x%02x%02x%02x%02x%02x%02x", readBuffer[15 + contAux], readBuffer[16 + contAux], readBuffer[17 + contAux], readBuffer[18 + contAux], readBuffer[19 + contAux], readBuffer[20 + contAux], readBuffer[21 + contAux], readBuffer[22 + contAux]);
//                        System.out.println("Liberado: " + volumeTratado);
//                        //setTxtStatus("LIBERADO");
//                        //txtStatus = "LIBERADO";
//                        //txtStatus.setText("LIBERADO");
//                        new FXDialog(FXDialog.Type.INFO, "Liberado!").showDialog();
//                        liberado = false;
//                    }
//                    if (readBuffer[15 + contAux] == (byte) 0x56) {//ENCHENDO
//                        String volumeTratado = String.format("%02x%02x%02x%02x%02x%02x%02x%02x", readBuffer[15 + contAux], readBuffer[16 + contAux], readBuffer[17 + contAux], readBuffer[18 + contAux], readBuffer[19 + contAux], readBuffer[20 + contAux], readBuffer[21 + contAux], readBuffer[22 + contAux]);
//                        outputBfj = new StringBuilder();
//                        for (int ij = 0; ij < volumeTratado.length(); ij += 2) {
//                            String strj = volumeTratado.substring(ij, ij + 2);
//                            outputBfj.append((char) Integer.parseInt(strj, 16));
//                        }
//                        if (outputBfj.length() > 5) {
//                            try {
//                                txtBicoUm.setText(outputBfj.toString());
//                                //setTxtStatus("APLICANDO");
//                                new FXDialog(FXDialog.Type.INFO, "Aplicando!").showDialog();
//                                System.out.println("outputBfj.toString(): " + outputBfj.toString());
//                                outputBfj.delete(0, outputBfj.length());
//                            } catch (NullPointerException e) {
//                                System.out.println("nullpointer");
//                            }
//                        } else {
//                            outputBfj.delete(0, outputBfj.length());
//                        }
//                    }
//                    if (readBuffer[15 + contAux] == (byte) 0x46) {
//                        try {
//                            String volumeTratado = String.format("%02x%02x%02x%02x%02x%02x%02x%02x", readBuffer[15 + contAux], readBuffer[16 + contAux], readBuffer[17 + contAux], readBuffer[18 + contAux], readBuffer[19 + contAux], readBuffer[20 + contAux], readBuffer[21 + contAux], readBuffer[22 + contAux]);
//                            System.out.println("FIM: " + volumeTratado);
//                            txtBicoUm.setText("FIM");
//                            new FXDialog(FXDialog.Type.INFO, "FIM!").showDialog();
//                            //setTxtEfetivo("VOL " + txtVolume.getText());
//                            execucaoWhile = false;
//                            outputBfj.delete(0, outputBfj.length());
//                        } catch (NullPointerException e) {
//                            System.out.println("nullpointer");
//                        }
//                    }
//
//                    if (readBuffer[15 + contAux] == (byte) 0x50) {
//                        try {
//                            String volumeTratado = String.format("%02x%02x%02x%02x%02x%02x%02x%02x", readBuffer[15], readBuffer[16], readBuffer[17], readBuffer[18], readBuffer[19], readBuffer[20], readBuffer[21], readBuffer[22]);
//                            txtBicoUm.setText("PARADO");
//                            new FXDialog(FXDialog.Type.INFO, "Parado!").showDialog();
//                            //setTxtEfetivo("VOL " + txtVolume.getText());
//                            execucaoWhile = false;
//                            outputBfj.delete(0, outputBfj.length());
//                        } catch (NullPointerException e) {
//                            System.out.println("nullpointer");
//                        }
//                    }
//                }
//            } catch (InterruptedException ex) {
//                System.out.println("InterruptedException");
//            } catch (IOException ex) {
//                System.out.println("IOException");
//            } catch (java.lang.NullPointerException e) {
//                System.out.println("NullPointerException: ");
//                e.printStackTrace();
//            }
////fim------------------------------------------------------------------------------
//        }
//
//        @Override
//
//        protected Void call() throws Exception {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        }
//    };
}
