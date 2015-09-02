package br.com.ezatta.controller;

import br.com.ezatta.dao.BicosDAO;
import br.com.ezatta.dao.MovimentacoesDAO;
import br.com.ezatta.dao.OperadorDAO;
import br.com.ezatta.dao.ProdutoDAO;
import br.com.ezatta.dao.UsuarioDAO;
import br.com.ezatta.model.EzattaBico;
import br.com.ezatta.model.EzattaMovimentacoes;
import br.com.ezatta.model.EzattaOperador;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.model.EzattaUsuario;
import br.com.ezatta.relatorios.Relatorio;
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
public class HistoricoController implements Initializable {

    private ObservableList<EzattaMovimentacoes> dadosMovimentacoes = FXCollections.observableArrayList();
    private ObservableList<EzattaProduto> dadosProduto = FXCollections.observableArrayList();
    private ObservableList<EzattaBico> dadosBico = FXCollections.observableArrayList();
    private ObservableList<EzattaOperador> dadosOperador = FXCollections.observableArrayList();
    private ObservableList<EzattaUsuario> dadosUsuario = FXCollections.observableArrayList();

    private ProdutoDAO produtoCtr = new ProdutoDAO();
    private MovimentacoesDAO movimentacaoCtr = new MovimentacoesDAO();
    private BicosDAO bicoCtr = new BicosDAO();
    private UsuarioDAO usuarioCtr = new UsuarioDAO();
    private OperadorDAO operadorCtr = new OperadorDAO();

    private int Operacao;

    private EzattaMovimentacoes ezattaMovimentacao;

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
    private TableView<EzattaMovimentacoes> tb;

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

    //----------------------------------inicio novos componentes------------------------
    @FXML
    private StackPane stack;

    @FXML
    private ComboBox<EzattaBico> cbBico;

    @FXML
    private ComboBox<EzattaOperador> cbOperador;

    @FXML
    private ComboBox<EzattaUsuario> cbUsuario;

    @FXML
    private TextField txtStatus;

    @FXML
    private Button btnVoltar;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private TextField txtKm;

    @FXML
    private TextField txtOs;

    @FXML
    private TextField txtPlaca;

    //-----------------------------------fim novos componentes-------------------------
    @FXML
    void cancelar(ActionEvent event) {
        popularDados();
        tabTela.getSelectionModel().select(0);
    }

    @FXML
    void btnBuscar(ActionEvent event) throws SQLException {
        if (isValidConsulta()) {
            dadosMovimentacoes.clear();
            dadosMovimentacoes.addAll(movimentacaoCtr.findAll("select c.* from EZATTA_MOVIMENTACOES c", " where c.os like  '%" + txtBuscar.getText() + "%'"));
            tb.setItems(dadosMovimentacoes);
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

            setEzattaMovimentacao(tb.getSelectionModel().getSelectedItem());
            SetValoresComponentes(getEzattaMovimentacao());
        }
    }

