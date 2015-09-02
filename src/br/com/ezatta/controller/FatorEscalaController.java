/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import static br.com.ezatta.controller.LoginController.portFound;
import static br.com.ezatta.controller.LoginController.saida;
import static br.com.ezatta.controller.LoginController.serialPort;
import br.com.ezatta.dao.BicosDAO;
import br.com.ezatta.dao.ProdutoDAO;
import br.com.ezatta.model.EzattaBico;
import br.com.ezatta.model.EzattaMovimentacoes;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.util.GenericTable;
import br.com.ezatta.util.ValidationFields;
import br.com.ezatta.view.FXDialog;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class FatorEscalaController implements Initializable {

    private ObservableList<EzattaBico> dados = FXCollections.observableArrayList();
    private ObservableList<EzattaProduto> dadosProduto = FXCollections.observableArrayList();
    private int Operacao;
    private ProdutoDAO produtoCtr = new ProdutoDAO();
    private BicosDAO bicoCtr = new BicosDAO();
    private EzattaBico ezattaBico;

    @FXML
    private Tab tabConsulta;

    @FXML
    private TextField txtEndereco;

    @FXML
    private Button btnEnviarQuatroLitros;

    @FXML
    private TextField txtNome;

    @FXML
    private Button btnEnviarQuatroLitros1;

    @FXML
    private Tab tabCadastro;

    @FXML
    private ComboBox<EzattaProduto> cbProduto;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnRedefinirConsulta;

    @FXML
    private TableView<EzattaBico> tb;

    @FXML
    private TextField txtId;

    @FXML
    private Button btnImprimirRegistros;

    @FXML
    private RadioButton rbEndereco;

    @FXML
    private RadioButton rbNome;

    @FXML
    private ToggleGroup buscarPor;

    @FXML
    private TabPane tabTela;

    @FXML
    private Button btnBuscar;

    @FXML
    private TextField txtFatorEscala;

    @FXML
    private TextField txtNovoFatorEscala;

    @FXML
    void enviarQuatroLitros(ActionEvent event) {
        try {
            setarFatorEscala(100);
            salvarFatorBanco(100);
            enviarQuatroLitros();
            tabTela.getSelectionModel().select(2);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(FatorEscalaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void enviarQuatroLitrosFinais(ActionEvent event) {
        enviarQuatroLitros();
        tabTela.getSelectionModel().select(0);
        txtVolumeInformado.setText("");
    }

    @FXML
    void cancelarVolumePlaca(ActionEvent event) {
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

            //EzattaEstoque ezattaEstoqueCancelar = estoqueCtr.getEstoque(idEstoqueTh);
            //EzattaBico bico = ezattaEstoqueCancelar.getBico();
            EzattaBico bico = ezattaBico;
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
                new FXDialog(FXDialog.Type.INFO, "Cancelado").showDialog();
                Thread.sleep(1000);
                tabTela.getSelectionModel().select(0);
                txtVolumeInformado.setText("");
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

//--------------------------------------------------------------------------------
    @FXML
    void cancelar(ActionEvent event) {
        popularDados();
        tabTela.getSelectionModel().select(0);
    }

    @FXML
    void btnBuscar(ActionEvent event) throws SQLException {
        if (isValidConsulta()) {
            dados.clear();
            if (rbNome.isSelected()) {
                dados.addAll(bicoCtr.findAll("select c.* from ezatta_bico c", " where c.nome like  '%" + txtBuscar.getText() + "%'"));
            } else if (rbEndereco.isSelected()) {
                dados.addAll(bicoCtr.findAll("select c.* from ezatta_bico c", " where c.endereco like  '%" + txtBuscar.getText() + "%'"));
            }
            tb.setItems(dados);
        } else {
            new FXDialog(FXDialog.Type.WARNING, "Escolha pelo menos uma das opções para consulta!").showDialog();
            txtBuscar.requestFocus();
        }
    }

    @FXML
    void redefinirConsulta(ActionEvent event) {
        popularDados();
    }

    @FXML
    void btnAlterarFrontal(ActionEvent event) {
        if (tb.getSelectionModel().isEmpty()) {
            new FXDialog(FXDialog.Type.ERROR, "Favor selecionar o item primeiro... ").showDialog();
        } else {
            tabTela.getSelectionModel().select(1);
            setOperacao(1);
            setEzattaBico(tb.getSelectionModel().getSelectedItem());
            SetValoresComponentes(getEzattaBico());
        }
    }

    @FXML
    void btnExcluirFrontal(ActionEvent event) {
        if (tb.getSelectionModel().isEmpty()) {
            new FXDialog(FXDialog.Type.ERROR, "Favor selecionar o item primeiro... ").showDialog();
        } else {
            boolean ok = new FXDialog(FXDialog.Type.CONFIRM, "Tem Certeza que deseja excluir este registro?").showDialog();
            if (ok) {
                try {
                    bicoCtr.removeBico(getEzattaBico());
                } catch (SQLException ex) {
                    Logger.getLogger(FatorEscalaController.class.getName()).log(Level.SEVERE, null, ex);
                }
                ok = true;
                if (ok) {
                    new FXDialog(FXDialog.Type.INFO, "Operaçao realizada com sucesso!").showDialog();
                    popularDados();
                    btnNovo(event);
                } else {
                    new FXDialog(FXDialog.Type.WARNING, "Existem relaçoes com outras entidades!").showDialog();
                }
            }
        }
    }

    @FXML
    void btnExcluir(ActionEvent event) {
        boolean ok = new FXDialog(FXDialog.Type.CONFIRM, "Tem Certeza que deseja excluir este registro?").showDialog();
        if ((ok) && (getOperacao() == 1)) {
            try {
                bicoCtr.removeBico(getEzattaBico());
            } catch (SQLException ex) {
                Logger.getLogger(FatorEscalaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            ok = true;
            if (ok) {
                new FXDialog(FXDialog.Type.INFO, "Operaçao realizada com sucesso!").showDialog();
                popularDados();
                tabTela.getSelectionModel().select(0);
            } else {
                new FXDialog(FXDialog.Type.WARNING, "Existem relaçoes com outras entidades!").showDialog();
            }
        }
    }

    private boolean isValidConsulta() {
        boolean ok = rbNome.isSelected() || rbEndereco.isSelected();
        if (ok) {
            ok = !txtBuscar.getText().isEmpty();
        }
        return ok;
    }

    @FXML
    void btnImprimir(ActionEvent event) {

    }

    @FXML
    void btnNovoFrontal(ActionEvent event) {
        setOperacao(0);
        tabTela.getSelectionModel().select(1);
        txtId.setText("");
        txtNome.setText("");
        txtNome.requestFocus();
        txtEndereco.setText("");
        txtFatorEscala.setText("");
        cbProduto.getSelectionModel().select(null);
    }

    @FXML
    void btnSalvar(ActionEvent event) {
        if (isValidaTela()) {
            switch (getOperacao()) {
                case 0:
                    //EzattaBico bico = new EzattaBico(txtNome.getText(),txtEndereco.getText(), cbProduto.getSelectionModel().getSelectedItem());
                    EzattaBico bico = new EzattaBico(txtNome.getText(), txtEndereco.getText(), "208", cbProduto.getSelectionModel().getSelectedItem());

                    try {
                        bicoCtr.addBico(bico);
                    } catch (SQLException ex) {
                        Logger.getLogger(FatorEscalaController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    popularDados();
                    new FXDialog(FXDialog.Type.INFO, "Registro inserido com sucesso!").showDialog();
                    tabTela.getSelectionModel().select(0);
                    break;
                case 1:
                    Integer id = Integer.parseInt(txtId.getText());
                    EzattaBico bicos = new EzattaBico(id, txtNome.getText(), txtEndereco.getText(), cbProduto.getSelectionModel().getSelectedItem());

                    try {
                        bicoCtr.updateBico(bicos);
                    } catch (SQLException ex) {
                        Logger.getLogger(FatorEscalaController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    new FXDialog(FXDialog.Type.INFO, "Registro atualizado com sucesso!").showDialog();
                    tabTela.getSelectionModel().select(0);
                    popularDados();
                    break;
            }
        }
    }

    private boolean isValidaTela() {
        boolean ok = true;
        if (txtNome.getText().isEmpty()) {
            new FXDialog(FXDialog.Type.WARNING, "Favor preencher o nome!").showDialog();
            txtNome.requestFocus();
            ok = false;
        } else if (txtEndereco.getText().isEmpty()) {
            new FXDialog(FXDialog.Type.WARNING, "Favor preencher o endereço físico!").showDialog();
            txtEndereco.requestFocus();
            ok = false;
        }

        return ok;
    }

    @FXML
    void btnNovo(ActionEvent event) {
        setOperacao(0);
        txtId.setText("");
        txtNome.setText("");
        txtNome.requestFocus();
        txtEndereco.setText("");
        txtFatorEscala.setText("");
        cbProduto.getSelectionModel().select(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ValidationFields.checkEmptyFields(txtNome, txtEndereco, cbProduto);
        tb.getColumns().addAll(new GenericTable<EzattaBico>().tableColunas(EzattaBico.class));
        popularDados();
        tb.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    tabTela.getSelectionModel().select(1);
                    setOperacao(1);
                    setEzattaBico(tb.getSelectionModel().getSelectedItem());
                    SetValoresComponentes(getEzattaBico());
                }

                if (event.getClickCount() > 0) {
                    try {
                        setOperacao(1);
                        setEzattaBico(tb.getSelectionModel().getSelectedItem());
                        SetValoresComponentes(getEzattaBico());
                        txtNovoFatorEscala.setText("");
                    } catch (NullPointerException e) {

                    }
                }
            }

        });

    }

    private void SetValoresComponentes(EzattaBico ezatta) {
        txtId.setText(ezatta.getId().toString());
        txtNome.setText(ezatta.getNome());
        txtEndereco.setText(ezatta.getEndereco());
        cbProduto.getSelectionModel().select(ezatta.getProduto());
        txtFatorEscala.setText(ezatta.getFatorescala());
    }

    private void popularDados() {
        dados.clear();
        dadosProduto.clear();
        cbProduto.getItems().clear();
        tb.getItems().clear();
        try {
            dados.addAll(bicoCtr.getAllBico());
            dadosProduto.addAll(produtoCtr.getAllProduto());
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, e.getCause().getMessage()).showDialog();
        }
        tb.setItems(dados);
        cbProduto.setItems(dadosProduto);
    }

    public int getOperacao() {
        return Operacao;
    }

    public void setOperacao(int operacao) {
        Operacao = operacao;
    }

    public EzattaBico getEzattaBico() {
        return ezattaBico;
    }

    public void setEzattaBico(EzattaBico ezattaBico) {
        this.ezattaBico = ezattaBico;
    }

    private void setarFatorEscala(long volume) throws IOException, InterruptedException {
        try {
            saida = serialPort.getOutputStream();
            System.out.println("FLUXO OK!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro.STATUS: " + e);
        }

        EzattaBico bico = ezattaBico;
        System.out.println("Fator escala atual: " + bico.getFatorescala());

        //long volume = Long.parseLong(bico.getFatorescala());
        //long volume = 200;
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
        saida.flush();  // ??  
    }

    private void enviarQuatroLitros() {
        try {
            saida = serialPort.getOutputStream();
            System.out.println("FLUXO OK!");
        } catch (Exception e) {
            System.out.println("Erro.STATUS: " + e);
        }

        try {

            String vo = "04.00";
            String enviar = "04.00";
            enviar = enviar.replace(".", "");

            Integer volume_inteiro = Integer.parseInt(enviar); // Volume total como inteiro
            String volumeLSB = vo.substring(3, 5);

            int volumeMSBL = (volume_inteiro) & 255;
            int volumeLSBL = ((volume_inteiro & 65280) / 256);
            //mostrar parte alta e baixa do inteiro
            System.out.println("String: " + (String.valueOf(volumeMSBL)) + " " + (String.valueOf(volumeLSBL)));

            //EzattaEstoque ezattaEstoqueCancelar = estoqueCtr.getEstoque(idEstoqueTh);
            //EzattaBico bico = ezattaEstoqueCancelar.getBico();
            EzattaBico bico = ezattaBico;
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
                new FXDialog(FXDialog.Type.INFO, "Enviado para a placa").showDialog();
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

    private void salvarFatorBanco(long i) {
        ezattaBico.setFatorescala(String.valueOf(i));
        try {
            bicoCtr.updateBico(ezattaBico);
        } catch (SQLException ex) {
            Logger.getLogger(FatorEscalaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private TextField txtVolumeInformado;

    @FXML
    void calcularCalibrar(ActionEvent event) throws InterruptedException, IOException {

        //validar
        String enviar = txtVolumeInformado.getText();
        //substitui a virgula por ponto
        if (enviar.contains(",")) {
            enviar = enviar.replace(",", ".");
        }

        double vol = Double.parseDouble(enviar);
        //double fator = 100 * vol / 4;
        double fator = 400 / vol;
        fator = Math.ceil(fator);

        System.out.println("fator escala: " + fator);
        setarFatorEscala((long) fator);
        salvarFatorBanco((long) fator);

        new FXDialog(FXDialog.Type.INFO, "Calibrado").showDialog();
        Thread.sleep(1000);
        tabTela.getSelectionModel().select(3);
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

    @FXML
    void calibrarManual(ActionEvent event) {
        
        String fator = txtNovoFatorEscala.getText();
        System.out.println("fator escala: " + fator);
        try {
            setarFatorEscala(Long.parseLong(fator));
            salvarFatorBanco(Long.parseLong(fator));
        } catch (IOException ex) {
            Logger.getLogger(FatorEscalaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(FatorEscalaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        new FXDialog(FXDialog.Type.INFO, "Calibrado").showDialog();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(FatorEscalaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tabTela.getSelectionModel().select(3);
    }

}
