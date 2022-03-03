package it.iris.ticket.core.Commands;

import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static it.iris.ticket.core.IrisManagaer.Configuration;

public class HelpCommand {
    static ArrayList<String> helpMessage = (ArrayList<String>) Configuration.getLanguage().get("help");

    public static void HelpCommand(Player p) {
        for(String message : helpMessage){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
        }
    }
}
