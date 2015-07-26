/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class AjudaSuporteController implements Initializable {

    @FXML
    private Tab tabConsulta;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private ComboBox<?> cbCor;

    @FXML
    private TextField txtNome;

    @FXML
    private Tab tabCadastro;

    @FXML
    private TextField txtMinimo;

    @FXML
    private TextField qtdRedefinir;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnRedefinirConsulta;

    @FXML
    private TableView<?> tb;

    @FXML
    private TextField txtId;

    @FXML
    private Button btnImprimirRegistros;

    @FXML
    private RadioButton rbNome;

    @FXML
    private ToggleGroup buscarPor;

    @FXML
    private ComboBox<?> cbEmpresa;

    @FXML
    private TabPane tabTela;

    @FXML
    private Button btnBuscar;

    @FXML
    private TextField txtEstoqueMaximo;

    @FXML
    void btnBuscar(ActionEvent event) {

    }

    @FXML
    void btnImprimir(ActionEvent event) {

    }

    @FXML
    void redefinirConsulta(ActionEvent event) {

    }

    @FXML
    void adicionarTanque(ActionEvent event) {

    }

    @FXML
    void removerTanque(ActionEvent event) {

    }

    @FXML
    void vazioTanque(ActionEvent event) {

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
