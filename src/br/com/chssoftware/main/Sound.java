package br.com.chssoftware.main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

	private AudioClip audioClip;
	public static final Sound musicBackground = new Sound("/music.wav");
	public static final Sound hurtEffect = new Sound("/hurt.wav");
	public static final Sound laserShoot = new Sound("/laserShoot.wav");
	public static final Sound explosion = new Sound("/explosion.wav");
	public static final Sound explosionBullet = new Sound("/explosionBullet.wav");
	public static final Sound shootEnemy = new Sound("/shootEnemy.wav");
	public static final Sound lifePlus = new Sound("/lifePlus.wav");
	public static final Sound weaponPlus = new Sound("/weaponPlus.wav");
	public static final Sound bulletPlus = new Sound("/bulletPlus.wav");
	public static final Sound selectMenu = new Sound("/selectMenu.wav");
	public static final Sound step = new Sound("/step.wav");
	public Sound(String path) {
		try {
			audioClip = Applet.newAudioClip(Sound.class.getResource(path));
		} catch (Throwable e) {
		}
	}

	public void play() {
		try {
			new Thread() {
				public void run() {
					audioClip.play();
				}
			}.start();
		} catch (Throwable e) {
		}
	}

	public void loop() {
		try {
			new Thread() {
				public void run() {
					audioClip.loop();
				}
			}.start();
		} catch (Throwable e) {
		}
	}

}
