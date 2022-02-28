package it.iris.ticket.core;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.TreeMap;

public class Configuration {
    @Getter public FileConfiguration fileConfiguration;

    @Getter public String url,username,password,database;
    @Getter public int port;
    @Getter public Boolean useMYSQL;

    @Getter public TreeMap<String,String> permissions = new TreeMap<>();

    public Configuration() {
        Core.getPlugin(Core.class).saveDefaultConfig();
        fileConfiguration = Core.getPlugin(Core.class).getConfig();

        //                      Getting Permissions                     //
        setPermission("ticket-all-list", true);
        setPermission("ticket-all-view", true);
        setPermission("ticket-all-comment", true);
        setPermission("ticket-all-close", true);

        setPermission("ticket-create", false);
        setPermission("ticket-his-view", false);
        setPermission("ticket-his-list", false);
        setPermission("ticket-his-comment", false);
        setPermission("ticket-his-close", false);

        //                      Getting Database Data                   //
        useMYSQL = fileConfiguration.getBoolean("database.useMYSQL");
        if(useMYSQL){
            url = (String) getMYSQLData("url");
            password = (String) getMYSQLData("password");
            port = (int) getMYSQLData("port");
            username = (String) getMYSQLData("username");
            database = (String) getMYSQLData("database");
        }
    }

    private void setPermission(String type, boolean admin){
        permissions.put(type,getPermissions(type,admin));
    }
    private String getPermissions(String type, boolean admin) {if(admin){return fileConfiguration.getString("permissions.admin."+type);} return(fileConfiguration.getString("permissions."+type));}
    private Object getMYSQLData(String type) {return fileConfiguration.get("database.MSYQL."+type);}
}
