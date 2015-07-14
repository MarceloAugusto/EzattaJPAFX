/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.util;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Curso
 */
public class JPAUtil {

    private static EntityManagerFactory emf = null;

    public static void closeManager(EntityManager manager) {
        try {
            manager.close();
        } catch (Exception ex) {
            System.err.println("Erro: " + ex.getMessage());
        }
    }

    public static EntityManager getEntityManager() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("TesteH2DBJPAPU2", getConf());
        }
        return emf.createEntityManager();
    }

    public static Map getConf() {
        Map prop = new HashMap();
        String rais = Path.workingDir + "\\Base\\data";
        prop.put("hibernate.connection.url", "jdbc:h2:" + rais);
        return prop;
    }
}
