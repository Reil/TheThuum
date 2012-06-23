package com.reilaos.bukkit.TheThuum.shouts;

import java.util.LinkedList;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.SmallFireball;
import org.bukkit.util.Vector;

import com.reilaos.bukkit.TheThuum.Plugin;
import com.reilaos.bukkit.TheThuum.delays.RemoveEntity;

/**
 * Fire Breath
 */
public class YolToorShul implements Shout {
	@Override
	public String[] words(){
		return new String[] {"yol", "toor", "shul"};
	}
	
	@Override
	public void shout(Player dovahkiin, int level) {
		if(level > 3 || level < 0) return;
		
		World world = dovahkiin.getWorld();
		Location location = dovahkiin.getEyeLocation();
		Location spawnFireball = location.clone().add(location.getDirection().normalize());
		Vector trajectory = new Vector();
		trajectory.copy(location.getDirection()).normalize();
		
		Class<? extends Fireball> projectile;
		if (level == 3) projectile = SmallFireball.class;
		else projectile = SmallFireball.class;
		
		// Two of the 4 cardinal directions on a plane normal to the direction the player is looking
		LinkedList<Vector> Lateral = new LinkedList<Vector>();
		Vector LateralSide = new Vector(0,1,0).crossProduct(trajectory).normalize();
		Vector LateralTop = new Vector().copy(trajectory).crossProduct(LateralSide).normalize();
		Lateral.add(LateralTop);
		Lateral.add(new Vector().zero().subtract(LateralTop));
		Lateral.add(LateralSide);
		// This nested for-loop:
		// Level 2: Makes diagonals
		// Level 3: Makes diagonals and makes an outer circle.
		for (int i = level - 1; i > 0; i--) {
			LinkedList<Vector> newLateral = new LinkedList<Vector>();
			for(Vector combineOne : Lateral){
				for(Vector combineTwo : Lateral){
					Vector addMe = new Vector().copy(combineOne).add(combineTwo);
					if (!(level == 3 && i == 1)) addMe.normalize();
					newLateral.add(addMe);
				}
			}
			Lateral.addAll(newLateral);
		}
		
		// Make the other half of the circle (we only make 12 to 6 o'clock)
		LinkedList<Vector> newLateral = new LinkedList<Vector>();
		for (Vector flipMe : Lateral){
			newLateral.add(new Vector().zero().subtract(flipMe));
		}
		Lateral.addAll(newLateral);
		Lateral.add(new Vector().zero());
        // There will be many duplicate vectors.  This is okay.
		
		for(Vector offset : Lateral){
			Fireball fireball = world.spawn(spawnFireball.clone().add(offset), projectile);
			fireball.setVelocity(trajectory);
			fireball.setDirection(trajectory);		
			fireball.setShooter(dovahkiin);
			fireball.setIsIncendiary(true);
			fireball.setYield((float) 0.4);
			Plugin.scheduler.scheduleSyncDelayedTask(Plugin.thisOne, new RemoveEntity(fireball), 15); // Fireballs veer off for some reason.  Kill it before it goes too far.
		}
		
		dovahkiin.getWorld().playEffect(dovahkiin.getLocation(), Effect.BLAZE_SHOOT, 0, 40);
	}
}
