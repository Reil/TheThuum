// Some shared functions that multiple shouts might want to use.
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
	/** Gets all the entities contained in a cylinder starting facing along the dragonBorn's line of sight.
	 * @param dragonBorn The player whose pov we are basing the cylinder off of.
	 * @param radius The radius of the cylinder
	 * @param length The approximate length of the cylinder (actual cylunder may be between length and sqrt(3*length^2) long
	 */
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
		if (dovahkiin.hasPermission(permission)) return true;
		for(String segment: segments){
			builder.append(segment + ".");
			if(dovahkiin.hasPermission(builder.toString() + "*")) return true;
		}
		
		return false;
	}
}
