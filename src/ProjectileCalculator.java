import java.util.HashMap;
import java.util.Map;

/**
 * Calculates the trajectory of a projectile, and various related fields, using
 * kimenatic equations.
 */
public class ProjectileCalculator {
    /** Starting [x, y] coordinates for the projectile. */
    private double[] startingCoordinates;

    /** Target [x, y] coordinates for the projectile. */
    private double[] targetCoordinates;

    /** Mass (kg) of the projectile. */
    private double mass;

    /** Speed (m/s) of the projectile. */
    private double speed;

    /** Direction (degrees) of the projectile. */
    private double direction;

    /** Default constructor. */
    public ProjectileCalculator() {
        this.startingCoordinates = new double[2];
        this.targetCoordinates = new double[2];
    }

    /**
     * General purpose constructor. Pass in any value for unknown fields.
     *
     * @param mass
     *            the mass (kg) of the projectile
     * @param speed
     *            the speed (m/s) of the projectile
     * @param direction
     *            the direction (degrees) of the projectile
     */
    public ProjectileCalculator(double mass, double speed, double direction) {
        this.mass = mass;
        this.speed = speed;
        this.direction = direction * Math.PI / 180;
        // starting coordinates assumed to be @ origin (0,0)
        this.startingCoordinates = new double[2];
        this.targetCoordinates = new double[2];
    }

    // kernel methods
    /**
     * Changes the starting [x, y] position of the projectile.
     *
     * @param newX
     *            the new x coordinate (m) to replace the current
     * @param newY
     *            the new y coordinate (m) to replace the current
     */
    public final void changeStartPosition(double newX, double newY) {
        this.startingCoordinates[0] = newX;
        this.startingCoordinates[1] = newY;
    }

    /**
     * Changes the target [x, y] position of the projectile.
     *
     * @param newX
     *            the new x coordinate (m) to replace the current
     * @param newY
     *            the new y coordinate (m) to replace the current
     */
    public final void changeTargetPosition(double newX, double newY) {
        this.targetCoordinates[0] = newX;
        this.targetCoordinates[1] = newY;
    }

    /**
     * Changes the starting speed of the projectile |v| = |v_x| + |v_y|.
     *
     * @param newSpeed
     *            the new speed (m/s) of the projectile
     */
    public final void changeSpeed(double newSpeed) {
        this.speed = newSpeed;
    }

    /**
     * Changes the starting direction of the projectile.
     *
     * @param newDirection
     *            the new direction (degrees) of the projectile
     */
    public final void changeDirection(double newDirection) {
        this.direction = newDirection;
    }

    /**
     * Changes the starting mass of the projectile.
     *
     * @param newMass
     *            the new mass (kg) of the projectile
     */
    public final void changeMass(double newMass) {
        this.mass = newMass;
    }

    /**
     * Calculates the position of the projectile at a given time.
     *
     * @param time
     *            the time to calculate the position of the projectile for
     * @return the projectile's position [x, y] at the given time as a Double
     *         array
     */
    public final Double[] calculatePosition(double time) {
        // find horiz. and vert. speed components |v_x| and |v_y|
        double speedX = this.speed * Math.cos(this.direction);
        double speedY = this.speed * Math.sin(this.direction);

        // calculate displacement, assuming zero wind resistance and g=9.81
        double deltaX = speedX * time;
        double deltaY = speedY * time + (0.5 * -9.81 * Math.pow(time, 2));

        Double[] endingCoordinates = { this.startingCoordinates[0],
                this.startingCoordinates[1] };
        endingCoordinates[0] += deltaX;
        endingCoordinates[1] += deltaY;

        return endingCoordinates;
    }

    /**
     * Returns the starting [x, y] position of the projectile.
     *
     * @return the projectile's starting position [x, y] as a Double array
     */
    public final double[] getStartingPosition() {
        return this.startingCoordinates;
    }

    /**
     * Returns the target [x, y] position.
     *
     * @return the target position [x, y] as a Double array
     */
    public final double[] getTargetPosition() {
        return this.targetCoordinates;
    }

    // secondary methods

