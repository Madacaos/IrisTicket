package it.iris.ticket.core.database;

import it.iris.ticket.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class LOCAL{
    private YamlConfiguration localDatabase;
    private File fileLocalDatabase;
    private Date date;
    private Core plugin;

    public LOCAL(Core plugin) {
        this.plugin = plugin;
    }

    public void initialize(){
        fileLocalDatabase = new File(plugin.getDataFolder(), "database.yml");

        if(!fileLocalDatabase.exists()){
            try {
                fileLocalDatabase.getParentFile().mkdirs();
                fileLocalDatabase.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        localDatabase = YamlConfiguration.loadConfiguration(fileLocalDatabase);
    }

    public void createLocalTicket(UUID player, String firstComment, String date, String category){
        verify();

        //UPDATE TICKETS LISTS IDS
        ArrayList<Integer> newTicketsIDs = new ArrayList<Integer>();

        int TicketID = getLastLocalTicketID()+1;
        newTicketsIDs.add(TicketID);

        if(localDatabase.getList("tickets.idList") != null) {
            newTicketsIDs.addAll((ArrayList<Integer>) localDatabase.getList("tickets.idList"));
        }
        localDatabase.set("tickets.idList",newTicketsIDs);

        //UPDATE COMMENT LIST IDS
        ArrayList<Integer> newCommentIDs = new ArrayList<Integer>();

        int CommentID = getLastLocalCommentID()+1;

        newCommentIDs.add(CommentID);

        if(localDatabase.getList("comments.idList") != null) {
            newCommentIDs.addAll((ArrayList<Integer>) localDatabase.getList("comments.idList"));
        }

        localDatabase.set("comments.idList",newCommentIDs);

        //SAVE COMMENT
        localDatabase.set("comments.commentsList."+CommentID+".author",player.toString());
        localDatabase.set("comments.commentsList."+CommentID+".date",date.toString());
        localDatabase.set("comments.commentsList."+CommentID+".comment", firstComment);

        //SAVE TICKET
        localDatabase.set("tickets.ticketList."+TicketID+".author",player.toString());
        localDatabase.set("tickets.ticketList."+TicketID+".category",category);
        localDatabase.set("tickets.ticketList."+TicketID+".date", date.toString());
        localDatabase.set("tickets.ticketList."+TicketID+".comments",new ArrayList<Integer>().add(CommentID));

        try {
            localDatabase.save(fileLocalDatabase);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLastLocalTicketID(){
        verify();

        ArrayList<Integer> ticketsIDList = (ArrayList<Integer>) localDatabase.getList("tickets.idList");
        if(ticketsIDList !=  null && ticketsIDList.size() > 0) {
            Bukkit.broadcastMessage("id: "+ticketsIDList);
            return ticketsIDList.get(0);
        }

        return 0;
    }

    public int getLastLocalCommentID(){
        verify();

        ArrayList<Integer> commentIDList = (ArrayList<Integer>) localDatabase.getList("comments.idList");
        if(commentIDList != null && commentIDList.size() > 0) {
            return commentIDList.get(0);
        }

        return 0;
    }


    public void verify(){
        if(localDatabase == null){
            initialize();
        }
    }
}