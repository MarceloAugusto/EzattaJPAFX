/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import static br.com.ezatta.controller.LoginController.defaultPort;
import static br.com.ezatta.controller.LoginController.serialPort;
import br.com.ezatta.util.JPAUtil;
import br.com.ezatta.view.FXDialog;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class DGPrincipalController implements Initializable {

    //-----------------------------------dgPrincipal
    @FXML
    private AnchorPane dgUltimosRegistros;

    @FXML
    private AnchorPane dgRelatorioProduto;

    @FXML
    private StackPane stack;

    PrincipalController p = PrincipalController.principal;
    //private StackPane stack = PrincipalController.stack;

    @FXML
    void dgRelatorio(ActionEvent event) {

        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/Relatorio.fxml"));
            p.setStack(stack);
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

    @FXML
    void dgCalibrar(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/FatorEscala.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

    @FXML
    void dgAbastecer(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/EncherTanque.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

    @FXML
    void dgBackup(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/BackUp.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de backup");
            e.printStackTrace();
        }
    }

    @FXML
    void dgChamado(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/AjudaChamado.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "É necessário estar conectado a internet \nTentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

    @FXML
    void dgContatos(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/AjudaSuporte.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

    @FXML
    void dgFechar(ActionEvent event) {
        //fechar conex'ao H2DB
        System.out.println("Fechou H2DB");
        JPAUtil.closeManager(JPAUtil.getEntityManager());

        //fecha aporta
        System.out.println("port " + defaultPort + " not found.");
        serialPort.close();
        System.out.println("Porta fechada...");

        Platform.exit();
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    void dgMovimentacoes(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/Movimentacoes_Estoque.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

    @FXML
    void dgHistorico(ActionEvent event) {
        try {
            stack.getChildren().clear();
            stack.getChildren().add(getNode("/br/com/ezatta/view/EnvaseMovimentacao.fxml"));
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, "Tentar novamente").showDialog();
            System.out.println("Erro ao carregar a tela de bicos");
            e.printStackTrace();
        }
    }

}
