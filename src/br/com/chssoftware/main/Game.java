package br.com.chssoftware.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import br.com.chssoftware.entities.BulletShoot;
import br.com.chssoftware.entities.Enemy;
import br.com.chssoftware.entities.Entity;
import br.com.chssoftware.entities.Player;
import br.com.chssoftware.graphics.Spritesheet;
import br.com.chssoftware.graphics.UI;
import br.com.chssoftware.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;

	public static JFrame frame;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public final int SCALE = 3;
	private Thread thread;
	private boolean isRunning = true;
	private BufferedImage image;

	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;

	public static Spritesheet spritesheet;
	public static Player player;
	public static World world;
	public UI ui;

	public static Random rand;

	public Game() {
		rand = new Random();
		this.addKeyListener(this);
		this.addMouseListener(this);
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		// Inicializando os objetos
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<>();
		enemies = new ArrayList<>();
		bullets = new ArrayList<BulletShoot>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(120, 80, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/map.png");

	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	public void initFrame() {
		frame = new JFrame("Game 1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void tick() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).tick();
		}
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		world.render(g);
		// renderização do player
		// Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		ui.render(g);
		//
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.setFont(new Font("arial black", Font.BOLD, 18));
		g.setColor(Color.white);
		g.drawString("Munição: " + player.getAmmo(), 580, 18);
		// Mostra a percentagem de vida
		g.setColor(Color.white);
		g.setFont(new Font("arial black", Font.BOLD, 18));
		g.drawString((int) player.getLife() + "/" + (int) player.getMaxLife(), 80, 18);
		bs.show();

	}

	@Override
	public void run() {
		requestFocus();
		long lastTime = System.nanoTime();
		double amountTicks = 60;
		double ns = 1000000000 / amountTicks;
		double delta = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();

		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
			player.setRight(true);
		} else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
			player.setLeft(true);
		}
		if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
			player.setUp(true);
		} else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
			player.setDown(true);
		}
		if (code == KeyEvent.VK_X) {
			player.setShoot(true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
			player.setRight(false);
		} else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
			player.setLeft(false);
		}
		if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
			player.setUp(false);
		} else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
			player.setDown(false);
		}
		if (code == KeyEvent.VK_X) {
			player.setShoot(false);
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.setMouseShoot(true);
		player.setMx((e.getX() / 3));
		player.setMy((e.getY() / 3));
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
