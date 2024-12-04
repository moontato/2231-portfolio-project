import java.util.Map;

/**
 * {@code ProjectileCalculatorKernel} enhanced with secondary methods.
 */
public interface ProjectileCalculator extends ProjectileCalculatorKernel {

    /**
     * Calculates the position of the projectile at a given time.
     *
     * @param time
     *            the time to calculate the position of the projectile for
     * @return the projectile's position [x, y] at the given time as a Double
     *         array
     */
    Double[] calculatePosition(double time);

    /**
     * Calculates the ending position of the projectile--when the projectile
     * reaches the target height.
     *
     * @param tStep
     *            the time step to be used in the calculation
     * @return the projectile's ending position [x, y] as a Double array
     */
    Double[] calculateEndingPosition(double tStep);

    /**
     * Calculates trajectory using time-step `tStep` and until current height is
     * equal to target height. A target height of zero will be assumed if none
     * is given.
     *
     * @param trajectory
     *            the trajectory of the projectile given as a map, where each
     *            key is some point in time, and its value being the [x, y]
     *            coordinates at that time
     * @param tStep
     *            the time step to be used in the calculation
     * @return the projectile flight time in seconds
     */
    double calculateTrajectory(Map<Double, Double[]> trajectory, double tStep);

    /**
     * Calculates the total flight time of the projectile.
     *
     * @param tStep
     *            the time step to be used in the calculation
     * @return the projectile flight time in seconds
     */
    double calculateFlightTime(double tStep);

    /**
     * Calculates the speed necessary to hit the target, given mass and
     * direction. Finds the closest neighbor.
     *
     * @param tStep
     *            the time step to be used in the calculation
     * @param vStep
     *            the speed step to be used in the calculation
     * @return the speed required for projectile to hit the target
     */
    double calculateRequiredSpeed(double tStep, double vStep);

    /**
     * Calculates the smalleset angle necessary to hit the target, given
     * projectile speed.
     *
     * @param tStep
     *            the time step to be used in the calculation
     * @param dPrecision
     *            the direction precision to be used in the calculation
     * @return the speed required for projectile to hit the target
     */
    double calculateRequiredDirection(double tStep, double dPrecision);

    /**
     * Returns [maximum range (m), launch angle (rad.)] determined by
     * user-defined projectile speed and starting/target positions, while
     * varying the launch angle.
     *
     * @param tStep
     *            the time step to be used in the calculation
     * @return a double array containing [max_range (m), corresponding_angle
     *         (rad.)]
     */
    double[] calculateMaxRange(double tStep);

    /**
     * Returns true if the requested target is physically beyond the upper range
     * limit for an accurate calculation to be possible.
     *
     * @param distance
     *            the distance to check against the max range
     * @param tStep
     *            the time step to be used in the calculation
     * @return true if distance >= max_range
     */
    boolean isAtMaxRange(double distance, double tStep);

}
