package zelda;

import gameframework.game.GameLevel;

import java.io.File;
import java.util.ArrayList;

import zelda.game.GameZeldaAWTImpl;
import zelda.level.ZeldaGameLevel;

public class Main {
	public static void main(String[] args) {
		GameZeldaAWTImpl g = new GameZeldaAWTImpl();
		ArrayList<GameLevel> levels = new ArrayList<GameLevel>();

		levels.add(new ZeldaGameLevel(g, new File("levels/levelTuto")));
		levels.add(new ZeldaGameLevel(g, new File("levels/level1")));
		levels.add(new ZeldaGameLevel(g, new File("levels/level2")));
		
		g.setLevels(levels);
		g.start();
		g.pause();
	}
}
