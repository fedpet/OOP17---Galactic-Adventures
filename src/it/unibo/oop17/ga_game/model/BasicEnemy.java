package it.unibo.oop17.ga_game.model;

import org.jbox2d.dynamics.World;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

/**
 * Models an enemy that switches moving direction when collides against an
 * obstacle.
 */
public final class BasicEnemy extends WalkingEntity {
	public static final Dimension2D SIZE = new Dimension2D(0.4, 0.8);
	private static final float WALK_SPEED = 8f;
	private static final float JUMP_SPEED = 0f;

	/**
	 * 
	 * @param world
	 *            The world in which to spawn the player.
	 * @param position
	 *            The position
	 */
	public BasicEnemy(final World world, final Point2D position) {
		super(world, position, SIZE);
	}

	@Override
	protected float getJumpSpeed() {
		return JUMP_SPEED;
	}

	@Override
	protected float getWalkSpeed() {
		return WALK_SPEED;
	}
}