package com.reilaos.bukkit.TheThuum.shouts;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.reilaos.bukkit.TheThuum.Plugin;
import com.reilaos.bukkit.TheThuum.Shout;

public class WuldNahKest implements Shout{
	static final double multiplier[] = {5, 6, 6.5};
	@Override
	public void shout(Player dovahkiin, int level) {
		Plugin.scheduler.scheduleSyncDelayedTask(Plugin.thisOne, new SetSpeed(dovahkiin.getVelocity(), dovahkiin), 8);
	
		Vector heading = dovahkiin.getEyeLocation().getDirection();
		
		Vector dash = new Vector();
		dash.copy(heading).setY(0).normalize();
		dash.multiply(multiplier[level-1]).setY(0.3);
		dovahkiin.setVelocity(dash);
		for(int i = 1; i < 2 + level; i++) {
			Plugin.scheduler.scheduleSyncDelayedTask(Plugin.thisOne, new SetSpeed(dash, dovahkiin), 4);
		}
		dovahkiin.getWorld().createExplosion(dovahkiin.getLocation(), 0);
	}
	
	class SetSpeed implements Runnable{
		Vector speed;
		Player dovahkiin;
		
		SetSpeed(Vector speed, Player dovahkiin){
			this.speed = speed;
			this.dovahkiin = dovahkiin;
		}
		@Override
		public void run() {
			dovahkiin.setVelocity(speed);
		}
	}
}
