/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import br.com.ezatta.dao.EmpresaDAO;
import br.com.ezatta.dao.ProdutoDAO;
import br.com.ezatta.dao.EstoqueProdutoDAO;
import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.model.EzattaEstoqueProduto;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.util.GenericTable;
import br.com.ezatta.util.JPAUtil;
import br.com.ezatta.util.ValidationFields;
import br.com.ezatta.view.FXDialog;
import br.com.ezatta.view.FXDialog.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import javafx.scene.control.CheckBox;
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
public class ProdutoController implements Initializable {

    private ObservableList<EzattaProduto> dados = FXCollections.observableArrayList();
    private ObservableList<EzattaEmpresa> dadosEmpresa = FXCollections.observableArrayList();
    private int Operacao;
    private ProdutoDAO produtoCtr = new ProdutoDAO();
    private EmpresaDAO empresaCtr = new EmpresaDAO();
    private EzattaProduto ezattaProduto;

    @FXML
    private Tab tabConsulta;

    @FXML
    private ComboBox<String> cbCor;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtMinimo;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnImprimirRegistros;

    @FXML
    private Button btnCancelar;

    @FXML
    private ToggleGroup buscarPor;

    @FXML
    private Button btnExcluir;

    @FXML
    private TextField txtEstoqueMaximo;

    @FXML
    private Button btnAlterarF;

    @FXML
    private ImageView btnNovoFrontal;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private Button btnNovoF;

    @FXML
    private Tab tabCadastro;

    @FXML
    private Button btnRedefinirConsulta;

    @FXML
    private TableView<EzattaProduto> tb;

    @FXML
    private Button btnExcluirF;

    @FXML
    private TextField txtId;

    @FXML
    private Button btnIncluir;

    @FXML
    private RadioButton rbNome;

    @FXML
    private ComboBox<EzattaEmpresa> cbEmpresa;

    @FXML
    private TabPane tabTela;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnNovo;

    @FXML
    private CheckBox chAtivar;

