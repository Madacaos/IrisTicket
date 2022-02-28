package it.iris.ticket.core.Database;

import it.iris.ticket.core.Configuration;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeMap;

public class Database {
    @Getter public it.iris.ticket.core.Configuration Configuration;
    @Getter public LocalDB localDB;

    @Getter @Setter public String url, username, password;
    @Getter @Setter public int port;

    @Getter @Setter public Statement statement;
    @Getter @Setter public Connection connection;

    public Database(){
        Configuration = new Configuration();

        if(Configuration.useMYSQL){

        }else{
            localDB = new LocalDB("database.db");
            connection = localDB.getConnection();
            statement = localDB.getStatment();
        }
    }

    @SneakyThrows
    public boolean executeUpdate(String update){
        if(connection.isClosed()){
            return false;
        }
        getStatement().executeUpdate(update);
        return true;
    }

    @SneakyThrows
    public ArrayList<TreeMap<String,Object>> executeQuery(String query, ArrayList<String> rowNames){
        if(connection.isClosed()){
            return null;
        }
        ArrayList<TreeMap<String,Object>> result = new ArrayList<>();
        ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                TreeMap<String,Object> result2 = new TreeMap<>();
                for(String rowName : rowNames) {
                    result2.put(rowName,rs.getObject(rowName));
                }
                result.add(result2);
            }
        return result;
    }
}
