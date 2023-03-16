package br.com.chssoftware.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import br.com.chssoftware.main.Game;
import br.com.chssoftware.world.Camera;
import br.com.chssoftware.world.World;

public class Enemy extends Entity {

	private double speed = 1;
	private int maskx = 8, masky = 8, maskw = 10, maskh = 10;
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;

	private BufferedImage[] sprites;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		sprites = new BufferedImage[2];
		for (int i = 0; i < sprites.length; i++) {
			sprites[i] = Game.spritesheet.getSprite(112 + (i * 16), 16, width, height);
		}
	}

	public void tick() {
		if (!isCollidingWithPlayer()) {
			if (((int) x < Game.player.getX()) && World.isFree((int) (x + speed), this.getY())
					&& !isColliding((int) (x + speed), this.getY())) {
				x += speed;
			} else if (((int) x > Game.player.getX()) && World.isFree((int) (x - speed), this.getY())
					&& !isColliding((int) (x - speed), this.getY())) {
				x -= speed;
			}
			if (((int) y < Game.player.getY()) && World.isFree(this.getX(), (int) (y + speed))
					&& !isColliding(this.getX(), (int) (y + speed))) {
				y += speed;
			} else if (((int) y > Game.player.getY()) && World.isFree(this.getX(), (int) (y - speed))
					&& !isColliding(this.getX(), (int) (y - speed))) {
				y -= speed;
			}
		} else {
			if (Game.rand.nextInt(100) < 10) {
				double life = Game.player.getLife();
				life -= Game.rand.nextInt(3);
				Game.player.setLife(life);
				Game.player.setDamaged(true);
			}
		}
		// Dinâmica de troca das sprites do Enemy
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index > maxIndex) {
				index = 0;
			}
		}
	}

	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Player gamePlayer = Game.player;
		Rectangle player = new Rectangle(gamePlayer.getX(), gamePlayer.getY(), gamePlayer.getWidth(), gamePlayer.getHeight());
		return enemyCurrent.intersects(player);
	}

	public boolean isColliding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this) {
				continue;
			}
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}

	public void render(Graphics g) {
		super.render(g);
		g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
//		g.setColor(Color.blue);
//		g.fillRect(this.getX() - Camera.x, this.getY() - Camera.y, this.width, this.height);
	}
}
