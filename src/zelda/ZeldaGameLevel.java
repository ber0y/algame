package zelda;

import gameframework.base.MoveStrategyKeyboard;
import gameframework.base.MoveStrategyRandom;
import gameframework.game.CanvasDefaultImpl;
import gameframework.game.Game;
import gameframework.game.GameLevelDefaultImpl;
import gameframework.game.GameMovableDriverDefaultImpl;
import gameframework.game.GameUniverseDefaultImpl;
import gameframework.game.GameUniverseViewPortDefaultImpl;
import gameframework.game.MoveBlockerChecker;
import gameframework.game.MoveBlockerCheckerDefaultImpl;
import gameframework.game.OverlapProcessor;
import gameframework.game.OverlapProcessorDefaultImpl;
import gameframework.game.OverlapRuleApplier;

import java.awt.Canvas;
import java.awt.Point;

import zelda.base.GuardMovableDriver;
import zelda.base.LinkMoveStrategy;
import zelda.entity.characters.Guard;
import zelda.entity.characters.Link;
import zelda.entity.characters.ZeldaPrincess;
import zelda.entity.decors.Bomb;
import zelda.entity.decors.Bush;
import zelda.entity.decors.Hammer;
import zelda.entity.decors.SuperPotion;
import zelda.entity.decors.Tree;
import zelda.rule.ZeldaMoveBlockers;
import zelda.rule.ZeldaOverlaps;

public class ZeldaGameLevel extends GameLevelDefaultImpl {
	
	Canvas canvas;
	
	public static final int SPRITE_SIZE = 16;
	public static final int NUMBER_OF_ENNEMYS = 2;
	protected static final int NB_ROWS = 31;
	protected static final int NB_COLUMNS = 50;
	
	public static enum direction {UP, DOWN, RIGHT, LEFT};

	public ZeldaGameLevel(Game g) {
		super(g);
		canvas = g.getCanvas();
	}

	@Override
	protected void init() {
		// TODO Mettre en place cette méthode et le tour est joué.
		OverlapProcessor overlapProcessor = new OverlapProcessorDefaultImpl();
		// TODO mieux définir les positions de départ et trouver des vrais variables !!!!
		OverlapRuleApplier overlapRules = new ZeldaOverlaps(
				new Point(14 * SPRITE_SIZE, 17 * SPRITE_SIZE), new Point(
						  14 * SPRITE_SIZE, 15 * SPRITE_SIZE), life[0], score[0], canvas);
		overlapProcessor.setOverlapRules(overlapRules);
		
		MoveBlockerChecker moveBlockerChecker = new MoveBlockerCheckerDefaultImpl();
		moveBlockerChecker.setMoveBlockerRules(new ZeldaMoveBlockers());
		
		universe = new GameUniverseDefaultImpl(moveBlockerChecker, overlapProcessor);
 		overlapRules.setUniverse(universe);
 		
 		gameBoard = new GameUniverseViewPortDefaultImpl(canvas, universe);
 		((GameUniverseViewPortDefaultImpl)gameBoard).setBackground("images/background/background_image_zelda.gif");
		((CanvasDefaultImpl) canvas).setDrawingGameBoard(gameBoard);
		
		Link link = new Link(canvas);
		GameMovableDriverDefaultImpl pacDriver = new GameMovableDriverDefaultImpl();
		MoveStrategyKeyboard keyStr = new LinkMoveStrategy(link);
		pacDriver.setStrategy(keyStr);
		pacDriver.setmoveBlockerChecker(moveBlockerChecker);
		canvas.addKeyListener(keyStr);
		link.setDriver(pacDriver);
		link.setPosition(new Point(14 * SPRITE_SIZE, 17 * SPRITE_SIZE));
		universe.addGameEntity(link);
		
		// Murs sur les 4 cotés
		for (int i = 0; i <= NB_COLUMNS; ++i) { 
			universe.addGameEntity(new Tree(canvas, new Point(i * SPRITE_SIZE, 0)));
			universe.addGameEntity(new Tree(canvas, new Point(i * SPRITE_SIZE, (NB_ROWS + 1) * SPRITE_SIZE)));
			universe.addGameEntity(new Tree(canvas, new Point(0, i * SPRITE_SIZE)));
			universe.addGameEntity(new Tree(canvas, new Point(NB_COLUMNS * SPRITE_SIZE, i * SPRITE_SIZE)));
			
			if(i == (NB_COLUMNS / 2))
				for (int j = 0; j <= NB_ROWS; ++j)
					if(j != (NB_ROWS / 2 -1))
						universe.addGameEntity(new Tree(canvas, new Point(i * SPRITE_SIZE, j * SPRITE_SIZE)));
					else 
						universe.addGameEntity(new Hammer(canvas, new Point(i * SPRITE_SIZE, j * SPRITE_SIZE)));
		}
		
		
		universe.addGameEntity(new ZeldaPrincess(canvas, new Point(2 * SPRITE_SIZE, 2 * SPRITE_SIZE)));
		universe.addGameEntity(new Hammer(canvas, new Point(20 * SPRITE_SIZE, 20 * SPRITE_SIZE)));
		universe.addGameEntity(new Bush(canvas, new Point(14 * SPRITE_SIZE, 14 * SPRITE_SIZE)));
		universe.addGameEntity(new Bush(canvas, new Point(10 * SPRITE_SIZE, 25 * SPRITE_SIZE)));
		universe.addGameEntity(new Bomb(canvas, new Point(4 * SPRITE_SIZE, 28 * SPRITE_SIZE)));
		universe.addGameEntity(new SuperPotion(canvas, new Point(10 * SPRITE_SIZE, 10 * SPRITE_SIZE)));
		
		Guard guard = new Guard(canvas, new Point(2 * SPRITE_SIZE, 4 * SPRITE_SIZE));
		GameMovableDriverDefaultImpl guardDriv = new GuardMovableDriver();
		MoveStrategyRandom ranStr = new MoveStrategyRandom();
		guardDriv.setStrategy(ranStr);
		guardDriv.setmoveBlockerChecker(moveBlockerChecker);
		guard.setDriver(guardDriv);
		universe.addGameEntity(guard);
		
		addWalls(new Point(5, 5), direction.UP, 5);
	}
	
	//TODO vérifier ce truc et le finir
	private void addWalls(Point origin, direction dir, int num) {
		if(dir.equals(direction.UP)){
			for(int i = 0; i < num; ++i){
				universe.addGameEntity(new Tree(canvas, new Point((int) origin.getX() * SPRITE_SIZE, (int) (origin.getY() + i * SPRITE_SIZE))));
			}
		}
	}
}
