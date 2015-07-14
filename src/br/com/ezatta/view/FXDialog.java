/**
 * 
 */
package br.com.ezatta.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class FXDialog extends Stage{	
	
	public enum Type {ERROR, WARNING, INFO, CONFIRM };
	
	private Type type;	
	private HBox buttonBox = new HBox();	
	private BorderPane pane = new BorderPane();
	private ImageView icon = new ImageView();
	private Scene scene;
	private Label message = new Label();
	private boolean retornoConfirm;
	
	/**
	 * M�todo para visualizar a Dialog
	 * @param type
	 * @param mensagem
	 */
	public FXDialog(Type type, String mensagem) {
		setType(type);
		initStyle(StageStyle.UTILITY);
		initModality(Modality.APPLICATION_MODAL);
		setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2);
		setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight() + mensagem.length());
		message.setText(mensagem);
	}
	
	public boolean showDialog(){
		setResizable(false);
		loadStage();		
        sizeToScene();
        centerOnScreen();
        showAndWait();
		return isRetornoConfirm();
	}
	
	private void setType(Type type) {
		this.type = type;
	}
	
	private void loadStage() {
		switch (type) {
		case ERROR:
			setTitle("Erro");
			addOKButton();
			break;
		case INFO:
			setTitle("Informação");
			addOKButton();
			break;
		case CONFIRM:
			setTitle("Confirma?");					
			addSimButton();
			addNaoButton();
			buttonBox.prefWidthProperty().bind(new SimpleDoubleProperty(320));		
			break;
		case WARNING:
			addOKButton();
			setTitle("Aviso");
			break;
		default:
			addOKButton();
			setTitle("Informação");
			break;
		}
		
		BorderPane.setAlignment(icon, Pos.CENTER);
		BorderPane.setMargin(icon, new Insets(20, 2, 2, 25));
		pane.setLeft(icon);

		BorderPane.setAlignment(message, Pos.CENTER_LEFT);
		BorderPane.setMargin(message, new Insets(20, 25, 2, 2));
		
		pane.setCenter(message);
		scene = new Scene(pane);
		setScene(scene);
	}
	 
	private void addOKButton() {
		Button btn = new Button("OK");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				close();				
			}
			
		});
		
		pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER){
					close();
					setRetornoConfirm(true);
				}
				if(event.getCode() == KeyCode.ESCAPE){
					close();
					setRetornoConfirm(true);
				}
			}
		});
		
		buttonBox.getChildren().add(marginButton(btn));
		configButtonBox();
	}
	
	private void addSimButton() {
		Button btn = new Button("Sim");
		btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent evt) {            	
            		setRetornoConfirm(true);
            		close();
                }
            });       
        buttonBox.getChildren().add(marginButton(btn));
        configButtonBox();
	}
	
	private void addNaoButton() {
		Button btn = new Button("N�o");
		btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent evt) {            	
            		setRetornoConfirm(false);
            		close();
                }
            });
        buttonBox.getChildren().add(marginButton(btn));
        configButtonBox();
	}
	 
	private void configButtonBox() {
		buttonBox.setAlignment(Pos.BASELINE_RIGHT);
		buttonBox.setStyle("-fx-background-color: #f0f0f0");
		buttonBox.setPadding(new Insets(0, 20, 2, 2));		
		buttonBox.widthProperty().add(Screen.getPrimary().getVisualBounds().getWidth() / 2);
		buttonBox.setPrefHeight(50);			
		pane.setBottom(buttonBox);
	}
	
	private HBox marginButton(Button btn){
		btn.getStyleClass().add("btnWindows7");
		HBox hButton = new HBox();        
        hButton.setPadding(new Insets(15, 2, 15, 2));
        hButton.setSpacing(10);        
        hButton.getChildren().addAll(btn);
        hButton.setAlignment(Pos.BASELINE_RIGHT);
        return hButton;
	}
    
	public boolean isRetornoConfirm() {
		return retornoConfirm;
	}
	private void setRetornoConfirm(boolean retornoConfirm) {
		this.retornoConfirm = retornoConfirm;
	}
	
	public static void notImp(){
		new FXDialog(Type.INFO, "Funcionalidade não implementada!").showDialog();
	}
}