    @FXML
    void btnExcluirFrontal(ActionEvent event) throws SQLException {
        if (tb.getSelectionModel().isEmpty()) {
            new FXDialog(Type.ERROR, "Favor selecionar o item primeiro... ").showDialog();
        } else {
            boolean ok = new FXDialog(Type.CONFIRM, "Tem Certeza que deseja excluir este registro?").showDialog();
            if (ok) {
                movimentacaoCtr.removeEstoque(getEzattaMovimentacao());
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
            movimentacaoCtr.removeEstoque(getEzattaMovimentacao());
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
        Relatorio relatorio = new Relatorio();
        try {
            relatorio.gerar();
        } catch (SQLException ex) {
            Logger.getLogger(HistoricoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void btnNovoFrontal(ActionEvent event) {
        setOperacao(0);
//        tabTela.getSelectionModel().select(1);
//        txtId.setText("");
//        txtQuantidade.setText("");
//        txtOs.requestFocus();
//        txtPlaca.setText("");
//        txtKm.setText("");
//        txtStatus.setText("");
//        cbProduto.getSelectionModel().select(null);
//        cbUsuario.getSelectionModel().select(null);
//        cbBico.getSelectionModel().select(null);
//        cbOperador.getSelectionModel().select(null);
    }

    @FXML
    void btnSalvar(ActionEvent event) throws SQLException {
        if (isValidaTela()) {
            switch (getOperacao()) {
                case 0:
                    //EzattaBico bico = new EzattaBico(txtNome.getText(),txtEndereco.getText(), cbProduto.getSelectionModel().getSelectedItem());

                    
                    
                    
                    //bico = new EzattaBico(txtNome.getText(), txtEndereco.getText(), "208", cbProduto.getSelectionModel().getSelectedItem());
                    //System.out.println("Bico fator escala: " + bico.getFatorescala());
                    //bicoCtr.addBico(bico);
                    
                    popularDados();
                    new FXDialog(Type.INFO, "Registro inserido com sucesso!").showDialog();
                    tabTela.getSelectionModel().select(0);
                    break;
                case 1:
                    Integer id = Integer.parseInt(txtId.getText());
                    
                    //EzattaMovimentacoes mov = new EzattaMovimentacoes();
                    EzattaMovimentacoes mov = tb.getSelectionModel().getSelectedItem();
                    mov.setId(id);
                    //mov.setBico(cbBico.getSelectionModel().getSelectedItem());
                    //mov.setDataEstoque(null);
                    mov.setKm(txtKm.getText());
                    mov.setOperador(cbOperador.getSelectionModel().getSelectedItem());
                    mov.setOs(txtOs.getText());
                    mov.setPlaca(txtPlaca.getText());
                    //mov.setProduto(cbProduto.getSelectionModel().getSelectedItem());
                    mov.setQtdEstoque(Float.parseFloat(txtQuantidade.getText()));
                    mov.setStatus(Integer.parseInt(txtStatus.getText()));
                    mov.setUsuario(cbUsuario.getSelectionModel().getSelectedItem());
                    
                   
                    movimentacaoCtr.updateEstoque(mov);
                    new FXDialog(Type.INFO, "Registro atualizado com sucesso!").showDialog();
                    tabTela.getSelectionModel().select(0);
                    popularDados();
                    break;
            }
        }
    }

    private boolean isValidaTela() {
        boolean ok = true;
//        if (txtNome.getText().isEmpty()) {
//            new FXDialog(Type.WARNING, "Favor preencher o nome!").showDialog();
//            txtNome.requestFocus();
//            ok = false;
//        } else if (txtEndereco.getText().isEmpty()) {
//            new FXDialog(Type.WARNING, "Favor preencher o endereço físico!").showDialog();
//            txtEndereco.requestFocus();
//            ok = false;
//        }

        return ok;
    }

    @FXML
    void btnNovo(ActionEvent event) {
        setOperacao(0);
//        txtId.setText("");
//        txtQuantidade.setText("");
//        txtOs.requestFocus();
//        txtPlaca.setText("");
//        txtKm.setText("");
//        txtStatus.setText("");
//        cbProduto.getSelectionModel().select(null);
//        cbUsuario.getSelectionModel().select(null);
//        cbBico.getSelectionModel().select(null);
//        cbOperador.getSelectionModel().select(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //ValidationFields.checkEmptyFields(txtNome, txtEndereco, cbProduto);
        tb.getColumns().addAll(new GenericTable<EzattaMovimentacoes>().tableColunas(EzattaMovimentacoes.class));
        popularDados();
        tb.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    tabTela.getSelectionModel().select(1);
                    setOperacao(1);
                    setEzattaMovimentacao(tb.getSelectionModel().getSelectedItem());
                    SetValoresComponentes(getEzattaMovimentacao());
                }

                if (event.getClickCount() > 0) {
                    try {
                        setOperacao(1);
                        setEzattaMovimentacao(tb.getSelectionModel().getSelectedItem());
                        SetValoresComponentes(getEzattaMovimentacao());
                    } catch (NullPointerException e) {

                    }
                }
            }

        });

    }

    private void SetValoresComponentes(EzattaMovimentacoes ezatta) {
        txtId.setText(ezatta.getId().toString());
        txtQuantidade.setText(ezatta.getQtdEstoque().toString());
        txtOs.setText(ezatta.getOs());
        txtPlaca.setText(ezatta.getPlaca());
        txtKm.setText(ezatta.getKm());
        txtStatus.setText(ezatta.getStatus().toString());
        cbProduto.getSelectionModel().select(ezatta.getProduto());
        cbBico.getSelectionModel().select(ezatta.getBico());
        cbUsuario.getSelectionModel().select(ezatta.getUsuario());
        cbOperador.getSelectionModel().select(ezatta.getOperador());
    }

    private void popularDados() {
        dadosMovimentacoes.clear();
        dadosProduto.clear();
        dadosOperador.clear();
        dadosBico.clear();
        dadosUsuario.clear();

        cbProduto.getItems().clear();
        cbOperador.getItems().clear();
        cbBico.getItems().clear();
        cbUsuario.getItems().clear();

        tb.getItems().clear();
        try {
            dadosMovimentacoes.addAll(movimentacaoCtr.getAllEstoque());
            dadosBico.addAll(bicoCtr.getAllBico());
            dadosOperador.addAll(operadorCtr.getAllOperador());
            dadosProduto.addAll(produtoCtr.getAllProduto());
            dadosUsuario.addAll(usuarioCtr.getAllUsers());
        } catch (Exception e) {
            new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
        }
        
        for (EzattaMovimentacoes dadosMovimentacoe : dadosMovimentacoes) {
            System.out.println("dadosMovimentacoe: "+dadosMovimentacoe);
        }
        tb.setItems(dadosMovimentacoes);
        cbProduto.setItems(dadosProduto);
        cbBico.setItems(dadosBico);
        cbOperador.setItems(dadosOperador);
        cbUsuario.setItems(dadosUsuario);
    }

    public int getOperacao() {
        return Operacao;
    }

    public void setOperacao(int operacao) {
        Operacao = operacao;
    }

    public EzattaMovimentacoes getEzattaMovimentacao() {
        return ezattaMovimentacao;
    }

    public void setEzattaMovimentacao(EzattaMovimentacoes ezattaMovimentacao) {
        this.ezattaMovimentacao = ezattaMovimentacao;
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
