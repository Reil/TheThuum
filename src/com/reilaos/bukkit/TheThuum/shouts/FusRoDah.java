package com.reilaos.bukkit.TheThuum.shouts;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.reilaos.bukkit.TheThuum.Plugin;
import com.reilaos.bukkit.TheThuum.Shared;
import com.reilaos.bukkit.TheThuum.delays.Explosion;

/**
 * Unrelenting Force
 */
public class FusRoDah implements Shout {
	@Override
	public String[] words(){
		return new String[] {"fus", "ro", "dah"};
	}
	
	
	final double fusHoriStrength[] = {.5,2,7};
	final double fusVertStrength[] = {.5,.7,1.5};
	@Override
	public void shout(Player dragonBorn, int level){
		int distance = 5 * level;
		Vector heading = dragonBorn.getEyeLocation().getDirection();
		
		// Do the blasting
		Vector blastVector = new Vector();
		blastVector.copy(heading).setY(0).normalize();
		blastVector.multiply(fusHoriStrength[level-1]).setY(fusVertStrength[level-1]);
		for(Entity victim : Shared.getAreaOfEffect(dragonBorn, 4, distance)){
			victim.setVelocity(victim.getVelocity().add(blastVector));  // Toss'em in the direction the dragonborn's looking at!
		}
		
		// These are just cool explosions and sound effects that don't actually do anything.
		dragonBorn.getWorld().playEffect(dragonBorn.getLocation(), Effect.GHAST_SHOOT, 0, distance + 10);
		if (level >= 2) {
			World world = dragonBorn.getWorld();
			List<Block> sight = dragonBorn.getLineOfSight(null, 4);
			if (sight.size() >=0 ) world.createExplosion(sight.get(sight.size() - 1).getLocation(),0);
		}
		
		if (level == 3){
			List<Block> sight = dragonBorn.getLineOfSight(null, 32);
			for(int i = 8; i < 32 && i < sight.size() ; i += 6){
				Plugin.scheduler.scheduleSyncDelayedTask(Plugin.thisOne, new Explosion(sight.get(i).getLocation(), 0, false), i/3);
			}
			
		}
		return;
	}
}
