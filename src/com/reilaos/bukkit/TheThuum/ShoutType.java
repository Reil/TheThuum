package com.reilaos.bukkit.TheThuum;

import com.reilaos.bukkit.TheThuum.shouts.FeimZiiGron;
import com.reilaos.bukkit.TheThuum.shouts.FusRoDah;
import com.reilaos.bukkit.TheThuum.shouts.KaanDremOv;
import com.reilaos.bukkit.TheThuum.shouts.LaasYahNir;
import com.reilaos.bukkit.TheThuum.shouts.LokVahKoor;
import com.reilaos.bukkit.TheThuum.shouts.WuldNahKest;
import com.reilaos.bukkit.TheThuum.shouts.YolToorShul;

/**
 * List of shouts
 */
enum ShoutType{
	FUSRODAH      (new FusRoDah()),
	YOLTOORSHUL   (new YolToorShul()),
	LOKVAHKOOR    (new LokVahKoor()),
	KAANDREMOV    (new KaanDremOv()),
	WULDNAHKEST   (new WuldNahKest()),
	FEIMZIIGRON   (new FeimZiiGron()),
	LAASYAHNIR    (new LaasYahNir());
	
	public Shout shout;
	private ShoutType(Shout shout){
		this.shout = shout;
	}
}