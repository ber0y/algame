package zelda.entity.decors;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import gameframework.base.Drawable;
import gameframework.base.DrawableImage;
import gameframework.base.Overlappable;
import gameframework.game.GameEntity;

/*
 * To give max life to link 
 */
public class SuperPotion implements Drawable, GameEntity, Overlappable {

	protected static DrawableImage image = null;
	protected Point position;
	public static final int SPRITE_SIZE = 16;

	public SuperPotion(Canvas defaultCanvas, Point pos) {
		if (image == null) {
			image = new DrawableImage("images/decor/z3_bottleblue.gif", defaultCanvas);
		}
		position = pos;
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(image.getImage(), (int) getPosition().getX(),
				(int) getPosition().getY(), SPRITE_SIZE, SPRITE_SIZE, null);
	}

	@Override
	public Point getPosition() {
		return position;
	}

	@Override
	public Rectangle getBoundingBox() {
		return (new Rectangle((int) position.getX(), (int) position.getY(),
				SPRITE_SIZE , SPRITE_SIZE));
	}
}
