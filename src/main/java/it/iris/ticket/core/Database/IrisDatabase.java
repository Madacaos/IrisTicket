package it.iris.ticket.core.database;

import it.iris.ticket.core.IrisManagaer;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class IrisDatabase {

    @Getter public LocalDB localDB;

    @Getter @Setter public String url, username, password;
    @Getter @Setter public int port;

    @Getter @Setter public Statement statement;
    @Getter @Setter public Connection connection;

    public static String QUERY_TYPE_TICKET_BY_ID = "QUERY_TYPE_TICKET_BY_ID";
    public static String QUERY_TYPE_COMMENT_BY_ID = "QUERY_TYPE_COMMENT_BY_ID";
    public static String QUERY_TYPE_COMMENTS_BY_TICKETID = "QUERY_TYPE_COMMENTS_BY_TICKETID";
    public static String QUERY_TYPE_TICKETS_OPENED = "QUERY_TYPE_TICKETS_OPENED";
    public static String QUERY_TYPE_TICKETS_BY_AUTHOR = "QUERY_TYPE_TICKETS_BY_AUTHOR";

    public static String UPDATE_TYPE_CREATE_TICKET = "UPDATE_TYPE_CREATE_TICKET";
    public static String UPDATE_TYPE_CREATE_COMMENT = "UPDATE_TYPE_CREATE_COMMENT";

    public IrisDatabase(){


        if(IrisManagaer.Configuration.useMYSQL){

        }else{
            localDB = new LocalDB("database.db");
            connection = localDB.getConnection();
            statement = localDB.getStatment();
        }
    }

    @SneakyThrows
    public void preSetUpdate(String updateType, ArrayList<String> datas){
        if(updateType.equals(UPDATE_TYPE_CREATE_TICKET)){
            executeUpdate(String.format("INSERT INTO TICKETLIST (AUTHOR, DATE, CATEGORY, OPENED) VALUES ('%s', '%s', '%s', 'true')",datas.get(0),datas.get(1),datas.get(2)));
        }else if(updateType.equals(UPDATE_TYPE_CREATE_COMMENT)){
            executeUpdate(String.format("INSERT INTO COMMENTLIST (TICKETID, AUTHOR, TEXT, DATE) VALUES (%s,'%s','%s','%s')",datas.get(0),datas.get(1),datas.get(2),datas.get(3)));
        }
    }

    @SneakyThrows
    public ArrayList<TreeMap<String,Object>> preSetQuery(String queryType, ArrayList<String> datas){
        if(queryType.equals(QUERY_TYPE_TICKET_BY_ID)){
            ArrayList<String> rowNames = new ArrayList<>();
            rowNames.add("ID");
            rowNames.add("AUTHOR");
            rowNames.add("DATE");
            rowNames.add("CATEGORY");
            rowNames.add("OPENED");
            return executeQuery(String.format("SELECT * FROM TICKETLIST WHERE ID = %s",datas.get(0)),rowNames);
        }else if(queryType.equals(QUERY_TYPE_TICKETS_OPENED)) {
            ArrayList<String> rowNames = new ArrayList<>();
            rowNames.add("ID");
            rowNames.add("AUTHOR");
            rowNames.add("DATE");
            rowNames.add("CATEGORY");
            rowNames.add("OPENED");
            return executeQuery(String.format("SELECT * FROM TICKETLIST WHERE OPENED = TRUE"), rowNames);
        }else if(queryType.equals(QUERY_TYPE_COMMENTS_BY_TICKETID)){
            ArrayList<String> rowNames = new ArrayList<>();
            rowNames.add("ID");
            rowNames.add("TICKETID");
            rowNames.add("AUTHOR");
            rowNames.add("TEXT");
            rowNames.add("DATE");
            return executeQuery(String.format("SELECT * FROM COMMENTLIST WHERE TICKETID = %s",datas.get(0)), rowNames);
        }else if(queryType.equals(QUERY_TYPE_COMMENT_BY_ID)){
            ArrayList<String> rowNames = new ArrayList<>();
            rowNames.add("ID");
            rowNames.add("TICKETID");
            rowNames.add("AUTHOR");
            rowNames.add("TEXT");
            rowNames.add("DATE");
            return executeQuery(String.format("SELECT * FROM COMMENTLIST WHERE ID = %s",datas.get(0)), rowNames);
        }else if(queryType.equals(QUERY_TYPE_TICKETS_BY_AUTHOR)){
            ArrayList<String> rowNames = new ArrayList<>();
            rowNames.add("ID");
            rowNames.add("AUTHOR");
            rowNames.add("DATE");
            rowNames.add("CATEGORY");
            rowNames.add("OPENED");
            return executeQuery(String.format("SELECT * FROM TICKETLIST WHERE AUTHOR = '%s'",datas.get(0)),rowNames);
        }
        return null;
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

    public String queryAdapter(String query){
        String split[] = query.split("\'");
        String result = "";
        boolean replace = false;
        for(String s : split){
            if(replace){
                result = result +"\'\'"+s;
            }else{
                result = result+s;
                replace = true;
            }
        }
        Bukkit.broadcastMessage(result);
        return result;
    }
}
