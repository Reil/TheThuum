package com.reilaos.bukkit.TheThuum.shouts;


/**
 * List of shouts
 */
public enum ShoutType{
	FUSRODAH      (new FusRoDah()),
	YOLTOORSHUL   (new YolToorShul()),
	LOKVAHKOOR    (new LokVahKoor()),
	KAANDREMOV    (new KaanDremOv()),
	WULDNAHKEST   (new WuldNahKest()),
	FEIMZIIGRON   (new FeimZiiGron()),
	LAASYAHNIR    (new LaasYahNir()),
	HUNKAALZOOR   (new HunKaalZoor()),
	KRIILUNAUS    (new KriiLunAus()),
	STRUNBAHQO    (new StrunBahQo());
	
	public Shout shout;
	private ShoutType(Shout shout){
		this.shout = shout;
	}
}