package br.com.ezatta.relatorios;

import br.com.ezatta.util.JPAUtil;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class Relatorio {

    public Relatorio() {
    }

    public JasperPrint gerar() throws SQLException {
        JasperPrint rel = null;
        try {
            EntityManager em = new JPAUtil().getEntityManager();
            Connection con = em.unwrap(java.sql.Connection.class);
            HashMap map = new HashMap();
            InputStream resource = getClass().getResourceAsStream("Geral.jasper");
            rel = JasperFillManager.fillReport(resource, map, con);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return rel;
    }

    public JasperPrint RelatorioData(java.sql.Timestamp data1, java.sql.Timestamp data2) throws SQLException {
        JasperPrint rel = null;
        try {
            EntityManager em = new JPAUtil().getEntityManager();
            Connection con = em.unwrap(java.sql.Connection.class);
            
            HashMap map = new HashMap();

            map.put("DataInicio", data1);
            map.put("DataFinal", data2);
            InputStream resource = getClass().getResourceAsStream("GeralDatas.jasper");
            rel = JasperFillManager.fillReport(resource, map, con);

            System.out.println(data1.getTime());
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
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
        JasperPrint rel = null;
        try {
            
            EntityManager em = new JPAUtil().getEntityManager();
            Connection con = em.unwrap(java.sql.Connection.class);
            
            HashMap map = new HashMap();

            map.put("DataInicio", data1);
            map.put("DataFinal", data2);
            map.put("Produto", idProduto);
            InputStream resource = getClass().getResourceAsStream("GeralDatasProduto.jasper");
            rel = JasperFillManager.fillReport(resource, map, con);

        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return rel;
    }

    public JasperPrint RelatorioDataBico(Timestamp datafim1, Timestamp datafim2, Integer id) {
        JasperPrint rel = null;
        try {
            
            EntityManager em = new JPAUtil().getEntityManager();
            Connection con = em.unwrap(java.sql.Connection.class);
            
            HashMap map = new HashMap();

            map.put("DataInicio", datafim1);
            map.put("DataFinal", datafim2);
            map.put("Produto", id);
            InputStream resource = getClass().getResourceAsStream("/br/com/devmedia/Relatorio/GeralDatasBico.jasper");
            rel = JasperFillManager.fillReport(resource, map, con);

            //System.out.println(data1.getTime());
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return rel;
    }

}
