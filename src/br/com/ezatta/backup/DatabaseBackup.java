/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.backup;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author marcelo
 */
public interface DatabaseBackup {
    public void backupDatabase(Connection conn, String file) throws SQLException;
}
