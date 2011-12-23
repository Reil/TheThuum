// Some shared functions that shouts might want to use.
// Generally a bunch of vector math/entity gathering.
package com.reilaos.bukkit.TheThuum;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Shared {
	// Just a general-purpose function some shouts will use
	// Gets all the entities contained in a cylinder starting 
	// that is radius wide and approximately length long (may actually be between length and sqrt(2*length^2) long 
	public static List<Entity> getAreaOfEffect(Player dragonBorn, int radius, int length){
		Location epicenter = dragonBorn.getEyeLocation();
		Vector heading = epicenter.getDirection();
		List<Entity> returnMe = new LinkedList<Entity>();
		
		length *= 2;
		for(Entity victim : dragonBorn.getNearbyEntities(length, length, length)){
			Vector dragonBornToVictim = victim.getLocation().subtract(epicenter).toVector();
			double dotProduct = dragonBornToVictim.dot(heading);
			
			if(dotProduct < 0) continue; // This entity is behind the dovahkiin
			if(dragonBornToVictim.lengthSquared() - dotProduct * dotProduct > radius*radius) continue; // Entity is too far laterally from the shout.
			
			returnMe.add(victim);
		}
		return returnMe;
	}
	
	public static boolean cascadePermission(Player dovahkiin, String permission){
		StringBuilder builder = new StringBuilder();
		String[] segments = permission.split("\\.");
		
		if (dovahkiin.hasPermission("*")) return true;
		for(int i = 0; i < segments.length - 1; i++){
			builder.append(segments[i] + ".");
			if(dovahkiin.hasPermission(builder.toString() + "*")) return true;
		}
		if(dovahkiin.hasPermission(permission)) return true;
		
		return false;
	}
}
