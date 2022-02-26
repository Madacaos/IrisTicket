package     it.iris.ticket.core;

import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {

    @Override
    public void onEnable() {
        local = new LOCAL(this);
        Date data = new Date();
        SimpleDateFormat type = new SimpleDateFormat("MM-dd-yyyy");
        data.setTime(System.currentTimeMillis());

        local.createLocalTicket(UUID.fromString("737c890e-9702-11ec-b909-0242ac120002"),"Ciccio super detective con gli occhiali",type.format(data),"Generale");
        local.createComment(UUID.fromString("737c890e-9702-11ec-b909-0242ac120002"),"questo è il secondo commento",type.format(data),1);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
