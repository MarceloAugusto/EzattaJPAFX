/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.controller;

import br.com.ezatta.backup.Backup;
import br.com.ezatta.backup.DatabaseBackup;
import br.com.ezatta.backup.H2DatabaseBackup;
import br.com.ezatta.backup.dao.BackupDao;
import br.com.ezatta.util.JPAUtil;
import br.com.ezatta.util.Path;
import br.com.ezatta.view.EzattaMain;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.stage.FileChooser;
import javax.persistence.EntityManager;
import org.hibernate.Session;

/**
 * FXML Controller class
 *
 * @author marcelo
 */
public class BackUpController implements Initializable {

    private Connection conn;
    Calendar cal = Calendar.getInstance();
    private BackupDao dao = new BackupDao();
    private Backup bkp;
    FileChooser fileChooser = new FileChooser();
    DatabaseBackup backup = new H2DatabaseBackup();

    @FXML
    private CheckBox chAtivar;

    @FXML
    void salvarArquivo(ActionEvent event) {

        //Set extension filter
        fileChooser.setTitle("Save backup");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIPpd backups", "*.zip");
        fileChooser.setInitialFileName("backup-" + cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE) + "-" + cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE) + ".zip");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
        File file = fileChooser.showSaveDialog(EzattaMain.stage);

        EntityManager em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        Session hibernateSession = em.unwrap(Session.class);
        hibernateSession.doWork(new org.hibernate.jdbc.Work() {
            @Override
            public void execute(Connection con) throws SQLException {
                conn = con;
            }
        });

        if (file != null) {
            try {
                backup.backupDatabase(conn, file.getAbsolutePath());
            } catch (Throwable t) {
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            System.out.println("dao.getValueBackup(): " + dao.getValueBackup());
            if (dao.getValueBackup() == 0) {
                chAtivar.setSelected(false);
            } else {
                bkp = dao.getBackup(1);
                chAtivar.setSelected(bkp.isBkp());
                System.out.println("tem registro:");
            }

        } catch (SQLException ex) {
            Logger.getLogger(BackUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void salvarAutomatico(ActionEvent event) throws SQLException {
        bkp = new Backup();
        if (dao.getValueBackup() == 0) {//add
            bkp.setId(1);
            bkp.setBkp(chAtivar.selectedProperty().getValue());
            Timestamp data = new Timestamp(System.currentTimeMillis());
            bkp.setData(data);
            dao.addBackup(bkp);
            fazerBkp();
        } else {//update
            bkp.setBkp(chAtivar.selectedProperty().getValue());
            Timestamp data = new Timestamp(System.currentTimeMillis());
            bkp.setData(data);
            bkp.setId(1);
            dao.updateBackup(bkp);
            fazerBkp();
        }
        System.out.println("atualizou " + chAtivar.selectedProperty().getValue());

    }

    private void fazerBkp() {
        String rais = Path.workingDir + "/bkp/";
        File diretorio = new File(rais); // ajfilho é uma pasta!  
        if (!diretorio.exists()) {
            diretorio.mkdirs(); //mkdir() cria somente um diretório, mkdirs() cria diretórios e subdiretórios.  
            System.out.println("criou");
        } else {
            System.out.println("Diretório já existente");
        }

        //Set extension filter
        fileChooser.setTitle("Save backup");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIPpd backups", "*.zip");
        fileChooser.setInitialDirectory(diretorio);
        fileChooser.setInitialFileName(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE) + "-" + cal.get(Calendar.HOUR_OF_DAY) + ".zip");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
        String nome = "backup-" + cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE) + "-" + cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE) + ".zip";
        File file = new File(rais,nome);
//        File file = fileChooser.showSaveDialog(EzattaMain.stage);
        
        //File file = new File(rais);
        

        EntityManager em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        Session hibernateSession = em.unwrap(Session.class);
        hibernateSession.doWork(new org.hibernate.jdbc.Work() {
            @Override
            public void execute(Connection con) throws SQLException {
                conn = con;
            }
        });

        if (file != null) {
            try {
                backup.backupDatabase(conn, file.getAbsolutePath());
            } catch (Throwable t) {
            }

        }
    }

}
