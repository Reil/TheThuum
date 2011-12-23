package com.reilaos.bukkit.TheThuum;

import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.reilaos.bukkit.TheThuum.GreyBeard.ShoutType;

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
		pm.registerEvent(Event.Type.PLAYER_CHAT, arngeir, Priority.Monitor, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, arngeir, Priority.Monitor, this);
		pm.registerEvent(Event.Type.ENTITY_TARGET, (EntityListener) ShoutType.KAANDREMOV.callMe, Priority.Monitor, this);
		log.info("The Thu'um" + getDescription().getVersion() + "loaded!");
	}
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

}
