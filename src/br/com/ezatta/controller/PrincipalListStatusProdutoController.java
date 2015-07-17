/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import br.com.ezatta.dao.ProdutoDAO;
import br.com.ezatta.model.EzattaProduto;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class PrincipalListStatusProdutoController implements Initializable {

    private HBox[] hbProduto = new HBox[10000];
    private Label[] strContainer = new Label[10000];
    private Integer idProduto = 0;

    @FXML
    private VBox listaProdutoStatus;

    @FXML
    private BorderPane borderPanePrincipal;

    @FXML
    void atualizarLitaStatusProduto(ActionEvent event) {
        listaProdutoStatus.getChildren().clear();
        hbProduto[idProduto].getChildren().clear();
        popularProduto();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popularProduto();
    }

    private void popularProduto() {
        try {
            ObservableList<EzattaProduto> dadosProdutosPrincipal = FXCollections.observableArrayList();
            dadosProdutosPrincipal.clear();
            ProdutoDAO produtoCtr = new ProdutoDAO();
            dadosProdutosPrincipal.addAll(produtoCtr.getAllProduto());
            
            for (EzattaProduto dado : dadosProdutosPrincipal) {
                adicionaLista(dado);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adicionaLista(EzattaProduto dado) {
        idProduto = dado.getId();
        hbProduto[idProduto] = new HBox();
        strContainer[idProduto] = new Label();
        hbProduto[idProduto].setSpacing(5.0);
        hbProduto[idProduto].setPadding(new Insets(10, 10, 10, 10));
        Separator separatorProduto = new Separator();
        separatorProduto.setMinHeight(5);
        
        //calcular porcentagem container
        Float qtdTotal = dado.getEstoqueMaximo().floatValue();
        Float qtdAtual = dado.getQuantidade().floatValue();
        Float porcent = (qtdAtual / qtdTotal) * 100;

        if (porcent >= 60) {
            strContainer[idProduto].setStyle("-fx-font-style: italic;");
            strContainer[idProduto].setStyle("-fx-font-size: 9px;");
            strContainer[idProduto].setStyle("-fx-text-fill: darkgreen;");
        } else if (porcent >= 40) {
            strContainer[idProduto].setStyle("-fx-font-style: italic;");
            strContainer[idProduto].setStyle("-fx-font-size: 9px;");
            strContainer[idProduto].setStyle("-fx-text-fill: darkorange;");
        } else {
            strContainer[idProduto].setStyle("-fx-font-style: italic;");
            strContainer[idProduto].setStyle("-fx-font-size: 9px;");
            strContainer[idProduto].setStyle("-fx-text-fill: darkred;");
        }

        //montando string container
        strContainer[idProduto].setText(dado.getNome() + "\t Qtd.: " + dado.getQuantidade() + "\t  %" + Math.round(porcent));

        //adiciona StringContainer[idProduto]
        hbProduto[idProduto].getChildren().addAll(strContainer[idProduto]);

        //adiciona hbProduto ao VbLista
        listaProdutoStatus.getChildren().add(hbProduto[idProduto]);
    }

}
