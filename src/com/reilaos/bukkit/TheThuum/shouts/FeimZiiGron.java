package com.reilaos.bukkit.TheThuum.shouts;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;

import com.reilaos.bukkit.TheThuum.EffectTracker;
import com.reilaos.bukkit.TheThuum.Plugin;
import com.reilaos.bukkit.TheThuum.Shout;
import com.reilaos.bukkit.TheThuum.shouts.WuldNahKest.SetSpeed;

public class FeimZiiGron extends EntityListener implements Shout {
	EffectTracker invincible = new EffectTracker();
	final int duration [] = {8, 13, 18};
	
	@Override
	public void shout(Player dovahkiin, int level) {
		invincible.add(dovahkiin, duration[level - 1] * 20);
		dovahkiin.playEffect(EntityEffect.WOLF_HEARTS);
		
		FeimZiiGronGlow task = new FeimZiiGronGlow(dovahkiin, duration[level-1] * 2);
		task.id = Plugin.scheduler.scheduleSyncRepeatingTask(Plugin.thisOne, task, 0, 10);
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event){
		if(invincible.containsKey(event.getEntity())){
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		if (invincible.containsKey(event.getEntity()) && event.getFoodLevel() < ((Player)event.getEntity()).getFoodLevel()){
			event.setCancelled(true);
		}
	}
	
	private class FeimZiiGronGlow implements Runnable {
		int id;
		Player dovahkiin;
		int duration;
		FeimZiiGronGlow(Player dovahkiin, int duration){
			this.dovahkiin = dovahkiin;
			this.duration = duration;
		}
		@Override
		public void run() {
			dovahkiin.playEffect(dovahkiin.getLocation().add(0,1,0), Effect.ENDER_SIGNAL, 0);
			if(duration-- <=0 ) Plugin.scheduler.cancelTask(id);
		}
		
	}
}
