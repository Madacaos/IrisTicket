package it.iris.ticket.core;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.TreeMap;

public class Configuration {
    @Getter public FileConfiguration fileConfiguration;

    @Getter public String url,username,password,database;
    @Getter public int port;
    @Getter public Boolean useMYSQL;

    @Getter ArrayList<TreeMap<String,String>> Categories = new ArrayList<>();

    @Getter public TreeMap<String,String> permissions = new TreeMap<>();

    @Getter TreeMap<String,Object> language = new TreeMap<>();

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

        permissions.put("ticket-max-opened",String.valueOf(fileConfiguration.getInt("permissions.max-opened-tickets")));
        //                      Getting Database Data                   //
        useMYSQL = fileConfiguration.getBoolean("database.useMYSQL");
        if(useMYSQL){
            url = (String) getMYSQLData("url");
            password = (String) getMYSQLData("password");
            port = (int) getMYSQLData("port");
            username = (String) getMYSQLData("username");
            database = (String) getMYSQLData("database");
        }

        //                  Getting Categories                      //
        for(Object s : fileConfiguration.getList("categories")){
            TreeMap<String,String> map = new TreeMap<>();
            String string = (String) s;
            String split[] = string.split("color:");
            if(split.length > 0){
                map.put("color",split[1]);
            }
            string = string.replace("color:"+split[1],"").replace(" ","");
            map.put("text",string);
            Categories.add(map);
        }

        //                  Getting Language                 //
        language.put("noticket",fileConfiguration.getString("language.list.noticket"));
        language.put("listheader",fileConfiguration.getList("language.list.header"));
        language.put("listfooter",fileConfiguration.getList("language.list.footer"));
        language.put("commentsamount",fileConfiguration.getString("language.list.commentsamount"));

        language.put("ticketheader",fileConfiguration.getList("language.ticket.header"));
        language.put("ticketcomments",fileConfiguration.getString("language.ticket.comments"));
        language.put("ticketfooter",fileConfiguration.getList("language.ticket.footer"));
        language.put("ticketbuttonscomment",fileConfiguration.getString("language.ticket.buttons.comment"));
        language.put("ticketbuttonsclose",fileConfiguration.getString("language.ticket.buttons.close"));
        language.put("ticketbuttonsopen",fileConfiguration.getString("language.ticket.buttons.open"));
        language.put("ticketstatusopened",fileConfiguration.getString("language.ticket.status.opened"));
        language.put("ticketstatusclosed",fileConfiguration.getString("language.ticket.status.closed"));

        language.put("arrowsright",fileConfiguration.getString("language.arrows.right"));
        language.put("arrowsleft",fileConfiguration.getString("language.arrows.left"));

        language.put("help", fileConfiguration.getList("language.help"));

        language.put("generalcreateticket",fileConfiguration.getString("language.general.createticket"));
        language.put("generalcloseticket",fileConfiguration.getString("language.general.closeticket"));
        language.put("generalopenticket",fileConfiguration.getString("language.general.openticket"));
        language.put("generaladdcomment",fileConfiguration.getString("language.general.addcomment"));
        language.put("generaltooticket",fileConfiguration.getString("language.general.tootickets"));
        language.put("nopermissions",fileConfiguration.getString("language.general.nopermissions"));

        language.put("selectcategory",fileConfiguration.getList("language.selectcategory"));
    }

    public TreeMap<String,String> getCategoryByText(String text){
        for(TreeMap<String,String> categ: Categories){
            if (categ.get("text").equals(text)) {
                return categ;
            }
        }
        return null;
    }

    private void setPermission(String type, boolean admin){
        permissions.put(type,getPermissions(type,admin));
    }
    private String getPermissions(String type, boolean admin) {if(admin){return fileConfiguration.getString("permissions.admin."+type);} return(fileConfiguration.getString("permissions."+type));}
    private Object getMYSQLData(String type) {return fileConfiguration.get("database.MSYQL."+type);}
}
