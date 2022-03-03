package it.iris.ticket.core.Commands;

import it.iris.ticket.core.Ticket.Comment;
import it.iris.ticket.core.Ticket.Ticket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.TreeMap;

import static it.iris.ticket.core.IrisManagaer.Configuration;
import static it.iris.ticket.core.IrisManagaer.Database;

public class NewTicketCommand {
    static String SuccessMessage = String.valueOf(Configuration.getLanguage().get("generalcreateticket"));
    static String TooTickets = String.valueOf(Configuration.getLanguage().get("generaltooticket"));
    static ArrayList<String> selectCategory = (ArrayList<String>) Configuration.getLanguage().get("selectcategory");

    public static void NewTicketCommand(Player player, String[] args){
        if(args.length > 1){
            if(Integer.parseInt(Configuration.permissions.get("ticket-max-opened")) > ticketsOpen(String.valueOf(player.getUniqueId()))){
                String comment = "";
                for (int i = 1; i < args.length; i++){
                    String arg = (args[i] + " ");
                    comment = (comment + arg);
                }
                if(comment.contains("-catg:")){
                    String category[] = comment.split("-catg:");
                    if(category.length > 0){
                        String Category = category[1].replace(" ","");
                        if(existCategory(Category)) {
                            Ticket ticket = new Ticket();
                            ticket.setOpen(true);
                            ticket.setCategory(Category);
                            ticket.setAuthor(player.getUniqueId());
                            ticket.upload();

                            Comment commentTk = new Comment();
                            commentTk.setTicketID(ticket.getID());
                            commentTk.setText(Database.queryAdapter(comment).replace("-catg:" + Category, ""));
                            commentTk.setAuthor(player.getUniqueId());
                            commentTk.upload();
                            if (SuccessMessage.contains("%id%")) {
                                String message[] = SuccessMessage.split("%id%");
                                TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message[0])));
                                TextComponent TextComponent2 = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',"&"+Configuration.getCategoryByText(Category).get("color") +"#"+ ticket.getID())));
                                TextComponent2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket info " + ticket.getID()));
                                textComponent.addExtra(TextComponent2);
                                message = removeTheElement(message, 0);
                                for (String msg : message) {
                                    TextComponent TextComponent3 = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', msg)));
                                    TextComponent3.setClickEvent(null);
                                    textComponent.addExtra(TextComponent3);
                                }
                                player.spigot().sendMessage(textComponent);
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', SuccessMessage));
                            }
                        }
                    }
                }else{

                    for(String message : selectCategory){

                        if(message.contains("%categories%")){
                            message = message.replace("%categories%","");
                            message = message.replace(" ","");
                            TextComponent component1 = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',message)));
                            for(TreeMap<String,String> category : Configuration.getCategories()){
                                String color = category.get("color");
                                String text = category.get("text");
                                TextComponent component = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',"&"+color+"["+text+"] ")));
                                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/ticket new "+comment+"-catg:"+text));
                                component1.addExtra(component);
                            }
                            player.spigot().sendMessage(component1);
                        }else{
                            TextComponent component = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',message)));
                            player.spigot().sendMessage(component);
                        }
                    }

                }
            }else{
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',TooTickets));
            }
        }else{
            HelpCommand.HelpCommand(player);
        }
    }

    public static boolean existCategory(String category){
        for(TreeMap<String, String> Category : Configuration.getCategories()){
            if(Category.get("text").equals(category)){
                return true;
            }
        }
        return false;
    }

    public static int ticketsOpen(String Author){
        ArrayList<String> rows = new ArrayList<>();
        rows.add("AUTHOR");
        rows.add("OPENED");
        ArrayList<TreeMap<String,Object>> result = Database.executeQuery("SELECT * FROM TICKETLIST WHERE AUTHOR = '"+Author+"' and OPENED = 'true'",rows);
        if(result != null){
            return result.size();
        }
        return 0;
    }

    public static String[] removeTheElement(String[] arr, int index)
    {
        if (arr == null || index < 0
                || index >= arr.length) {
            return arr;
        }

        String[] anotherArray = new String[arr.length - 1];
        for (int i = 0, k = 0; i < arr.length; i++) {
            if (i == index) {
                continue;
            }
            anotherArray[k++] = arr[i];
        }
        return anotherArray;
    }
}
