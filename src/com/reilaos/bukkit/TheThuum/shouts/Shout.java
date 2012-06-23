

package com.reilaos.bukkit.TheThuum.shouts;

import org.bukkit.entity.Player;

/**
 * All shouts should:
 * 1) implement this shout.
 * 2) Be added to the ShoutType enum.
 * 3) Have their default cooldowns added to config.yml
 */
public interface Shout {
	/**
	 * Function should provide a three-entry string array, each entry being the words of the shout, in order.
	 * @return A string array containing the words of this shout.
	 */
	public String[] words();
	
	/**
	 * This function is called when the player uses this shout. Should do everything needed for the shout.
	 * @param dovahkiin The player who used this shout.
	 * @param level     The level of the shout used.
	 */
	public void shout(Player dovahkiin, int level);
}
