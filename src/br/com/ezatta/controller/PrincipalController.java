package br.com.ezatta.controller;

import static br.com.ezatta.controller.LoginController.defaultPort;
import static br.com.ezatta.controller.LoginController.entrada;
import static br.com.ezatta.controller.LoginController.ezattaProdutoStatic;
import static br.com.ezatta.controller.LoginController.ezattaUsuarioStatic;
import static br.com.ezatta.controller.LoginController.portFound;
import static br.com.ezatta.controller.LoginController.saida;
import static br.com.ezatta.controller.LoginController.serialPort;
import br.com.ezatta.dao.BicosDAO;
import br.com.ezatta.dao.EstoqueDAO;
import br.com.ezatta.dao.OperadorDAO;
import br.com.ezatta.dao.ProdutoDAO;
import br.com.ezatta.model.EzattaBico;
import br.com.ezatta.model.EzattaEstoque;
import br.com.ezatta.model.EzattaOperador;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.util.JPAUtil;
import br.com.ezatta.util.MaskTextField;
import br.com.ezatta.util.ValidationFields;
import br.com.ezatta.view.EzattaMain;
import br.com.ezatta.view.FXDialog;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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
import javafx.scene.image.Image;
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

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class PrincipalController implements Initializable {

    public static PrincipalController principal = new PrincipalController();

    private ObservableList<EzattaEstoque> dadosParaEnvase = FXCollections.observableArrayList();
    private EstoqueDAO EstoqueCtr = new EstoqueDAO();
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
    private EstoqueDAO estoqueCtr = new EstoqueDAO();
    private MaskTextField maskTextField = new MaskTextField();
    private EzattaEstoque estoque;
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
    private Separator[] separator = new Separator[10000];
    private Text[] stringEstoque = new Text[10000];
    //--------------------------------------fim var componentes envase

    @FXML
    private ListView<EzattaEstoque> lvEstoque;

    @FXML
    private MenuItem miRLog;

    @FXML
    private StackPane stack;

    @FXML
    private MenuItem miEncherTank;

    @FXML
    private Button btnOperacoes;

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
    private MenuItem miRProduto;

    @FXML
    private MenuItem miRBico;

    @FXML
    private MenuItem miEmpresa;

    @FXML
    private Button btnCancelamento;

    @FXML
    private StackPane stackContainer;

    @FXML
    private MenuItem miBackUp;

    @FXML
    private MenuItem miFechar;

    @FXML
    private MenuItem miRAplicador;

    @FXML
    private TitledPane acEstoqueAtual;

    @FXML
    private TitledPane acProcessosEnvase;

    @FXML
    public static VBox estoqueAtual;

    @FXML
    private MenuItem miRLiberabor;

    @FXML
    private Text txtNomeEmpresa;

    @FXML
    private MenuItem miProduto;

    @FXML
    private MenuItem miRPlaca;

    @FXML
    private MenuItem miCBico;

    @FXML
    private MenuItem miRGeral;

    @FXML
    private MenuItem miManual;

    @FXML
    private MenuItem miUsuario;

    @FXML
    private Button btnEstoque;

    @FXML
    public VBox vbList;

    @FXML
    public AnchorPane AnchorPanePrincipal;

    @FXML
    private TitledPane acCancelarEnvasePrincipal;

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

            for (EzattaEstoque dado : dadosParaEnvase) {
                adicionarVbList(dado);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarVolumePlaca(Integer idEstoqueTh) {
        try {
            EzattaEstoque dado = estoqueCtr.getEstoque(idEstoqueTh);

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

            //atualiza banco
            EzattaEstoque est = EstoqueCtr.getEstoque(idEstoqueTh);
            est.setStatus(1);
            EstoqueCtr.updateEstoque(est);

            //desabilitar botão enviar
            btnEnviar[idEstoqueTh].setDisable(true);

            new FXDialog(FXDialog.Type.INFO, "Enviado para a placa").showDialog();
        } catch (InterruptedException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cancelarVolumePlaca(Integer idEstoqueTh) {
        vbList.getChildren().remove(gridpane[idEstoqueTh]);
        vbList.getChildren().remove(separator[idEstoqueTh]);
        //cancela no banco de dados
        cancelarNoBanco(idEstoqueTh);
        cancelarPlaca(idEstoqueTh);
    }

    public void completarTanque(int idEstoqueTh) {
        try {
            EzattaEstoque ezattaEstoqueSalvar = estoqueCtr.getEstoque(idEstoqueTh);

            //Atualizar qtd Tbl_Produto-------------------------------------------------------------------
            EzattaProduto ezattaProdutoSalvar = produtoCtr.getProduto(ezattaEstoqueSalvar.getProduto().getId());//busca produto 
            //pega o valor atual na tabela produto
            Float qtdProduto = ezattaProdutoSalvar.getQuantidade().floatValue();
            //pega o valor do estoque a ser retornado na tabela produto
            Float valorARetornar = ezattaEstoqueSalvar.getQtdEstoque();
            //soma a quantidade tblEstoque a tblProduto
            Float calcularValor = 0f;
            calcularValor = qtdProduto + valorARetornar;
            calcularValor = calcularValor - Float.parseFloat(efetivo[idEstoqueTh].getText());

            //atualizar quantidade tblProduto
            ezattaProdutoSalvar.setQuantidade(new BigDecimal(calcularValor));
            produtoCtr.updateProduto(ezattaProdutoSalvar);

            //Salvar no vanco volume efetivo----------------------------------------------------------------
            Float quantidadeEfetiva = new Float(efetivo[idEstoqueTh].getText());
            ezattaEstoqueSalvar.setQtdEstoque(quantidadeEfetiva);
            ezattaEstoqueSalvar.setStatus(4);
            Thread.sleep(100);
            estoqueCtr.updateEstoque(ezattaEstoqueSalvar);

            //Parar Thread
            execucaoWhile[idEstoqueTh] = false;

            //um segundos
            Thread.sleep(1000);

            //Cancelar Placa
            cancelarPlaca(idEstoqueTh);

            //remove Registro Tela
            removerRegistroTela(idEstoqueTh);

        } catch (InterruptedException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //ocultar progress bar bico
        progressBar.setVisible(false);
        //carrega aberto
        acPrincipal.setExpandedPane(acEstoqueAtual);

        //mostra tela produtos e oculta bicos
        paneProduto.setVisible(true);
        paneBico.setVisible(false);

        txtNomeEmpresa.setText("Seja bem vindo: " + ezattaUsuarioStatic.getEmpresa().getNome());
        EzattaMain.stage.close();

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
        dadosProdutos.clear();
        lvProdutos.getItems().clear();
        try {
            dadosProdutos.addAll(produtoCtr.getAllProduto());
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, e.getCause().getMessage()).showDialog();
        }
        lvProdutos.setItems(dadosProdutos);
    }

    //fim popula lista Produtos ==================================================================================
    @FXML
    void estoquePrincipal(ActionEvent event) {

    }

    @FXML
    void operacoes(ActionEvent event) {
    }

    Task taskLeituraEnvase = new Task() {

        @Override
        public void run() {
            System.out.println("entrou thread");
            System.out.println("taskLeituraEnvase: " + taskLeituraEnvase.toString());
            try {
                long numBytes = 0;
                int contAux = 0;
                byte[] readBuffer = new byte[1000];
                execucaoWhile[idEstoqueTh] = true;
                while (execucaoWhile[idEstoqueTh]) {
                    entrada = null;
                    entrada = serialPort.getInputStream();

                    //joga fora primeiras menssagens de outros pedidos 
                    if (entrada.available() > 0) {
                        entrada.read(readBuffer);
                    }

                    int timeout = 0;
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
                            btnCancelarEstoque[idEstoqueTh].disableProperty();
                            btnCancelarEstoque[idEstoqueTh].setDisable(true);

                            //btnSalvar
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
                            Thread.sleep(1000);

                            removerRegistroTela(idEstoqueTh);

                            //cancela thread
                            taskLeituraEnvase.cancel();
                            //--------------------------atualizar status
                            EzattaEstoque e = EstoqueCtr.getEstoque(idEstoqueTh);
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
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    System.out.println("saber como validar");
                    //e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void enviarStringPlaca(EzattaEstoque dado) throws IOException {

        try {
            saida = serialPort.getOutputStream();
            System.out.println("FLUXO OK!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro.STATUS: " + e);
        }

        EzattaBico bico = (EzattaBico) dado.getBico();
        String txtPlaca = dado.getPlaca();
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

    private void atualizaFatorEscala(EzattaEstoque dado) throws InterruptedException, IOException {
        try {
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

    private void enviarVolume(EzattaEstoque dado) {

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
                Thread.sleep(3000);
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

        Platform.exit();
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
    void envase(ActionEvent event) {
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

    @FXML
    void cancelamento(ActionEvent event) {

    }

    public PrincipalController() {

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
        EzattaEstoque estoque = new EzattaEstoque();
        EstoqueDAO estoquedao = new EstoqueDAO();
        List<EzattaEstoque> lista = estoquedao.getAllEstoque();
        for (EzattaEstoque lista1 : lista) {
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
            salvarNoBanco();
            cancelar(event);
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

        estoque = new EzattaEstoque();
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
        estoque.setPlaca(txtPlaca.getText());
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
        produtoCtr.updateProduto(ezattaProdutoStatic);

        //new FXDialog(FXDialog.Type.INFO, "ezattaProdutoStatic: "+ezattaProdutoStatic.getNome()+" - id: "+ezattaProdutoStatic.getId()+ " qtd: "+ezattaProdutoStatic.getQuantidade()).showDialog();
        estoqueCtr.addEstoque(estoque);

        System.out.println("=================================inicio===================================================");
        List<EzattaEstoque> list = estoqueCtr.getUltimoEstoque();
        EzattaEstoque itemPassado = new EzattaEstoque();
        for (EzattaEstoque list1 : list) {
            System.out.println("list1: " + list1);
            itemPassado = list1;
            System.out.println("itemPassado: " + itemPassado);
        }
        System.out.println("=================================Fim===================================================");

        adicionarVbList(itemPassado);

    }

    private void cancelarNoBanco(Integer idEstoqueTh) {
        EzattaEstoque e = EstoqueCtr.getEstoque(idEstoqueTh);
        e.setStatus(3);
        EstoqueCtr.updateEstoque(e);
        //atualizar Quantidade do produto
        ProdutoDAO produtoCtr = new ProdutoDAO();
        EzattaProduto prod = new EzattaProduto();
        prod = produtoCtr.getProduto(ezattaUsuarioStatic.getId());
        BigDecimal quantidadeProduto = prod.getQuantidade().setScale(2, RoundingMode.FLOOR);
        BigDecimal quantidadeEstoque = new BigDecimal(e.getQtdEstoque()).setScale(2, RoundingMode.FLOOR);
        BigDecimal quantidadecalculada = quantidadeProduto.add(quantidadeEstoque).setScale(2, RoundingMode.FLOOR);
        prod.setQuantidade(quantidadecalculada);
        produtoCtr.updateProduto(prod);
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

            EzattaEstoque ezattaEstoqueCancelar = estoqueCtr.getEstoque(idEstoqueTh);
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
    private void adicionarVbList(EzattaEstoque dado) {
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
        btnCancelarEstoque[idEstoqueTh].setText("Cancelar");
        btnCancelarEstoque[idEstoqueTh].setMinWidth(75);
        btnSalvarEstoque[idEstoqueTh].setText("Salvar");
        btnSalvarEstoque[idEstoqueTh].setMinWidth(75);
        btnSalvarEstoque[idEstoqueTh].setDisable(true);
        sp.setContent(hb[idEstoqueTh]);
        hb[idEstoqueTh].setSpacing(15.0);

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
        hb[idEstoqueTh].getChildren().addAll(btnEnviar[idEstoqueTh], btnCancelarEstoque[idEstoqueTh], btnSalvarEstoque[idEstoqueTh]);

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
                idEstoqueTh = dado.getId();
                cancelarVolumePlaca(idEstoqueTh);
            }
        });

        btnSalvarEstoque[idEstoqueTh].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                completarTanque(idEstoqueTh);
            }
        });
    }
}
