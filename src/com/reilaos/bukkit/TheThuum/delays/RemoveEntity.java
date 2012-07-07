package com.reilaos.bukkit.TheThuum.delays;

import org.bukkit.entity.Entity;

public class RemoveEntity implements Runnable {
	
	Entity removeMe;
	boolean explode;
	
	public RemoveEntity(Entity victim){
		this.removeMe = victim;
		this.explode  = false;
	}
	
	public RemoveEntity(Entity victim, boolean explode){
		this.removeMe = victim;
		this.explode  = explode;
	}
	
	@Override
	public void run() {
		if (removeMe == null) return;
		if (explode  == true) removeMe.getWorld().createExplosion(removeMe.getLocation(), 0);
		if (!removeMe.isDead()) removeMe.remove();
	}
}