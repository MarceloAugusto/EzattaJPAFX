package br.com.ezatta.dao;

import static br.com.ezatta.controller.LoginController.ezattaEmpresaStatic;
import br.com.ezatta.model.EzattaEmpresa;
import br.com.ezatta.model.EzattaLog;
import br.com.ezatta.model.EzattaUsuario;
import br.com.ezatta.util.JPAUtil;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class UsuarioDAO {

    private static final long serialVersionUID = 1L;

    EntityManager em = JPAUtil.getEntityManager();

    public UsuarioDAO() {
    }

    public boolean isValidUserAndPassowrd(EzattaUsuario ezattaUsuario) throws SQLException {
        boolean toReturn = false;
        System.out.println("Login: " + ezattaUsuario.getLogin() + " - Senha: " + ezattaUsuario.getSenha());
        TypedQuery<EzattaUsuario> query = em.createNamedQuery("EzattaUsuario.findByLoginSenha", EzattaUsuario.class);
        query.setParameter("login", ezattaUsuario.getLogin());
        query.setParameter("senha", ezattaUsuario.getSenha());
        EzattaUsuario resultadoUsuario = null;

        try {
            resultadoUsuario = query.getSingleResult();
            if (resultadoUsuario.getId() != 0) {
                toReturn = true;
            }
        } catch (NoResultException e) {
            toReturn = false;
        }

        return toReturn;
    }

    public void addUser(EzattaUsuario usuarios) throws SQLException {
        em.getTransaction().begin();
        em.persist(usuarios);
        em.getTransaction().commit();
        
        //log-------------------------------------------------------------------
        String acao = "Adicionar usuário: "+usuarios;
        EzattaLog log = new EzattaLog();
        Timestamp data = new Timestamp(System.currentTimeMillis());
        log.setData(data);
        log.setAcao(acao);
        log.setEmpresa(ezattaEmpresaStatic);
        LogDAO dao = new LogDAO();
        
        System.out.println("log: "+log);
        dao.addLog(log);
        //----------------------------------------------------------------------
    }

    public void removeUser(EzattaUsuario usuario) throws SQLException {
        em.getTransaction().begin();
        EzattaUsuario usuarioRemover = em.find(EzattaUsuario.class, usuario.getId());
        em.remove(usuarioRemover);
        em.getTransaction().commit();
        
         //log-------------------------------------------------------------------
        String acao = "Remover usuário: "+usuario;
        EzattaLog log = new EzattaLog();
        Timestamp data = new Timestamp(System.currentTimeMillis());
        log.setData(data);
        log.setAcao(acao);
        log.setEmpresa(ezattaEmpresaStatic);
        LogDAO dao = new LogDAO();
        
        System.out.println("log: "+log);
        dao.addLog(log);
        //----------------------------------------------------------------------
    }

    public void updateUser(EzattaUsuario usr) throws SQLException {
        em.getTransaction().begin();
        em.merge(usr);
        em.getTransaction().commit();
        
         //log-------------------------------------------------------------------
        String acao = "Alterar usuário: "+usr;
        EzattaLog log = new EzattaLog();
        Timestamp data = new Timestamp(System.currentTimeMillis());
        log.setData(data);
        log.setAcao(acao);
        log.setEmpresa(ezattaEmpresaStatic);
        LogDAO dao = new LogDAO();
        
        System.out.println("log: "+log);
        dao.addLog(log);
        //----------------------------------------------------------------------
    }

    public EzattaUsuario getUser(int idUser) {
        EzattaUsuario usuario = em.find(EzattaUsuario.class, idUser);
        return usuario;
    }

    public EzattaUsuario getUserByLogAndPassword(EzattaUsuario ezattaUsuario) {
        TypedQuery<EzattaUsuario> query = em.createNamedQuery("EzattaUsuario.findByLoginSenha", EzattaUsuario.class);
        query.setParameter("login", ezattaUsuario.getLogin());
        query.setParameter("senha", ezattaUsuario.getSenha());
        EzattaUsuario resultadoUsuario = query.getSingleResult();
        return resultadoUsuario;
    }

    public List<EzattaUsuario> getAllUsers() {
        TypedQuery<EzattaUsuario> query = em.createNamedQuery("EzattaUsuario.findAll", EzattaUsuario.class);
        List<EzattaUsuario> listaUsuarios = query.getResultList();
        return listaUsuarios;
    }

    public List<EzattaUsuario> getAllUsersPerEmpresa(EzattaEmpresa empresa) {
        TypedQuery<EzattaUsuario> query = em.createNamedQuery("EzattaUsuario.findByEmpresa", EzattaUsuario.class);
        query.setParameter("empresa", empresa);
        List<EzattaUsuario> usuarios = query.getResultList();
        return usuarios;
    }

    public List<EzattaUsuario> findAll(String qry, String parametros)
            throws SQLException {
        if (!parametros.isEmpty()) {
            qry = qry.concat(parametros);
        }
        return em.createNativeQuery(qry, EzattaUsuario.class).getResultList();
    }
}
