/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import br.com.ezatta.chamado.dao.ChaChamadoDAO;
import br.com.ezatta.chamado.dao.ChaInteracoesDAO;
import br.com.ezatta.chamado.model.ChaChamado;
import br.com.ezatta.chamado.model.ChaInteracoes;
import br.com.ezatta.chamado.model.Status;
import static br.com.ezatta.controller.LoginController.ezattaEmpresaStatic;
import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.util.GenericTable;
import br.com.ezatta.util.JPAUtilChamado;
import br.com.ezatta.view.FXDialog;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javax.persistence.EntityManager;

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class AjudaChamadoController implements Initializable {

    private ObservableList<ChaChamado> dados = FXCollections.observableArrayList();
    private ObservableList<ChaInteracoes> dadosInteracoes = FXCollections.observableArrayList();
    private int Operacao;
    private ChaChamadoDAO chamadoCtr = new ChaChamadoDAO();
    private ChaInteracoesDAO interacoesCtr = new ChaInteracoesDAO();
    private ChaChamado ezattaChamado;

    @FXML
    private Tab tabConsulta;

    @FXML
    private ImageView btnNovoFrontal;

    @FXML
    private HTMLEditor txtTexto;

    @FXML
    private AnchorPane selecionar;

    @FXML
    private AnchorPane cadastrar;

    @FXML
    private AnchorPane AnchorRespostas;

    @FXML
    private Button btnNovoF;

    @FXML
    private Tab tabCadastro;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnRedefinirConsulta;

    @FXML
    private TableView<ChaChamado> tb;

    @FXML
    private Button btnExcluirF;

    @FXML
    private Button btnImprimirRegistros;

    @FXML
    private Button btnIncluir;

    @FXML
    private ToggleGroup buscarPor;

    @FXML
    private TabPane tabTela;

    @FXML
    private Button btnBuscar;

    @FXML
    private TextField txtTitulo;

    @FXML
    private VBox vboxRespostas;

    @FXML
    void btnBuscar(ActionEvent event) throws SQLException {
        if (isValidConsulta()) {
            dados.clear();
            dados.addAll(chamadoCtr.findAllChaChamado("select c.* from cha_chamado c", " where c.titulo like  '%" + txtBuscar.getText() + "%'"));
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

    @FXML
    void btnNovoFrontal(ActionEvent event) {
        setOperacao(0);

        cadastrar.setVisible(true);
        selecionar.setVisible(false);

        tabTela.getSelectionModel().select(1);
        txtTitulo.setText("");
        txtTitulo.requestFocus();
        txtTexto.setHtmlText("");
    }

    @FXML
    private Button btnNovaMensagem;

    @FXML
    void btnExcluirFrontal(ActionEvent event) throws SQLException {
        if (tb.getSelectionModel().isEmpty()) {
            new FXDialog(FXDialog.Type.ERROR, "Favor selecionar o item primeiro... ").showDialog();
        } else {
            boolean ok = new FXDialog(FXDialog.Type.CONFIRM, "Tem Certeza que deseja encerrar esse chamado?").showDialog();
            if (ok) {
                ChaChamado cha = getEzattaChamado();
                cha.setStatus(Status.FECHADO);
                chamadoCtr.updateChaChamado(cha);
                ok = true;
                if (ok) {
                    new FXDialog(FXDialog.Type.INFO, "Operaçao realizada com sucesso!").showDialog();
                    popularDados();
                } else {
                    new FXDialog(FXDialog.Type.WARNING, "Existem relaçoes com outras entidades!").showDialog();
                }
            }
        }
    }

    @FXML
    void btnSalvar(ActionEvent event) throws SQLException {
        if (isValidaTela()) {

            Timestamp dataDeHoje = new Timestamp(System.currentTimeMillis());
            switch (getOperacao()) {
                case 0:
                    ChaChamado chamado = new ChaChamado(txtTitulo.getText(), Status.ABERTO, ezattaEmpresaStatic.getId(), dataDeHoje);
                    chamadoCtr.addChaChamado(chamado);

                    ChaInteracoes interacoes = new ChaInteracoes();
                    interacoes.setTexto(txtTexto.getHtmlText());
                    interacoes.setData(dataDeHoje);
                    interacoes.setAdm(0);
                    interacoes.setIdChamado(chamado);

                    interacoesCtr.addChaInteracoes(interacoes);

                    popularDados();
                    new FXDialog(FXDialog.Type.INFO, "Registro inserido com sucesso!").showDialog();
                    tabTela.getSelectionModel().select(0);
                    break;
                case 1:
                    ChaChamado ch = getEzattaChamado();
                    ch.setStatus(Status.PROCESSO);
                    chamadoCtr.updateChaChamado(ch);

                    ChaInteracoes interacoess = new ChaInteracoes();
                    interacoess.setTexto(txtTexto.getHtmlText());
                    interacoess.setData(dataDeHoje);
                    interacoess.setAdm(0);
                    interacoess.setIdChamado(ch);

                    interacoesCtr.addChaInteracoes(interacoess);

                    popularDados();
                    new FXDialog(FXDialog.Type.INFO, "Registro inserido com sucesso!").showDialog();
                    tabTela.getSelectionModel().select(0);

                    new FXDialog(FXDialog.Type.INFO, "Registro atualizado com sucesso!").showDialog();
                    tabTela.getSelectionModel().select(0);
                    popularDados();
                    break;
            }
        }
    }

    private boolean isValidaTela() {
        boolean ok = true;
        if (txtTitulo.getText().isEmpty()) {
            new FXDialog(FXDialog.Type.WARNING, "Favor preencher o nome!").showDialog();
            txtTitulo.requestFocus();
            ok = false;
        } else if (txtTexto.getHtmlText().isEmpty()) {
            new FXDialog(FXDialog.Type.WARNING, "Favor preencher o endereço físico!").showDialog();
            txtTexto.requestFocus();
            ok = false;
        }
        return ok;
    }

    @FXML
    void cancelar(ActionEvent event) {
        popularDados();
        tabTela.getSelectionModel().select(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        EntityManager managerMysql = JPAUtilChamado.getEntityManager();
        managerMysql.clear();

        tb.getColumns().addAll(new GenericTable<ChaChamado>().tableColunas(ChaChamado.class));
        popularDados();
        tb.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    //limpa interacoes
                    AnchorRespostas.getChildren().clear();
                    
                    tabTela.getSelectionModel().select(1);
                    setOperacao(1);

                    cadastrar.setVisible(false);
                    selecionar.setVisible(true);

                    setEzattaChamado(tb.getSelectionModel().getSelectedItem());
                    SetValoresComponentes(getEzattaChamado());

                    popularInteracoes();

                    if (getEzattaChamado().getStatus() == Status.FECHADO) {
                        btnNovaMensagem.setVisible(false);
                    } else {
                        btnNovaMensagem.setVisible(true);
                    }
                    
                   
                    
                }

                if (event.getClickCount() > 0) {
                    try {
                        setOperacao(1);
                        setEzattaChamado(tb.getSelectionModel().getSelectedItem());
                        SetValoresComponentes(getEzattaChamado());

                        if (getEzattaChamado().getStatus() == Status.FECHADO) {
                            btnNovaMensagem.setVisible(false);
                        } else {
                            btnNovaMensagem.setVisible(true);
                        }
                    } catch (NullPointerException e) {

                    }
                }
            }

            private VBox[] vboxInteracoes = new VBox[10000];
            private Label[] dataHora = new Label[10000];
            private WebView[] txInteracoes = new WebView[10000];
            ScrollPane sp = new ScrollPane();
            private AnchorPane[] AnchorTopicos = new AnchorPane[10000];

            private void popularInteracoes() {

                VBox vbLista = new VBox();
                AnchorRespostas.getChildren().add(vbLista);

                ChaInteracoesDAO interacoesdao = new ChaInteracoesDAO();
                List<ChaInteracoes> listInteracao = interacoesdao.getChaInteracoesByChamada(getEzattaChamado());

                for (ChaInteracoes listInteracao1 : listInteracao) {

                    System.out.println("list: " + listInteracao1.toString());
                    System.out.println("Adm: " + listInteracao1.getAdm());

                    int idInteracao = listInteracao1.getId();
                    sp.setContent(vboxInteracoes[idInteracao]);

                    String adm = "";

                    int admInteracao = 0;
                    if (listInteracao1.getAdm() == null) {
                        admInteracao = 0;
                    }
                    if (listInteracao1.getAdm() == 0) {
                        admInteracao = 0;
                    }
                    if (listInteracao1.getAdm() == 1) {
                        admInteracao = 1;
                    }
                    //---------------------------------
                    String cssDefault = "-fx-background-color: lightgray;";
                    //--------------------------------
                    if (admInteracao == 1) {
                        adm = " - SUPORTE EZATTA";
                        System.out.println("Entrou");
                        // vboxInteracoes[idInteracao].setStyle("-fx-background-color: lightgray");
                    }
                    if (admInteracao == 0) {
                        adm = " - " + ezattaEmpresaStatic.getNome();
                    }

                    dataHora[idInteracao] = new Label();
                    dataHora[idInteracao].setText("Data: " + listInteracao1.getData() + " " + adm);
                    vboxInteracoes[idInteracao] = new VBox();
                    vboxInteracoes[idInteracao].setMinWidth(selecionar.getWidth());
                    vboxInteracoes[idInteracao].setMaxWidth(selecionar.getWidth());
                    vboxInteracoes[idInteracao].setPadding(new Insets(10, 5, 10, 5));
                    vboxInteracoes[idInteracao].setMargin(sp, new Insets(20, 5, 20, 5));
                    vboxInteracoes[idInteracao].setMaxWidth(selecionar.getWidth());

                    txInteracoes[idInteracao] = new WebView();
                    txInteracoes[idInteracao].setMinHeight(100);
                    txInteracoes[idInteracao].setMaxHeight(100);
                    txInteracoes[idInteracao].setMinWidth(selecionar.getWidth());
                    txInteracoes[idInteracao].setMaxWidth(selecionar.getWidth());
                    txInteracoes[idInteracao].getEngine().loadContent(listInteracao1.getTexto());

                    vboxInteracoes[idInteracao].getChildren().addAll(dataHora[idInteracao], txInteracoes[idInteracao]);

                    vbLista.getChildren().add(vboxInteracoes[idInteracao]);

                }
            }

        });
    }

    private void popularDados() {
        dados.clear();
        dados.clear();
        tb.getItems().clear();
        try {

            EzattaEmpresa emp = ezattaEmpresaStatic;
            dados.addAll(chamadoCtr.getAllByEmpresa(emp));

        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Favor verifique sua conexão com a internet").showDialog();
        }
        tb.setItems(dados);
    }

    public int getOperacao() {
        return Operacao;
    }

    public void setOperacao(int operacao) {
        Operacao = operacao;
    }

    public ChaChamado getEzattaChamado() {
        return ezattaChamado;
    }

    public void setEzattaChamado(ChaChamado ezattaChamado) {
        this.ezattaChamado = ezattaChamado;
    }

    private void SetValoresComponentes(ChaChamado ezatta) {
        //txtId.setText(ezatta.getId().toString());
        txtTitulo.setText(ezatta.getTitulo());
    }

    private boolean isValidConsulta() {
        boolean ok = true;
        if (ok) {
            ok = !txtBuscar.getText().isEmpty();
        }
        return ok;
    }

    @FXML
    void novaMensagem(ActionEvent event) {

        setOperacao(1);

        cadastrar.setVisible(true);
        selecionar.setVisible(false);

        txtTexto.setHtmlText("");

        setEzattaChamado(tb.getSelectionModel().getSelectedItem());
        SetValoresComponentes(getEzattaChamado());
    }
}
