package com.reilaos.bukkit.TheThuum.shouts;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.reilaos.bukkit.TheThuum.EffectTracker;
import com.reilaos.bukkit.TheThuum.Plugin;

/**
 * Aura Whisper
 */
public class LaasYahNir implements Shout {
	@Override
	public String[] words() {
		return new String[] {"las", "yah", "nir"};
	}
	
	EffectTracker glowing = new EffectTracker();
	Glow task;
	
	@Override
	public void shout(Player dovahkiin, int level) {
		if (task == null){
			task = new Glow();
			task.id = Plugin.scheduler.scheduleSyncRepeatingTask(Plugin.thisOne, task, 0, 20);
		}
		List<Entity> entities = dovahkiin.getNearbyEntities(90,90,90);
		for(Entity test : entities){
			if (!(test instanceof LivingEntity)){
				entities.remove(test);
			}
		}
		glowing.addAll(entities, 200*level);
		return;
	}
	
	class Glow implements Runnable {
		int id;
		@Override
		public void run() {
			if (glowing.isEmpty()) {
				Plugin.scheduler.cancelTask(id);
				task = null;
				return;
			}
			for (Entity glows : glowing.keySet()){
				glows.getWorld().playEffect(glows.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
			}
		}
	}
	
}
