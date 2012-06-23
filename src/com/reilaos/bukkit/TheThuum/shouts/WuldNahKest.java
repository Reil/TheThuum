package com.reilaos.bukkit.TheThuum.shouts;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.reilaos.bukkit.TheThuum.Plugin;

/**
 * Whirlwind Sprint
 */
public class WuldNahKest implements Shout{
	@Override
	public String[] words(){
		return new String[] {"wuld", "nah", "kest"};
	}
	
	static final double multiplier[] = {5.5, 5.8, 6.6};
	@Override
	public void shout(Player dovahkiin, int level) {
		Plugin.scheduler.scheduleSyncDelayedTask(Plugin.thisOne, new SetSpeed(dovahkiin.getVelocity(), dovahkiin, 1), 3 + level);
	
		Vector heading = dovahkiin.getEyeLocation().getDirection();
		
		Vector dash = new Vector();
		dash.copy(heading).setY(0).normalize();
		dash.multiply(multiplier[level-1]).setY(0.3);
		dovahkiin.setVelocity(dash);
		
		SetSpeed task = new SetSpeed(dash, dovahkiin, 2 + level);
		task.id = Plugin.scheduler.scheduleSyncRepeatingTask(Plugin.thisOne, task, 0, 1);
		
		dovahkiin.getWorld().createExplosion(dovahkiin.getLocation(), 0);
	}
	
	class SetSpeed implements Runnable{
		int id;
		int ticks;
		Vector speed;
		Player dovahkiin;
		
		SetSpeed(Vector speed, Player dovahkiin, int ticks){
			this.speed = speed;
			this.dovahkiin = dovahkiin;
			this.ticks = ticks;
		}
		@Override
		public void run() {
			dovahkiin.setVelocity(speed);
			if(ticks-- == 0) {
				Plugin.scheduler.cancelTask(id);
			}
		}
	}
}
