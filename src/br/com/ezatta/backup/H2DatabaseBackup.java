/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ezatta.backup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author marcelo
 */
public class H2DatabaseBackup implements DatabaseBackup {

    @Override
    public void backupDatabase(Connection conn, String file) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("BACKUP TO ?");
        statement.setString(1, file);
        statement.execute();
    }

}
