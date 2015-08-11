/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import br.com.ezatta.dao.EstoqueProdutoDAO;
import br.com.ezatta.dao.LogDAO;
import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.model.EzattaEstoqueProduto;
import br.com.ezatta.model.EzattaLog;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.model.EzattaUsuario;
import br.com.ezatta.util.GenericTable;
import br.com.ezatta.view.FXDialog;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class LogController implements Initializable {

    private ObservableList<EzattaLog> dados = FXCollections.observableArrayList();
    private ObservableList<EzattaUsuario> dadosUsuario = FXCollections.observableArrayList();
    private ObservableList<EzattaEmpresa> dadosEmpresa = FXCollections.observableArrayList();
    private ObservableList<EzattaProduto> dadosProduto = FXCollections.observableArrayList();
    private int Operacao;
    private LogDAO logCtr = new LogDAO();
    private EstoqueProdutoDAO EstoqueProdutoCtrl = new EstoqueProdutoDAO();
    private EzattaEstoqueProduto ezattaMovimentacao;
    
    @FXML
    private StackPane stack;

    @FXML
    private Button btnImprimirRegistros;

    @FXML
    private Button btnVoltar;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnRedefinirConsulta;

    @FXML
    private Button btnBuscar;

    @FXML
    private TableView<EzattaLog> tb;

    @FXML
    void btnBuscar(ActionEvent event) throws SQLException {
        if (isValidConsulta()) {
            dados.clear();
                dados.addAll(logCtr.findAll("select c.* from EZATTA_LOG c", " where c.acao like  '%" + txtBuscar.getText() + "%'"));
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

    }

    @FXML
    void btnExcluirFrontal(ActionEvent event) {

    }

    @FXML
    void btnExcluir(ActionEvent event) {

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
//        String path = JPAUtil.getConfRelatorio();
//        EntityManager em = new JPAUtil().getEntityManager();
//        em.getTransaction().begin();
//        Session hibernateSession = em.unwrap(Session.class);
//        hibernateSession.doWork(new org.hibernate.jdbc.Work() {
//            @Override
//            public void execute(Connection con) throws SQLException {
//                try {
//                    HashMap map = new HashMap();
//                    JasperPrint rel = JasperFillManager.fillReport(path + "Bico.jasper", map, con);
//                    JasperViewer jrviewer = new JasperViewer(rel, false);
//                    jrviewer.show();
//                } catch (JRException ex) {
//                    Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
    }

    @FXML
    void btnNovoFrontal(ActionEvent event) {

    }

    @FXML
    void btnSalvar(ActionEvent event) {
    }

    private boolean isValidaTela() {
        boolean ok = true;
//        if (txtNome.getText().isEmpty()) {
//            new FXDialog(FXDialog.Type.WARNING, "Favor preencher o nome!").showDialog();
//            txtNome.requestFocus();
//            ok = false;
//        }else if (txtEndereco.getText().isEmpty()) {
//            new FXDialog(FXDialog.Type.WARNING, "Favor preencher o endereço físico!").showDialog();
//            txtEndereco.requestFocus();
//            ok = false;
//        }


        return ok;
    }

    @FXML
    void btnNovo(ActionEvent event) {
       
    }
    
    private void popularDados() {
        dados.clear();
        tb.getItems().clear();
        try {
            dados.addAll(logCtr.getAllLog());
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, e.getCause().getMessage()).showDialog();
        }
        tb.setItems(dados);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tb.getColumns().addAll(new GenericTable<EzattaLog>().tableColunas(EzattaLog.class));
        popularDados();
    }    
    
    

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
