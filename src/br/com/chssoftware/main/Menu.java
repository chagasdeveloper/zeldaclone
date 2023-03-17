package br.com.chssoftware.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu {
	private String[] options = { "novo jogo", "carregar jogo", "sair" };
	private boolean up;
	private boolean down;
	private int currentOption = 0;
	private int maxOption = options.length - 1;

	public void tick() {
		if (up) {
			up = false;
			currentOption--;
			if (currentOption < 0) {
				currentOption = maxOption;
			}
		}
		if (down) {
			down = false;
			currentOption++;
			if (currentOption > maxOption) {
				currentOption = 0;
			}

		}
	}

	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		g.setColor(Color.red);
		g.setFont(new Font("arial", Font.BOLD, 36));
		g.drawString(">Zeldinha<", (Game.WIDTH * Game.SCALE) / 2 - 80, (Game.HEIGHT * Game.SCALE) / 2 - 160);
		g.setFont(new Font("arial black", Font.BOLD, 16));
		g.drawString("developed by @chagas.dev", (Game.WIDTH * Game.SCALE) / 2 - 120, (Game.HEIGHT * Game.SCALE) - 10);
		// Opções de menu
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 24));
		g.drawString("Novo Jogo", (Game.WIDTH * Game.SCALE) / 2 - 50, 160);
		g.drawString("Carregar Jogo", (Game.WIDTH * Game.SCALE) / 2 - 70, 200);
		g.drawString("Sair", (Game.WIDTH * Game.SCALE) / 2 - 10, 240);
		g.setColor(Color.blue);
		if (options[currentOption] == "novo jogo") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 65, 160);
			g.drawString("<", (Game.WIDTH * Game.SCALE) / 2 + 76, 160);
		} else if (options[currentOption] == "carregar jogo") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 86, 200);
			g.drawString("<", (Game.WIDTH * Game.SCALE) / 2 + 93, 200);
		} else if (options[currentOption] == "sair") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 25, 240);
			g.drawString("<", (Game.WIDTH * Game.SCALE) / 2 + 37, 240);
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
}
