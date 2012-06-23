package com.reilaos.bukkit.TheThuum.shouts;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.reilaos.bukkit.TheThuum.Plugin;


/**
 * Clear Skies
 */
public class LokVahKoor implements Shout {
	@Override
	public String[] words(){
		return new String[] {"lok", "vah", "koor"};
	}
	
	@Override
	public void shout(Player dovahkiin, int level) {
		World world = dovahkiin.getWorld();
		if (level != 3){
			int stormDuration = 0;
			int thunderDuration = 0;
			if (world.hasStorm()){
				stormDuration = world.getWeatherDuration();
				if (world.isThundering())	thunderDuration = world.getThunderDuration();
				Plugin.scheduler.scheduleSyncDelayedTask(Plugin.thisOne, new Storm(world, thunderDuration, stormDuration), level * 200);
			}
		}
		world.setStorm(false);
	}
	
	private class Storm implements Runnable{
		World world;
		int stormDuration;
		int thunderDuration;
		public Storm(World world, int thunderDuration, int stormDuration){
			this.world = world;
			this.thunderDuration = thunderDuration;
			this.stormDuration = stormDuration;
		}
		@Override
		public void run() {
			world.setStorm(true);
			if (thunderDuration != 0) world.setThundering(true);
			world.setWeatherDuration(stormDuration);
			world.setThunderDuration(thunderDuration);
		}
	}
}
