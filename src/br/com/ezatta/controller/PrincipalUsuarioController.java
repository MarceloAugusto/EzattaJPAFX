package br.com.ezatta.controller;

import static br.com.ezatta.controller.LoginController.configurarPorta;
import static br.com.ezatta.controller.LoginController.defaultPort;
import static br.com.ezatta.controller.LoginController.entrada;
import static br.com.ezatta.controller.LoginController.ezattaProdutoStatic;
import static br.com.ezatta.controller.LoginController.ezattaUsuarioStatic;
import static br.com.ezatta.controller.LoginController.outputStream;
import static br.com.ezatta.controller.LoginController.portFound;
import static br.com.ezatta.controller.LoginController.portId;
import static br.com.ezatta.controller.LoginController.portList;
import static br.com.ezatta.controller.LoginController.saida;
import static br.com.ezatta.controller.LoginController.serialPort;
import br.com.ezatta.dao.BicosDAO;
import br.com.ezatta.dao.MovimentacoesDAO;
import br.com.ezatta.dao.OperadorDAO;
import br.com.ezatta.dao.ProdutoDAO;
import br.com.ezatta.dao.EstoqueProdutoDAO;
import br.com.ezatta.mail.TesteEmail;
import br.com.ezatta.model.EzattaBico;
import br.com.ezatta.model.EzattaEstoqueProduto;
import br.com.ezatta.model.EzattaMovimentacoes;
import br.com.ezatta.model.EzattaOperador;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.util.JPAUtil;
import br.com.ezatta.util.MaskTextField;
import br.com.ezatta.util.ValidationFields;
import br.com.ezatta.view.EzattaMain;
import br.com.ezatta.view.FXDialog;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
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

public class PrincipalUsuarioController implements Initializable {

    public static PrincipalUsuarioController principal = new PrincipalUsuarioController();

    private ObservableList<EzattaMovimentacoes> dadosParaEnvase = FXCollections.observableArrayList();
    private MovimentacoesDAO EstoqueCtr = new MovimentacoesDAO();

    String portaSelecionada = "";

    static StringBuilder outputBfj;
    static StringBuilder volumeBfj;
    static BufferedInputStream bufferedinputStream;
    public int tipoBaixa = 0;
    boolean enchendoo;
    //----------------------------------------Vars bicos----------------------
    private ObservableList<EzattaBico> dadosBico = FXCollections.observableArrayList();
    private ObservableList<EzattaOperador> dadosOperador = FXCollections.observableArrayList();
    private OperadorDAO operadorCtr = new OperadorDAO();
    private BicosDAO bicoCtr = new BicosDAO();
    private MovimentacoesDAO estoqueCtr = new MovimentacoesDAO();
    private MaskTextField maskTextField = new MaskTextField();
    private EzattaMovimentacoes estoque;
    //---------------------------------------fim vars bicos-------------------
    //--------------------------------------inicio var componentes envase
    private HBox[] hb = new HBox[10000];
    private GridPane[] gridpane = new GridPane[10000];
    private Text[] efetivo = new Text[10000];
    private ProgressBar[] progressBarEstoque = new ProgressBar[10000];
    private Float[] volumeTotal = new Float[10000];
    private Thread[] t = new Thread[10000];
    private Integer idEstoqueTh = 0;
    private boolean[] execucaoWhile = new boolean[10000];
    private String[] endBico = new String[10000];
    private Button[] btnEnviar = new Button[10000];
    private Button[] btnCancelarEstoque = new Button[10000];
    private Button[] btnSalvarEstoque = new Button[10000];
    private Button[] btnFechar = new Button[10000];
    private Separator[] separator = new Separator[10000];
    private Text[] stringEstoque = new Text[10000];
    //--------------------------------------fim var componentes envase

    @FXML
    private ListView<EzattaMovimentacoes> lvEstoque;

    @FXML
    private StackPane stack;

    @FXML
    private MenuItem miEncherTank;

    @FXML
    private MenuItem miBico;

    @FXML
    private Button btnEnvase;

    @FXML
    private MenuItem miOperador;

    @FXML
    private Accordion acPrincipal;

    @FXML
    private MenuItem miAjuda;

    @FXML
    private MenuItem miEmpresa;

    @FXML
    private StackPane stackContainer;

    @FXML
    private MenuItem miBackUp;

    @FXML
    private MenuItem miFechar;

    @FXML
    private TitledPane acEstoqueAtual;

    @FXML
    private TitledPane acProcessosEnvase;

    @FXML
    public static VBox estoqueAtual;

//    @FXML
//    private Text txtNomeEmpresa;
    @FXML
    private MenuItem miProduto;

    @FXML
    private MenuItem miCBico;

    @FXML
    private MenuItem miManual;

    @FXML
    private MenuItem miUsuario;

    @FXML
    public VBox vbList;

    @FXML
    public AnchorPane AnchorPanePrincipal;

    @FXML
    private BorderPane borderPanePrincipal;

    @FXML
    private ComboBox<EzattaBico> cbBico;

    @FXML
    private ListView<EzattaProduto> lvProdutos;

    @FXML
    private ComboBox<EzattaOperador> cbOperador;

    @FXML
    private Button btnEnviarPPlaca;

    @FXML
    private BorderPane paneProduto;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField txtVolume;

    @FXML
    private Button btnCancelar;

    @FXML
    private BorderPane paneBico;

    @FXML
    private TextField txtKm;

    @FXML
    private TextField txtOs;

    @FXML
    private TextField txtPlaca;

    @FXML
    private AnchorPane anchorContainer;

    @FXML
    private StackPane portaSerial;

    @FXML
    private AnchorPane anchorUltimosRegistros;

    @FXML
    private ListView<EzattaMovimentacoes> ultimosRegistros;

    @FXML
    private StackPane aviso;

    @FXML
    private AnchorPane anchorComunicacao;

    @FXML
    private TitledPane acComunicacao;

    public StackPane getStack() {
        return stack;
    }

    public void setStack(StackPane stack) {
        this.stack.getChildren().clear();
        this.stack = stack;
    }

