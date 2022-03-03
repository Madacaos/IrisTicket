package     it.iris.ticket.core;

import it.iris.ticket.core.Commands.listener;
import it.iris.ticket.core.database.IrisDatabase;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {
    @Override
    public void onEnable() {
        IrisManagaer.initialize();
        this.getCommand("ticket").setExecutor(new listener());
        this.getCommand("tk").setExecutor(new listener());
        this.getCommand("ticket").setTabCompleter(new listener());
        this.getCommand("tk").setTabCompleter(new listener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
