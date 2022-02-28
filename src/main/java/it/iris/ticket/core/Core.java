package     it.iris.ticket.core;

import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {
    @Override
    public void onEnable() {
        /*
        Database database = new Database();
        database.executeUpdate("CREATE TABLE IF NOT EXISTS PIPPO (CIAO VARCHAR(255))");
        database.executeUpdate("INSERT INTO PIPPO (CIAO) VALUES ('provola')");
        ArrayList<String> rows = new ArrayList<>();
        rows.add("CIAO");
        Bukkit.broadcastMessage(String.valueOf(database.executeQuery("SELECT * FROM PIPPO",rows)));
        */

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
