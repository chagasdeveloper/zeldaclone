package br.com.chssoftware.graphics;

import java.awt.Color;
import java.awt.Graphics;

import br.com.chssoftware.main.Game;

public class UI {

	public void render(Graphics g) {

		g.setColor(Color.red);
		g.fillRect(8, 1, 70, 6);

		g.setColor(Color.green);
		int life = (int) ((Game.player.getLife() / Game.player.getMaxLife()) * 70);
		g.fillRect(8, 1, life, 6);

//		g.setColor(Color.white);
//		g.setFont(new Font("arial", Font.BOLD, 8));
//		g.drawString((int) Game.player.life + "/" + (int) Game.player.maxLife, 35, 11);
	}
	/*
	 * g.setColor(Color.red); g.fillRect(Game.player.getX() - Camera.x + 3,
	 * Game.player.getY() - Camera.y - 5, 14, 2);
	 * 
	 * g.setColor(Color.green); int life = (int) ((Game.player.life /
	 * Game.player.maxLife) * 14); g.fillRect(Game.player.getX() - Camera.x + 3,
	 * Game.player.getY() - Camera.y - 5, life, 2);
	 */
}
