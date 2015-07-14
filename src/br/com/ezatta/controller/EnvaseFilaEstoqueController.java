/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import static com.lowagie.text.pdf.PdfFileSpecification.url;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class EnvaseFilaEstoqueController implements Initializable {

//public static EnvaseFilaEstoqueController principalFila;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 
//        txtEfetivo.setText(variavel);
        System.out.println("iniciou");
    }
    
    public String variavel;

    @FXML
    public TextField txtEfetivo;

    public EnvaseFilaEstoqueController() {
    }

    public TextField getTxtEfetivo() {
        return txtEfetivo;
    }

    public void setTxtEfetivo(TextField txtEfetivo) {
        this.txtEfetivo = txtEfetivo;
    }

    public void setTxtEfetivo(String str){
        variavel = str;
        txtEfetivo.setText("novooooo");
        System.out.println("variavel: "+variavel);
        
        //EnvaseFilaEstoqueController envaseFilaEstoqueController = new EnvaseFilaEstoqueController();
    }
    
}
