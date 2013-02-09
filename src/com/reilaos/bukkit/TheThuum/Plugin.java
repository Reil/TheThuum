package com.reilaos.bukkit.TheThuum;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.reilaos.bukkit.TheThuum.shouts.CustomShout;
import com.reilaos.bukkit.TheThuum.shouts.ShoutType;


public class Plugin extends JavaPlugin{
	GreyBeard arngeir;
	public static BukkitScheduler scheduler;
	public static Plugin thisOne;
	public Logger log;
	
	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		thisOne = this;
		arngeir = new GreyBeard();
		scheduler = getServer().getScheduler();
		log = Logger.getLogger("Minecraft");
		PluginManager pm = getServer().getPluginManager();
		
		// Build the shout table
		log.info("[TheThuum] Loading default shouts.");
		for (ShoutType shoutType : ShoutType.values()){
			arngeir.ShoutTable.put(shoutType.shout.words()[0], shoutType.shout);
			arngeir.ShoutTable.put(shoutType.shout.words()[0] + " " + shoutType.shout.words()[1], shoutType.shout);
			arngeir.ShoutTable.put(shoutType.shout.words()[0] + " " + shoutType.shout.words()[1]+ " " + shoutType.shout.words()[2], shoutType.shout);			
		}
		log.info("[TheThuum] Loading custom shouts.");
		for (Object customShout : getConfig().getList("customshouts")){
			Map<String, ?> customShoutConfig = ((LinkedHashMap)customShout);
			CustomShout constructShout = new CustomShout();
			constructShout.words[0] = (String) customShoutConfig.get("firstword");
			constructShout.words[1] = (String) customShoutConfig.get("secondword");
			constructShout.words[2] = (String) customShoutConfig.get("thirdword");
			constructShout.words[3] = (String) customShoutConfig.get("name");
			constructShout.words[3] = (String) customShoutConfig.get("description");
			constructShout.playerCommands.put(1, (List<String>) (customShoutConfig.get("firstcommands")));
			constructShout.playerCommands.put(2, (List<String>) (customShoutConfig.get("secondcommands")));
			constructShout.playerCommands.put(3, (List<String>) (customShoutConfig.get("thirdcommands")));
			arngeir.ShoutTable.put(constructShout.words[0], constructShout);
			arngeir.ShoutTable.put(constructShout.words[0] + " " + constructShout.words[1], constructShout);
			arngeir.ShoutTable.put(constructShout.words[0] + " " + constructShout.words[1] + " " + constructShout.words[2], constructShout);
		}
		
		pm.registerEvents(arngeir,this);
		for(ShoutType blah:ShoutType.values()){
			if (blah.shout instanceof Listener){
				pm.registerEvents((Listener) blah.shout, this);
			}
		}
		log.info("The Thu'um" + getDescription().getVersion() + "loaded!");
	}
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

}
