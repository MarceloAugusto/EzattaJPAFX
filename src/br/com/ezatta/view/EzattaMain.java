/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.view;

import static br.com.ezatta.controller.LoginController.defaultPort;
import static br.com.ezatta.controller.LoginController.serialPort;
import br.com.ezatta.util.JPAUtil;
import br.com.ezattaloaderfx.EzattaLoaderFx;
import com.sun.javafx.application.LauncherImpl;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author marcelo
 */
public class EzattaMain extends Application {

    public static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        Image icon = new Image(getClass().getResourceAsStream("icone.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);

        stage.initStyle(StageStyle.UNDECORATED);
        //stage.setTitle("Ezatta");

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(final WindowEvent event) {
                System.out.println("Fechouuuuuuuuuuu");
            }
        });
            

        stage.show();
        EzattaMain.stage = stage;

        //stage
//        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            
//    @Override
//    public void handle(WindowEvent arg0) {
//        Platform.exit();
//    }
//    
//});
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        //fechar conexao hibernate
        JPAUtil.closeManager(JPAUtil.getEntityManager());
        Platform.exit();

        //fecha aporta
        System.out.println("port " + defaultPort + " not found.");
        serialPort.close();
        System.out.println("Porta fechada...");
        
        //fechar tudo
        Platform.exit();
        System.exit(0);

    }

    public void handle(WindowEvent event) {
        stop();
        Platform.exit();
        System.exit(0);
    }

}