    @FXML
    private TextField txtEmail;

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
                dados.addAll(produtoCtr.findAll("select c.* from ezatta_produto c", " where c.nome like  '%" + txtBuscar.getText() + "%'"));
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
            txtQuantidade.setDisable(true);
            setOperacao(1);
            setEzattaProduto(tb.getSelectionModel().getSelectedItem());
            SetValoresComponentes(getEzattaProduto());
        }
    }

    @FXML
    void btnExcluirFrontal(ActionEvent event) throws SQLException {
        if (tb.getSelectionModel().isEmpty()) {
            new FXDialog(Type.ERROR, "Favor selecionar o item primeiro... ").showDialog();
        } else {
            boolean ok = new FXDialog(Type.CONFIRM, "Tem Certeza que deseja excluir este registro?").showDialog();
            if (ok) {
                produtoCtr.removeProduto(getEzattaProduto());
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
    }

    @FXML
    void btnExcluir(ActionEvent event) throws SQLException {
        boolean ok = new FXDialog(Type.CONFIRM, "Tem Certeza que deseja excluir este registro?").showDialog();
        if ((ok) && (getOperacao() == 1)) {
            produtoCtr.removeProduto(getEzattaProduto());
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
                    JasperPrint rel = JasperFillManager.fillReport(path + "Produto.jasper", map, con);
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
        txtQuantidade.setText("");
        txtEstoqueMaximo.setText("");
        txtMinimo.setText("");
        cbCor.getSelectionModel().select(null);
        cbEmpresa.getSelectionModel().select(null);
        txtQuantidade.setDisable(false);
        txtEmail.setText("");
        chAtivar.setSelected(false);
    }

    @FXML
    void btnSalvar(ActionEvent event) throws SQLException {
        if (isValidaTela()) {
            switch (getOperacao()) {
                case 0:
                    EzattaProduto produto = new EzattaProduto();

                    produto.setNome(txtNome.getText());
                    produto.setQuantidade(new BigDecimal(txtQuantidade.getText()));
                    produto.setEstoqueMaximo(new BigDecimal(txtEstoqueMaximo.getText()));
                    produto.setEstoqueMinimo(new BigDecimal(txtMinimo.getText()));
                    produto.setEmpresa(cbEmpresa.getSelectionModel().getSelectedItem());
                    produto.setCor(cbCor.getSelectionModel().getSelectedItem());
                    produto.setNotificacaoEstoqueEmail(chAtivar.selectedProperty().getValue());
                    produto.setEmail(txtEmail.getText());

                    produtoCtr.addProduto(produto);
                    EzattaProduto pNovo = produtoCtr.getProdutoByNome(produto.getNome());
                    System.out.println("produto inserido: "+pNovo.getNome()+" id: "+pNovo.getId());
                    
                    //adiciona estoque produto inicio
                    EstoqueProdutoDAO produtoEstoqueDAO = new EstoqueProdutoDAO();
                    EzattaEstoqueProduto estoque = new EzattaEstoqueProduto();
                    
                    Timestamp data = new Timestamp(System.currentTimeMillis());
                    estoque.setDataoperacao(data);
                    estoque.setEmpresa(cbEmpresa.getSelectionModel().getSelectedItem());
                    estoque.setQuantidade(new Float(txtQuantidade.getText()));
                    estoque.setOperacao("Cadastro");
                    estoque.setProduto(pNovo);
                    
                    System.out.println("Estoque: "+estoque);
                    produtoEstoqueDAO.addPEstoque(estoque);
                    System.out.println("adicionou no estoque");
                    //adiciona estoque produto fim
                    
                    popularDados();
                    new FXDialog(Type.INFO, "Registro inserido com sucesso!").showDialog();
                    tabTela.getSelectionModel().select(0);
                    break;
                case 1:
                    Integer id = Integer.parseInt(txtId.getText());
                    //EzattaProduto produtos = new EzattaProduto(id, txtNome.getText(), new BigDecimal(txtQuantidade.getText()), new BigDecimal(txtEstoqueMaximo.getText()), new BigDecimal(txtMinimo.getText()), cbCor.getSelectionModel().getSelectedItem(), cbEmpresa.getSelectionModel().getSelectedItem());
                    EzattaProduto produtos = new EzattaProduto();

                    produtos.setId(id);
                    produtos.setNome(txtNome.getText());
                    produtos.setQuantidade(new BigDecimal(txtQuantidade.getText()));
                    produtos.setEstoqueMaximo(new BigDecimal(txtEstoqueMaximo.getText()));
                    produtos.setEstoqueMinimo(new BigDecimal(txtMinimo.getText()));
                    produtos.setEmpresa(cbEmpresa.getSelectionModel().getSelectedItem());
                    produtos.setCor(cbCor.getSelectionModel().getSelectedItem());
                    produtos.setNotificacaoEstoqueEmail(chAtivar.selectedProperty().getValue());
                    produtos.setEmail(txtEmail.getText());
                    
                    produtoCtr.updateProduto(produtos);
                    new FXDialog(Type.INFO, "Registro atualizado com sucesso!").showDialog();
                    tabTela.getSelectionModel().select(0);
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
        } else if (txtQuantidade.getText().isEmpty()) {
            new FXDialog(Type.WARNING, "Favor preencher o Quantidade!").showDialog();
            txtQuantidade.requestFocus();
            ok = false;
        } else if (txtMinimo.getText().isEmpty()) {
            new FXDialog(Type.WARNING, "Favor preencher o Quantidade mínima!").showDialog();
            txtMinimo.requestFocus();
            ok = false;
        } else if (txtEstoqueMaximo.getText().isEmpty()) {
            new FXDialog(Type.WARNING, "Favor preencher a senha!").showDialog();
            txtEstoqueMaximo.requestFocus();
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
        txtQuantidade.setText("");
        txtEstoqueMaximo.setText("");
        txtMinimo.setText("");
        cbCor.getSelectionModel().select(null);
        cbEmpresa.getSelectionModel().select(null);
        txtQuantidade.setDisable(false);
        txtEmail.setText("");
        chAtivar.setSelected(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chAtivar.setVisible(false);
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

                    if (txtQuantidade.getText() != "" || txtQuantidade.getText() != null) {
                        txtQuantidade.setDisable(true);
                    }
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

    public void SetValoresComponentes(EzattaProduto ezattaProduto) {
        txtId.setText(ezattaProduto.getId().toString());
        txtNome.setText(ezattaProduto.getNome());
        txtQuantidade.setText(ezattaProduto.getQuantidade().toString());
        txtEstoqueMaximo.setText(ezattaProduto.getEstoqueMaximo().toString());
        txtMinimo.setText(ezattaProduto.getEstoqueMinimo().toString());
        cbEmpresa.getSelectionModel().select(ezattaProduto.getEmpresa());
        cbCor.getSelectionModel().select(ezattaProduto.getCor());
        txtEmail.setText(ezattaProduto.getEmail());
        chAtivar.setSelected(ezattaProduto.isNotificacaoEstoqueEmail());
        
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

    public EzattaProduto getEzattaProduto() {
        return ezattaProduto;
    }

    public void setEzattaProduto(EzattaProduto ezattaProduto) {
        this.ezattaProduto = ezattaProduto;
    }
    
    @FXML
    private Button btnVoltar;

    @FXML
    private StackPane stack;

    PrincipalController p = PrincipalController.principal;

    @FXML
    void btnVoltarTela(ActionEvent event) {
        System.out.println("entrou");
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
