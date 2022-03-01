package it.iris.ticket.core.database;

import it.iris.ticket.core.Core;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

public class LocalDB {
    @Getter @Setter public String FilePathDatabase;
    @Getter @Setter public Connection connection;
    @Getter @Setter public Statement statment;

    @SneakyThrows
    public LocalDB(String FilePathDatabase){
        this.FilePathDatabase = FilePathDatabase;
        this.connection = getSQLConnection();
        setStatment(connection.createStatement());
    }

    public Connection getSQLConnection() {
        File dataFolder = new File(Core.getPlugin(Core.class).getDataFolder(), getFilePathDatabase());
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                Core.getPlugin(Core.class).getLogger().log(Level.SEVERE, "File write error: "+getFilePathDatabase());
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            Core.getPlugin(Core.class).getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            Core.getPlugin(Core.class).getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }
}
