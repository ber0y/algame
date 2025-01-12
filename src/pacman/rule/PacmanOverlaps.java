package pacman.rule;

import gameframework.base.IntegerObservable;
import gameframework.base.MoveStrategyRandom;
import gameframework.base.MoveStrategyStraightLine;
import gameframework.base.Overlap;
import gameframework.game.GameMovableDriverDefaultImpl;
import gameframework.game.GameUniverse;
import gameframework.game.OverlapRuleApplierDefaultImpl;

import java.awt.Point;
import java.util.Vector;

import pacman.entity.Ghost;
import pacman.entity.Jail;
import pacman.entity.Pacgum;
import pacman.entity.Pacman;
import pacman.entity.SuperPacgum;
import pacman.entity.TeleportPairOfPoints;

public class PacmanOverlaps extends OverlapRuleApplierDefaultImpl {
	
	protected GameUniverse universe;
	protected Vector<Ghost> vGhosts = new Vector<Ghost>();

	// Delay during which pacman is invulnerable and during which ghosts can be
	// eaten (in number of cycles)
	static final int INVULNERABLE_DELAY = 60;
	protected Point pacManStartPos;
	protected Point ghostStartPos;
	protected boolean managePacmanDeath;
	private IntegerObservable score;
	private IntegerObservable life;

	public PacmanOverlaps(Point pacPos, Point gPos,
			IntegerObservable life, IntegerObservable score) {
		pacManStartPos = (Point) pacPos.clone();
		ghostStartPos = (Point) gPos.clone();
		this.life = life;
		this.score = score;
	}

	public void setUniverse(GameUniverse universe) {
		this.universe = universe;
	}

	public void addGhost(Ghost g) {
		vGhosts.addElement(g);
	}

	@Override
	public void applyOverlapRules(Vector<Overlap> overlappables) {
		managePacmanDeath = true;
		super.applyOverlapRules(overlappables);
	}

	public void overlapRule(Pacman p, Ghost g) {
		if (!p.isVulnerable()) {
			if (g.isActive()) {
				g.setAlive(false);
				MoveStrategyStraightLine strat = new MoveStrategyStraightLine(g
						.getPosition(), ghostStartPos);
				GameMovableDriverDefaultImpl ghostDriv = (GameMovableDriverDefaultImpl) g
						.getDriver();
				ghostDriv.setStrategy(strat);

			}
		} else {
			if (g.isActive()) {
				if (managePacmanDeath == true) {
					life.setValue(life.getValue() - 1);
					p.setPosition(pacManStartPos);
					for (int i = 0; i < vGhosts.size(); i++) {
						((vGhosts.elementAt(i))).setPosition(ghostStartPos);
					}
					managePacmanDeath = false;
				}
			}
		}
	}

	public void overlapRule(Ghost g, SuperPacgum spg) {
	}

	public void overlapRule(Ghost g, Pacgum spg) {
	}

	public void overlapRule(Ghost g, TeleportPairOfPoints teleport) {
		g.setPosition(teleport.getDestination());
	}

	public void overlapRule(Pacman p, TeleportPairOfPoints teleport) {
		p.setPosition(teleport.getDestination());
	}

	public void overlapRule(Ghost g, Jail jail) {
		if (!g.isActive()) {
			g.setAlive(true);
			MoveStrategyRandom strat = new MoveStrategyRandom();
			GameMovableDriverDefaultImpl ghostDriv = (GameMovableDriverDefaultImpl) g
					.getDriver();
			ghostDriv.setStrategy(strat);
			g.setPosition(ghostStartPos);
		}
	}

	public void overlapRule(Pacman p, SuperPacgum spg) {
		score.setValue(score.getValue() + 5);
 		universe.removeGameEntity(spg);
		p.setInvulnerable(INVULNERABLE_DELAY);
		for (Ghost ghost : vGhosts) {
			ghost.setAfraid(INVULNERABLE_DELAY);
		}
	}

	public void overlapRule(Pacman p, Pacgum pg) {
		score.setValue(score.getValue() + 1);
 		universe.removeGameEntity(pg);
	}
}
