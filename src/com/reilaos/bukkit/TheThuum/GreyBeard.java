/* Deals with shout parsing, cooldowns, permission checking, then lets the
 * shout classes do their thing. */

package com.reilaos.bukkit.TheThuum;

import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Set;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.reilaos.bukkit.TheThuum.shouts.ShoutType;


public class GreyBeard implements Listener{
	
	// Parses chat to see if it's a shout.  Determines level of the shout.
	// Does parsing only.  Permissions and the like are handled by shout()
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {		
		String[] message = event.getMessage().toLowerCase().replaceAll("[^A-Za-z\\s]", "").split(" ", 4);
		int length = message.length;
		
		if (length == 4) return;  // There are no 4-word shouts.
		
		shouting: for (ShoutType shoutType : ShoutType.values()){
			int power;
			for(power = 0; power < length; power++){
				if(!message[power].equalsIgnoreCase(shoutType.shout.words()[power])) continue shouting;
			}
			shout(event.getPlayer(), shoutType, power);
			return;
		}
	}
	
	// Checks permissions, cooldown, performs shout if this checks out.
	public static void shout(Player dragonBorn, ShoutType word, int level){
		if (level > 3 || level < 0) return;
		
		if (!Shared.cascadePermission(dragonBorn,"thuum.shout." + word.toString().toLowerCase() + "." + level)) return;
		
		if (!Shared.cascadePermission(dragonBorn, "thuum.ignorecooldown." + word.toString().toLowerCase() + "." + level)) {
			if (!Plugin.thisOne.arngeir.putOnCooldown(dragonBorn, word, level)) {
				dragonBorn.sendMessage(Plugin.thisOne.getConfig().getString("cooldown.alert message"));
				return;
			}
		}
		word.shout.shout(dragonBorn, level);
	}
	
	
	/**
	 *  Cooldown handling stuff
	 */
	// Good practice would put this as its own player listener, but still.
	
	Configuration shoutCooldowns;	
	Hashtable<Player, Set<ShoutType>> onCooldown = new Hashtable<Player, Set<ShoutType>>();
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		int persistence = Plugin.thisOne.getConfig().getInt("cooldown.persistence");
		Plugin.scheduler.scheduleSyncDelayedTask(Plugin.thisOne, new ClearCooldowns(event.getPlayer()), persistence * 20);
	}
	
	// If not already on cooldown, puts this shout on cooldown and returns true
	// Returns false if the player is already on cooldown.
	public boolean putOnCooldown(Player dovahkiin, ShoutType shout, int level){
		int cooldownDuration = (int) (Plugin.thisOne.getConfig().getDoubleList("shouts." + shout.toString().toLowerCase()).get(level - 1) * 20);
		
		if (Plugin.thisOne.getConfig().getBoolean("single cooldown", true)) shout = ShoutType.FUSRODAH;
		
		if (!onCooldown.containsKey(dovahkiin)) onCooldown.put(dovahkiin, EnumSet.noneOf(ShoutType.class));
		if (onCooldown.get(dovahkiin).contains(shout)) return false;

		onCooldown.get(dovahkiin).add(shout);
		Cooldown task = new Cooldown(dovahkiin, shout);
		Plugin.scheduler.scheduleSyncDelayedTask(Plugin.thisOne, task,cooldownDuration);
		return true;
	}
		
	public class Cooldown implements Runnable{
		Player dovahkiin;
		ShoutType shout;
		
		public Cooldown(Player player, ShoutType shoutType){
			dovahkiin = player;
			shout = shoutType;
		}
		
		@Override
		public void run() {
			if (!onCooldown.containsKey(dovahkiin)) return;
			onCooldown.get(dovahkiin).remove(shout);
			dovahkiin.sendMessage(Plugin.thisOne.getConfig().getString("cooldown.ready message"));
		}
	}
	
	public class ClearCooldowns implements Runnable {
		Player dovahkiin;
		
		public ClearCooldowns (Player player){
			dovahkiin = player;
		}
		
		@Override
		public void run() {
			if (!dovahkiin.isOnline()) onCooldown.remove(dovahkiin);
		}
	}
}