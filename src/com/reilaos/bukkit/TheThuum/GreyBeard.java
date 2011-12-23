/* Deals with shout parsing, cooldowns, permission checking, then lets the
 * shout classes do their thing. */

package com.reilaos.bukkit.TheThuum;

import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Set;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.reilaos.bukkit.TheThuum.shouts.FusRoDah;
import com.reilaos.bukkit.TheThuum.shouts.KaanDremOv;
import com.reilaos.bukkit.TheThuum.shouts.LokVahKoor;
import com.reilaos.bukkit.TheThuum.shouts.YolToorShul;


public class GreyBeard extends PlayerListener{
	
	/**
	 * Shout parsing/handling stuff
	 */
	// To add a shout, put it into this enum.
	// Also, put default cooldowns in the config.yml
	// DON'T TOUCH ANYTHING ELSE IF ALL YOU'RE DOING
	// IS ADDING A SHOUT.
	enum ShoutType{
		FUSRODAH      ("fus",  "ro",   "dah",  new FusRoDah()),
		YOLTOORSHUL   ("yol",  "toor", "shul", new YolToorShul()),
		LOKVAHKOOR    ("lok",  "vah",  "koor", new LokVahKoor()),
		KAANDREMOV    ("kaan", "drem", "ov",   new KaanDremOv());
		
		public final String[] words;
		public Shout callMe;
		private ShoutType(String one, String two, String three, Shout shoutFunction){
			words = new String[]{one, two, three};
			callMe = shoutFunction;
		}
	}
	
	// Parses chat to see if it's a shout based off data in the shoutHandler class.  Determines level of the shout.
	// Does parsing only.  Permissions and the like are handled by shout()
	@Override
	public void onPlayerChat(PlayerChatEvent event) {		
		String[] message = event.getMessage().toLowerCase().replaceAll("[^A-Za-z\\s]", "").split(" ", 4);
		int length = message.length;
		
		if (length == 4) return;  // There are no 4-word shouts.
		
		shouting: for (ShoutType shout : ShoutType.values()){
			int power;
			for(power = 0; power < length; power++){
				if(!message[power].equalsIgnoreCase(shout.words[power])) continue shouting;
			}
			shout(event.getPlayer(), shout, power);
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
		 word.callMe.shout(dragonBorn, level);
	}
	
	
	/**
	 *  Cooldown handling stuff
	 */
	// Good practice would put this as its own player listener, but still.
	
	Configuration shoutCooldowns;	
	Hashtable<Player, Set<ShoutType>> onCooldown = new Hashtable<Player, Set<ShoutType>>();
	
	@Override
	public void onPlayerQuit(PlayerQuitEvent event){
		onCooldown.remove(event.getPlayer());
	}
	
	// If not already on cooldown, puts this shout on cooldown and returns true
	// Returns false if the player is already on cooldown.
	public boolean putOnCooldown(Player dovahkiin, ShoutType shout, int level){
		int cooldownDuration = (int) (Plugin.thisOne.getConfig().getDoubleList("shouts." + shout.toString().toLowerCase()).get(level - 1) * 20);
		
		if (Plugin.thisOne.getConfig().getBoolean("single cooldown", true)) shout = ShoutType.FUSRODAH;
		
		if (!onCooldown.containsKey(dovahkiin)) onCooldown.put(dovahkiin, EnumSet.noneOf(ShoutType.class));
		if (onCooldown.get(dovahkiin).contains(shout)) return false;

		onCooldown.get(dovahkiin).add(shout);
		cooldown task = new cooldown(dovahkiin, shout);
		Plugin.scheduler.scheduleSyncDelayedTask(Plugin.thisOne, task,cooldownDuration);
		return true;
	}
	
	
	public class cooldown implements Runnable{
		Player dovahkiin;
		ShoutType shout;
		
		public cooldown(Player player, ShoutType shoutType){
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
}