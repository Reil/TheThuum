package com.reilaos.bukkit.TheThuum;

import java.util.logging.Logger;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

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
