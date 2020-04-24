package club.hardcoreminecraft.javase.RAD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


import net.md_5.bungee.api.ChatColor;

public class main extends JavaPlugin {


	static main plugin;
	




	// Called when the plugin is enabled. It is used to set up variables and to register things such as commands.
	@Override
	public void onEnable() {
		plugin = getPlugin(main.class);
		PluginManager pluginManager = getServer().getPluginManager();

	    getConfig().options().copyDefaults(true);
	    saveDefaultConfig();
	    
		
		/*
		 * Register a command to the list of usable commands. If you don't register the
		 * command, it won't work! Also if you change the command name, make sure to
		 * also change in the plugin.yml file.
		 */
		this.getCommand("rules").setExecutor(this);
		this.getCommand("erules").setExecutor(this);

		/*
		 * Make sure you register your listeners if you have any! If you have a class
		 * that implements Listener, you need to make sure to register it. Otherwise it
		 * will DO NOTHING!
		 */
		pluginManager.registerEvents(new playerJoinListener(), this);

		/*
		 * This line lets you send out information to the console. In this case it would
		 * This line should inform the user where they can download updates
		 */
		this.getLogger().info("Awesome rules plugin that uses cool threads. Check out my github! https://github.com/JavaSE77/RulesAreDumb");
	}







	/* Called when the command is ran
	args variable is the commands arguments in an array of strings.
	sender variable is the sender who ran the command
	*/
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
	

		/* Checks if the sender is sending from the console
		   ChatColor is the variable for color
		   § <- Minecraft color code symbol or use chat_color
		*/
		if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			if(sender.hasPermission("JavaSE.rules.reload")) {
		

			reloadConfig();
		    File usersFile = new File(main.plugin.getDataFolder(), "users.yml");
		    YamlConfiguration.loadConfiguration(usersFile);
		    
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "The rules have been reloaded");

			return true;
		} else { 
			String noPerms = getConfig().getString("ErrorNoPerms");
			if(noPerms != null && !noPerms.isEmpty())
			sender.sendMessage(noPerms.replaceAll("&", "§"));
		return true;
		}
		} 
		
		if(sender instanceof Player) {
			if(args.length == 1 && args[0].equalsIgnoreCase("accept")) {
				Player player = (Player) sender;

						acceptRules(player);
				    
			} else 
			
			if(cmdLabel.equalsIgnoreCase("rules") || cmdLabel.equalsIgnoreCase("erules")) {
				
				//Get the player
				final Player player = (Player) sender;
				
				final String index;
				if(args.length == 0) {index = null;} else index = args[0];
				
				
				Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
				    @Override
				    public void run() {
				    	
				        threadRules(player, index);
				    }
				});
				  
				  }
				
				
			
			
	
			//Check if the type of sender is a player
		} else sender.sendMessage(ChatColor.RED +"This command can only be used by players. ");
		return false;

	}
	
	
	public void threadRules(Player player, String index) {  

		
		
		
		//setup the path to the file
		  String filePath = plugin.getDataFolder() + "";
		  
	  File dr = new File(filePath);
		  
		  if(!dr.exists())
			 dr.mkdir();
		  
		  
		  File file = new File(filePath + File.separator + "rules.txt"); 
		  
		  
		  if(!file.exists())
			  documentHandler.createNewRulesFile(file);
		  
		  
		  //At this point we assume that the file exists, and if it does not, we already created it.
		  BufferedReader br;
		try {
			
			
			br = new BufferedReader(new FileReader(file));
			int upper = getConfig().getInt("linesPerPage");
			int lower = 0;
			
			if(!(index == null || index.isEmpty())) {

				lower = (Integer.parseInt(index) -1) * upper;
				upper = Integer.parseInt(index) * upper;
			}
		  
		  String st; 
		  int i = 0;
		  boolean wasSent = false;
		  
			while ((st = br.readLine()) != null)  {
				if(i >= lower && i < upper) {
			    player.sendMessage(st.replaceAll("&", "§"));
			    wasSent = true;
				}
			i++;
			}
			
			 br.close();
			 
			 if(!wasSent)
				 player.sendMessage(ChatColor.DARK_RED + "Sorry, that page of rules does not exist.");
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  
		 
		  
		  
		
		
		
		}  
	
	//This function is called when a player accepts the rules
	public void acceptRules(Player player) {
		

	    File usersFile = new File(main.plugin.getDataFolder(), "users.yml");
	    FileConfiguration users = YamlConfiguration.loadConfiguration(usersFile);
	    
	  
	    //Check if the user has already accepted the rules.
	    double rulesVersion = getConfig().getDouble("rulesVersion");
	    double playerVersion = users.getDouble( player.getUniqueId()+"");
	    
	    if(playerVersion >= rulesVersion) {

	    	String invalid = getConfig().getString("invalidAccept");
	    	if(invalid != null && !invalid.isEmpty())
	    	player.sendMessage(invalid.replaceAll("&", "§"));
	    	return;
	    }
	    
	    //Check if the player has already accepted the rules before. If they have, then run the second set of commands
	    if(playerVersion != 0) {
	    	acceptAgain(player);
	    	
	    	    } else {
	    	    	//Else this must be there first accept
	    	    	acceptFirst(player);
	    	    }
	    
	    //Try to set the userdata to be equal to the current rules version
	    try {
	    	users.set(player.getUniqueId() + "", rulesVersion);
			users.save(usersFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    //If we get here, the we need to run the first time command
		

		
	}
	
	public void acceptAgain(Player player) {
		
		String message = getConfig().getString("secondAcceptMessage");
		if(message != null && !message.isEmpty())
		player.sendMessage(message.replaceAll("&", "§"));
		
		String accept = getConfig().getString("acceptTwiceBroadcast");

		
		if(accept != null && !accept.isEmpty())
		Bukkit.broadcastMessage(accept.replaceAll("&", "§").replaceAll("%player%", player.getName()));
		
	
		
		
		List<String> list = getConfig().getStringList("acceptTwiceCommands");
		for (String string : list) {
		if(string != null && !string.isEmpty())
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), string.replaceAll("%player%", player.getName()));
		
	}
	}
	
	public void acceptFirst(Player player) {
		String accept = getConfig().getString("acceptBroadcast");

		
		if(accept != null && !accept.isEmpty())
		Bukkit.broadcastMessage(accept.replaceAll("&", "§").replaceAll("%player%", player.getName()));
		
		
		String pm = getConfig().getString("acceptMessage");

		
		if(pm != null && !pm.isEmpty())
		player.sendMessage(pm.replaceAll("&", "§").replaceAll("%player%", player.getName()));
		
		
		List<String> list = getConfig().getStringList("acceptCommands");
		for (String string : list) {
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), string.replaceAll("%player%", player.getName()));
		
	}
	}
   

	
	}


