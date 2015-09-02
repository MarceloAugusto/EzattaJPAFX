/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.dao;

import static br.com.ezatta.controller.LoginController.ezattaEmpresaStatic;
import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.model.EzattaLog;
import br.com.ezatta.util.JPAUtil;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Marcelo
 */
public class EmpresaDAO {

    private static final long serialVersionUID = 1L;

    EntityManager em = JPAUtil.getEntityManager();

    public EmpresaDAO() {
    }

    public void addEmpresa(EzattaEmpresa empresa) throws SQLException {
        
        em.getTransaction().begin();
        em.persist(empresa);
        em.getTransaction().commit();
        
       
    }

    public void removeEmpresa(EzattaEmpresa emp) throws SQLException {
        em.getTransaction().begin();
        EzattaEmpresa empresa = em.find(EzattaEmpresa.class, emp.getId());
        em.remove(empresa);
        em.getTransaction().commit();
        
        //log-------------------------------------------------------------------
//        String acao = "Remover empresa: "+empresa;
//        EzattaLog log = new EzattaLog();
//        Timestamp data = new Timestamp(System.currentTimeMillis());
//        log.setData(data);
//        log.setAcao(acao);
//        log.setEmpresa(ezattaEmpresaStatic);
//        LogDAO dao = new LogDAO();
//        dao.addLog(log);
        //----------------------------------------------------------------------
    }

    public void updateEmpresa(EzattaEmpresa empresa) throws SQLException {
        em.getTransaction().begin();
        em.merge(empresa);
        em.getTransaction().commit();
        
        //log-------------------------------------------------------------------
        String acao = "Alterou empresa: "+empresa;
        EzattaLog log = new EzattaLog();
        Timestamp data = new Timestamp(System.currentTimeMillis());
        log.setData(data);
        log.setAcao(acao);
        log.setEmpresa(ezattaEmpresaStatic);
        LogDAO dao = new LogDAO();
        dao.addLog(log);
        //----------------------------------------------------------------------
    }

    public EzattaEmpresa getEmpresa(int id) throws SQLException {
        EzattaEmpresa empersa = em.find(EzattaEmpresa.class, id);
        return empersa;
    }

    public List<EzattaEmpresa> getAllEmpresa() {
        TypedQuery<EzattaEmpresa> query = em.createNamedQuery("EzattaEmpresa.findAll", EzattaEmpresa.class);
        List<EzattaEmpresa> results = query.getResultList();
        return results;
    }

    public boolean getLogAndPasswordSistema(String login, String senha) throws SQLException {
        boolean toReturn = false;

        Calendar dataAtual = Calendar.getInstance();
        int diaMes = dataAtual.get(Calendar.MONTH);
        diaMes = diaMes + 1;

        String mes = String.valueOf(diaMes);
        String dia = String.valueOf(dataAtual.get(Calendar.DAY_OF_MONTH));
        //String senhaValidada = mes + "ezatta" + dia;
        String senhaValidada = "ezatta";

        if (login.equals("ezatta") && senha.equals(senhaValidada)) {
            toReturn = true;
        }

        return toReturn;
    }

    public boolean isValidUserAndPassowrd(EzattaEmpresa ezattaEmpresa) throws SQLException {
        boolean toReturn = false;
        TypedQuery<EzattaEmpresa> query = em.createNamedQuery("EzattaEmpresa.findUserSenha", EzattaEmpresa.class);
        query.setParameter("senha", ezattaEmpresa.getSenha());
        query.setParameter("login", ezattaEmpresa.getLogin());
        try {
            EzattaEmpresa resultadoEmpresa = query.getSingleResult();
            System.out.println("resultadoEmpresa " + resultadoEmpresa);
            toReturn = true;
        } catch (NoResultException e) {
            toReturn = false;
        }
        return toReturn;
    }

    public EzattaEmpresa listUserAndPassowrd(EzattaEmpresa ezattaEmpresa) throws SQLException {
        TypedQuery<EzattaEmpresa> query = em.createNamedQuery("EzattaEmpresa.findUserSenha", EzattaEmpresa.class);
        query.setParameter("senha", ezattaEmpresa.getSenha());
        query.setParameter("login", ezattaEmpresa.getLogin());
        EzattaEmpresa resultadoEmpresa = new EzattaEmpresa();
        try{
        resultadoEmpresa = query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("Erro no listUserAndPassowrd");
        }
        return resultadoEmpresa;
        
    }

    public List<EzattaEmpresa> findAll(String qry, String parametros)
            throws SQLException {
        if (!parametros.isEmpty()) {
            qry = qry.concat(parametros);
        }
        return em.createNativeQuery(qry, EzattaEmpresa.class).getResultList();
    }
    
    public long getValueEmpresa(){
        Query query = em.createQuery("SELECT count(e) from EzattaEmpresa e");
        long count = (long) query.getSingleResult(); 
        return count;
    }

}
