package br.com.ezatta.controller;

import br.com.ezatta.dao.BicosDAO;
import br.com.ezatta.dao.ProdutoDAO;
import br.com.ezatta.model.EzattaBico;
import br.com.ezatta.model.EzattaProduto;
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
public class BicoController implements Initializable {

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
    private ComboBox<EzattaProduto> cbProduto;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnRedefinirConsulta;

    @FXML
    private TableView<EzattaBico> tb;

    @FXML
    private Button btnExcluirF;

    @FXML
    private TextField txtId;

    @FXML
    private Button btnImprimirRegistros;

    @FXML
    private Button btnIncluir;

    @FXML
    private RadioButton rbEndereco;

    @FXML
    private Button btnCancelar;

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
    void cancelar(ActionEvent event) {
        popularDados();
        tabTela.getSelectionModel().select(0);
    }

    @FXML
    void btnBuscar(ActionEvent event) throws SQLException {
        if (isValidConsulta()) {
            dados.clear();
            //if (rbNome.isSelected()) {
                dados.addAll(bicoCtr.findAll("select c.* from ezatta_bico c", " where c.nome like  '%" + txtBuscar.getText() + "%'"));
//            } else if (rbEndereco.isSelected()) {
//                dados.addAll(bicoCtr.findAll("select c.* from ezatta_bico c", " where c.endereco like  '%" + txtBuscar.getText() + "%'"));
//            }
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
            setEzattaBico(tb.getSelectionModel().getSelectedItem());
            SetValoresComponentes(getEzattaBico());
        }
    }

    @FXML
    void btnExcluirFrontal(ActionEvent event) {
        if (tb.getSelectionModel().isEmpty()) {
            new FXDialog(Type.ERROR, "Favor selecionar o item primeiro... ").showDialog();
        } else {
            boolean ok = new FXDialog(Type.CONFIRM, "Tem Certeza que deseja excluir este registro?").showDialog();
            if (ok) {
                bicoCtr.removeBico(getEzattaBico());
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
    void btnExcluir(ActionEvent event) {
        boolean ok = new FXDialog(Type.CONFIRM, "Tem Certeza que deseja excluir este registro?").showDialog();
        if ((ok) && (getOperacao() == 1)) {
            bicoCtr.removeBico(getEzattaBico());
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
                    System.out.println("Bico fator escala: "+bico.getFatorescala());
                    bicoCtr.addBico(bico);
                    popularDados();
                    new FXDialog(Type.INFO, "Registro inserido com sucesso!").showDialog();
                    tabTela.getSelectionModel().select(0);
                    break;
                case 1:
                    Integer id = Integer.parseInt(txtId.getText());
                    EzattaBico bicos = new EzattaBico(id, txtNome.getText(),txtEndereco.getText(), cbProduto.getSelectionModel().getSelectedItem());
                    bicoCtr.updateBico(bicos);
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
        }else if (txtEndereco.getText().isEmpty()) {
            new FXDialog(Type.WARNING, "Favor preencher o endereço físico!").showDialog();
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
            new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
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
