package br.com.ezatta.relatorios;

import br.com.ezatta.controller.RelatorioController;
import br.com.ezatta.util.JPAUtil;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

public class Relatorio {

    private JasperPrint rel = null;

    public Relatorio() {
        
    }

    public void gerar() throws SQLException {
        String path = JPAUtil.getConfRelatorio();
        System.out.println("End relat: "+path);
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
                    Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public JasperPrint RelatorioData(java.sql.Timestamp data1, java.sql.Timestamp data2) throws SQLException {
        String path = JPAUtil.getConfRelatorio();
        EntityManager em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        Session hibernateSession = em.unwrap(Session.class);

        hibernateSession.doWork(new org.hibernate.jdbc.Work() {
            @Override
            public void execute(Connection con) throws SQLException {
                try {
                    HashMap map = new HashMap();
                    map.put("DataInicio", data1);
                    map.put("DataFinal", data2);
                    rel = JasperFillManager.fillReport(path + "GeralDatas.jasper", map, con);
                    JasperViewer jrviewer = new JasperViewer(rel, false);
                    jrviewer.show();
                } catch (JRException ex) {
                    Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        return rel;
    }

    public JasperPrint RelatorioDataLog(java.sql.Timestamp data1, java.sql.Timestamp data2) throws SQLException {
        JasperPrint rel = null;
        try {
            EntityManager em = new JPAUtil().getEntityManager();
            Connection con = em.unwrap(java.sql.Connection.class);

            HashMap map = new HashMap();

            map.put("DataInicio", data1);
            map.put("DataFinal", data2);
            InputStream resource = getClass().getResourceAsStream("LogDatas.jasper");
            rel = JasperFillManager.fillReport(resource, map, con);

            System.out.println(data1.getTime());
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return rel;
    }

    public JasperPrint RelatorioDataProduto(java.sql.Timestamp data1, java.sql.Timestamp data2, int idProduto) throws SQLException {
        String path = JPAUtil.getConfRelatorio();
        EntityManager em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        Session hibernateSession = em.unwrap(Session.class);

        hibernateSession.doWork(new org.hibernate.jdbc.Work() {
            @Override
            public void execute(Connection con) throws SQLException {
                try {
                    HashMap map = new HashMap();
                    map.put("DataInicio", data1);
                    map.put("DataFinal", data2);
                    map.put("Produto", idProduto);
                    rel = JasperFillManager.fillReport(path + "GeralDatasProduto.jasper", map, con);
                    JasperViewer jrviewer = new JasperViewer(rel, false);
                    jrviewer.show();
                } catch (JRException ex) {
                    Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        return rel;
    }

    public JasperPrint RelatorioDataBico(Timestamp data1, Timestamp data2, Integer id) {
        String path = JPAUtil.getConfRelatorio();
        EntityManager em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        Session hibernateSession = em.unwrap(Session.class);

        hibernateSession.doWork(new org.hibernate.jdbc.Work() {
            @Override
            public void execute(Connection con) throws SQLException {
                try {
                    HashMap map = new HashMap();
                    map.put("DataInicio", data1);
                    map.put("DataFinal", data2);
                    map.put("Bico", id);
                    rel = JasperFillManager.fillReport(path + "GeralDatasBico.jasper", map, con);
                    JasperViewer jrviewer = new JasperViewer(rel, false);
                    jrviewer.show();
                } catch (JRException ex) {
                    Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        return rel;
    }

    public JasperPrint RelatorioDataOperador(Timestamp data1, Timestamp data2, Integer id) {
        String path = JPAUtil.getConfRelatorio();
        EntityManager em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        Session hibernateSession = em.unwrap(Session.class);

        hibernateSession.doWork(new org.hibernate.jdbc.Work() {
            @Override
            public void execute(Connection con) throws SQLException {
                try {
                    HashMap map = new HashMap();
                    map.put("DataInicio", data1);
                    map.put("DataFinal", data2);
                    map.put("Aplicador", id);
                    rel = JasperFillManager.fillReport(path + "GeralDatasAplicador.jasper", map, con);
                    JasperViewer jrviewer = new JasperViewer(rel, false);
                    jrviewer.show();
                } catch (JRException ex) {
                    Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        return rel;
    }

    public JasperPrint RelatorioDataUsuario(Timestamp datafim1, Timestamp datafim2, Integer id) {
        String path = JPAUtil.getConfRelatorio();
        EntityManager em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        Session hibernateSession = em.unwrap(Session.class);

        hibernateSession.doWork(new org.hibernate.jdbc.Work() {
            @Override
            public void execute(Connection con) throws SQLException {
                try {
                    HashMap map = new HashMap();
                    map.put("DataInicio", datafim1);
                    map.put("DataFinal", datafim2);
                    map.put("Empresa", id);
                    rel = JasperFillManager.fillReport(path + "GeralDatasEmpresa.jasper", map, con);
                    JasperViewer jrviewer = new JasperViewer(rel, false);
                    jrviewer.show();
                } catch (JRException ex) {
                    Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        return rel;
    }
    
    public JasperPrint RelatorioDataPlaca(Timestamp datafim1, Timestamp datafim2, String placa) {
        String path = JPAUtil.getConfRelatorio();
        EntityManager em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        Session hibernateSession = em.unwrap(Session.class);

        hibernateSession.doWork(new org.hibernate.jdbc.Work() {
            @Override
            public void execute(Connection con) throws SQLException {
                try {
                    HashMap map = new HashMap();
                    map.put("DataInicio", datafim1);
                    map.put("DataFinal", datafim2);
                    map.put("Placa", placa);
                    rel = JasperFillManager.fillReport(path + "GeralDatasPlaca.jasper", map, con);
                    JasperViewer jrviewer = new JasperViewer(rel, false);
                    jrviewer.show();
                } catch (JRException ex) {
                    Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        return rel;
    }

}
