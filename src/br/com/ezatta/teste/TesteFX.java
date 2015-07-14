/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.teste;

import br.com.ezatta.dao.JpaController;
import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.util.JPAUtil;
import java.util.List;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * @author marcelo
 */
public class TesteFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Clique aki'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                System.out.println("Iniciou...");
                Scanner sc = new Scanner(System.in);

                for (int i = 0; i < 10; i++) {
                    System.out.println("Informe um nome da Empresa: ");
                    String nome = sc.nextLine();
                    EzattaEmpresa p = new EzattaEmpresa();
                    p.setNome(nome);
                    JpaController<EzattaEmpresa> jpaEmpresa = new JpaController<>();
                    jpaEmpresa.create(p);
                }

//        //verificar native query
                EntityManager em = new JPAUtil().getEntityManager();
                em.getTransaction().begin();
                TypedQuery<EzattaEmpresa> query = em.createNamedQuery("EzattaEmpresa.findAll", EzattaEmpresa.class);
                List<EzattaEmpresa> ps = query.getResultList();
                for (EzattaEmpresa p1 : ps) {
                    System.out.println("Nome: " + p1.getNome());
                }
                JPAUtil.closeManager(em);

                System.out.println("Finalizou");

            }

        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
