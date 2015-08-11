/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import br.com.ezatta.dao.EmpresaDAO;
import br.com.ezatta.dao.OperadorDAO;
import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.model.EzattaOperador;
import br.com.ezatta.util.GenericTable;
import br.com.ezatta.util.JPAUtil;
import br.com.ezatta.util.ValidationFields;
import br.com.ezatta.view.FXDialog;
import br.com.ezatta.view.FXDialog.Type;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javax.persistence.EntityManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class OperadorController implements Initializable {

    private ObservableList<EzattaOperador> dados = FXCollections.observableArrayList();
    private ObservableList<EzattaEmpresa> dadosEmpresa = FXCollections.observableArrayList();
    private int Operacao;
    private OperadorDAO operadorCtr = new OperadorDAO();
    private EmpresaDAO empresaCtr = new EmpresaDAO();
    private EzattaOperador ezattaOperador;

    @FXML
    private Tab tabConsulta;

    @FXML
    private Button btnAlterarF;

    @FXML
    private ImageView btnNovoFrontal;

    @FXML
    private TextField txtNome;

    @FXML
    private Button btnNovoF;

    @FXML
    private Tab tabCadastro;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnRedefinirConsulta;

    @FXML
    private TableView<EzattaOperador> tb;

    @FXML
    private Button btnExcluirF;

    @FXML
    private TextField txtId;

    @FXML
    private Button btnImprimirRegistros;

    @FXML
    private Button btnIncluir;

    @FXML
    private Button btnCancelar;

    @FXML
    private RadioButton rbNome;

    @FXML
    private ToggleGroup buscarPor;

    @FXML
    private Button btnExcluir;

    @FXML
    private TextField txtMatricula;

    @FXML
    private ComboBox<EzattaEmpresa> cbEmpresa;

    @FXML
    private TabPane tabTela;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnNovo;

    @FXML
    void cancelar(ActionEvent event) {
        popularDados();
        tabTela.getSelectionModel().select(0);
    }

    @FXML
    void btnBuscar(ActionEvent event) throws SQLException {
        if (isValidConsulta()) {
            dados.clear();
            //if (rbNome.isSelected()) {
                dados.addAll(operadorCtr.findAll("select c.* from ezatta_operador c", " where c.nome like  '%" + txtBuscar.getText() + "%'"));
            //}
            tb.setItems(dados);
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
        if (tb.getSelectionModel().isEmpty()) {
            new FXDialog(Type.ERROR, "Favor selecionar o item primeiro... ").showDialog();
        } else {
            tabTela.getSelectionModel().select(1);
            setOperacao(1);

            setOperacao(1);
            setEzattaOperador(tb.getSelectionModel().getSelectedItem());
            SetValoresComponentes(getEzattaOperador());
        }
    }

    @FXML
    void btnExcluirFrontal(ActionEvent event) throws SQLException {
        if (tb.getSelectionModel().isEmpty()) {
            new FXDialog(Type.ERROR, "Favor selecionar o item primeiro... ").showDialog();
        } else {
            boolean ok = new FXDialog(Type.CONFIRM, "Tem Certeza que deseja excluir este registro?").showDialog();
            if (ok) {
                operadorCtr.removeOperador(getEzattaOperador());
                ok = true;
                if (ok) {
                    new FXDialog(Type.INFO, "Operaçao realizada com sucesso!").showDialog();
                    popularDados();
                    btnNovo(event);
                } else {
                    new FXDialog(Type.WARNING, "Existem relaçoes com outras entidades!").showDialog();
                }
            }
        }
    }

    @FXML
    void btnExcluir(ActionEvent event) throws SQLException {
        boolean ok = new FXDialog(Type.CONFIRM, "Tem Certeza que deseja excluir este registro?").showDialog();
        if ((ok) && (getOperacao() == 1)) {
            operadorCtr.removeOperador(getEzattaOperador());
            ok = true;
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
        boolean ok = true;
        if (ok) {
            ok = !txtBuscar.getText().isEmpty();
        }
        return ok;
    }

    @FXML
    void btnImprimir(ActionEvent event) {
        String path = JPAUtil.getConfRelatorio();
        EntityManager em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        Session hibernateSession = em.unwrap(Session.class);
        hibernateSession.doWork(new org.hibernate.jdbc.Work() {
            @Override
            public void execute(Connection con) throws SQLException {
                try {
                    HashMap map = new HashMap();
                    JasperPrint rel = JasperFillManager.fillReport(path + "Operador.jasper", map, con);
                    JasperViewer jrviewer = new JasperViewer(rel, false);
                    jrviewer.show();
                } catch (JRException ex) {
                    Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @FXML
    void btnNovoFrontal(ActionEvent event) {

        setOperacao(0);
        tabTela.getSelectionModel().select(1);
        txtId.setText("");
        txtNome.setText("");
        txtNome.requestFocus();
        txtMatricula.setText("");
        cbEmpresa.getSelectionModel().select(null);
    }

    @FXML
    void btnSalvar(ActionEvent event) throws SQLException {
        if (isValidaTela()) {
            switch (getOperacao()) {
                case 0:
                    EzattaOperador operador = new EzattaOperador(txtNome.getText(), txtMatricula.getText(), cbEmpresa.getSelectionModel().getSelectedItem());
                    operadorCtr.addOperador(operador);
                    popularDados();
                    new FXDialog(Type.INFO, "Registro inserido com sucesso!").showDialog();
                    tabTela.getSelectionModel().select(0);
                    break;
                case 1:
                    Integer id = Integer.parseInt(txtId.getText());
                    EzattaOperador operadores = new EzattaOperador(id, txtNome.getText(), txtMatricula.getText(), cbEmpresa.getSelectionModel().getSelectedItem());
                    operadorCtr.updateOperador(operadores);
                    new FXDialog(Type.INFO, "Registro atualizado com sucesso!").showDialog();
                    popularDados();
                    tabTela.getSelectionModel().select(0);
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
        }

        return ok;
    }

    @FXML
    void btnNovo(ActionEvent event) {
        setOperacao(0);
        txtId.setText("");
        txtNome.setText("");
        txtNome.requestFocus();
        txtMatricula.setText("");
        cbEmpresa.getSelectionModel().select(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ValidationFields.checkEmptyFields(txtNome,cbEmpresa);
        tb.getColumns().addAll(new GenericTable<EzattaOperador>().tableColunas(EzattaOperador.class));
        popularDados();
        tb.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    tabTela.getSelectionModel().select(1);
                    setOperacao(1);
                    setEzattaOperador(tb.getSelectionModel().getSelectedItem());
                    SetValoresComponentes(getEzattaOperador());
                }
                if (event.getClickCount() > 0) {
                    try {
                        setOperacao(1);
                        setEzattaOperador(tb.getSelectionModel().getSelectedItem());
                        SetValoresComponentes(getEzattaOperador());
                    } catch (NullPointerException e) {
                    }
                }
            }

        });
    }

    public void SetValoresComponentes(EzattaOperador ezatta) {
        txtId.setText(ezatta.getId().toString());
        txtNome.setText(ezatta.getNome());
        txtMatricula.setText(ezatta.getMatricula());
        cbEmpresa.getSelectionModel().select(ezatta.getEmpresa());
    }

    private void popularDados() {
        dados.clear();
        dadosEmpresa.clear();
        cbEmpresa.getItems().clear();
        tb.getItems().clear();
        try {
            dados.addAll(operadorCtr.getAllOperador());
            dadosEmpresa.addAll(empresaCtr.getAllEmpresa());
        } catch (Exception e) {
            new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
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

    public EzattaOperador getEzattaOperador() {
        return ezattaOperador;
    }

    public void setEzattaOperador(EzattaOperador ezattaOperador) {
        this.ezattaOperador = ezattaOperador;
    }
    
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
