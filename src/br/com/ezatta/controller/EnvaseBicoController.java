/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import static br.com.ezatta.controller.LoginController.entrada;
import br.com.ezatta.dao.BicosDAO;
import br.com.ezatta.dao.OperadorDAO;
import br.com.ezatta.model.EzattaBico;
import br.com.ezatta.model.EzattaOperador;
import br.com.ezatta.view.FXDialog;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import static br.com.ezatta.controller.LoginController.ezattaProdutoStatic;
import static br.com.ezatta.controller.LoginController.ezattaUsuarioStatic;
import static br.com.ezatta.controller.LoginController.portFound;
import static br.com.ezatta.controller.LoginController.saida;
import static br.com.ezatta.controller.LoginController.serialPort;
import br.com.ezatta.dao.MovimentacoesDAO;
import br.com.ezatta.model.EzattaMovimentacoes;
import br.com.ezatta.util.MaskTextField;
import br.com.ezatta.util.ValidationFields;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import static br.com.ezatta.controller.PrincipalController.principal;
import br.com.ezatta.dao.ProdutoDAO;
import br.com.ezatta.model.EzattaProduto;
import javafx.scene.layout.VBox;
/*------------------------------------------------------------fim importação java com --------------------*/

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class EnvaseBicoController implements Initializable {

    private String[] portas;
    private boolean flag = false;	// variavel sinalizadora (flag)
    private int valor;			// regiao crítica

    private ObservableList<EzattaBico> dadosBico = FXCollections.observableArrayList();
    private ObservableList<EzattaOperador> dadosOperador = FXCollections.observableArrayList();
    private int Operacao;
    private OperadorDAO operadorCtr = new OperadorDAO();
    private BicosDAO bicoCtr = new BicosDAO();
    private MovimentacoesDAO estoqueCtr = new MovimentacoesDAO();
    private MaskTextField maskTextField = new MaskTextField();
    private EzattaMovimentacoes estoque = new EzattaMovimentacoes();

    static StringBuilder outputBfj;
    static StringBuilder volumeBfj;
    static BufferedInputStream bufferedinputStream;
    boolean execucaoWhile = true;
    //public String valorEfetivo = "";
    public int tipoBaixa = 0;
    boolean enchendoo;
    Thread t;

    @FXML
    private BorderPane bPane;

    @FXML
    private StackPane stackEfetivo;

    @FXML
    private TextField txtPlaca;

    @FXML
    private ComboBox<EzattaBico> cbBico;

    @FXML
    private TextField txtEfetivo;

    @FXML
    private TextField txtKm;

    @FXML
    private Button btnCancelar;

    @FXML
    private ComboBox<EzattaOperador> cbOperador;

    @FXML
    private TextField txtOs;

    @FXML
    private Button btnEnviarPPlaca;

    @FXML
    private TextField txtVolume;

    @FXML
    private StackPane stackPane;

    @FXML
    private StackPane principalEnvase;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Text txtStatus;

    @FXML
    private Tooltip mensagens;

    @FXML
    private Tooltip toolVolume;

    //PrincipalController principal;
    //public Object txtBicoUm;
    public EnvaseBicoController() {
        this.t = new Thread(task);
    }

    public String getTxtEfetivo() {
        return txtEfetivo.getText();
    }

    public void setTxtEfetivo(String txtEfetivo) {
        this.txtEfetivo.setText(txtEfetivo);

    }

    public void atualizaBarraProgress(String txtEfetivo) {
        if (txtEfetivo != null && txtEfetivo.length() > 0) {
            try {
                try {
                    BigDecimal volEfetivoUm = new BigDecimal(txtEfetivo);
                    volEfetivoUm = volEfetivoUm.setScale(2, BigDecimal.ROUND_DOWN);
                    double volEfetivo = Double.parseDouble(volEfetivoUm.toString());
                    double volTotal = Double.parseDouble(txtVolume.getText()); //Total
                    double porcentagem = ((volEfetivo / volTotal));
                    System.out.println("volTotal: " + volTotal + " - volEfetivo: " + volEfetivoUm + " - porcentagem: " + porcentagem);
                    progressBar.setProgress(porcentagem);
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    System.out.println("saber como validar");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //---------------fim progress bar
    }

    public String getTxtStatus() {
        return txtStatus.getText();
    }

    @FXML
    public VBox estoqueAtualBico;

    public VBox getEstoqueAtualBico() {
        return estoqueAtualBico;
    }

    public PrincipalController p = principal;

    public void setEstoqueAtualBico(VBox estoqueAtualBico) {
        this.estoqueAtualBico = estoqueAtualBico;
    }

    @FXML
    void enviarPPlaca(ActionEvent event) throws InterruptedException, IOException {

        if (isValidaTela()) {
            btnEnviarPPlaca.isDisabled();
            btnEnviarPPlaca.disabledProperty();
            btnEnviarPPlaca.setDisable(true);

//            try {
//                //atualiza fator escala
//                atualizaFatorEscala();
//                Thread.sleep(1000);
//                //Enviar operador e placa
//                enviarStringPlaca();
//                Thread.sleep(1000);
//                //enviar Volume 00.00
//                enviarVolume();
//            } catch (IOException ex) {
//                Logger.getLogger(EnvaseBicoController.class.getName()).log(Level.SEVERE, null, ex);
//            }
            salvarNoBanco();
            cancelar(event);
        }
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
            new FXDialog(FXDialog.Type.WARNING, "Favor preencher o Volume com a seguinte formatação 01.00!").showDialog();
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

    @FXML
    void cancelar(ActionEvent event) throws IOException {
        try {
            principalEnvase.getChildren().clear();
            principalEnvase.getChildren().add(getNode("/br/com/ezatta/view/EnvaseProduto.fxml"));
            principal.setStack(principalEnvase);

        } catch (Exception e) {
            //new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
        }
    }

    public void limpar() {
        try {
            stackPane.getChildren().clear();
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            e.printStackTrace();
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ValidationFields.checkEmptyFields(txtVolume, txtOs, txtPlaca, cbBico, cbOperador);
        popularDados();
        txtStatus.setText("");

//        new Thread(taskLeituraEnvase).start();
        //principal.txtBicoUm.setVisible(true);
        //principal.bicoUm.setVisible(true);
        //principal.txtBicoUm.setText("...");
    }

    private void popularDados() {
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

    private void salvarNoBanco() {

        EzattaProduto prod = ezattaProdutoStatic;
        System.out.println("prod: " + prod.toString() + " - " + prod.getQuantidade());
        prod.removQuantidade(new BigDecimal(estoque.getQtdEstoque()));
        System.out.println("prod: " + prod.toString() + " - " + prod.getQuantidade());
        ProdutoDAO produtoCtr = new ProdutoDAO();
        produtoCtr.updateProduto(prod);
        System.out.println("prod: " + prod.toString() + " - " + prod.getQuantidade());

        estoque.setQtdEstoque(Float.parseFloat(txtVolume.getText()));
        estoque.setOperador((EzattaOperador) cbOperador.getValue());
        estoque.setBico((EzattaBico) cbBico.getValue());
        estoque.setOs(txtOs.getText());
        estoque.setKm(txtKm.getText());
        estoque.setPlaca(txtPlaca.getText());
        estoque.setProduto(ezattaProdutoStatic);
        estoque.setStatus(0);
        Timestamp data = new Timestamp(System.currentTimeMillis());
        estoque.setDataEstoque(data);
        estoque.setUsuario(ezattaUsuarioStatic);

        new FXDialog(FXDialog.Type.INFO, "Solicitação confirmada. 1 ").showDialog();
        System.out.println("Estoque: " + estoque);
        estoqueCtr.addEstoque(estoque);

    }

    private void atualizaFatorEscala() throws IOException, InterruptedException {
        try {
            saida = serialPort.getOutputStream();
            System.out.println("FLUXO OK!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro.STATUS: " + e);
        }

        EzattaBico bico = new EzattaBico();
        bico = (EzattaBico) cbBico.getValue();
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
        Thread.sleep(200);  // ??  
        //saida.flush();  // ??  

    }

    private void enviarStringPlaca() throws IOException {

        try {
            saida = serialPort.getOutputStream();
            System.out.println("FLUXO OK!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro.STATUS: " + e);
        }

        EzattaBico bico = new EzattaBico();
        bico = (EzattaBico) cbBico.getValue();
        int envia_texto = 0;
        int tamanho = txtPlaca.getText().length();
        System.out.println("txtPlaca: " + txtPlaca.getText());
        for (int i = 0; i < tamanho; i++) {
            String placa = txtPlaca.getText().toUpperCase();

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
        EzattaOperador nomeOperador = (EzattaOperador) cbOperador.getValue();
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

    private void enviarVolume() {

        try {
            saida = serialPort.getOutputStream();
            //System.out.println("FLUXO OK!");
        } catch (Exception e) {
            //System.out.println("Erro.STATUS: " + e);
        }

        try {

            //Quebrar valor do volume
            String vo = txtVolume.getText();
            //String volumeMSB = vo.substring(0, 2);
            String enviar = txtVolume.getText();
            enviar = enviar.replace(".", "");

            Integer volume_inteiro = Integer.parseInt(enviar); // Volume total como inteiro
            String volumeLSB = vo.substring(3, 5);

            int volumeMSBL = (volume_inteiro) & 255;
            int volumeLSBL = ((volume_inteiro & 65280) / 256);
            //mostrar parte alta e baixa do inteiro
            //System.out.println("String: " + (String.valueOf(volumeMSBL)) + " " + (String.valueOf(volumeLSBL)));

            EzattaBico bico = new EzattaBico();
            bico = ((EzattaBico) cbBico.getValue());

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
            new FXDialog(FXDialog.Type.INFO, "Enviar Volume? ").showDialog();
            try {
                saida.write(uartout);
                //System.out.println("Teoricamente foi");
                Thread.sleep(1000);
            } catch (IOException ex) {
                //System.out.println("Erro na comunicação");
            }
            /*-------------------------inicio leitura------------------------------*/
            t.start();
            /*-------------------------Fim leitura--------------------------------*/
        } catch (Exception e) {
            serialPort.close();
        }
        if (!portFound) {
            serialPort.close();
        }
    }

    private boolean validaFormatacaoVolume() {
        boolean valida = false;
        String vol = txtVolume.getText();
        CharSequence ponto = ".";
        valida = vol.contains(ponto);
        System.out.println("se tem ou não ponto : " + valida);
        return valida;
    }

    Task task = new Task<Void>() {
        @Override
        public void run() {
            //inicio-----------------------------------------------------------------------
            try {
                long numBytes = 0;
                int contAux = 0;
                byte[] readBuffer = new byte[1000];
                while (execucaoWhile) {
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
                    if (readBuffer[15 + contAux] == (byte) 0x56) {//ENCHENDO
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
                                //principal.txtBicoUm.setText(outputBfj.toString());
                                //inicio metodo cria textText
                                adicionaProcessoEnvase(outputBfj.toString());

                                txtEfetivo.setText(outputBfj.toString());
                                txtStatus.setText("APLICANDO"); //mostra texto aplicando
                                if (!volumeSemVol.isEmpty() && volumeSemVol != null && volumeSemVol.length() > 0) {
                                    atualizaBarraProgress(volumeBfj.toString());//barra de progresso
                                }
                            } catch (NullPointerException e) {
                                System.out.println("nullpointer");
                                e.printStackTrace();
                            }
                            outputBfj.delete(0, outputBfj.length());//limpa buffer
                        }
                    } else {
                        outputBfj.delete(0, outputBfj.length());
                    }
                    if (readBuffer[15 + contAux] == (byte) 0x46) {
                        txtStatus.setText("FIM");
                        txtEfetivo.setText("VOL " + txtVolume.getText());
                        execucaoWhile = false;
                        t.interrupt();
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

//-----------------------------------------------inserirr texto no HBOX principal    
    private void adicionaProcessoEnvase(String string) {
        TextField text = new TextField();
        text.setText("Marcelo");
//        principal.addElementoFilaPrincipal();
        //principal.addElementoFilaPrincipal();
        //principal.estoqueAtual.getChildren().add(text);
        //addElementoFilaPrincipal();
    }
}
