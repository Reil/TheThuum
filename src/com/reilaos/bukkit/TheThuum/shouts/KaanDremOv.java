package com.reilaos.bukkit.TheThuum.shouts;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import com.reilaos.bukkit.TheThuum.EffectTracker;
import com.reilaos.bukkit.TheThuum.Shared;

/**
 * Kyne's Peace
 */
public class KaanDremOv implements Shout,Listener{
	@Override
	public String[] words(){
		return new String[] {"kaan", "drem", "ov"};
	}
	
	EffectTracker peaced = new EffectTracker();
	@Override
	public void shout(Player dovahkiin, int level){
		Set<Entity> peaceThese = new HashSet<Entity>();
		for (Entity toPeace : Shared.getAreaOfEffect(dovahkiin, 4 + 2 * level, 15 + 5 * level)){
			if (toPeace instanceof Creature){
				((Creature) toPeace).setTarget(null);
				peaceThese.add(toPeace);
			}
		}
		dovahkiin.sendMessage("You calmed " + peaceThese.size() + " creatures.");
		peaced.addAll(peaceThese, (30+20*level) * 20);
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event){
		if(peaced.containsKey(event.getEntity())){
			event.setCancelled(true);
		}
	}
}
