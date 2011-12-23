// Use this with BukkitScheduler to make an explosion happen in the future. -- Reil

package com.reilaos.bukkit.TheThuum.delays;

import org.bukkit.Location;

public class Explosion implements Runnable {
	
	Location explodeHere;
	int strength;
	boolean fire;
	
	public Explosion(Location location, int power, boolean setFire){
		explodeHere = location;
		fire = setFire;
		strength = power;
	}
	
	@Override
	public void run() {
		explodeHere.getWorld().createExplosion(explodeHere, strength, fire);			
	}
}