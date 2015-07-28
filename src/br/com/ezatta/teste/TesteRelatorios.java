/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.teste;

import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.util.JPAUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

/**
 *
 * @author marcelo
 */
public class TesteRelatorios {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String path = JPAUtil.getConfRelatorio();

        JasperPrint relat;

            System.out.println("path: " + path);

            EntityManager em = new JPAUtil().getEntityManager();
            em.getTransaction().begin();

            Session hibernateSession = em.unwrap(Session.class);

            hibernateSession.doWork(new org.hibernate.jdbc.Work() {

                @Override
                public void execute(Connection con) throws SQLException {
                    try {
                        HashMap map = new HashMap();
                        JasperPrint rel = JasperFillManager.fillReport(path + "Geral.jasper", map, con);
                        JasperViewer jrviewer = new JasperViewer(rel, false);
                        jrviewer.show();
                    } catch (JRException ex) {
                        Logger.getLogger(TesteRelatorios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            });

            TypedQuery<EzattaEmpresa> query = em.createNamedQuery("EzattaEmpresa.findAll", EzattaEmpresa.class);
            List<EzattaEmpresa> ps = query.getResultList();
            for (EzattaEmpresa p1 : ps) {
                System.out.println("Nome: " + p1.getNome());
            }

       

    }

}
