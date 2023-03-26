package br.com.chssoftware.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.chssoftware.main.Game;
import br.com.chssoftware.main.Sound;
import br.com.chssoftware.world.Camera;
import br.com.chssoftware.world.World;

public class BulletShoot extends Entity {

	private double dx;
	private double dy;
	private double spd = 4;
	private int life = 60, curLife = 0;
	private boolean explosionBullet;
	private int timeExplosion;

	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {
		if (World.isFree((int) (x + dx * spd), (int) (y + dy * spd), width, height)) {
			x += dx * spd;
			y += dy * spd;
			curLife++;
			if (curLife == life) {
				Game.bullets.remove(this);
				return;
			}
		} else {
			explosionBullet = true;
			timeExplosion++;
			if (timeExplosion == 7) {
				timeExplosion = 0;
				explosionBullet = false;
				Sound.explosionBullet.play();
				Game.bullets.remove(this);
				return;
			}
		}
	}

	public void render(Graphics g) {
		if (!explosionBullet) {
			g.setColor(Color.yellow);
			g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
		} else {
			g.drawImage(Entity.BULLET_EXPLOSION, this.getX() - Camera.x, this.getY() - Camera.y, width, height, null);
		}
	}

}
