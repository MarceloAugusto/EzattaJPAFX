/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import br.com.ezatta.dao.EmpresaDAO;
import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.util.GenericTable;
import br.com.ezatta.util.Path;
import br.com.ezatta.util.ValidationFields;
import br.com.ezatta.view.FXDialog;
import br.com.ezatta.view.FXDialog.Type;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class EmpresaController implements Initializable {

    private ObservableList<EzattaEmpresa> dados = FXCollections.observableArrayList();
    private int Operacao;
    private EmpresaDAO EmpresaCtr = new EmpresaDAO();
    private EzattaEmpresa ezattaEmpresa;

    @FXML
    private TextField txtId;

    @FXML
    private Tab tabConsulta;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private Button btnBuscar2;

    @FXML
    private ImageView btnNovoFrontal;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtEmail;

    @FXML
    private Tab tabCadastro;

    @FXML
    private TextField txtBuscar;

    @FXML
    private RadioButton rbEmail;

    @FXML
    private TextField txtLogin;

    @FXML
    private Button btnImprimirRegistros;

    @FXML
    private Button btnIncluir;

    @FXML
    private TableView<EzattaEmpresa> tbEmpresa;

    @FXML
    private RadioButton rbNome;

    @FXML
    private ToggleGroup buscarPor;

    @FXML
    private Button btnExcluir;

    @FXML
    private TabPane tabTela;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnNovo;

    @FXML
    private Button btnRedefinirConsulta;

    @FXML
    private Button btnAlterarF;

    @FXML
    private Button btnNovoF;

    @FXML
    private Button btnExcluirf;

    @FXML
    private Button btnCancelar;

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
                dados.addAll(EmpresaCtr.findAll("select c.* from ezatta_empresa c", " where c.nome like  '%" + txtBuscar.getText() + "%'"));
            } else if (rbEmail.isSelected()) {
                dados.addAll(EmpresaCtr.findAll("select c.* from ezatta_empresa c", " where c.email like  '%" + txtBuscar.getText() + "%'"));
            }
            tbEmpresa.setItems(dados);
        } else {
            new FXDialog(Type.WARNING, "Escolha pelo menos uma das opções para consulta!").showDialog();
            txtBuscar.requestFocus();
        }
    }

    @FXML
    void redefinirConsulta(ActionEvent event) {
        popularDados();
    }

    @FXML
    void btnAlterarFrontal(ActionEvent event) {
        if (tbEmpresa.getSelectionModel().isEmpty()) {
            new FXDialog(Type.ERROR, "Favor selecionar o item primeiro... ").showDialog();
        } else {
            tabTela.getSelectionModel().select(1);
            setOperacao(1);
            setEzattaEmpresa(tbEmpresa.getSelectionModel().getSelectedItem());
            txtId.setText(tbEmpresa.getSelectionModel().getSelectedItem().getId().toString());
            txtNome.setText(tbEmpresa.getSelectionModel().getSelectedItem().getNome());
            txtLogin.setText(tbEmpresa.getSelectionModel().getSelectedItem().getLogin());
            txtSenha.setText(tbEmpresa.getSelectionModel().getSelectedItem().getSenha());
            txtEmail.setText(tbEmpresa.getSelectionModel().getSelectedItem().getEmail());
        }
    }

    @FXML
    void btnExcluirFrontal(ActionEvent event) {
        boolean ok = new FXDialog(Type.CONFIRM, "Tem Certeza que deseja excluir este registro?").showDialog();
        if ((ok) && (getOperacao() == 1)) {
            try {
                EmpresaCtr.removeEmpresa(getEzattaEmpresa());
                ok = true;
            } catch (SQLException e) {
                new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
            }
            if (ok) {
                new FXDialog(Type.INFO, "Operaçao realizada com sucesso!").showDialog();
                popularDados();
                btnNovo(event);
            } else {
                new FXDialog(Type.WARNING, "Existem relaçoes com outras entidades!").showDialog();
            }
        }
    }

    @FXML
    void btnExcluir(ActionEvent event) {
        boolean ok = new FXDialog(Type.CONFIRM, "Tem Certeza que deseja excluir este registro?").showDialog();
        if ((ok) && (getOperacao() == 1)) {
            try {
                EmpresaCtr.removeEmpresa(getEzattaEmpresa());
                ok = true;
            } catch (SQLException e) {
                new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
            }
            if (ok) {
                new FXDialog(Type.INFO, "Operaçao realizada com sucesso!").showDialog();
                popularDados();
                tabTela.getSelectionModel().select(0);
            } else {
                new FXDialog(Type.WARNING, "Existem relaçoes com outras entidades!").showDialog();
            }
        }
    }

    private boolean isValidConsulta() {
        boolean ok = rbNome.isSelected() || rbEmail.isSelected();
        if (ok) {
            ok = !txtBuscar.getText().isEmpty();
        }
        return ok;
    }

    @FXML
    void btnImprimir(ActionEvent event) {
        
        //String arquivos = Path.workingDir + "/relatorio/Empresa.jasper";
        String arquivos = Path.workingDir + "/relatorio/Empresa.jasper";
//--------------------------------------------
        //String arquivo = "\\relatorio\\AdmEmpresa.jasper";
        EmpresaDAO daoCli = new EmpresaDAO();
        
        List<EzattaEmpresa> emp = daoCli.getAllEmpresa();
        try {
            //String arquivos = System.getProperty("user.dir")+arquivo;
            System.out.println("arquivo dir : "+arquivos);
            JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(emp);
            JasperPrint impressao = JasperFillManager.fillReport(arquivos, null, beanDataSource);
            JasperViewer jasperViewer = new JasperViewer(impressao, false);
            jasperViewer.show();
        } catch (JRException ex) {
            System.out.println("Erro na geração do relatorio...");
            ex.printStackTrace();
        }
    }

    @FXML
    void btnNovoFrontal(ActionEvent event) {

        setOperacao(0);
        tabTela.getSelectionModel().select(1);
        txtId.setText("");
        txtNome.setText("");
        txtNome.requestFocus();
        txtLogin.setText("");
        txtSenha.setText("");
        txtEmail.setText("");
    }

    @FXML
    void btnSalvar(ActionEvent event) {
        if (isValidaTela()) {
            switch (getOperacao()) {
                case 0:
                    try {
                        EzattaEmpresa empresa = new EzattaEmpresa(txtNome.getText(), txtLogin.getText(), txtSenha.getText(), txtEmail.getText());
                        EmpresaCtr.addEmpresa(empresa);
                        popularDados();
                        new FXDialog(Type.INFO, "Registro inserido com sucesso!").showDialog();
                        tabTela.getSelectionModel().select(0);
                    } catch (SQLException e) {
                        new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
                    }
                    popularDados();
                    break;
                case 1:
                    try {
                        Integer id = Integer.parseInt(txtId.getText());
                        EzattaEmpresa empresa = new EzattaEmpresa(id, txtNome.getText(), txtLogin.getText(), txtSenha.getText(), txtEmail.getText());
                        EmpresaCtr.updateEmpresa(empresa);
                        new FXDialog(Type.INFO, "Registro atualizado com sucesso!").showDialog();
                        popularDados();
                        tabTela.getSelectionModel().select(0);
                    } catch (SQLException e) {
                        new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
                    }
                    popularDados();
                    break;
            }
        }
    }

    private boolean isValidaTela() {
        boolean ok = true;
        if (txtNome.getText().isEmpty()) {
            new FXDialog(Type.WARNING, "Favor preencher o nome!").showDialog();
            txtNome.requestFocus();
            ok = false;
        }else if (txtLogin.getText().isEmpty()) {
            new FXDialog(Type.WARNING, "Favor preencher o login!").showDialog();
            txtLogin.requestFocus();
            ok = false;
        }else if (txtSenha.getText().isEmpty()) {
            new FXDialog(Type.WARNING, "Favor preencher o Senha!").showDialog();
            txtSenha.requestFocus();
            ok = false;
        }else if (txtEmail.getText().isEmpty()) {
            new FXDialog(Type.WARNING, "Favor preencher o email!").showDialog();
            txtEmail.requestFocus();
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
        txtLogin.setText("");
        txtSenha.setText("");
        txtEmail.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ValidationFields.checkEmptyFields(txtNome,txtLogin,txtSenha,txtEmail);
        tbEmpresa.getColumns().addAll(new GenericTable<EzattaEmpresa>().tableColunas(EzattaEmpresa.class));
        popularDados();
        tbEmpresa.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    tabTela.getSelectionModel().select(1);
                    setOperacao(1);
                    setEzattaEmpresa(tbEmpresa.getSelectionModel().getSelectedItem());
                    txtId.setText(tbEmpresa.getSelectionModel().getSelectedItem().getId().toString());
                    txtNome.setText(tbEmpresa.getSelectionModel().getSelectedItem().getNome());
                    txtLogin.setText(tbEmpresa.getSelectionModel().getSelectedItem().getLogin());
                    txtSenha.setText(tbEmpresa.getSelectionModel().getSelectedItem().getSenha());
                    txtEmail.setText(tbEmpresa.getSelectionModel().getSelectedItem().getEmail());
                }

                if (event.getClickCount() > 0) {
                    try {
                        setOperacao(1);
                        setEzattaEmpresa(tbEmpresa.getSelectionModel().getSelectedItem());
                        txtId.setText(tbEmpresa.getSelectionModel().getSelectedItem().getId().toString());
                        txtNome.setText(tbEmpresa.getSelectionModel().getSelectedItem().getNome());
                        txtLogin.setText(tbEmpresa.getSelectionModel().getSelectedItem().getLogin());
                        txtSenha.setText(tbEmpresa.getSelectionModel().getSelectedItem().getSenha());
                        txtEmail.setText(tbEmpresa.getSelectionModel().getSelectedItem().getEmail());
                    } catch (NullPointerException e) {
                    }
                }
            }
        }
        );
    }

    private void popularDados() {
        dados.clear();
        tbEmpresa.getItems().clear();
        try {
            dados.addAll(EmpresaCtr.getAllEmpresa());
        } catch (Exception e) {
            new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
        }
        tbEmpresa.setItems(dados);
    }

    public int getOperacao() {
        return Operacao;
    }

    public void setOperacao(int operacao) {
        Operacao = operacao;
    }

    public EzattaEmpresa getEzattaEmpresa() {
        return ezattaEmpresa;
    }

    public void setEzattaEmpresa(EzattaEmpresa ezattaEmpresa) {
        this.ezattaEmpresa = ezattaEmpresa;
    }

}
