package it.iris.ticket.core.Commands;

import it.iris.ticket.core.IrisManagaer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import it.iris.ticket.core.Commands.NewTicketCommand;
import org.bukkit.util.StringUtil;

public class listener implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(args.length > 0){
                switch(args[0].toLowerCase(Locale.ROOT)){
                    case("help"):
                        HelpCommand.HelpCommand(p);
                        break;
                    case("new"):
                        if(permsCheck(p,IrisManagaer.Configuration.getPermissions().get("ticket-create"),true)) {
                            NewTicketCommand.NewTicketCommand(p, args);
                        }
                        break;
                    case("list"):
                        if(permsCheck(p,IrisManagaer.Configuration.getPermissions().get("ticket-his-list"),true)){
                            TicketListCommand.TicketListCommand(p,args);
                        }
                        break;
                    case("open"):
                        if(permsCheck(p,IrisManagaer.Configuration.getPermissions().get("ticket-all-list"),true)){

                        }
                        break;
                }
            }else{
                HelpCommand.HelpCommand(p);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> COMMANDS = new ArrayList<>();
        if(sender instanceof Player && args.length == 1) {
            if(permsCheck((Player)sender ,IrisManagaer.Configuration.getPermissions().get("ticket-create"),false)) {
                COMMANDS.add("new");
            }
            if(permsCheck((Player)sender ,IrisManagaer.Configuration.getPermissions().get("ticket-his-list"),false)) {
                COMMANDS.add("list");
            }
            if(permsCheck((Player)sender ,IrisManagaer.Configuration.getPermissions().get("ticket-his-view"),false) || permsCheck((Player)sender ,IrisManagaer.Configuration.getPermissions().get("ticket-all-view"),false)) {
                COMMANDS.add("info");
            }
            if(permsCheck((Player)sender ,IrisManagaer.Configuration.getPermissions().get("ticket-his-close"),false) || permsCheck((Player)sender ,IrisManagaer.Configuration.getPermissions().get("ticket-all-close"),false)) {
                COMMANDS.add("close");
            }
        }
        final List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0], COMMANDS, completions);
        Collections.sort(completions);

        return completions;
    }

    public boolean permsCheck(Player player, String permission, boolean sendMessage){
        if(!(IrisManagaer.getConfiguration().getPermissions().get("nopermissions") == null)) {
            if (player.hasPermission(permission)) {
                if (sendMessage) {
                    TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', IrisManagaer.getConfiguration().getPermissions().get("nopermissions"))));
                    player.spigot().sendMessage(textComponent);
                }
                return true;
            }
        }else{
            return true;
        }
        return false;
    }
}
