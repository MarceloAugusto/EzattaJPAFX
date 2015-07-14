/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.teste;


import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.util.JPAUtil;
import br.com.ezatta.util.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author marcelo
 */
public class Teste {

    public static void main(String[] args) {

        System.out.println("Dir: " + getConf());
        Scanner sc = new Scanner(System.in);

        for (int i = 0; i < 10; i++) {
            System.out.println("Informe um nome da Empresa: ");
            String nome = sc.nextLine();
            EntityManager em = new JPAUtil().getEntityManager();
            em.getTransaction().begin();

            EzattaEmpresa p = new EzattaEmpresa();
            p.setNome(nome);
            em.persist(p);
            em.getTransaction().commit();
            JPAUtil.closeManager(em);
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

    public static Map getConf() {

        Map prop = new HashMap();

        String rais = Path.workingDir + "\\Base\\data";
        prop.put("hibernate.connection.url", "jdbc:h2:" + rais);

        return prop;
    }

}
