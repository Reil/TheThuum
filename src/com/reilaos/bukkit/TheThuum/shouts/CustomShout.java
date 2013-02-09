/**
 * 
 */
package com.reilaos.bukkit.TheThuum.shouts;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

/**
 * @author Francis
 *
 */
public class CustomShout implements Shout {

	/* (non-Javadoc)
	 * @see com.reilaos.bukkit.TheThuum.shouts.Shout#words()
	 */
	public HashMap<Integer,List<String>> playerCommands = new HashMap<Integer,List<String>>();
	public String[] words = {"", "", "", "", ""};
	
	@Override
	public String[] words() {
		return words;
	}

	/* (non-Javadoc)
	 * @see com.reilaos.bukkit.TheThuum.shouts.Shout#shout(org.bukkit.entity.Player, int)
	 */
	@Override
	public void shout(Player dovahkiin, int level) {
		List<String> doThese = playerCommands.get(level);
		if (doThese == null) return;
		
		for(String doThis : doThese){
			dovahkiin.performCommand(doThis);
		}
	}

}
