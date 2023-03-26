package br.com.chssoftware.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import br.com.chssoftware.world.World;

public class Menu {
	private String[] options = { "continuar", "novo jogo", "carregar jogo", "sair" };
	private boolean up;
	private boolean down;
	private boolean enter;
	private boolean pause;
	private int currentOption;
	private int maxOption = options.length - 1;

	public void tick() {
		if (pause) {
			if (up) {
				Sound.selectMenu.play();
				up = false;
				currentOption--;
				if (currentOption < 0) {
					currentOption = maxOption;
				}
			}
			if (down) {
				Sound.selectMenu.play();
				down = false;
				currentOption++;
				if (currentOption > maxOption) {
					currentOption = 0;
				}
			}
		} else {
			if (currentOption == 0) {
				currentOption = 1;
			}
			if (up) {
				Sound.selectMenu.play();
				up = false;
				currentOption--;
				if (currentOption < 1) {
					currentOption = maxOption;
				}
			}
			if (down) {
				Sound.selectMenu.play();
				down = false;
				currentOption++;
				if (currentOption > maxOption) {
					currentOption = 1;
				}
			}
		}
		if (enter) {
			enter = false;
			if (options[currentOption] == "continuar") {
				Game.gameState = "NORMAL";
				pause = false;
			} else if (options[currentOption] == "novo jogo") {
				Game.gameState = "NORMAL";
				World.restartGame("level1.png");
				pause = false;
				currentOption = 0;
			} else if (options[currentOption] == "sair") {
				System.exit(1);
			}
		}
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 230));
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		g.setColor(Color.red);
		g.setFont(new Font("arial", Font.BOLD, 36));
		g.drawString(">Zeldinha<", (Game.WIDTH * Game.SCALE) / 2 - 80, (Game.HEIGHT * Game.SCALE) / 2 - 160);
		g.setFont(new Font("arial black", Font.BOLD, 16));
		g.drawString("developed by @chagas.dev", (Game.WIDTH * Game.SCALE) / 2 - 120, (Game.HEIGHT * Game.SCALE) - 10);
		// Opções de menu
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 24));
		if (pause) {
			g.drawString("Continuar", (Game.WIDTH * Game.SCALE) / 2 - 50, 160);
			g.drawString("Novo Jogo", (Game.WIDTH * Game.SCALE) / 2 - 50, 190);
			g.drawString("Carregar Jogo", (Game.WIDTH * Game.SCALE) / 2 - 70, 220);
			g.drawString("Sair", (Game.WIDTH * Game.SCALE) / 2 - 10, 250);
		} else {
			g.drawString("Novo Jogo", (Game.WIDTH * Game.SCALE) / 2 - 50, 160);
			g.drawString("Carregar Jogo", (Game.WIDTH * Game.SCALE) / 2 - 70, 190);
			g.drawString("Sair", (Game.WIDTH * Game.SCALE) / 2 - 10, 220);
		}

		g.setColor(Color.blue);
		if (pause) {
			if (options[currentOption] == "continuar") {
				g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 65, 160);
				g.drawString("<", (Game.WIDTH * Game.SCALE) / 2 + 65, 160);
			} else if (options[currentOption] == "novo jogo") {
				g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 65, 190);
				g.drawString("<", (Game.WIDTH * Game.SCALE) / 2 + 76, 190);
			} else if (options[currentOption] == "carregar jogo") {
				g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 86, 220);
				g.drawString("<", (Game.WIDTH * Game.SCALE) / 2 + 93, 220);
			} else if (options[currentOption] == "sair") {
				g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 25, 250);
				g.drawString("<", (Game.WIDTH * Game.SCALE) / 2 + 37, 250);
			}
		} else {
			if (options[currentOption] == "novo jogo") {
				g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 65, 160);
				g.drawString("<", (Game.WIDTH * Game.SCALE) / 2 + 76, 160);
			} else if (options[currentOption] == "carregar jogo") {
				g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 86, 190);
				g.drawString("<", (Game.WIDTH * Game.SCALE) / 2 + 93, 190);
			} else if (options[currentOption] == "sair") {
				g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 25, 220);
				g.drawString("<", (Game.WIDTH * Game.SCALE) / 2 + 37, 220);
			}
		}
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isEnter() {
		return enter;
	}

	public void setEnter(boolean enter) {
		this.enter = enter;
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}
}
