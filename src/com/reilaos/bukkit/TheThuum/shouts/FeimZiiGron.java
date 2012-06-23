package com.reilaos.bukkit.TheThuum.shouts;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.Effect;

import com.reilaos.bukkit.TheThuum.EffectTracker;
import com.reilaos.bukkit.TheThuum.Plugin;


/**
 * Become Ethereal 
 */
public class FeimZiiGron implements Shout,Listener {
	@Override
	public String[] words(){
		return new String[] {"feim", "zii", "gron"};
	}
	
	EffectTracker invincible = new EffectTracker();
	final int duration [] = {8, 13, 18};
	
	@Override
	public void shout(Player dovahkiin, int level) {
		invincible.add(dovahkiin, duration[level - 1] * 20);
		
		FeimZiiGronGlow task = new FeimZiiGronGlow(dovahkiin);
		task.id = Plugin.scheduler.scheduleSyncRepeatingTask(Plugin.thisOne, task, 0, 10);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		if(invincible.containsKey(event.getEntity())){
			event.setCancelled(true);
		}
		if (event instanceof EntityDamageByEntityEvent){
			invincible.remove(((EntityDamageByEntityEvent)event).getDamager());
		}
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		if (invincible.containsKey(event.getEntity()) && event.getFoodLevel() < ((Player)event.getEntity()).getFoodLevel()){
			event.setCancelled(true);
		}
	}
	
	private class FeimZiiGronGlow implements Runnable {
		int id;
		Player dovahkiin;
		FeimZiiGronGlow(Player dovahkiin){
			this.dovahkiin = dovahkiin;
		}
		@Override
		public void run() {
			if (invincible.containsKey(dovahkiin))
				dovahkiin.playEffect(dovahkiin.getLocation().add(0,1,0), Effect.ENDER_SIGNAL, 0);
			else Plugin.scheduler.cancelTask(id);
		}
		
	}
}