    public void popularDadosListaEnvase() {
        try {
            dadosParaEnvase.clear();
            dadosParaEnvase.addAll(EstoqueCtr.getEstoqueByStatus(0));

            for (EzattaMovimentacoes dado : dadosParaEnvase) {
                adicionarVbList(dado);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarVolumePlaca(Integer idEstoqueTh) {
        try {
            EzattaMovimentacoes dado = estoqueCtr.getEstoque(idEstoqueTh);

            volumeTotal[idEstoqueTh] = dado.getQtdEstoque();
            endBico[idEstoqueTh] = dado.getBico().getEndereco();
            //teste toString
            System.out.println("idEstoqueTh: " + idEstoqueTh);
            System.out.println("ProgressBar: " + progressBarEstoque[idEstoqueTh].toString());
            System.out.println("Efetivo: " + efetivo[idEstoqueTh].toString());
            System.out.println("endBico: " + endBico[idEstoqueTh]);

            atualizaFatorEscala(dado);
            Thread.sleep(1000);
            enviarStringPlaca(dado);
            Thread.sleep(1000);
            enviarVolume(dado);

            t[idEstoqueTh] = new Thread(taskLeituraEnvase);
            t[idEstoqueTh].start();
//            ---------------------------------------INICIO----------------------------------
//            t[idEstoqueTh] = new Thread(new Runnable() { //thread que executa a leitura
//                public void run() {
//                
//                }
//            });
            //----------------------------------------FIM-----------------------------------

            System.out.println("t: " + t[idEstoqueTh].toString());
            //atualiza banco
            EzattaMovimentacoes est = EstoqueCtr.getEstoque(idEstoqueTh);
            est.setStatus(1);
            EstoqueCtr.updateEstoque(est);

            //desabilitar botão enviar
            btnEnviar[idEstoqueTh].setDisable(true);

            new FXDialog(FXDialog.Type.INFO, "Enviado para a placa").showDialog();
        } catch (InterruptedException ex) {
            Logger.getLogger(PrincipalUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PrincipalUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    Task taskLeituraEnvase = new Task() {
        
        @Override
        public void run() {
        //public void run(){
            
            System.out.println("entrou thread: ");
            System.out.println("taskLeituraEnvase: " + taskLeituraEnvase.toString());
            try {
                long numBytes = 0;
                int contAux = 0;
                byte[] readBuffer = new byte[1000];
                execucaoWhile[idEstoqueTh] = true;
                while (execucaoWhile[idEstoqueTh]) {
                    entrada = null;
                    int timeout = 0;
                    entrada = serialPort.getInputStream();

                    //joga fora primeiras menssagens de outros pedidos 
                    if (entrada.available() > 0) {
                        entrada.read(readBuffer);
                    }
                                       
                    numBytes = 0;
                    while (numBytes < 24) {
                        while (entrada.available() > 0) {
                            numBytes = numBytes + entrada.read(readBuffer, (int) numBytes, 1);//recebe os bytes na leitura.
                            timeout = 0;
                        }

                        Thread.sleep(1);

                        timeout++;
                        if (timeout > 25) {
                            numBytes = 0;
                            timeout = 0;
                        }
                        if (numBytes > 0) {
                            System.out.println("numBytes: " + numBytes);

                        }
                    }

                    String strCompleta = String.format("%02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x", readBuffer[0], readBuffer[1], readBuffer[2], readBuffer[3], readBuffer[4], readBuffer[5], readBuffer[6], readBuffer[7], readBuffer[8], readBuffer[9], readBuffer[10], readBuffer[11], readBuffer[12], readBuffer[13], readBuffer[14], readBuffer[15], readBuffer[16], readBuffer[17], readBuffer[18], readBuffer[19], readBuffer[20], readBuffer[21], readBuffer[22], readBuffer[23], readBuffer[24]);
                    System.out.println("string completa: " + strCompleta);

                    String endBicoRadio = String.format("%02x%02x%02x%02x%02x%02x%02x%02x", readBuffer[4 + contAux], readBuffer[5 + contAux], readBuffer[6 + contAux], readBuffer[7 + contAux], readBuffer[8 + contAux], readBuffer[9 + contAux], readBuffer[10 + contAux], readBuffer[11 + contAux]);
                    if (endBico[idEstoqueTh].toUpperCase().equals(endBicoRadio.toUpperCase())) {

                        if (readBuffer[15 + contAux] == (byte) 0x4c) { //LIBERADO
                            //if (readBuffer[15] == (byte) 0x4c || readBuffer[02] == (byte) 0x07) {
                            String volumeTratado = String.format("%02x%02x%02x%02x%02x%02x%02x%02x", readBuffer[15], readBuffer[16], readBuffer[17], readBuffer[18], readBuffer[19], readBuffer[20], readBuffer[21], readBuffer[22]);
                            System.out.println("Liberado: " + volumeTratado);
                            efetivo[idEstoqueTh].setText("Liberado");
                            btnCancelarEstoque[idEstoqueTh].disableProperty();
                        }
                        if (readBuffer[15 + contAux] == (byte) 0x56) {//ENCHENDO

                            //alterando status do botão enviar
                            Tooltip tpCancelar = new Tooltip();
                            tpCancelar.setText("Clique em cancelar para cancelar o volume na placa.");
                            btnCancelarEstoque[idEstoqueTh].disableProperty();
                            btnCancelarEstoque[idEstoqueTh].setDisable(true);
                            btnCancelarEstoque[idEstoqueTh].setTooltip(tpCancelar);

                            //btnSalvar
                            Tooltip tpSalvar = new Tooltip();
                            tpSalvar.setText("Clique em salvar para completar."
                                    + "\n Esse recurso é utilizado quando se tem a necessidade de parar o envase"
                                    + "\n e debitar a quantidade envasada...");
                            btnSalvarEstoque[idEstoqueTh].setTooltip(tpSalvar);
                            btnSalvarEstoque[idEstoqueTh].setDisable(false);

                            //ProgressBar
                            progressBarEstoque[idEstoqueTh].setVisible(true);

                            String volumeTratado = String.format("%02x%02x%02x%02x%02x%02x%02x%02x", readBuffer[15 + contAux], readBuffer[16 + contAux], readBuffer[17 + contAux], readBuffer[18 + contAux], readBuffer[19 + contAux], readBuffer[20 + contAux], readBuffer[21 + contAux], readBuffer[22 + contAux]);
                            String volumeTratadoDouble = String.format("%02x%02x%02x%02x", readBuffer[19 + contAux], readBuffer[20 + contAux], readBuffer[21 + contAux], readBuffer[22 + contAux]);

                            outputBfj = new StringBuilder();
                            volumeBfj = new StringBuilder();

                            //monta uma string com vol
                            for (int ij = 0; ij < volumeTratado.length(); ij += 2) {
                                String strj = volumeTratado.substring(ij, ij + 2);
                                outputBfj.append((char) Integer.parseInt(strj, 16));
                            }
                            //monta uma string 00.00
                            for (int ij = 0; ij < volumeTratadoDouble.length(); ij += 2) {
                                String strjDouble = volumeTratadoDouble.substring(ij, ij + 2);
                                volumeBfj.append((char) Integer.parseInt(strjDouble, 16));
                            }
                            String volumeSemVol = volumeBfj.toString();
                            System.out.println("volumeSemVol: " + volumeSemVol);

                            if (readBuffer[15 + contAux] == (byte) 0x56 && outputBfj.length() > 5) {
                                try {
                                    //efetivo[idEstoqueTh].setText(outputBfj.toString());
                                    efetivo[idEstoqueTh].setText(volumeBfj.toString());
                                    System.out.println("VolumeTotal " + volumeTotal[idEstoqueTh]);
                                    if (!volumeSemVol.isEmpty() && volumeSemVol.length() > 0) {
                                        atualizaBarraProgress(volumeBfj.toString());//barra de progresso
                                    } else {
                                        outputBfj.delete(0, outputBfj.length());//limpa buffer
                                    }
                                } catch (NullPointerException e) {
                                    System.out.println("nullpointer");
                                    e.printStackTrace();
                                }
                                outputBfj.delete(0, outputBfj.length());//limpa buffer
                            }
                        } else {
                            try {
                                outputBfj.delete(0, outputBfj.length());
                            } catch (Exception e) {

                            }
                        }
                        if (readBuffer[15 + contAux] == (byte) 0x46) {
                            //mensagem de fim
                            efetivo[idEstoqueTh].setText("FIM");
                            execucaoWhile[idEstoqueTh] = false;
                            Thread.sleep(100);

                            removerRegistroTela(idEstoqueTh);

                            //cancela thread
                            //taskLeituraEnvase.cancel();
                            //--------------------------atualizar status
                            EzattaMovimentacoes e = EstoqueCtr.getEstoque(idEstoqueTh);
                            e.setStatus(2);
                            EstoqueCtr.updateEstoque(e);

                            //limpa outputBuffer
                            outputBfj.delete(0, outputBfj.length());
                        }
                    }
                }
            } catch (InterruptedException ex) {
                System.out.println("InterruptedException");
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println("IOException");
                ex.printStackTrace();
            } catch (java.lang.NullPointerException e) {
                System.out.println("NullPointerException: ");
                e.printStackTrace();
            }
//fim------------------------------------------------------------------------------
        }

        @Override
        protected Void call() throws Exception {
            return null;
        }

        @Override
        protected void succeeded() {
        }

    };

    private void cancelarVolumePlaca(Integer idEstoqueTh) {

        vbList.getChildren().remove(gridpane[idEstoqueTh]);
        vbList.getChildren().remove(separator[idEstoqueTh]);
        //cancela no banco de dados
        cancelarNoBanco(idEstoqueTh);
        cancelarPlaca(idEstoqueTh);
    }

    public void completarTanque(int idEstoqueTh) {
        Float valorARetornar;
        try {
            EzattaMovimentacoes ezattaEstoqueSalvar = estoqueCtr.getEstoque(idEstoqueTh);

            //Atualizar qtd Tbl_Produto-------------------------------------------------------------------
            EzattaProduto ezattaProdutoSalvar = produtoCtr.getProduto(ezattaEstoqueSalvar.getProduto().getId());//busca produto 
            //pega o valor atual na tabela produto
            Float qtdProduto = ezattaProdutoSalvar.getQuantidade().floatValue();
            //pega o valor do estoque a ser retornado na tabela produto
            valorARetornar = ezattaEstoqueSalvar.getQtdEstoque();
            //soma a quantidade tblEstoque a tblProduto
            Float calcularValor = 0f;
            calcularValor = qtdProduto + valorARetornar;
            System.out.println("qtdProduto: " + qtdProduto);
            System.out.println("valorARetornar: " + valorARetornar);
            calcularValor = calcularValor - Float.parseFloat(efetivo[idEstoqueTh].getText());
            System.out.println("calcularValor: " + calcularValor);

            //atualizar quantidade tblProduto
            ezattaProdutoSalvar.setQuantidade(new BigDecimal(calcularValor));

            //Salvar no banco volume efetivo----------------------------------------------------------------
            Float quantidadeEfetiva = new Float(efetivo[idEstoqueTh].getText());
            Float resto = valorARetornar - quantidadeEfetiva;
            ezattaEstoqueSalvar.setQtdEstoque(resto);
            ezattaEstoqueSalvar.setStatus(4);
            //Thread.sleep(100);
            estoqueCtr.updateEstoque(ezattaEstoqueSalvar);

            try {
                produtoCtr.updateProduto(ezattaProdutoSalvar);
            } catch (SQLException ex) {
                Logger.getLogger(PrincipalUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Parar Thread
            execucaoWhile[idEstoqueTh] = false;

            //um segundos
            Thread.sleep(100);

            //Cancelar Placa
            cancelarPlaca(idEstoqueTh);

            //remove Registro Tela
            removerRegistroTela(idEstoqueTh);

        } catch (InterruptedException | NumberFormatException ex) {
            //Logger.getLogger(PrincipalUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    Tooltip desconectado = new Tooltip("Favor verificar a conexão USB \ncom o Rádio transmissor \nVerifique a guia Configuração.");
    Tooltip conectado = new Tooltip("Rádio conectado.");

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        iconeConectado.setVisible(false);
        iconeDesconectado.setVisible(false);

        aviso.setVisible(false);

        //valida porta e verifica se esta conectado na com 4
        validaPorta();

        //popular combobox portas
        popularCbPortas();

        //ocultar progress bar bico
        progressBar.setVisible(false);
        //carrega aberto
        acPrincipal.setExpandedPane(acEstoqueAtual);

        //mostra tela produtos e oculta bicos
        paneProduto.setVisible(true);
        paneBico.setVisible(false);

        //txtNomeEmpresa.setText("Seja bem vindo: " + ezattaUsuarioStatic.getEmpresa().getNome());
        //EzattaMain.stage.close();
        EzattaMain.stage.hide();

        //carrega produtos tela principal
        popularProdutos();

        //clicar no produto da lista
        lvProdutos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //inicio
                try {
                    limparComponentesBicoEnvase();
                    ezattaProdutoStatic = lvProdutos.getSelectionModel().getSelectedItem();
                    System.out.println("ezattaProdutoStatic: " + ezattaProdutoStatic.getNome() + " - " + ezattaProdutoStatic.getId());

                    //tratamento bicos + populadados
                    ValidationFields.checkEmptyFields(txtVolume, txtOs, txtPlaca, cbBico, cbOperador);
                    populaDadosBico();

                    //mostra tela bicos e oculta produtos
                    paneProduto.setVisible(false);
                    paneBico.setVisible(true);

                } catch (Exception ex) {
                    //new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
                }
            }
        });

        //popula listEstoque 
        popularDadosListaEnvase();
        popularProdutos();

        //carregar Tela Container
        try {
            stackContainer.getChildren().clear();
            stackContainer.getChildren().add(getNode("/br/com/ezatta/view/PrincipalListStatusProduto.fxml"));
            //stackContainer.getChildren().add(getNode("/br/com/ezatta/view/Usuario.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

    //inicio popula dados Bicos =====================================================================================
    public void populaDadosBico() {
        dadosBico.clear();
        dadosOperador.clear();
        cbBico.getItems().clear();
        cbOperador.getItems().clear();
        try {
            dadosBico.addAll(bicoCtr.getAllBicoByProd(ezattaProdutoStatic));
            dadosOperador.addAll(operadorCtr.getAllOperador());
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, e.getCause().getMessage()).showDialog();
        }
        cbBico.setItems(dadosBico);
        cbOperador.setItems(dadosOperador);
    }
    //fim popula dados bicos ========================================================================================

    //inicio popula lista Produtos ==================================================================================
    private ObservableList<EzattaProduto> dadosProdutos = FXCollections.observableArrayList();
    private ProdutoDAO produtoCtr = new ProdutoDAO();

    private void popularProdutos() {
        dadosParaEnvase.clear();
        ultimosRegistros.getItems().clear();

        dadosProdutos.clear();
        lvProdutos.getItems().clear();
        try {
            dadosParaEnvase.addAll(estoqueCtr.getAllEstoqueLimitOrderBy());
            dadosProdutos.addAll(produtoCtr.getAllProduto());
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, e.getCause().getMessage()).showDialog();
        }
        ultimosRegistros.setItems(dadosParaEnvase);
        lvProdutos.setItems(dadosProdutos);
    }

    //fim popula lista Produtos ==================================================================================
    @FXML
    void carregarProdutoTela(ActionEvent event) {
        popularProdutos();
    }

    private void removerRegistroTela(Integer idEstoqueTh) {
        //remove registro da tela
        gridpane[idEstoqueTh].setGridLinesVisible(false);
        gridpane[idEstoqueTh].setMinHeight(0);
        gridpane[idEstoqueTh].setMaxHeight(0);
        gridpane[idEstoqueTh].setMaxWidth(0);
        gridpane[idEstoqueTh].setMinWidth(0);
        hb[idEstoqueTh].setVisible(false);
        hb[idEstoqueTh].setMaxHeight(0);
        separator[idEstoqueTh].setVisible(false);
        separator[idEstoqueTh].setMaxHeight(0);
        stringEstoque[idEstoqueTh].setText("");
        efetivo[idEstoqueTh].setText("");
        progressBarEstoque[idEstoqueTh].setMinHeight(0);
        progressBarEstoque[idEstoqueTh].setMaxHeight(0);
        progressBarEstoque[idEstoqueTh].setMaxWidth(0);
        progressBarEstoque[idEstoqueTh].setMinWidth(0);
        progressBarEstoque[idEstoqueTh].setVisible(false);
    }

    public void atualizaBarraProgress(String txtEfetivo) {
        System.out.println("txtEfetivo: " + txtEfetivo);
        if (txtEfetivo.isEmpty()) {
            System.out.println("String Empty");
            txtEfetivo = "0";
        }
        if (txtEfetivo != null && txtEfetivo.length() > 0 && txtEfetivo != "") {
            try {
                try {
                    progressBarEstoque[idEstoqueTh].setProgress(0);
                    //Efetivo
                    double volEfetivo = Double.parseDouble(txtEfetivo);

                    //total
                    double volTotal = volumeTotal[idEstoqueTh]; //Total

                    System.out.println("volTotal: " + volTotal + " - volEfetivo: " + volEfetivo);
                    //progressBar[idEstoqueTh].setProgress(porcentagem);
                    progressBarEstoque[idEstoqueTh].setProgress(volEfetivo / volTotal);
                } catch (IndexOutOfBoundsException | NumberFormatException | NullPointerException e) {
                    System.out.println("saber como validar");
                    //e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void enviarStringPlaca(EzattaMovimentacoes dado) throws IOException {

        try {
            saida = serialPort.getOutputStream();
            System.out.println("FLUXO OK!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro.STATUS: " + e);
        }

        EzattaBico bico = (EzattaBico) dado.getBico();
        String txtPlaca = dado.getPlaca();
        //String txtPlaca = "AAA12";
        int envia_texto = 0;
        int tamanho = txtPlaca.length();
        System.out.println("txtPlaca: " + txtPlaca);
        for (int i = 0; i < tamanho; i++) {
            String placa = txtPlaca.toUpperCase();

            int stringConvertidaemInt = placa.charAt(i);

            if (stringConvertidaemInt > 127) {
                stringConvertidaemInt = 45;
            }

            System.out.println("Valor asc: " + stringConvertidaemInt);
            envia_texto += 1;
            int label19 = envia_texto + 59;
            long crc = 238L - Long.parseLong(bico.getEndereco().substring(0, 2), 16) - Long.parseLong(bico.getEndereco().substring(2, 4), 16) - Long.parseLong(bico.getEndereco().substring(4, 6), 16) - Long.parseLong(bico.getEndereco().substring(6, 8), 16) - Long.parseLong(bico.getEndereco().substring(8, 10), 16) - Long.parseLong(bico.getEndereco().substring(10, 12), 16) - Long.parseLong(bico.getEndereco().substring(12, 14), 16) - Long.parseLong(bico.getEndereco().substring(14, 16), 16) - 255L - 254L - 87L - 77L - label19 - stringConvertidaemInt - 0L - 13L;
            String FinalrCrc = Long.toHexString(crc).substring(14, 16);
            System.out.println("Label19: " + label19);
            System.out.println("Letra:  " + stringConvertidaemInt);
            System.out.println("CRC:  " + FinalrCrc);
            System.out.println("CRCinteiro:  " + crc);
            byte[] uartout = {126, 0, 20, 16, 1, (byte) (int) Long.parseLong(bico.getEndereco().substring(0, 2), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(2, 4), 16), (byte) (int) Long.parseLong(bico.getEndereco().substring(4, 6), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(6, 8), 16), (byte) (int) Long.parseLong(bico.getEndereco().substring(8, 10), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(10, 12), 16), (byte) (int) Long.parseLong(bico.getEndereco().substring(12, 14), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(14, 16), 16), -1, -2, 0, 0, 87, 77, (byte) label19, 0, (byte) stringConvertidaemInt, 13,
                (byte) (int) crc};
            saida.write(uartout);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int stringConvertidaemInt = 32;
        for (int i = 0; i < 2; i++) {
            stringConvertidaemInt = 32;

            System.out.println("Valor asc: " + stringConvertidaemInt);

            envia_texto += 1;

            int label19 = envia_texto + 59;
            long crc = 238L - Long.parseLong(bico.getEndereco().substring(0, 2), 16) - Long.parseLong(bico.getEndereco().substring(2, 4), 16)
                    - Long.parseLong(bico.getEndereco().substring(4, 6), 16) - Long.parseLong(bico.getEndereco().substring(6, 8), 16)
                    - Long.parseLong(bico.getEndereco().substring(8, 10), 16) - Long.parseLong(bico.getEndereco().substring(10, 12), 16)
                    - Long.parseLong(bico.getEndereco().substring(12, 14), 16) - Long.parseLong(bico.getEndereco().substring(14, 16), 16) - 255L - 254L - 87L - 77L
                    - label19 - stringConvertidaemInt - 0L - 13L;
            String FinalrCrc = Long.toHexString(crc).substring(14, 16);
            System.out.println("Label19: " + label19);
            System.out.println("Letra:  " + stringConvertidaemInt);
            System.out.println("CRC:  " + FinalrCrc);
            System.out.println("CRCinteiro:  " + crc);
            byte[] uartout = {126, 0, 20, 16, 1, (byte) (int) Long.parseLong(bico.getEndereco().substring(0, 2), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(2, 4), 16), (byte) (int) Long.parseLong(bico.getEndereco().substring(4, 6), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(6, 8), 16), (byte) (int) Long.parseLong(bico.getEndereco().substring(8, 10), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(10, 12), 16), (byte) (int) Long.parseLong(bico.getEndereco().substring(12, 14), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(14, 16), 16), -1, -2, 0, 0, 87, 77, (byte) label19, 0, (byte) stringConvertidaemInt, 13,
                (byte) (int) crc};
            saida.write(uartout);
        }
        int envia_Operador = 0;
        EzattaOperador nomeOperador = dado.getOperador();
        int tamanhoOperador = nomeOperador.getNome().length();
        for (int i = 0; i < tamanhoOperador; i++) {
            String operador = nomeOperador.getNome().toUpperCase();
            stringConvertidaemInt = operador.charAt(i);

            System.out.println("Valor asc: " + stringConvertidaemInt);

            if (stringConvertidaemInt > 127) {
                stringConvertidaemInt = 45;
            }

            envia_texto += 1;
            int label19 = envia_texto + 59;
            long crc = 238L - Long.parseLong(bico.getEndereco().substring(0, 2), 16)
                    - Long.parseLong(bico.getEndereco().substring(2, 4), 16) - Long.parseLong(bico.getEndereco().substring(4, 6), 16)
                    - Long.parseLong(bico.getEndereco().substring(6, 8), 16) - Long.parseLong(bico.getEndereco().substring(8, 10), 16)
                    - Long.parseLong(bico.getEndereco().substring(10, 12), 16) - Long.parseLong(bico.getEndereco().substring(12, 14), 16)
                    - Long.parseLong(bico.getEndereco().substring(14, 16), 16) - 255L - 254L - 87L - 77L - label19 - stringConvertidaemInt - 0L - 13L;
            String FinalrCrc = Long.toHexString(crc).substring(14, 16);

            System.out.println("Label: " + label19);
            System.out.println("Letra:  " + stringConvertidaemInt);
            System.out.println("CRC:  " + FinalrCrc);
            System.out.println("CRCinteiro:  " + crc);
            byte[] uartout = {126, 0, 20, 16, 1, (byte) (int) Long.parseLong(bico.getEndereco().substring(0, 2), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(2, 4), 16), (byte) (int) Long.parseLong(bico.getEndereco().substring(4, 6), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(6, 8), 16), (byte) (int) Long.parseLong(bico.getEndereco().substring(8, 10), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(10, 12), 16), (byte) (int) Long.parseLong(bico.getEndereco().substring(12, 14), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(14, 16), 16), -1, -2, 0, 0, 87, 77, (byte) label19, 0, (byte) stringConvertidaemInt, 13,
                (byte) (int) crc};
            saida.write(uartout);
        }
        /*------------------------------------------------------enviar 0 --------------------------------------*/
        String StringZero = "0";

        int tamanhoZero = StringZero.length();
        for (int i = 0; i < tamanhoZero; i++) {
            String zero = "48";

            stringConvertidaemInt = zero.charAt(i);
            System.out.println("Valor asc: " + stringConvertidaemInt);
            envia_texto += 1;
            int label19 = envia_texto + 59;
            long crc = 238L - Long.parseLong(bico.getEndereco().substring(0, 2), 16)
                    - Long.parseLong(bico.getEndereco().substring(2, 4), 16) - Long.parseLong(bico.getEndereco().substring(4, 6), 16)
                    - Long.parseLong(bico.getEndereco().substring(6, 8), 16) - Long.parseLong(bico.getEndereco().substring(8, 10), 16)
                    - Long.parseLong(bico.getEndereco().substring(10, 12), 16) - Long.parseLong(bico.getEndereco().substring(12, 14), 16)
                    - Long.parseLong(bico.getEndereco().substring(14, 16), 16) - 255L - 254L - 87L - 77L - label19 - 0L - 0L - 13L;
            String FinalrCrc = Long.toHexString(crc).substring(14, 16);
            System.out.println("Label: " + label19);
            System.out.println("Letra:  " + stringConvertidaemInt);
            System.out.println("CRC:  " + FinalrCrc);
            System.out.println("CRCinteiro:  " + crc);
            System.out.println("Valor:  " + zero);

            byte[] uartout = {126, 0, 20, 16, 1, (byte) (int) Long.parseLong(bico.getEndereco().substring(0, 2), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(2, 4), 16), (byte) (int) Long.parseLong(bico.getEndereco().substring(4, 6), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(6, 8), 16), (byte) (int) Long.parseLong(bico.getEndereco().substring(8, 10), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(10, 12), 16), (byte) (int) Long.parseLong(bico.getEndereco().substring(12, 14), 16),
                (byte) (int) Long.parseLong(bico.getEndereco().substring(14, 16), 16), -1, -2, 0, 0, 87, 77, (byte) label19, 0, (byte) 0, 13,
                (byte) (int) crc};
            saida.write(uartout);
        }
        /*----------------------------------------------------------------------------------fim envio 0 --------------------------*/
//                            this.saida.flush();
//                            this.saida.close();
//                            serialPort.removeEventListener();
//                            outputStream.close();

        /*--fechar---------------------------------------------------------*/
    }

    private void atualizaFatorEscala(EzattaMovimentacoes dado) throws InterruptedException, IOException {
        try {
            System.out.println("dados: " + dado);
            saida = serialPort.getOutputStream();
            System.out.println("FLUXO OK!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro.STATUS: " + e);
        }

        EzattaBico bico = dado.getBico();
        System.out.println("Fator escala atual: " + bico.getFatorescala());

        long volume = Long.parseLong(bico.getFatorescala());

        long crc = 0xff - 0x11 - Long.parseLong(bico.getEndereco().substring(0, 2), 16) - Long.parseLong(bico.getEndereco().substring(2, 4), 16) - Long.parseLong(bico.getEndereco().substring(4, 6), 16) - Long.parseLong(bico.getEndereco().substring(6, 8), 16) - Long.parseLong(bico.getEndereco().substring(8, 10), 16) - Long.parseLong(bico.getEndereco().substring(10, 12), 16) - Long.parseLong(bico.getEndereco().substring(12, 14), 16) - Long.parseLong(bico.getEndereco().substring(14, 16), 16)
                - 0xFF - 0xFE - 0x57 - 0x4D - 0x18 - 0x00 - (volume) - 0x0D;
        String FinalrCrc = Long.toHexString(crc).substring(14, 16);
        System.out.println("CRC: " + FinalrCrc);
        System.out.println("primeiro: " + volume);

        byte[] uartout = new byte[]{
            (byte) 0x7E, (byte) 0x00, (byte) 0x14, (byte) 0x10, (byte) 0x01,
            (byte) (Long.parseLong(bico.getEndereco().substring(0, 2), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(2, 4), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(4, 6), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(6, 8), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(8, 10), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(10, 12), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(12, 14), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(14, 16), 16)),
            (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00,
            (byte) 0x57, (byte) 0x4D, (byte) 0x18,
            (byte) 0x00, (byte) volume, (byte) 0x0D, (byte) crc};

        saida.write(uartout);
        Thread.sleep(900);  // ??  
        //saida.flush();  // ??  
    }

    private void enviarVolume(EzattaMovimentacoes dado) {

        try {
            saida = serialPort.getOutputStream();
            System.out.println("FLUXO OK!");
        } catch (Exception e) {
            System.out.println("Erro.STATUS: " + e);
        }

        try {

            String txtVolume = String.valueOf(dado.getQtdEstoque());
            EzattaBico bico = dado.getBico();

            //Quebrar valor do volume
            String vo = txtVolume;
            String volumeMSB = vo.substring(0, 2);
            String enviar = txtVolume;
            System.out.println("enviar: " + enviar);

            //substitui a virgula por ponto
            if (enviar.contains(",")) {
                enviar = enviar.replace(",", ".");
                System.out.println("enviar 1: " + enviar);
            }
            //remove caracteres a mais
            if (enviar.length() > 5) {
                enviar = enviar.substring(1, 6);
                System.out.println("enviar 2: " + enviar);
            }

            //tratamento do volume
            if (enviar.contains(".")) { //verifica se tem o ponto
                String parts[] = enviar.trim().split("\\.");//04-03
                String part1 = parts[0]; // 04
                String part2 = parts[1]; // 03
                enviar = "";
                if (part1.length() == 1) {
                    part1 = "0" + part1;
                }
                if (part2.length() == 1) {
                    part2 = part2 + "0";
                }
                enviar = part1.concat(part2);
                System.out.println("Volume = " + enviar);
            } else {
                enviar = "0" + enviar + "00";
                System.out.println("Volume = " + enviar);
            }

            Integer volume_inteiro = Integer.parseInt(enviar); // Volume total como inteiro
            System.out.println("Volume total como inteiro " + volume_inteiro);
            //String volumeLSB = vo.substring(3, 5);

            int volumeMSBL = (volume_inteiro) & 255;
            int volumeLSBL = ((volume_inteiro & 65280) / 256);
            //mostrar parte alta e baixa do inteiro
            System.out.println("String: " + (String.valueOf(volumeMSBL)) + " " + (String.valueOf(volumeLSBL)));

            long crc = 0xff - 0x11 - Long.parseLong(bico.getEndereco().substring(0, 2), 16) - Long.parseLong(bico.getEndereco().substring(2, 4), 16) - Long.parseLong(bico.getEndereco().substring(4, 6), 16) - Long.parseLong(bico.getEndereco().substring(6, 8), 16) - Long.parseLong(bico.getEndereco().substring(8, 10), 16) - Long.parseLong(bico.getEndereco().substring(10, 12), 16) - Long.parseLong(bico.getEndereco().substring(12, 14), 16) - Long.parseLong(bico.getEndereco().substring(14, 16), 16)
                    - 0xFF - 0xFE - 0x57 - 0x4D - 0x01 - (volumeLSBL) - (volumeMSBL) - 0x0D;
            String FinalrCrc = Long.toHexString(crc).substring(14, 16);

            byte[] uartout = new byte[]{
                (byte) 0x7E, (byte) 0x00, (byte) 0x14, (byte) 0x10, (byte) 0x01,
                (byte) (Long.parseLong(bico.getEndereco().substring(0, 2), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(2, 4), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(4, 6), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(6, 8), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(8, 10), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(10, 12), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(12, 14), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(14, 16), 16)),
                //COMPLEMENTAR
                (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00, (byte) 0x57, (byte) 0x4D, (byte) 0x01,
                //volume a ser enviado
                (byte) volumeLSBL, (byte) volumeMSBL, (byte) 0x0D, (byte) crc};
            try {
                saida.write(uartout);
                System.out.println("Foi...");
                //Thread.sleep(3000);
            } catch (IOException ex) {
                System.out.println("Erro na comunicação");
            }

        } catch (Exception e) {
            e.printStackTrace();
            serialPort.close();
        }
        if (!portFound) {
            serialPort.close();
        }
    }

    @FXML
    void fechar(ActionEvent event) {
        //fechar conex'ao H2DB
        System.out.println("Fechou H2DB");
        JPAUtil.closeManager(JPAUtil.getEntityManager());
        //fechar conexao Mysql
//        System.out.println("Fechou Mysql");
//        JPAUtilChamado.closeManager(JPAUtilChamado.getEntityManager());
        //fecha aporta
        System.out.println("port " + defaultPort + " not found.");
        serialPort.close();
        System.out.println("Porta fechada...");

        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(PrincipalUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Platform.exit();
        System.exit(0);
    }

    @FXML
    void cadEmpresa(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/Empresa.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            e.printStackTrace();
        }
    }

    @FXML
    void cadProduto(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/Produto.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            e.printStackTrace();
        }
    }

    @FXML
    void cadBico(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/Bico.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

    @FXML
    void cadOperador(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/Operador.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

    @FXML
    void ajuda(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/AjudaChamado.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "É necessário estar conectado a internet \nTentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

    @FXML
    void cadUsuario(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/Usuario.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

    @FXML
    void encherTank(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/EncherTanque.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

    @FXML
    void backup(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/BackUp.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de backup");
            e.printStackTrace();
        }
    }

    @FXML
    void calibrarBico(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/FatorEscala.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

    @FXML
    void suporte(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/AjudaSuporte.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

    public PrincipalUsuarioController() {

    }

    public void limpar() {
        try {
            stack.getChildren().clear();
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            e.printStackTrace();
        }
    }

    public void limparContainerPrincipal() {
        try {
            stackContainer.getChildren().clear();
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            e.printStackTrace();
        }
    }

//metodo que organiza o node das telas fxml
    public Node getNode(String node) {
        Node no = null;
        try {
            no = FXMLLoader.load(getClass().getResource(node));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return no;
    }

    void start(Stage stage) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("Principal.fxml"));
        Scene scene = new Scene(parent);

        //icone
        Image icon = new Image(getClass().getResourceAsStream("icone.png"));
        stage.getIcons().add(icon);
        //inicio
        stage.centerOnScreen();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setScene(scene);
        stage.show();

    }

    public void stop() {
        //fechar conex'ao
        System.out.println("fechar");
        JPAUtil.closeManager(JPAUtil.getEntityManager());

        //fecha aporta
        System.out.println("port " + defaultPort + " not found.");
        serialPort.close();

        //fechar app
        System.out.println("Porta fechada...");
        Platform.exit();
    }

    public void verEstoque() {
        EzattaMovimentacoes estoque = new EzattaMovimentacoes();
        MovimentacoesDAO estoquedao = new MovimentacoesDAO();
        List<EzattaMovimentacoes> lista = estoquedao.getAllEstoque();
        for (EzattaMovimentacoes lista1 : lista) {
            System.out.println(lista1);
        }
    }

    public void handle(WindowEvent event) {
        stop();
        Platform.exit();
        System.exit(0);
    }

    //-------------------------------------------------inicio novos metodos
    @FXML
    void cancelar(ActionEvent event) {
        //mostra tela produtos e oculta bicos
        paneProduto.setVisible(true);
        paneBico.setVisible(false);
    }

    @FXML
    void enviarPPlaca(ActionEvent event) {
        if (isValidaTela()) {
            BigDecimal quantidade = ezattaProdutoStatic.getQuantidade();
            BigDecimal minimo = ezattaProdutoStatic.getEstoqueMinimo();
            System.out.println("quantidade: " + quantidade + " - minimo: " + minimo);
            if (minimo.compareTo(quantidade) == 1) {
                System.out.println("Entrou");
                new FXDialog(FXDialog.Type.WARNING, "Envio Bloqueado, volume minimo foi atingido!").showDialog();
                try {
                    enviarEmailBloqueio();
                } catch (MessagingException ex) {
                    Logger.getLogger(PrincipalUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                salvarNoBanco();
                cancelar(event);
                acPrincipal.setExpandedPane(acEstoqueAtual);
            }
        }
    }

    public void limparComponentesBicoEnvase() {
        cbBico.getSelectionModel().clearSelection();
        cbOperador.getSelectionModel().clearSelection();
        txtVolume.setText("");
        txtOs.setText("");
        txtKm.setText("");
        txtPlaca.setText("");
    }

    private boolean isValidaTela() {
        boolean ok = true;
        if (cbBico.getValue() == null) {
            new FXDialog(FXDialog.Type.WARNING, "Favor preencher o bico!").showDialog();
            cbBico.requestFocus();
            ok = false;
        } else if (cbOperador.getValue() == null) {
            new FXDialog(FXDialog.Type.WARNING, "Favor preencher o Operador!").showDialog();
            cbOperador.requestFocus();
            ok = false;
        } else if (txtVolume.getText().isEmpty()) {
            new FXDialog(FXDialog.Type.WARNING, "Favor preencher o Volume a ser envasado!").showDialog();
            txtVolume.requestFocus();
            ok = false;
        } else if (!validaFormatacaoVolume()) {

            new FXDialog(FXDialog.Type.WARNING, "Favor preencher o Volume com a seguinte formatação 01.00! \n Volume maximo 99.99").showDialog();
            txtVolume.requestFocus();
            ok = false;
        } else if (txtOs.getText().isEmpty()) {
            new FXDialog(FXDialog.Type.WARNING, "Favor preencher a Ordem de serviço!").showDialog();
            txtOs.requestFocus();
            ok = false;
        } else if (txtPlaca.getText().isEmpty()) {
            new FXDialog(FXDialog.Type.WARNING, "Favor preencher a Placa do veículo!").showDialog();
            txtPlaca.requestFocus();
            ok = false;
        }
        return ok;
    }

    private boolean validaFormatacaoVolume() {

        boolean valida = false;

        String vol = txtVolume.getText();
        vol = vol.replace(",", ".");

        CharSequence ponto = ".";
        if (vol.contains(ponto)) {

            String parts[] = vol.trim().split("\\.");//04-03
            String part1 = parts[0]; // 04
            valida = true;

            if (part1.length() > 2) {
                valida = false;
                System.out.println("part1: " + part1);
            }

        }

        return valida;
    }

    private void salvarNoBanco() {

        estoque = new EzattaMovimentacoes();
        String vol = txtVolume.getText();
        System.out.println("Salva no banco 1");
        if (vol.contains(",")) {
            vol = vol.replace(",", ".");
        }

        if (vol.contains(".")) { //verifica se tem o ponto
            String parts[] = vol.trim().split("\\.");//04-03
            String part1 = parts[0]; // 04
            String part2 = parts[1]; // 03
            vol = "";
            //part1------------------
            if (part1.length() == 1) {
                part1 = "0" + part1;
            } else if (part1.length() > 2) {
                part1 = part1.substring(0, 1);
            }
            //part2------------------
            System.out.println("part2: " + part2);
            if (part2.length() == 1) {
                part2 = part2 + "0";
                System.out.println("part2: " + part2 + " 1 ");
            } else if (part2.length() > 2) {
                part2 = part2.substring(0, 2);
                System.out.println("part2: " + part2 + " 2 ");
            }
            //junta as duas strings com ponto
            vol = part1.concat(".").concat(part2);
            System.out.println("Volume = " + vol);
        } else {
            vol = "0" + vol + "00";
            System.out.println("Volume = " + vol);
        }
        //--------------------------fim valida antes de inserir
        estoque.setQtdEstoque(Float.parseFloat(vol));
        estoque.setOperador((EzattaOperador) cbOperador.getValue());
        estoque.setBico((EzattaBico) cbBico.getValue());
        estoque.setOs(txtOs.getText());
        estoque.setKm(txtKm.getText());
        String placa = removerAcentos(txtPlaca.getText().toUpperCase());
        if (placa.contains("-")) {
            placa = placa.replace("-", "");
        }
        estoque.setPlaca(placa);
        //estoque.setPlaca(txtPlaca.getText());
        estoque.setProduto(ezattaProdutoStatic);//ezattaProdutoStatic
        estoque.setStatus(0);
        Timestamp data = new Timestamp(System.currentTimeMillis());
        estoque.setDataEstoque(data);
        estoque.setUsuario(ezattaUsuarioStatic);

        ProdutoDAO produtoCtr = new ProdutoDAO();
        BigDecimal quantidadeProduto = ezattaProdutoStatic.getQuantidade().setScale(2, RoundingMode.FLOOR);
        BigDecimal quantidadeEstoque = new BigDecimal(estoque.getQtdEstoque()).setScale(2, RoundingMode.FLOOR);
        BigDecimal quantidadecalculada = quantidadeProduto.subtract(quantidadeEstoque).setScale(2, RoundingMode.FLOOR);
        ezattaProdutoStatic.setQuantidade(quantidadecalculada);
        try {
            produtoCtr.updateProduto(ezattaProdutoStatic);
        } catch (SQLException ex) {
            Logger.getLogger(PrincipalUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //new FXDialog(FXDialog.Type.INFO, "ezattaProdutoStatic: "+ezattaProdutoStatic.getNome()+" - id: "+ezattaProdutoStatic.getId()+ " qtd: "+ezattaProdutoStatic.getQuantidade()).showDialog();
        estoqueCtr.addEstoque(estoque);

        //adiciona estoque produto inicio------------------------------------
        EstoqueProdutoDAO produtoEstoqueDAO = new EstoqueProdutoDAO();
        EzattaEstoqueProduto estoque = new EzattaEstoqueProduto();

        Timestamp datas = new Timestamp(System.currentTimeMillis());
        estoque.setDataoperacao(datas);
        estoque.setEmpresa(ezattaUsuarioStatic.getEmpresa());
        estoque.setUsuario(ezattaUsuarioStatic);
        estoque.setQuantidade(Float.parseFloat(vol));
        estoque.setOperacao("-");
        estoque.setProduto(ezattaProdutoStatic);

        System.out.println("Estoque: " + estoque);
        produtoEstoqueDAO.addPEstoque(estoque);
        System.out.println("adicionou no estoque");
        //adiciona estoque produto fim------------------------------------------------

        System.out.println("=================================inicio===================================================");
        List<EzattaMovimentacoes> list = estoqueCtr.getUltimoEstoque();
        EzattaMovimentacoes itemPassado = new EzattaMovimentacoes();
        for (EzattaMovimentacoes list1 : list) {
            System.out.println("list1: " + list1);
            itemPassado = list1;
            System.out.println("itemPassado: " + itemPassado);
        }
        System.out.println("=================================Fim===================================================");

        adicionarVbList(itemPassado);

    }

    private void cancelarNoBanco(Integer idEstoqueTh) {
        EzattaMovimentacoes e = EstoqueCtr.getEstoque(idEstoqueTh);
        e.setStatus(3);
        EstoqueCtr.updateEstoque(e);
        //atualizar Quantidade do produto
        ProdutoDAO produtoCtr = new ProdutoDAO();
        EzattaProduto prod = new EzattaProduto();
        System.out.println("ezattaUsuarioStatic.getId(): " + e.getProduto().getId());
        prod = produtoCtr.getProduto(e.getProduto().getId());
        BigDecimal quantidadeProduto = prod.getQuantidade().setScale(2, RoundingMode.FLOOR);
        BigDecimal quantidadeEstoque = new BigDecimal(e.getQtdEstoque()).setScale(2, RoundingMode.FLOOR);
        BigDecimal quantidadecalculada = quantidadeProduto.add(quantidadeEstoque).setScale(2, RoundingMode.FLOOR);
        prod.setQuantidade(quantidadecalculada);
        try {
            produtoCtr.updateProduto(prod);
        } catch (SQLException ex) {
            Logger.getLogger(PrincipalUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //adiciona estoque produto inicio------------------------------------
        System.out.println("ezattaUsuarioStatic: " + ezattaUsuarioStatic);
        System.out.println("ezattaUsuarioStatic.getEmpresa(): " + ezattaUsuarioStatic.getEmpresa());
        System.out.println("Produto Estoque: " + e.getProduto());

        EstoqueProdutoDAO produtoEstoqueDAO = new EstoqueProdutoDAO();
        EzattaEstoqueProduto estoque = new EzattaEstoqueProduto();
        Timestamp datas = new Timestamp(System.currentTimeMillis());
        estoque.setDataoperacao(datas);
        estoque.setUsuario(ezattaUsuarioStatic);
        estoque.setEmpresa(ezattaUsuarioStatic.getEmpresa());
        estoque.setQuantidade(quantidadeEstoque.floatValue());
        estoque.setOperacao("Cancelada");
        estoque.setProduto(ezattaProdutoStatic);

        System.out.println("Estoque: " + estoque);
        produtoEstoqueDAO.addPEstoque(estoque);
        System.out.println("adicionou no estoque");
        //adiciona estoque produto fim------------------------------------------------
    }

    private void cancelarPlaca(int idEstoqueTh) {

        try {
            saida = serialPort.getOutputStream();
            System.out.println("FLUXO OK!");
        } catch (Exception e) {
            System.out.println("Erro.STATUS: " + e);
        }

        try {

            String vo = "00.00";
            String enviar = "00.00";
            enviar = enviar.replace(".", "");

            Integer volume_inteiro = Integer.parseInt(enviar); // Volume total como inteiro
            String volumeLSB = vo.substring(3, 5);

            int volumeMSBL = (volume_inteiro) & 255;
            int volumeLSBL = ((volume_inteiro & 65280) / 256);
            //mostrar parte alta e baixa do inteiro
            System.out.println("String: " + (String.valueOf(volumeMSBL)) + " " + (String.valueOf(volumeLSBL)));

            EzattaMovimentacoes ezattaEstoqueCancelar = estoqueCtr.getEstoque(idEstoqueTh);
            EzattaBico bico = ezattaEstoqueCancelar.getBico();
            System.out.println("bico: " + bico.getNome());

            long crc = 0xff - 0x11 - Long.parseLong(bico.getEndereco().substring(0, 2), 16) - Long.parseLong(bico.getEndereco().substring(2, 4), 16) - Long.parseLong(bico.getEndereco().substring(4, 6), 16) - Long.parseLong(bico.getEndereco().substring(6, 8), 16) - Long.parseLong(bico.getEndereco().substring(8, 10), 16) - Long.parseLong(bico.getEndereco().substring(10, 12), 16) - Long.parseLong(bico.getEndereco().substring(12, 14), 16) - Long.parseLong(bico.getEndereco().substring(14, 16), 16)
                    - 0xFF - 0xFE - 0x57 - 0x4D - 0x01 - (volumeLSBL) - (volumeMSBL) - 0x0D;

            String FinalrCrc = Long.toHexString(crc).substring(14, 16);

            byte[] uartout = new byte[]{
                (byte) 0x7E, (byte) 0x00, (byte) 0x14, (byte) 0x10, (byte) 0x01,
                (byte) (Long.parseLong(bico.getEndereco().substring(0, 2), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(2, 4), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(4, 6), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(6, 8), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(8, 10), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(10, 12), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(12, 14), 16)), (byte) (Long.parseLong(bico.getEndereco().substring(14, 16), 16)),
                (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00, (byte) 0x57, (byte) 0x4D, (byte) 0x01,
                (byte) volumeLSBL, (byte) volumeMSBL, (byte) 0x0D, (byte) crc};
            try {
                saida.write(uartout);
                Thread.sleep(1000);
            } catch (IOException ex) {
                System.out.println("erro");
            }

        } catch (Exception e) {
            e.printStackTrace();
            serialPort.close();
        }
        if (!portFound) {
            serialPort.close();
        }

    }

    //----------------------------------------------------fim novos metodos
    private void adicionarVbList(EzattaMovimentacoes dado) {
        idEstoqueTh = dado.getId(); //id estoque -> identificado
        //instanciar componentes vetores
        hb[idEstoqueTh] = new HBox();
        gridpane[idEstoqueTh] = new GridPane();
        progressBarEstoque[idEstoqueTh] = new ProgressBar(0);
        efetivo[idEstoqueTh] = new Text();
        endBico[idEstoqueTh] = new String();
        btnEnviar[idEstoqueTh] = new Button();
        btnCancelarEstoque[idEstoqueTh] = new Button();
        btnSalvarEstoque[idEstoqueTh] = new Button();
        btnFechar[idEstoqueTh] = new Button();
        separator[idEstoqueTh] = new Separator();
        stringEstoque[idEstoqueTh] = new Text();

        //variaveis auxiliares
        Text volTotalAEnvasar = new Text();
        //Text stringEstoque = new Text();

        ScrollPane sp = new ScrollPane();
        volTotalAEnvasar.setText(dado.getQtdEstoque() + "");
        //separator[idEstoqueTh].setMinHeight(15);
        btnEnviar[idEstoqueTh].setText("Enviar");
        btnEnviar[idEstoqueTh].setMinWidth(70);
        Tooltip tpEnviar = new Tooltip();
        tpEnviar.setText("Enviar dados para o ponto de abastecimento.");
        btnEnviar[idEstoqueTh].setTooltip(tpEnviar);
        btnCancelarEstoque[idEstoqueTh].setText("Cancelar");
        btnCancelarEstoque[idEstoqueTh].setMinWidth(75);
        Tooltip tpCancelarr = new Tooltip();
        tpCancelarr.setText("Remove da fila de envios"
                + "\nCancela no ponto de abastecimento e"
                + "\nCancelar o registro.");
        btnCancelarEstoque[idEstoqueTh].setTooltip(tpCancelarr);
        btnFechar[idEstoqueTh].setText("Fechar");
        btnFechar[idEstoqueTh].setMinWidth(75);
        Tooltip tpFechar = new Tooltip();
        tpFechar.setText("Remove da fila de envios mas não cancela o envio nem o registro.");
        btnFechar[idEstoqueTh].setTooltip(tpFechar);
        btnSalvarEstoque[idEstoqueTh].setText("Salvar");
        btnSalvarEstoque[idEstoqueTh].setMinWidth(75);
        btnSalvarEstoque[idEstoqueTh].setDisable(true);
        sp.setContent(hb[idEstoqueTh]);
        hb[idEstoqueTh].setSpacing(5.0);

        //gridPane
        gridpane[idEstoqueTh].setPadding(new Insets(5));
        gridpane[idEstoqueTh].setHgap(5);
        gridpane[idEstoqueTh].setVgap(5);
        ColumnConstraints column1 = new ColumnConstraints(100);
        gridpane[idEstoqueTh].getColumnConstraints().addAll(column1);

        stringEstoque[idEstoqueTh].setText("OS: " + dado.getOs() + "\t Qtd.: " + dado.getQtdEstoque() + "\t Prod.: " + dado.getProduto().getNome() + "\t Bico " + dado.getBico().getNome());
        progressBarEstoque[idEstoqueTh].setMinWidth(150);
        progressBarEstoque[idEstoqueTh].setMinHeight(22);
        progressBarEstoque[idEstoqueTh].setVisible(false);
        hb[idEstoqueTh].getChildren().addAll(btnEnviar[idEstoqueTh], btnCancelarEstoque[idEstoqueTh], btnSalvarEstoque[idEstoqueTh], btnFechar[idEstoqueTh]);
        //hb[idEstoqueTh].getChildren().addAll(btnEnviar[idEstoqueTh], btnCancelarEstoque[idEstoqueTh], btnFechar[idEstoqueTh]);

        GridPane.setHalignment(stringEstoque[idEstoqueTh], HPos.LEFT);
        gridpane[idEstoqueTh].add(stringEstoque[idEstoqueTh], 0, 0);

        GridPane.setHalignment(hb[idEstoqueTh], HPos.LEFT);
        gridpane[idEstoqueTh].add(hb[idEstoqueTh], 0, 1);

        GridPane.setHalignment(efetivo[idEstoqueTh], HPos.LEFT);
        gridpane[idEstoqueTh].add(efetivo[idEstoqueTh], 0, 2);

        GridPane.setHalignment(progressBarEstoque[idEstoqueTh], HPos.LEFT);
        gridpane[idEstoqueTh].add(progressBarEstoque[idEstoqueTh], 1, 2);

        System.out.println("idEstoqueTh: " + idEstoqueTh);
        System.out.println("Efetivo: " + efetivo[idEstoqueTh]);

        vbList.getChildren().add(gridpane[idEstoqueTh]);
        vbList.getChildren().add(separator[idEstoqueTh]);

        btnEnviar[idEstoqueTh].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                idEstoqueTh = dado.getId();
                enviarVolumePlaca(idEstoqueTh);
            }
        });

        btnCancelarEstoque[idEstoqueTh].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                int idEstoqueTh = dado.getId();
                System.out.println("idEstoqueTh cancelar: " + idEstoqueTh);
                cancelarVolumePlaca(idEstoqueTh);
            }
        });

        btnSalvarEstoque[idEstoqueTh].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                completarTanque(idEstoqueTh);
            }
        });

        btnFechar[idEstoqueTh].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int idEstoqueTh = dado.getId();
                System.out.println("idEstoqueTh fechar: " + idEstoqueTh);
                fecharItem(idEstoqueTh);
                cancelarVolumePlaca(idEstoqueTh);
            }
        });

    }

    private void fecharItem(Integer idEstoqueTh) {
        vbList.getChildren().remove(gridpane[idEstoqueTh]);
        vbList.getChildren().remove(separator[idEstoqueTh]);
//        //cancela no banco de dados
//        cancelarNoBanco(idEstoqueTh);
//        cancelarPlaca(idEstoqueTh);
    }

    @FXML
    void gerarRelatorio(ActionEvent event
    ) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/Relatorio.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

    String endArquivoUpload = "";

    private void enviarEmailBloqueio() throws MessagingException {
        //
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
            String corpoEmail = "O sistema <b> Ezatta </b> de troca de óleo atingil o estoque minimo favor reabastecer o produto: "
                    + ezattaProdutoStatic.getNome() + " quantidade atual: " + ezattaProdutoStatic.getQuantidade();
            message.setFrom(new InternetAddress("marceloaugusto16@gmail.com"));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("marceloaugusto16@gmail.com"));

            if (ezattaProdutoStatic.getEmail() != null) {
                System.out.println("ezattaProdutoStatic.getEmail(): " + ezattaProdutoStatic.getEmail());
                message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(ezattaProdutoStatic.getEmail()));
            }

            if (ezattaUsuarioStatic.getEmpresa().getEmail() != null) {
                System.out.println("ezattaUsuarioStatic.getEmpresa().getEmail(): " + ezattaUsuarioStatic.getEmpresa().getEmail());
                message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(ezattaUsuarioStatic.getEmpresa().getEmail()));
            }
            message.setSubject("Sistema Ezatta - Alerta - " + ezattaUsuarioStatic.getEmpresa().getNome());
            message.setContent(corpoEmail, "text/html");

            //--------------------------inicio anexo------------------------------------------
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(corpoEmail, "text/html");
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
            //new FXDialog(FXDialog.Type.INFO, "Enviou Email").showDialog();
        } catch (AddressException ex) {
            Logger.getLogger(TesteEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void btnEnvasar(ActionEvent event) {
        //---------------------------------------------------fim
        limpar();
        limparComponentesBicoEnvase();
        try {
            stack.getChildren().clear();
            stack.getChildren().add(paneProduto);
            stack.getChildren().add(paneBico);
            paneProduto.setVisible(true);
            paneBico.setVisible(false);
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    @FXML
    private Text txtTituloPrincipal;

    //-----------------------------------------Tratar abrertura de porta inicio
    @FXML
    private Text txtTextoSelecionarPorta;

    @FXML
    private Button btnAtualizarPort;

    public void validaPorta() {
        if (!configurarPorta) {

            iconeConectado.setVisible(false);
            iconeDesconectado.setVisible(true);

            lvProdutos.setDisable(true);
            Tooltip tooltip = new Tooltip();

            tooltip.setText(
                    "Selecione o produto"
            );
            lvProdutos.setTooltip(tooltip);
            txtTituloPrincipal.setText("Favor configurar porta de comunicação.");
            aviso.setVisible(true);
        } else {
            txtTituloPrincipal.setText("Selecione um produtos para abastecer");
            iconeConectado.setVisible(true);
            iconeDesconectado.setVisible(false);

            mudarStatusConeccao();
        }

    }

    public void mudarStatusConeccao() {
        //mudar o status componentes conexao informando ja estar conectado
        txtTextoSelecionarPorta.setText("Disopitivo ja conectado"
                + "\n Porta Serial: " + defaultPort);
        cbPorta.setDisable(true);
        btnAtualizarPort.setDisable(true);
        btnConectar.setDisable(true);
    }

    public void popularCbPortas() {
        Enumeration<CommPortIdentifier> numPorta = CommPortIdentifier.getPortIdentifiers();
        ObservableList<String> portas = FXCollections.observableArrayList();
        while (numPorta.hasMoreElements()) {
            CommPortIdentifier identificacaoPorta = numPorta.nextElement();
            //portas.add(identificacaoPorta);
            portas.add(identificacaoPorta.getName());
            System.out.println("For Portas: " + identificacaoPorta.getName());
            cbPorta.getItems().addAll(identificacaoPorta.getName());
        }
        System.out.println("Não entrou");
    }

    //-----------------------------------------Tratar abertura de porta fim
    @FXML
    void fecharAviso(ActionEvent event) {
        System.out.println("Fechar aviso");
        aviso.setVisible(false);
    }

    @FXML
    private Button btnConectar;

    //------------------------------inicio montar anchor comunicacao
    @FXML
    private ComboBox<String> cbPorta;

    //String output = (String) Sample.getSelectionModel().getSelectedItem().toString();
    //System.out.println(output);
    @FXML
    void conectar(ActionEvent event) {
        String output = cbPorta.getSelectionModel().getSelectedItem();
        System.out.println(output);
        //defaultPort = "/dev/ttyACM0";
        defaultPort = output;
        System.out.println("Abrindo porta serial: " + defaultPort);

        portList = gnu.io.CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (gnu.io.CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == gnu.io.CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(defaultPort)) {

                    System.out.println("Found port " + defaultPort);
                    portFound = true;

                    configurarPorta = true;

                    //Verifica se a porta esta em Uso   
                    try {
                        serialPort = (gnu.io.SerialPort) portId.open("SimpleWrite", 9600);
                        System.out.println("Abriu");
                        iconeConectado.setVisible(true);
                        iconeDesconectado.setVisible(false);
                    } catch (PortInUseException e) {
                        System.out.println("Port in use.");
                        serialPort.close();
                        new FXDialog(FXDialog.Type.ERROR, "Porta serial em uso...").showDialog();
                        //Aqui tratar... 

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
                        System.out.println("Abriu...");
                        mudarStatusConeccao();
                        outputStream = serialPort.getOutputStream();
                    } catch (IOException e) {
                        System.out.println("Erro na abertura da porta.");
                        new FXDialog(FXDialog.Type.ERROR, "Porta serial em uso.").showDialog();
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
                    /*-------------------------lv Produtos-------------------------------*/
                    lvProdutos.setDisable(false);
                    /*-------------------------lv Produtos---------------------------------*/
                } else {
                    System.out.println("não conectou");
                    //new FXDialog(FXDialog.Type.ERROR, "Porta serial em uso.").showDialog();
                }
                //se a porta não estiver funcionando mostra na tela...

            } else {
                new FXDialog(FXDialog.Type.ERROR, "Porta serial em uso.").showDialog();
            }

        }
    }
    //------------------------------fim montar anchor comunicação

    @FXML
    private Button btnConfigurarPorta;

    @FXML
    void configurarPorta(ActionEvent event) {
        System.out.println("Fechar aviso");
        aviso.setVisible(false);
        acPrincipal.setExpandedPane(acComunicacao);
    }

    @FXML
    void btnAtualizarPorta(ActionEvent event) {
        popularCbPortas();
    }

    @FXML
    private ImageView iconeConectado;

    @FXML
    private ImageView iconeDesconectado;

    @FXML
    void atualizarListaRegistros(ActionEvent event) {
        popularProdutos();
    }

}
