package br.com.ezatta.controller;

import br.com.ezatta.dao.EmpresaDAO;
import br.com.ezatta.dao.ProdutoDAO;
import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.util.GenericTable;
import br.com.ezatta.util.ValidationFields;
import br.com.ezatta.view.FXDialog;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class EncherTanqueController implements Initializable {

    private ObservableList<EzattaProduto> dados = FXCollections.observableArrayList();
    private ObservableList<EzattaEmpresa> dadosEmpresa = FXCollections.observableArrayList();
    private int Operacao;
    private ProdutoDAO produtoCtr = new ProdutoDAO();
    private EmpresaDAO empresaCtr = new EmpresaDAO();
    private EzattaProduto ezattaProduto;

    @FXML
    private Tab tabConsulta;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private ComboBox<String> cbCor;

    @FXML
    private TextField txtNome;

    @FXML
    private Tab tabCadastro;

    @FXML
    private TextField txtMinimo;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnRedefinirConsulta;

    @FXML
    private TableView<EzattaProduto> tb;

    @FXML
    private TextField txtId;

    @FXML
    private Button btnImprimirRegistros;

    @FXML
    private RadioButton rbNome;

    @FXML
    private ToggleGroup buscarPor;

    @FXML
    private ComboBox<EzattaEmpresa> cbEmpresa;

    @FXML
    private TabPane tabTela;

    @FXML
    private Button btnBuscar;

    @FXML
    private TextField txtEstoqueMaximo;

    @FXML
    private TextField qtdRedefinir;

    @FXML
    void btnBuscar(ActionEvent event) {
        if (isValidConsulta()) {
            dados.clear();
            if (rbNome.isSelected()) {
                try {
                    dados.addAll(produtoCtr.findAll("select c.* from ezatta_produto c", " where c.nome like  '%" + txtBuscar.getText() + "%'"));
                } catch (SQLException ex) {
                    Logger.getLogger(EncherTanqueController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            tb.setItems(dados);
        } else {
            new FXDialog(FXDialog.Type.WARNING, "Escolha pelo menos uma das opções para consulta!").showDialog();
            txtBuscar.requestFocus();
        }
    }

    @FXML
    void btnImprimir(ActionEvent event) {

    }

    @FXML
    void redefinirConsulta(ActionEvent event) {
        popularDados();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ValidationFields.checkEmptyFields(txtNome, txtQuantidade, txtEstoqueMaximo, txtMinimo, cbEmpresa, cbCor);
        tb.getColumns().addAll(new GenericTable<EzattaProduto>().tableColunas(EzattaProduto.class));
        popularDados();
        tb.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    tabTela.getSelectionModel().select(1);
                    setOperacao(1);
                    setEzattaProduto(tb.getSelectionModel().getSelectedItem());
                    SetValoresComponentes(getEzattaProduto());
                    qtdRedefinir.setText("");
                }
                if (event.getClickCount() > 0) {
                    try {
                        setOperacao(1);
                        setEzattaProduto(tb.getSelectionModel().getSelectedItem());
                        SetValoresComponentes(getEzattaProduto());
                    } catch (NullPointerException e) {
                    }

                }

            }

        });
    }

    private boolean isValidaTela() {
        boolean ok = true;
        if (txtNome.getText().isEmpty()) {
            new FXDialog(FXDialog.Type.WARNING, "Favor preencher o nome!").showDialog();
            txtNome.requestFocus();
            ok = false;
        } else if (txtQuantidade.getText().isEmpty()) {
            new FXDialog(FXDialog.Type.WARNING, "Favor preencher o Quantidade!").showDialog();
            txtQuantidade.requestFocus();
            ok = false;
        } else if (txtMinimo.getText().isEmpty()) {
            new FXDialog(FXDialog.Type.WARNING, "Favor preencher o Quantidade mínima!").showDialog();
            txtMinimo.requestFocus();
            ok = false;
        } else if (txtEstoqueMaximo.getText().isEmpty()) {
            new FXDialog(FXDialog.Type.WARNING, "Favor preencher a senha!").showDialog();
            txtEstoqueMaximo.requestFocus();
            ok = false;
        }
        return ok;
    }

    private boolean isValidConsulta() {
        boolean ok = rbNome.isSelected();
        if (ok) {
            ok = !txtBuscar.getText().isEmpty();
        }
        return ok;
    }

    public void SetValoresComponentes(EzattaProduto ezattaProduto) {
        txtId.setText(ezattaProduto.getId().toString());
        txtNome.setText(ezattaProduto.getNome());
        txtQuantidade.setText(ezattaProduto.getQuantidade().toString());
        txtEstoqueMaximo.setText(ezattaProduto.getEstoqueMaximo().toString());
        txtMinimo.setText(ezattaProduto.getEstoqueMinimo().toString());
        cbEmpresa.getSelectionModel().select(ezattaProduto.getEmpresa());
        cbCor.getSelectionModel().select(ezattaProduto.getCor());
    }

    private void popularDados() {
        dados.clear();
        dadosEmpresa.clear();
        cbEmpresa.getItems().clear();
        cbCor.getItems().clear();
        tb.getItems().clear();
        try {
            dados.addAll(produtoCtr.getAllProduto());
            dadosEmpresa.addAll(empresaCtr.getAllEmpresa());
            cbCor.getItems().addAll(new String[]{"Azul", "Amarelo", "Verde", "Vermelho", "Roza", "Cinza", "Magento", "Ciano"});
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, e.getCause().getMessage()).showDialog();
        }
        tb.setItems(dados);
        cbEmpresa.setItems(dadosEmpresa);
    }

    public int getOperacao() {
        return Operacao;
    }

    public void setOperacao(int operacao) {
        Operacao = operacao;
    }

    public EzattaProduto getEzattaProduto() {
        return ezattaProduto;
    }

    public void setEzattaProduto(EzattaProduto ezattaProduto) {
        this.ezattaProduto = ezattaProduto;
    }

    //--------------------------novos btns adicionar - remover - limpar
    BigDecimal qtd;
    @FXML
    void adicionarTanque(ActionEvent event) {     
        qtd = new BigDecimal(BigInteger.ONE);
        qtd = ezattaProduto.getQuantidade();
        
        String qtdRedefinirStr = qtdRedefinir.getText();
        qtdRedefinirStr = qtdRedefinirStr.replace(",", ".");
        qtd = qtd.add(new BigDecimal(qtdRedefinirStr));
        
        ezattaProduto.setQuantidade(qtd);
        produtoCtr.updateProduto(ezattaProduto);
        new FXDialog(FXDialog.Type.INFO, "Volume adicionado ao tanque.").showDialog();
        tabTela.getSelectionModel().select(0);
        redefinirConsulta(event);
    }

    @FXML
    void removerTanque(ActionEvent event) {
        qtd = new BigDecimal(0);
        qtd = ezattaProduto.getQuantidade();
        
        String qtdRedefinirStr = qtdRedefinir.getText();
        qtdRedefinirStr = qtdRedefinirStr.replace(",", ".");
        qtd = qtd.subtract(new BigDecimal(qtdRedefinirStr));
        
//        String qtdStr = qtdRedefinir.getText();
//        BigDecimal qtdN = new BigDecimal(qtdStr);
//        qtd = qtd.subtract(qtdN);
//        
        ezattaProduto.setQuantidade(qtd);
        produtoCtr.updateProduto(ezattaProduto);
        System.out.println("qtd: "+qtd);
        new FXDialog(FXDialog.Type.INFO, "Volume removido do tanque.").showDialog();
        tabTela.getSelectionModel().select(0);
        redefinirConsulta(event);
    }

    @FXML
    void vazioTanque(ActionEvent event) {
        qtd = new BigDecimal(0);
        ezattaProduto.setQuantidade(qtd);
        produtoCtr.updateProduto(ezattaProduto);
        System.out.println("qtd: "+qtd);
        new FXDialog(FXDialog.Type.INFO, "Tanque vazio.").showDialog();
        tabTela.getSelectionModel().select(0);
        redefinirConsulta(event);
    }

}
