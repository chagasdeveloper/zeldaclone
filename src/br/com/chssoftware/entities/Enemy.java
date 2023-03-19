package br.com.chssoftware.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import br.com.chssoftware.main.Game;
import br.com.chssoftware.main.Sound;
import br.com.chssoftware.world.Camera;
import br.com.chssoftware.world.World;

public class Enemy extends Entity {

	private double speed = 0.9;
	private int maskx = 8, masky = 8, maskw = 10, maskh = 10;
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;
	private int life = 3;
	private boolean isDamaged = false;
	private int damagedFrames = 10, damagedCurrent = 0;

	private BufferedImage[] sprites;

	public Enemy(int x, int y, int width, int height, BufferedImage[] sprites) {
		super(x, y, width, height, null);
		this.sprites = sprites;
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
				Sound.hurtEffect.play();
				Game.player.setDamaged(true);
				double life = Game.player.getLife();
				life -= Game.rand.nextInt(3);
				Game.player.setLife(life);
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
		// tempo da Sprite de dano
		if (isDamaged) {
			damagedCurrent++;
			if (damagedCurrent == damagedFrames) {
				isDamaged = false;
				damagedCurrent = 0;
			}
		}

		if (life <= 0) {
			destroySelf();
			return;
		}
		collidingBullet();
	}

	public void destroySelf() {
		Sound.explosion.play();
		Game.entities.remove(this);
		Game.enemies.remove(this);
	}

	public void collidingBullet() {
		for (int i = 0; i < Game.bullets.size(); i++) {
			BulletShoot bulletShoot = Game.bullets.get(i);
			if (Entity.isColliding(bulletShoot, this)) {
				Sound.shootEnemy.play();
				life--;
				isDamaged = true;
				Game.bullets.remove(bulletShoot);
				return;
			}
		}
	}

	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Player gamePlayer = Game.player;
		Rectangle player = new Rectangle(gamePlayer.getX(), gamePlayer.getY(), gamePlayer.getWidth(),
				gamePlayer.getHeight());
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
		if (!isDamaged) {
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else {
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
//		g.setColor(Color.blue);
//		g.fillRect(this.getX() - Camera.x, this.getY() - Camera.y, this.width, this.height);
	}
}
