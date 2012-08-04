package com.reilaos.bukkit.TheThuum.shouts;

import java.util.HashSet;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.reilaos.bukkit.TheThuum.EffectTracker;
import com.reilaos.bukkit.TheThuum.Shared;

public class KriiLunAus implements Shout, Listener{
	String words[] = new String[] {"krii", "lun", "aus", "Marked For Death", "Reduces armor and drains HP."};
	EffectTracker marked = new EffectTracker();
	int damage[] = {8196, 8260, 16420};
	
	@Override
	public String[] words() {
		return words;
	}

	@Override
	public void shout(Player dovahkiin, int level) {
		HashSet<Entity> addThese = new HashSet<Entity>();
		for (Entity victim : Shared.getAreaOfEffect(dovahkiin, 4, 10)){
			if (victim instanceof LivingEntity){
				addThese.add(victim);
				((LivingEntity)victim).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1200, damage[level - 1]));
			}
		}
		marked.addAll(addThese, 1200);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if (marked.contains(event.getEntity())){
			event.setDamage(event.getDamage() + 1);
		}
	}

}
