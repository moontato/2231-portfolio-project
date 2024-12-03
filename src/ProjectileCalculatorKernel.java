import components.standard.Standard;

/**
 * Projectile Calculator kernel component with primary methods.
 */
public interface ProjectileCalculatorKernel
        extends Standard<ProjectileCalculator> {

    /** Starting [x, y] coordinates for the projectile. */
    double G = 9.81;

    /** Conversion factor for radians to degrees. */
    double RADTODEG = Math.PI / 180;

    /** Scalar multiple, (1/2). */
    double HALF = 0.5;

    /**
     * Returns the starting [x, y] position of the projectile.
     *
     * @return the projectile's starting position [x, y] as a Double array
     */
    Double[] getStartingPosition();

    /**
     * Returns the target [x, y] position.
     *
     * @return the target position [x, y] as a Double array
     */
    Double[] getTargetPosition();

    /**
     * Returns the initial of the projectile.
     *
     * @return the speed (m/s) of the projectile
     */
    double getSpeed();

    /**
     * Returns the direction of the projectile.
     *
     * @return the direction (radians) of the projectile
     */
    double getDirection();

    /**
     * Changes the starting [x, y] position of the projectile.
     *
     * @param newX
     *            the new x coordinate (m) to replace the current
     * @param newY
     *            the new y coordinate (m) to replace the current
     */
    void changeStartPosition(double newX, double newY);

    /**
     * Changes the target [x, y] position of the projectile.
     *
     * @param newX
     *            the new x coordinate (m) to replace the current
     * @param newY
     *            the new y coordinate (m) to replace the current
     */
    void changeTargetPosition(double newX, double newY);

    /**
     * Changes the starting speed of the projectile |v| = |v_x| + |v_y|.
     *
     * @param newSpeed
     *            the new speed (m/s) of the projectile
     */
    void changeSpeed(double newSpeed);

    /**
     * Changes the starting direction of the projectile.
     *
     * @param newDirection
     *            the new direction (radians) of the projectile
     */
    void changeDirection(double newDirection);

}
