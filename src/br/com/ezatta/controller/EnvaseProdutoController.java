/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import br.com.ezatta.dao.ProdutoDAO;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.view.FXDialog;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import static br.com.ezatta.controller.LoginController.ezattaProdutoStatic;
import static br.com.ezatta.controller.PrincipalController.principal;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class EnvaseProdutoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    private int Operacao;
    private ObservableList<EzattaProduto> dados = FXCollections.observableArrayList();
    private ProdutoDAO ProdutoCtr = new ProdutoDAO();
    private EzattaProduto ezattaProduto;

    @FXML
    public StackPane stackPane;

    @FXML
    private ListView<EzattaProduto> lvProdutos;

    @FXML
    private StackPane stackPanePrincipal;

    @FXML
    void listaProdutos(ActionEvent event) {
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //tbEmpresa.getColumns().addAll(new GenericTable<EzattaEmpresa>().tableColunas(EzattaEmpresa.class));
        popularDados();
        lvProdutos.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                //inicio
                try {
                    ezattaProdutoStatic = lvProdutos.getSelectionModel().getSelectedItem();
                    System.out.println("ezattaProdutoStatic: " + ezattaProdutoStatic.getNome() + " - " + ezattaProdutoStatic.getId());

                    stackPanePrincipal.getChildren().clear();
                    stackPanePrincipal.getChildren().add(getNode("/br/com/ezatta/view/EnvaseBico.fxml"));
                    principal.setStack(stackPanePrincipal);

                } catch (Exception ex) {
                    //new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
                }

                //new Thread(taskLeituraEnvase).start();
                //abre janela fx funcionando
//                Stage telaPrincipal = new Stage();
//                try {
//                    new FormFX<PrincipalController>("EnvaseBico.fxml", telaPrincipal, "Ezatta Inteligent Oil Supply", false);
//                } catch (Exception e) {
//                    new FXDialog(FXDialog.Type.ERROR, "Erro ao carregar a tela -> Principal").showDialog();
//                }
                //fim
                Thread threadAtualizaTitledPanel = new Thread(taskAtualizaTela);
                threadAtualizaTitledPanel.start();
            }
        });

    }

    Task taskAtualizaTela = new Task() {
        @Override
        public void run() {

            System.out.println("iniciou thread");
            //vbList.setOnMouseMoved(new EventHandler<MouseEvent>() {
            stackPanePrincipal.setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    principal.vbList.getChildren().clear();
                    principal.popularDadosListaEnvase();
                    System.out.println("Executou");

                }
            });

        }

        @Override
        protected Object call() {
            return null;
        }
    };

    public void limpar() {
        try {
            stackPane.getChildren().clear();
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            e.printStackTrace();
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

    private void popularDados() {
        dados.clear();
        lvProdutos.getItems().clear();

        try {
            dados.addAll(ProdutoCtr.getAllProduto());
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, e.getCause().getMessage()).showDialog();
        }
        lvProdutos.setItems(dados);
    }

    public int getOperacao() {
        return Operacao;
    }

    public void setOperacao(int operacao) {
        Operacao = operacao;
    }

}
