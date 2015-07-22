/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import br.com.ezatta.dao.BicosDAO;
import br.com.ezatta.dao.ProdutoDAO;
import br.com.ezatta.model.EzattaBico;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.util.GenericTable;
import br.com.ezatta.util.ValidationFields;
import br.com.ezatta.view.FXDialog;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
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
import javafx.scene.input.MouseEvent;

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
    void enviarQuatroLitros(ActionEvent event) {

    }

    @FXML
    void enviarQuatroLitrosFinais(ActionEvent event) {

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
                bicoCtr.removeBico(getEzattaBico());
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
            bicoCtr.removeBico(getEzattaBico());
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
        cbProduto.getSelectionModel().select(null);
    }

    @FXML
    void btnSalvar(ActionEvent event) {
        if (isValidaTela()) {
            switch (getOperacao()) {
                case 0:
                    //EzattaBico bico = new EzattaBico(txtNome.getText(),txtEndereco.getText(), cbProduto.getSelectionModel().getSelectedItem());
                    EzattaBico bico = new EzattaBico(txtNome.getText(),txtEndereco.getText(), "208", cbProduto.getSelectionModel().getSelectedItem());
                    bicoCtr.addBico(bico);
                    popularDados();
                    new FXDialog(FXDialog.Type.INFO, "Registro inserido com sucesso!").showDialog();
                    tabTela.getSelectionModel().select(0);
                    break;
                case 1:
                    Integer id = Integer.parseInt(txtId.getText());
                    EzattaBico bicos = new EzattaBico(id, txtNome.getText(),txtEndereco.getText(), cbProduto.getSelectionModel().getSelectedItem());
                    bicoCtr.updateBico(bicos);
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
        }else if (txtEndereco.getText().isEmpty()) {
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
                    try{
                    setOperacao(1);
                    setEzattaBico(tb.getSelectionModel().getSelectedItem());
                    SetValoresComponentes(getEzattaBico());
                    }catch(NullPointerException e){
                        
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
  
    
}
