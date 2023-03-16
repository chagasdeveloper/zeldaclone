package br.com.chssoftware.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.chssoftware.main.Game;
import br.com.chssoftware.world.Camera;

public class BulletShoot extends Entity {

	private double dx;
	private double dy;
	private double spd = 4;
	private int life = 60, curLife = 0;
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	

	public void tick() {
		x += dx * spd;
		y += dy * spd;
		curLife++;
		if (curLife == life) {
			Game.bullets.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
	}

}
