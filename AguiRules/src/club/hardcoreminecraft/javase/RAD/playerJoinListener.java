package club.hardcoreminecraft.javase.RAD;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class playerJoinListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		final Player player = event.getPlayer();
		
		Bukkit.getScheduler().runTaskAsynchronously(main.plugin, new Runnable() {
		    @Override
		    public void run() {
		    	
		       checkVersion(player);
		    }
		});
	
		
	}
	
	
	
	
	
	public void checkVersion(Player player) {
		
		
		
		//get what version the player accepted. If it is 0, then we know that they have yet to accept the rules.
		//do not message players who have yet to accept the rules.
	    File usersFile = new File(main.plugin.getDataFolder(), "users.yml");
	    FileConfiguration users = YamlConfiguration.loadConfiguration(usersFile);
	    File configFile = new File(main.plugin.getDataFolder(), "config.yml");
	    FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		double playerVersion = users.getDouble(player.getUniqueId() + "");
		double version = config.getDouble("rulesVersion");
		
		if(playerVersion == 0) {
			String message = config.getString("neverAccepted");
			if(message != null && !message.isEmpty())
			player.sendMessage(message.replaceAll("&", "§"));
			return;
		}

		
		if(version > playerVersion) {
			String message = config.getString("outdatedRulesMessage");
			if(message != null && !message.isEmpty())
			player.sendMessage(message.replaceAll("&", "§"));

		}
		
	}
	
	
}
