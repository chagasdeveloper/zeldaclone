package br.com.chssoftware.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import br.com.chssoftware.graphics.Spritesheet;
import br.com.chssoftware.main.Game;
import br.com.chssoftware.world.Camera;
import br.com.chssoftware.world.World;

public class Player extends Entity {

	private boolean up, left, down, right;
	private int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	private int dir = right_dir;
	private double speed = 1.5;
	// ------------------------
	private double life = 100;
	private double maxLife = 100;
	// ------------------------
	private int ammo = 0;
	private boolean isDamaged = false;
	private int damagedFrames = 0;
	private boolean hasGun = false;
	private boolean shoot = false;

	private int frames = 0, maxFrames = 10, index = 0, maxIndex = 3;
	private boolean moved = false;

	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage playerDamaged;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];
		playerDamaged = Game.spritesheet.getSprite(0, 16, 16, 16);
		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, width, height);
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, width, height);
			upPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 32, width, height);
			downPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 48, width, height);
		}
	}

	public void tick() {
		moved = false;
		if (isRight() && World.isFree((int) (x + speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x += speed;
		} else if (isLeft() && World.isFree((int) (x - speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}
		if (isUp() && World.isFree(this.getX(), (int) (y - speed))) {
			moved = true;
			dir = up_dir;
			y -= speed;
		} else if (isDown() && World.isFree(this.getX(), (int) (y + speed))) {
			moved = true;
			dir = down_dir;
			y += speed;
		}

		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		}
		checkCollision();
		if (isDamaged) {
			damagedFrames++;
			if (damagedFrames == 8) {
				damagedFrames = 0;
				isDamaged = false;
			}

		}
		// Sistema de tiro
		if (shoot && hasGun && (ammo > 0)) {
			// Criar Bala e atirar
			shoot = false;
			ammo--;
			int dx = 0;
			int dy = 0;
			int px = 0;
			int py = 0;
			if (dir == right_dir) {
				dx = 1;
				px = 17;
				py = 6;
			} else if (dir == up_dir) {
				dx = 1;
				px = 17;
				py = 6;
			} else if (dir == down_dir) {
				dx = 1;
				px = 17;
				py = 6;
			} else {
				dx = -1;
				px = -5;
				py = 6;
			}
			BulletShoot bulletShoot = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
			Game.bullets.add(bulletShoot);
		}

		if (life <= 0) {
			Game.entities.clear();
			Game.enemies.clear();
			Game.entities = new ArrayList<>();
			Game.enemies = new ArrayList<>();
			Game.spritesheet = new Spritesheet("/spritesheet.png");
			Game.player = new Player(120, 80, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
			Game.entities.add(Game.player);
			Game.world = new World("/map.png");
			return;
		}

		checkCollision();
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}

	public void checkCollision() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			// Lifepack
			if (atual instanceof Lifepack) {
				if (Entity.isColliding(this, atual)) {
					if (life < 100) {
						life += 25;
						if (life > 100) {
							life = 100;
						}
						Game.entities.remove(atual);
					}
				}
				// Bullet
			} else if (atual instanceof Bullet) {
				if (Entity.isColliding(this, atual)) {
					ammo += 10;
					Game.entities.remove(atual);
				}
				// Weapon
			} else if (atual instanceof Weapon) {
				if (Entity.isColliding(this, atual)) {
					hasGun = true;
					Game.entities.remove(atual);
				}
			}
		}
	}

	public void render(Graphics g) {
		if (!isDamaged) {
			if (dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					g.drawImage(Entity.GUN_RIGHT, this.getX() + 8 - Camera.x, this.getY() + 2 - Camera.y, null);
				}
			} else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					g.drawImage(Entity.GUN_LEFT, this.getX() - 8 - Camera.x, this.getY() + 2 - Camera.y, null);
				}
			} else if (dir == up_dir) {
				g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					g.drawImage(Entity.GUN_UP, this.getX() + 6 - Camera.x, this.getY() + 2 - Camera.y, null);
				}
			} else if (dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					g.drawImage(Entity.GUN_RIGHT, this.getX() + 8 - Camera.x, this.getY() + 2 - Camera.y, null);
				}
			}
		} else {
			g.drawImage(playerDamaged, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
	// Encapsulamentos

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public double getLife() {
		return life;
	}

	public void setLife(double life) {
		this.life = life;
	}

	public double getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(double maxLife) {
		this.maxLife = maxLife;
	}

	public int getAmmo() {
		return ammo;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public boolean isDamaged() {
		return isDamaged;
	}

	public void setDamaged(boolean isDamaged) {
		this.isDamaged = isDamaged;
	}

	public boolean isHasGun() {
		return hasGun;
	}

	public void setHasGun(boolean hasGun) {
		this.hasGun = hasGun;
	}

	public boolean isShoot() {
		return shoot;
	}

	public void setShoot(boolean shoot) {
		this.shoot = shoot;
	}

	public boolean isMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}
}
