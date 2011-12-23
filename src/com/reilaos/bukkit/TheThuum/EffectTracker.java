/*
 * Essentially a HashSet where members only spend a certain amount of time inside.
 * Adding a member to the tracker will 'refresh' its time to live.
 * I.E. Put entity Bob in tracker bleh for 5 ticks at t=0.
 *      Put entity bob in tracker bleh for 5 ticks again at t=3;
 *      Bob will remain in bleh from t = 0 to t = 8 inclusive,
 *      and doesn't get evicted at t = 5.
 *      
 * NOTE: Use the addAll instead of add when there are multiple things to add
 *       whenever you can!  AddAll makes only one new task for everything
 *       in the set, while using add multiple times in the same tick creates
 *       a new task for each one.
 */


package com.reilaos.bukkit.TheThuum;

import java.util.Hashtable;
import java.util.Set;

import org.bukkit.entity.Entity;

public class EffectTracker extends Hashtable<Entity, EffectTracker.EffectCooldown> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6363176253052499818L;
	
	public void add (Entity effectsThis, int duration){
		EffectCooldown cooldown = new EffectCooldown(effectsThis);
		Plugin.scheduler.scheduleSyncDelayedTask(Plugin.thisOne, cooldown, duration);
		put(effectsThis, cooldown);
	}
	
	public void addAll(Set<Entity> effectsThese, int duration){
		EffectCooldown cooldown = new EffectCooldownSet(effectsThese);
		Plugin.scheduler.scheduleSyncDelayedTask(Plugin.thisOne, cooldown, duration);
		for (Entity effectsThis : effectsThese)	put(effectsThis, cooldown);
	}
	
	public class EffectCooldown implements Runnable{
		Entity target;
		public EffectCooldown (Entity effectsThis){
			target = effectsThis;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(containsKey(target) && get(target) == this){
				remove(target);
			}
		}
	}
	
	public class EffectCooldownSet extends EffectCooldown {
		Set<Entity> targets;
		public EffectCooldownSet (Set<Entity> effectsThese){
			super(null);
			targets = effectsThese;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			for(Entity target : targets){
				if(containsKey(target) && get(target) == this){
					remove(target);
				}
			}
		}
	}
}
