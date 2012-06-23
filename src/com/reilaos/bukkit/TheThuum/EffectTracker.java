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

import java.util.Collection;
import java.util.Hashtable;

import org.bukkit.entity.Entity;

public class EffectTracker extends Hashtable<Entity, EffectTracker.EffectCooldown> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6363176253052499818L;
	
	/**
	 * 
	 * @param effectsThis The entity you want to put into the set.
	 * @param duration  Time you want it to remain in the set.  In server ticks, not seconds.
	 */
	public void add (Entity effectsThis, int duration){
		EffectCooldown cooldown = new EffectCooldown(effectsThis);
		Plugin.scheduler.scheduleSyncDelayedTask(Plugin.thisOne, cooldown, duration);
		put(effectsThis, cooldown);
	}
	
	/**
	 * If you have more than 1 thing to add with the same duration, use this instead of using add a whole bunch.  It spins off fewer tasks.
	 * @param effectsThese Set of entities that you want added to the set.
	 * @param duration Time you want them to remain in the set.  In server ticks, not seconds.
	 */
	public void addAll(Collection<Entity> effectsThese, int duration){
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
			if(containsKey(target) && get(target) == this){
				remove(target);
			}
		}
	}
	
	public class EffectCooldownSet extends EffectCooldown {
		Collection<Entity> targets;
		public EffectCooldownSet (Collection<Entity> effectsThese){
			super(null);
			targets = effectsThese;
		}
		@Override
		public void run() {
			for(Entity target : targets){
				if(containsKey(target) && get(target) == this){
					remove(target);
				}
			}
		}
	}
}
