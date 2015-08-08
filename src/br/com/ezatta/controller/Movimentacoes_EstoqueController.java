/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import br.com.ezatta.dao.EstoqueProdutoDAO;
import br.com.ezatta.dao.ProdutoDAO;
import br.com.ezatta.model.EzattaBico;
import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.model.EzattaEstoqueProduto;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.model.EzattaUsuario;
import br.com.ezatta.util.GenericTable;
import br.com.ezatta.util.JPAUtil;
import br.com.ezatta.util.ValidationFields;
import br.com.ezatta.view.FXDialog;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
public class Movimentacoes_EstoqueController implements Initializable {

    private ObservableList<EzattaEstoqueProduto> dados = FXCollections.observableArrayList();
    private ObservableList<EzattaUsuario> dadosUsuario = FXCollections.observableArrayList();
    private ObservableList<EzattaEmpresa> dadosEmpresa = FXCollections.observableArrayList();
    private ObservableList<EzattaProduto> dadosProduto = FXCollections.observableArrayList();
    private int Operacao;
    private ProdutoDAO produtoCtr = new ProdutoDAO();
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
    private TableView<EzattaEstoqueProduto> tb;

    @FXML
    void btnBuscar(ActionEvent event) throws SQLException {
        if (isValidConsulta()) {
            dados.clear();
            //if (rbNome.isSelected()) {
                dados.addAll(EstoqueProdutoCtrl.findAll("select c.* from EZATTA_ESTOQUE_PRODUTO c", " where c.quantidade like  '%" + txtBuscar.getText() + "%'"));
//            } else if (rbEndereco.isSelected()) {
//                dados.addAll(bicoCtr.findAll("select c.* from ezatta_bico c", " where c.endereco like  '%" + txtBuscar.getText() + "%'"));
//            }
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
        String path = JPAUtil.getConfRelatorio();
        EntityManager em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        Session hibernateSession = em.unwrap(Session.class);
        hibernateSession.doWork(new org.hibernate.jdbc.Work() {
            @Override
            public void execute(Connection con) throws SQLException {
                try {
                    HashMap map = new HashMap();
                    JasperPrint rel = JasperFillManager.fillReport(path + "Bico.jasper", map, con);
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
            dados.addAll(EstoqueProdutoCtrl.getAllMovimentacoes());
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, e.getCause().getMessage()).showDialog();
        }
        tb.setItems(dados);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tb.getColumns().addAll(new GenericTable<EzattaEstoqueProduto>().tableColunas(EzattaEstoqueProduto.class));
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
