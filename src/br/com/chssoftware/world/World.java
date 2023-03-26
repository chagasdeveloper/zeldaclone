package br.com.chssoftware.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import br.com.chssoftware.entities.Bullet;
import br.com.chssoftware.entities.Enemy;
import br.com.chssoftware.entities.Entity;
import br.com.chssoftware.entities.Lifepack;
import br.com.chssoftware.entities.Player;
import br.com.chssoftware.entities.Weapon;
import br.com.chssoftware.graphics.Spritesheet;
import br.com.chssoftware.main.Game;

public class World {

	private static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static int TILE_SIZE = 16;

	public World(String path) {
		try {
			// Imagem contendo o mapa.
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			// vetor contendo todos os pixels.
			int[] pixels = new int[map.getWidth() * map.getHeight()]; // 0 - 399 (level 1)
			// Largura e altura do mapa
			WIDTH = map.getWidth(); // 20 (level 1)
			HEIGHT = map.getHeight(); // 20 (level 1)
			// todos os Tiles contidos no mapa
			tiles = new Tile[map.getWidth() * map.getHeight()]; // 0 - 399
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for (int xx = 0; xx < map.getWidth(); xx++) { // Vai percorrer o mapa horizalmente.
				for (int yy = 0; yy < map.getHeight(); yy++) { // Vai percorrer o mapa verticalmente.
					int pixelAtual = pixels[xx + (yy * map.getWidth())]; // 0 - 399.
					// Sempre vai renderizar o chão.
					tiles[xx + (yy * map.getWidth())] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
					if (pixelAtual == 0xFF000000) {
						// Floor || Chão
						tiles[xx + (yy * map.getWidth())] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE,
								Tile.TILE_FLOOR);
					} else if (pixelAtual == 0xFFFFFFFF) {
						// Wall || Parede
						tiles[xx + (yy * map.getWidth())] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE,
								Tile.TILE_WALL);
					} else if (pixelAtual == 0xFF0026FF) {
						// Player
						Game.player.setX(xx * TILE_SIZE);
						Game.player.setY(yy * TILE_SIZE);
						tiles[xx + (yy * map.getWidth())] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE,
								Tile.TILE_FLOOR);
					} else if (pixelAtual == 0xFFFFD800) {
						// Bullet
						Game.entities.add(new Bullet(xx * TILE_SIZE, yy * TILE_SIZE, 16, 16, Entity.BULLET_EN));
					} else if (pixelAtual == 0xFFFF7F7F) {
						// Lifepack
						Game.entities.add(new Lifepack(xx * TILE_SIZE, yy * TILE_SIZE, 16, 16, Entity.LIFEPACK_EN));
					} else if (pixelAtual == 0xFFFF0000) {
						// Enemy
						 BufferedImage[] sprites = new BufferedImage[2];
						 sprites[0] = Entity.ENEMY_EN;
						 sprites[1] = Entity.ENEMY_EN2;
						Enemy enemy = new Enemy(xx * TILE_SIZE, yy * TILE_SIZE, 16, 16, sprites);
						Game.entities.add(enemy);
						Game.enemies.add(enemy);
					} else if (pixelAtual == 0xFFFF6A00) {
						// Weapon
						Game.entities.add(new Weapon(xx * TILE_SIZE, yy * TILE_SIZE, 16, 16, Entity.WEAPON_EN));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void restartGame(String level) {
		
		Game.entities.clear();
		Game.enemies.clear();
		Game.entities = new ArrayList<>();
		Game.enemies = new ArrayList<>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(120, 80, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);
		return;
	}
	
	public static boolean isFree(int xnext, int ynext, int width, int height) {
		// vértices da sprite
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + width - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + height - 1) / TILE_SIZE;

		int x4 = (xnext + width - 1) / TILE_SIZE;
		int y4 = (ynext + height - 1) / TILE_SIZE;

		return !((tiles[x1 + (y1 * WIDTH)] instanceof WallTile) || (tiles[x2 + (y2 * WIDTH)] instanceof WallTile)
				|| (tiles[x3 + (y3 * WIDTH)] instanceof WallTile) || (tiles[x4 + (y4 * WIDTH)] instanceof WallTile));
	}

	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);

		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
}
