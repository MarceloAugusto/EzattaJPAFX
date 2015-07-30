package br.com.ezatta.backup.dao;

import br.com.ezatta.backup.Backup;
import br.com.ezatta.util.JPAUtil;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class BackupDao {

    private static final long serialVersionUID = 1L;

    EntityManager em = JPAUtil.getEntityManager();

    public BackupDao() {
    }
    
    public void addBackup(Backup chamado) throws SQLException {
        em.getTransaction().begin();
        em.persist(chamado);
        em.getTransaction().commit();
    }

    public void removeBackup(Backup emp) throws SQLException {
        em.getTransaction().begin();
        Backup chamado = em.find(Backup.class, emp.getId());
        em.remove(chamado);
        em.getTransaction().commit();
    }

    public void updateBackup(Backup chamado) throws SQLException {
        em.getTransaction().begin();
        em.merge(chamado);
        em.getTransaction().commit();
    }

    public Backup getBackup(int id) throws SQLException {
        Backup empersa = em.find(Backup.class, id);
        return empersa;
    }

    public List<Backup> getAllBackup() {
        TypedQuery<Backup> query = em.createNamedQuery("Backup.findAll", Backup.class);
        List<Backup> results = query.getResultList();
        return results;
    }
    
    public long getValueBackup(){
        Query query = em.createQuery("SELECT count(b) from Backup b");
        long count = (long) query.getSingleResult(); 
        return count;
    }
    
    public Backup getBackup() {
        Query query = em.createQuery("SELECT b from Backup b");
        int max = query.getMaxResults();
        Backup resultado = (Backup) query.getSingleResult();
        
        System.out.println("resultado: "+resultado);
        return resultado;
    }

    public List<Backup> findAllBackup(String qry, String parametros)
            throws SQLException {
        if (!parametros.isEmpty()) {
            qry = qry.concat(parametros);
        }
        return em.createNativeQuery(qry, Backup.class).getResultList();
    }

}
