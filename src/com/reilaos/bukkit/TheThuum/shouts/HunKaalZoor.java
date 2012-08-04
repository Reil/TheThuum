package com.reilaos.bukkit.TheThuum.shouts;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.reilaos.bukkit.TheThuum.Plugin;
import com.reilaos.bukkit.TheThuum.delays.RemoveEntity;

public class HunKaalZoor implements Shout {
	
	String[] words = {"hun", "kaal", "zoor", "Call of Valor", "Summons a hero of Sovngarde."};
	
	@Override
	public String[] words(){
		return words;
	}
	
	private EntityType[] heroes = {EntityType.SNOWMAN, EntityType.WOLF, EntityType.IRON_GOLEM};
	
	@Override
	public void shout(Player dovahkiin, int level) {
		Location spawnHere = dovahkiin.getLastTwoTargetBlocks(null, 30).get(0).getLocation();
		LivingEntity hero = spawnHere.getWorld().spawnCreature(spawnHere, heroes[level-1]);
		if (hero instanceof Tameable)
			((Tameable)  hero).setOwner(dovahkiin);
		else if (hero instanceof IronGolem)
			((IronGolem) hero).setPlayerCreated(true);
		
		
		hero.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 20));
		spawnHere.getWorld().createExplosion(spawnHere, 0, false);
		
		Plugin.scheduler.scheduleSyncDelayedTask(Plugin.thisOne, new RemoveEntity(hero, true), 1200);
	}
	
}
