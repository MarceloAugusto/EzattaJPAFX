/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import br.com.ezatta.dao.BicosDAO;
import br.com.ezatta.dao.OperadorDAO;
import br.com.ezatta.dao.ProdutoDAO;
import br.com.ezatta.dao.UsuarioDAO;
import br.com.ezatta.model.EzattaBico;
import br.com.ezatta.model.EzattaOperador;
import br.com.ezatta.model.EzattaProduto;
import br.com.ezatta.model.EzattaUsuario;
import br.com.ezatta.relatorios.Relatorio;
import br.com.ezatta.view.FXDialog;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class RelatorioController implements Initializable {

    private ObservableList<EzattaProduto> dadosProduto = FXCollections.observableArrayList();
    private ObservableList<EzattaBico> dadosBico = FXCollections.observableArrayList();
    private ObservableList<EzattaOperador> dadosOperador = FXCollections.observableArrayList();
    private ObservableList<EzattaUsuario> dadosUsuario = FXCollections.observableArrayList();

    private ProdutoDAO produtoCtr = new ProdutoDAO();
    private BicosDAO bicoCtr = new BicosDAO();
    private OperadorDAO operadorCtr = new OperadorDAO();
    private UsuarioDAO usuarioCtr = new UsuarioDAO();

    private Relatorio relatorio = new Relatorio();

    @FXML
    private ComboBox<EzattaProduto> cbProdutoR;

    @FXML
    private ComboBox<EzattaBico> cbBicoR;

    @FXML
    private ComboBox<EzattaOperador> cbAplicadorR;

    @FXML
    private ComboBox<EzattaUsuario> cbUsuarioR;

    //-----------------inicio componentes dts
    @FXML
    private DatePicker dtiBico;

    @FXML
    private DatePicker dtiPlaca;

    @FXML
    private DatePicker dtiData;

    @FXML
    private DatePicker dtiUsuario;

    @FXML
    private DatePicker dtfOperador;

    @FXML
    private DatePicker dtfUsuario;

    @FXML
    private DatePicker dtfProduto;

    @FXML
    private DatePicker dtiLog;

    @FXML
    private DatePicker dtiProduto;

    @FXML
    private DatePicker dtfBico;

    @FXML
    private DatePicker dtfLog;

    @FXML
    private DatePicker dtfPlaca;

    @FXML
    private DatePicker dtfData;

    @FXML
    private DatePicker dtiOperador;

    @FXML
    private TextField txtPlacaVeiculo;
    //------------------fim componentes dts

    String hora = " 00:00:00";
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @FXML
    void relatorioData(ActionEvent event) throws SQLException, ParseException {
        String data1 = dtiData.getValue().toString();
        String data2 = dtfData.getValue().toString();
        java.sql.Timestamp datafim1 = getDataFormatada(data1);
        java.sql.Timestamp datafim2 = getDataFormatada(data2);
        relatorio.RelatorioData(datafim1, datafim2);
    }

    @FXML
    void relatorioProduto(ActionEvent event) throws ParseException, SQLException {
        String data1 = dtiProduto.getValue().toString();
        String data2 = dtfProduto.getValue().toString();
        java.sql.Timestamp datafim1 = getDataFormatada(data1);
        java.sql.Timestamp datafim2 = getDataFormatada(data2);
        EzattaProduto produto = cbProdutoR.getSelectionModel().getSelectedItem();
        relatorio.RelatorioDataProduto(datafim1, datafim2, produto.getId());
    }

    @FXML
    void relatorioBico(ActionEvent event) throws SQLException, ParseException {
        String data1 = dtiBico.getValue().toString();
        String data2 = dtfBico.getValue().toString();
        java.sql.Timestamp datafim1 = getDataFormatada(data1);
        java.sql.Timestamp datafim2 = getDataFormatada(data2);
        EzattaBico bico = cbBicoR.getSelectionModel().getSelectedItem();
        relatorio.RelatorioDataBico(datafim1, datafim2, bico.getId());
    }

    @FXML
    void relatorioAplicador(ActionEvent event) throws SQLException, ParseException {
        String data1 = dtiOperador.getValue().toString();
        String data2 = dtfOperador.getValue().toString();
        java.sql.Timestamp datafim1 = getDataFormatada(data1);
        java.sql.Timestamp datafim2 = getDataFormatada(data2);
        EzattaOperador operador = cbAplicadorR.getSelectionModel().getSelectedItem();
        relatorio.RelatorioDataOperador(datafim1, datafim2, operador.getId());
    }

    @FXML
    void relatoriousuario(ActionEvent event) throws SQLException, ParseException {
        String data1 = dtiUsuario.getValue().toString();
        String data2 = dtfUsuario.getValue().toString();
        java.sql.Timestamp datafim1 = getDataFormatada(data1);
        java.sql.Timestamp datafim2 = getDataFormatada(data2);
        EzattaUsuario usuario = cbUsuarioR.getSelectionModel().getSelectedItem();
        relatorio.RelatorioDataUsuario(datafim1, datafim2, usuario.getId());
    }

    @FXML
    void relatorioPlaca(ActionEvent event) throws SQLException, ParseException {
        String data1 = dtiPlaca.getValue().toString();
        String data2 = dtfPlaca.getValue().toString();
        java.sql.Timestamp datafim1 = getDataFormatada(data1);
        java.sql.Timestamp datafim2 = getDataFormatada(data2);
        String placa = txtPlacaVeiculo.getText();
        relatorio.RelatorioDataPlaca(datafim1, datafim2, placa);
        System.out.println("datafim1: "+datafim1+" - datafim2: "+datafim2);
        System.out.println("placa: "+placa);
    }

    @FXML
    void relatorioLog(ActionEvent event) {

    }

    @FXML
    void relatorioGeral(ActionEvent event) throws SQLException {
        relatorio.gerar();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        popularComboBox();

        //instanciar dti com data de hoje
        dtiData.setValue(LocalDate.now());
        dtiProduto.setValue(LocalDate.now());
        dtiBico.setValue(LocalDate.now());
        dtiOperador.setValue(LocalDate.now());
        dtiPlaca.setValue(LocalDate.now());
        dtiUsuario.setValue(LocalDate.now());
        dtiLog.setValue(LocalDate.now());

        dtfData.setValue(LocalDate.now());
        dtfProduto.setValue(LocalDate.now());
        dtfBico.setValue(LocalDate.now());
        dtfOperador.setValue(LocalDate.now());
        dtfPlaca.setValue(LocalDate.now());
        dtfUsuario.setValue(LocalDate.now());
        dtfLog.setValue(LocalDate.now());
    }

    private void popularComboBox() {
        dadosProduto.clear();
        dadosBico.clear();
        dadosOperador.clear();
        dadosUsuario.clear();

        cbProdutoR.getItems().clear();
        cbBicoR.getItems().clear();
        cbAplicadorR.getItems().clear();
        cbUsuarioR.getItems().clear();

        try {
            dadosProduto.addAll(produtoCtr.getAllProduto());
            dadosBico.addAll(bicoCtr.getAllBico());
            dadosOperador.addAll(operadorCtr.getAllOperador());
            dadosUsuario.addAll(usuarioCtr.getAllUsers());
        } catch (Exception e) {
            new FXDialog(FXDialog.Type.ERROR, e.getCause().getMessage()).showDialog();
        }
        cbProdutoR.setItems(dadosProduto);
        cbBicoR.setItems(dadosBico);
        cbAplicadorR.setItems(dadosOperador);
        cbUsuarioR.setItems(dadosUsuario);
    }

    private Timestamp getDataFormatada(String data1) throws ParseException {
        data1 = data1.concat(hora);
        java.util.Date parsedDate1 = fmt.parse(data1);
        java.sql.Timestamp datafim1 = new java.sql.Timestamp(parsedDate1.getTime());
        return datafim1;
    }

}