    /**
     * Calculates the ending position of the projectile--when the projectile
     * reaches the target height.
     *
     * @param tStep
     *            the time step to be used in the calculation
     * @return the projectile's ending position [x, y] as a Double array
     */
    public final Double[] calculateEndingPosition(double tStep) {
        Map<Double, Double[]> trajectory = new HashMap<Double, Double[]>();
        double elapsedTime = this.calculateTrajectory(trajectory, tStep);

        Double[] endingPosition = trajectory.get(elapsedTime);

        return endingPosition;
    }

    /**
     * Calculates the total flight time of the projectile.
     *
     * @param tStep
     *            the time step to be used in the calculation
     * @return the projectile flight time in seconds
     */
    public final double calculateFlightTime(double tStep) {
        Map<Double, Double[]> trajectory = new HashMap<Double, Double[]>();

        return this.calculateTrajectory(trajectory, tStep);
    }

    // finds closest neighbor

    /**
     * Calculates the speed necessary to hit the target, given mass and
     * direction.
     *
     * @param tStep
     *            the time step to be used in the calculation
     * @param vStep
     *            the speed step to be used in the calculation
     * @return the speed required for projectile to hit the target
     */
    public final double calculateRequiredSpeed(double tStep, double vStep) {
        // current position at current time
        double currentSpeed = 0;
        this.changeSpeed(currentSpeed);
        Double[] currentEndingPosition = this.calculateEndingPosition(tStep);

        // next position = current position + tStep
        currentSpeed += vStep;
        this.changeSpeed(currentSpeed);
        Double[] nextEndingPosition = this.calculateEndingPosition(tStep);

        /*
         * calculate position differences against the target, and find nearest
         * neighbor using current guess and the next guess
         */
        double[] currentPositionDifference = {
                Math.abs(currentEndingPosition[0] - this.targetCoordinates[0]),
                Math.abs(
                        currentEndingPosition[1] - this.targetCoordinates[1]) };
        double[] nextPositionDifference = {
                Math.abs(nextEndingPosition[0] - this.targetCoordinates[0]),
                Math.abs(nextEndingPosition[1] - this.targetCoordinates[1]) };

        // find the nearest neighbor
        while (nextPositionDifference[0] < currentPositionDifference[0]
                || nextPositionDifference[1] < currentPositionDifference[1]) {
            currentSpeed += vStep;
            this.changeSpeed(currentSpeed);

            currentPositionDifference[0] = nextPositionDifference[0];
            nextEndingPosition = this.calculateEndingPosition(tStep);

            nextPositionDifference[0] = Math
                    .abs(nextEndingPosition[0] - this.targetCoordinates[0]);
            nextPositionDifference[1] = Math
                    .abs(nextEndingPosition[1] - this.targetCoordinates[1]);
        }

        // bc currentSpeed was increased for next guess, but current is better
        currentSpeed -= vStep;

        return currentSpeed;
    }

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
    public double calculateTrajectory(Map<Double, Double[]> trajectory,
            double tStep) {
        Double elapsedTime = (double) 0;

        while (this.calculatePosition(
                elapsedTime)[1] >= this.targetCoordinates[1]) {
            trajectory.put(elapsedTime, this.calculatePosition(elapsedTime));
            elapsedTime += tStep;
            this.calculatePosition(elapsedTime);
        }

        return elapsedTime - tStep;
    }

    /**
     * Main method.
     */
    public static void main(String[] args) {
        // ProjectileCalculator pc = new ProjectileCalculator(0.008, 304.8, 10);
        // pc.changeStartPosition(0, 1);
        // Double[] endPosition = pc.calculateEndingPosition(0.01);

        // configure projectile
        double tStep = 0.00001;
        ProjectileCalculator pc = new ProjectileCalculator(0.41, 30, 45);
        pc.changeStartPosition(0, 15);
        pc.changeTargetPosition(104.8, 0);

        // calculate details
        Double[] endPosition = pc.calculateEndingPosition(tStep);

        // print details
        System.out.println(
                "end x: " + endPosition[0] + " | end y: " + endPosition[1]);
        System.out.println(
                "flight time: " + pc.calculateFlightTime(tStep) + " seconds");
        // System.out.println(
        //         "Required speed: " + pc.calculateRequiredSpeed(tStep, 1, 1));
        System.out.println(
                "Required speed: " + pc.calculateRequiredSpeed(tStep, 1));

    }
}