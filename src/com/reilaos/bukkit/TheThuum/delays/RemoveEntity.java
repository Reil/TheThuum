package com.reilaos.bukkit.TheThuum.delays;

import org.bukkit.entity.Entity;

public class RemoveEntity implements Runnable {
	
	Entity removeMe;
	
	public RemoveEntity(Entity Victim){
		removeMe = Victim;
	}
	
	@Override
	public void run() {
		if (removeMe != null && !removeMe.isDead()) removeMe.remove();			
	}
}