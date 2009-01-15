package zelda.game;

import gameframework.base.IntegerObservable;
import gameframework.game.CanvasDefaultImpl;
import gameframework.game.GameLevel;
import gameframework.game.GameLevelDefaultImpl;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import sun.java2d.pipe.DrawImage;

public class GameZeldaAWTImpl implements GameZelda, Observer {
	private static final long serialVersionUID = -3150854596831664346L;
	protected static final int SPRITE_SIZE = 16;
	public static final int MAX_NUMBER_OF_PLAYER = 1;
	public static final int NUMBER_OF_LIVES = 100;

	public enum result {
		NOT_WIN, WIN
	};

	public static final int NB_ROWS = 50;
	public static final int NB_COLUMNS = 70;

	protected CanvasDefaultImpl defaultCanvas = null;
	protected IntegerObservable score[] = new IntegerObservable[MAX_NUMBER_OF_PLAYER];
	protected IntegerObservable life[] = new IntegerObservable[MAX_NUMBER_OF_PLAYER];
	protected IntegerObservable win = new IntegerObservable();

	private Frame f;
	private GameLevelDefaultImpl currentPlayedLevel = null;

	protected int levelNumber;
	protected ArrayList<GameLevel> gameLevels;

	protected Label lifeText, scoreText;
	protected Label information;
	protected Label informationValue;
	protected Label lifeValue, scoreValue;
	protected Label currentLevel;
	protected Label currentLevelValue;

	public GameZeldaAWTImpl() {
		for (int i = 0; i < MAX_NUMBER_OF_PLAYER; ++i) {
			score[i] = new IntegerObservable();
			life[i] = new IntegerObservable();
		}
		lifeText = new Label("Lives:");
		scoreText = new Label("Score:");
		information = new Label("State:");
		informationValue = new Label("Playing");
		currentLevel = new Label("Level:");
		createGUI();
	}

	public void createGUI() {
		f = new Frame("Default Game");
		f.dispose();

		createMenuBar();
		Container c = createStatusBar();

		defaultCanvas = new CanvasDefaultImpl();
		defaultCanvas.setSize(SPRITE_SIZE * NB_COLUMNS, SPRITE_SIZE * NB_ROWS);
		f.add(defaultCanvas);
		f.add(c, BorderLayout.NORTH);
		
		try {
			f.setIconImage(ImageIO.read(new File("images/characters/zeldaPrincess.gif")));
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}
		
		f.pack();
		f.setVisible(true);

		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	private void createMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu file = new Menu("file");
		MenuItem start = new MenuItem("new game");
		MenuItem redo = new MenuItem("restart level");
		MenuItem save = new MenuItem("save");
		MenuItem restore = new MenuItem("load");
		MenuItem quit = new MenuItem("quit");
		Menu game = new Menu("game");
		MenuItem pause = new MenuItem("pause");
		MenuItem resume = new MenuItem("resume");
		menuBar.add(file);
		menuBar.add(game);
		f.setMenuBar(menuBar);

		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				start();
			}
		});
		redo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				run();
			}
		});
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		restore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restore();
			}
		});
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause();
			}
		});
		resume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resume();
			}
		});

		file.add(start);
		file.add(redo);
		file.add(save);
		file.add(restore);
		file.add(quit);
		game.add(pause);
		game.add(resume);
		
		start.setEnabled(false);
		save.setEnabled(false);
		restore.setEnabled(false);
	}

	private Container createStatusBar() {
		JPanel c = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		c.setLayout(layout);
		lifeValue = new Label(Integer.toString(life[0].getValue()));
		scoreValue = new Label(Integer.toString(score[0].getValue()));
		currentLevelValue = new Label(Integer.toString(levelNumber));
		c.add(lifeText);
		c.add(lifeValue);
		c.add(scoreText);
		c.add(scoreValue);
		c.add(currentLevel);
		c.add(currentLevelValue);
		c.add(information);
		c.add(informationValue);
		return c;
	}

	public Canvas getCanvas() {
		return defaultCanvas;
	}

	private void run(){
		win.setValue(result.NOT_WIN.ordinal());

		informationValue.setText("Playing");

		try {
			if (currentPlayedLevel != null && currentPlayedLevel.isAlive()) {
				currentPlayedLevel.interrupt();
				currentPlayedLevel = null;
			}
			currentPlayedLevel = (GameLevelDefaultImpl) gameLevels.get(levelNumber-1);
			currentLevelValue.setText(Integer.toString(levelNumber));
			currentPlayedLevel.start();
			currentPlayedLevel.join();
		} catch (Exception ex) {
		}
	}
	
	public void start() {
		for (int i = 0; i < MAX_NUMBER_OF_PLAYER; ++i) {
			score[i].addObserver(this);
			life[i].addObserver(this);
			life[i].setValue(NUMBER_OF_LIVES);
			score[i].setValue(0);
		}
		win.addObserver(this);
		
		levelNumber = 1;
		run();
	}

	public void restore() {
		System.out.println("restore(): Unimplemented operation");
	}

	public void save() {
		System.out.println("save(): Unimplemented operation");
	}

	@SuppressWarnings("deprecation")
	public void pause() {
		currentPlayedLevel.suspend();
	}

	@SuppressWarnings("deprecation")
	public void resume() {
		currentPlayedLevel.resume();
	}

	public IntegerObservable[] score() {
		return score;
	}

	public IntegerObservable[] life() {
		return life;
	}

	public IntegerObservable win() {
		return win;
	}

	public void setLevels(ArrayList<GameLevel> levels) {
		gameLevels = levels;
	}

	/*
	 * update method so as to make Game as an updatable Observer (TODO Handle 2
	 * or more players)
	 */
	public void update(Observable o, Object arg) {
		if (o instanceof IntegerObservable) {
			IntegerObservable observable = (IntegerObservable) o;
			handleScoreObservable(observable);
			handleLifeObservable(observable);
			handleWinObservable(observable);
		}
	}

	private void handleWinObservable(IntegerObservable observable) {
		if (observable == win) {
			if (win.getValue() == result.WIN.ordinal()) {
				informationValue.setText("Win");
				currentPlayedLevel.interrupt();
				
				try {
					if (levelNumber < gameLevels.size()) {
						nextLevel();
						win.setValue(result.NOT_WIN.ordinal());
						currentPlayedLevel.start();
						currentPlayedLevel.join();
					} else {
						currentPlayedLevel.end();
					}
				} catch (InterruptedException e) {
				}
			} else
				informationValue.setText("Playing");
		}
	}

	private void handleLifeObservable(IntegerObservable observable) {
		for (IntegerObservable lifeObservable : life) {
			if (observable == lifeObservable) {
				int lives = observable.getValue();
				lifeValue.setText(Integer.toString(lives <= 0 ? 0 : lives));
				if (lives <= 0) {
					informationValue.setText("Defeat");
					currentPlayedLevel.interrupt();
					currentPlayedLevel.end();
				}
			}
		}
	}

	private void handleScoreObservable(IntegerObservable observable) {
		for (IntegerObservable scoreObservable : score) {
			if (observable == scoreObservable) {
				scoreValue.setText(Integer.toString(observable.getValue()));
			}
		}
	}

	private void nextLevel() {
		currentPlayedLevel = (GameLevelDefaultImpl) gameLevels.get(levelNumber++);
		currentLevelValue.setText(Integer.toString(levelNumber));
	}
}
