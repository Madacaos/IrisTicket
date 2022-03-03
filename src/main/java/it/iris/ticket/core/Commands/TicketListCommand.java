package it.iris.ticket.core.Commands;

import it.iris.ticket.core.Ticket.Ticket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

import static it.iris.ticket.core.IrisManagaer.Configuration;
import static it.iris.ticket.core.IrisManagaer.Database;

public class TicketListCommand {
    public static void TicketListCommand(Player player, String[] args){
        if(args[0].equalsIgnoreCase("open")){

        }else if(args[0].equalsIgnoreCase("list")){
            if(args.length > 1){
                sendPlayerList(player, Integer.parseInt(args[1]));
            }else{
                sendPlayerList(player,1);
            }
        }
    }

    public static void sendPlayerList(Player p, int page){
        ArrayList<String> datas = new ArrayList<String>();
        datas.add(p.getUniqueId().toString());

        ArrayList<TreeMap<String, Object>> result = Database.preSetQuery(Database.QUERY_TYPE_TICKETS_BY_AUTHOR,datas);
        if(result != null){
            ArrayList<Ticket> Tickets = new ArrayList<>();
            for(TreeMap<String,Object> map : result){
                Ticket ticket = new Ticket();
                ticket.getById((Integer) map.get("ID"));

                Tickets.add(ticket);
            }
            if(Tickets != null){
                List<Ticket> list = new ArrayList<>();
                list.addAll(Tickets);
                list = reverseArray(list);
                List pages = getPage(list,page,10);
                if(pages != null && pages.size() > 0){
                    ArrayList<String> headerTexts = (ArrayList<String>) Configuration.getLanguage().get("listheader");
                    for(String headerText : headerTexts) {
                        headerText = headerText.replace("%page_actual%", String.valueOf(page));
                        headerText = headerText.replace("%page_total%", String.valueOf(totalPages(Tickets)));
                        TextComponent Header = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', headerText)));
                        p.spigot().sendMessage(Header);
                    }
                    for(Ticket ticket : (List<Ticket>) pages){
                        ticket.getComments();
                        if(ticket.Comments.size() > 0) {
                            if(Configuration.getCategoryByText(ticket.getCategory()) == null){
                                ticket.setCategory(Configuration.getCategories().get(0).get("text"));
                                ticket.upload();
                            }
                            String color =Configuration.getCategoryByText(ticket.getCategory()).get("color");
                            TextComponent idComponent = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', String.format(" &%s#%04d &8• &f%s  &8»",color, ticket.getID(), Bukkit.getOfflinePlayer(ticket.getAuthor()).getName()))));
                            TextComponent textCommentComponent = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('§', "  §7" + commentAdapter(ticket.Comments.get(0).Text))));
                            textCommentComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(ChatColor.translateAlternateColorCodes('§',"§7"+ticket.Comments.get(0).getText())).create()));
                            TextComponent amountCommentComponent = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "  " + Configuration.getLanguage().get("commentsamount").toString().replace("%commentamount%", String.valueOf(ticket.Comments.size())))));
                            textCommentComponent.addExtra(amountCommentComponent);
                            idComponent.addExtra(textCommentComponent);
                            p.spigot().sendMessage(idComponent);
                        }
                        }
                    ArrayList<String> footerTexts = (ArrayList<String>) Configuration.getLanguage().get("listfooter");
                    for(String footerText : footerTexts) {
                        if(footerText.contains("%arrows%")){
                            footerText = footerText+" §";
                            String split[] = footerText.split("%arrows%");
                            TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText(""));
                            boolean change = false;
                            for(String s : split){
                                s = s.replace("§","");
                                if(change){
                                    if(page > 1  && totalPages(Tickets) > 1) {
                                        TextComponent arrow1 = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', String.valueOf(Configuration.getLanguage().get("arrowsleft")))));
                                        arrow1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tk list " + (page - 1)));
                                        textComponent.addExtra(arrow1);
                                    }
                                    if(page < totalPages(Tickets)) {
                                        TextComponent arrow2 = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', String.valueOf(Configuration.getLanguage().get("arrowsright")))));
                                        arrow2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tk list " + (page + 1)));
                                        textComponent.addExtra(arrow2);
                                    }
                                    change = false;
                                }else {
                                    TextComponent textComponent2 = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s)));
                                    textComponent2.setClickEvent(null);
                                    textComponent.addExtra(textComponent2);
                                    change = true;
                                }
                            }
                            p.spigot().sendMessage(textComponent);
                        }else{
                            p.spigot().sendMessage(new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',footerText))));
                        }
                    }
                }else{
                    p.spigot().sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', (String) Configuration.getLanguage().get("noticket"))));
                }
            }
        }else{
            p.spigot().sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', (String) Configuration.getLanguage().get("noticket"))));
        }
    }

    public static Integer totalPages(ArrayList<Ticket> tickets){
        int i = 1;
        while(getPage(tickets, i, 10) != null) {

            i++;
        }
        return i-1;
    }

    public static String commentAdapter(String comment){
        if(comment.length() >= 20) {
            comment = comment.substring(0, Math.min(comment.length(), 17)) + "...";
        }else{
            for(int i = comment.length(); i <= 20; i++){
                comment = comment+" ";
            }
        }
        return comment;
    }

    public static <T> List<T> getPage(List<T> sourceList, int page, int pageSize) {

        if(pageSize <= 0 || page <= 0) {
            return null;
        }

        int fromIndex = (page - 1) * pageSize;
        if(sourceList == null || sourceList.size() < fromIndex){
            return null;
        }

        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }

    public static <T>List<T> reverseArray(List<T> sourceList){
        List result = new ArrayList<>();
        for(int i = sourceList.size(); i > 0; i--){
            result.add(sourceList.get(i - 1));
        }
        return result;
    }
}
