package it.iris.ticket.core;

import it.iris.ticket.core.database.IrisDatabase;
import it.iris.ticket.core.Ticket.Ticket;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.UUID;

public class IrisManagaer {
    @Getter public static it.iris.ticket.core.Configuration Configuration;
    @Getter public static IrisDatabase Database;

    public static void initialize(){
        Configuration = new Configuration();
        Database = new IrisDatabase();

        //          INITIALIZE DATABASE TABLES         //
        Database.executeUpdate("CREATE TABLE IF NOT EXISTS TICKETLIST (ID INTEGER PRIMARY KEY AUTOINCREMENT, AUTHOR TEXT(255), DATE TEXT(255), CATEGORY TEXT(255), OPENED BOOL)");
        Database.executeUpdate("CREATE TABLE IF NOT EXISTS COMMENTLIST (ID INTEGER PRIMARY KEY AUTOINCREMENT, TICKETID INTEGER, AUTHOR TEXT(255), DATE TEXT(255), TEXT TEXT(255))");
        /*
        Ticket tk = new Ticket();
        tk.setAuthor(UUID.randomUUID());
        tk.setCategory("Generale");
        tk.setOpen(true);
        tk.upload();

        Ticket bla = new Ticket();
        bla.getById(3);
        Bukkit.broadcastMessage(String.valueOf(bla.getCategory()));
        
         */
    }
}
