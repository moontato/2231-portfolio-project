
// import java.util.HashMap;
// import java.util.Map;
// import java.util.HashMap;

import components.map.Map;
import components.map.Map1L;

/**
 * Calculates the trajectory of a projectile, and various related fields, using
 * kimenatic equations.
 */
public class ProjectileCalculatorPOC {
    /** Starting [x, y] coordinates for the projectile. */
    private Double[] startingCoordinates;

    /** Target [x, y] coordinates for the projectile. */
    private Double[] targetCoordinates;

    /** Speed (m/s) of the projectile. */
    private double speed;

    /** Direction (radians) of the projectile. */
    private double direction;

    /** Default constructor. */
    public ProjectileCalculatorPOC() {
        this.startingCoordinates = new Double[2];
        this.targetCoordinates = new Double[2];
    }

    /**
     * General purpose constructor. Pass in any value for unknown fields.
     *
     * @param mass
     *            the mass (kg) of the projectile
     * @param speed
     *            the speed (m/s) of the projectile
     * @param direction
     *            the direction (radians) of the projectile
     */
    public ProjectileCalculatorPOC(double mass, double speed,
            double direction) {
        this.speed = speed;
        this.direction = direction;
        // starting coordinates assumed to be @ origin (0,0)
        this.startingCoordinates = new Double[2];
        this.targetCoordinates = new Double[2];
    }

    // kernel methods

    /**
     * Returns the starting [x, y] position of the projectile.
     *
     * @return the projectile's starting position [x, y] as a Double array
     */
    public Double[] getStartingPosition() {
        Double[] newCoordinates = new Double[2];
        newCoordinates[0] = this.startingCoordinates[0].doubleValue();
        newCoordinates[1] = this.startingCoordinates[1].doubleValue();
        return newCoordinates;
    }

    /**
     * Returns the target [x, y] position.
     *
     * @return the target position [x, y] as a Double array
     */
    public Double[] getTargetPosition() {
        Double[] newCoordinates = new Double[2];
        newCoordinates[0] = this.targetCoordinates[0].doubleValue();
        newCoordinates[1] = this.targetCoordinates[1].doubleValue();
        return newCoordinates;
    }

    /**
     * Returns the initial of the projectile.
     *
     * @return the speed (m/s) of the projectile
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * Returns the direction of the projectile.
     *
     * @return the direction (radians) of the projectile
     */
    public double getDirection() {
        return this.direction;
    }

    /**
     * Changes the starting [x, y] position of the projectile.
     *
     * @param newX
     *            the new x coordinate (m) to replace the current
     * @param newY
     *            the new y coordinate (m) to replace the current
     */
    public void changeStartPosition(double newX, double newY) {
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
    public void changeTargetPosition(double newX, double newY) {
        this.targetCoordinates[0] = newX;
        this.targetCoordinates[1] = newY;
    }

    /**
     * Changes the starting speed of the projectile |v| = |v_x| + |v_y|.
     *
     * @param newSpeed
     *            the new speed (m/s) of the projectile
     */
    public void changeSpeed(double newSpeed) {
        this.speed = newSpeed;
    }

    /**
     * Changes the starting direction of the projectile.
     *
     * @param newDirection
     *            the new direction (radians) of the projectile
     */
    public void changeDirection(double newDirection) {
        this.direction = newDirection;
    }

    // secondary methods

    /**
     * Calculates the position of the projectile at a given time.
     *
     * @param time
     *            the time to calculate the position of the projectile for
     * @return the projectile's position [x, y] at the given time as a Double
     *         array
     */
    public Double[] calculatePosition(double time) {
        // find horiz. and vert. speed components |v_x| and |v_y|
        double speedX = this.getSpeed() * Math.cos(this.getDirection());
        double speedY = this.getSpeed() * Math.sin(this.getDirection());

        // calculate displacement, assuming zero wind resistance and g=9.81
        double deltaX = speedX * time;
        double deltaY = speedY * time + (0.5 * -9.807 * Math.pow(time, 2));

        Double[] endingCoordinates = this.getStartingPosition();
        endingCoordinates[0] += deltaX;
        endingCoordinates[1] += deltaY;

        return endingCoordinates;
    }

    /**
     * Calculates the ending position of the projectile--when the projectile
     * reaches the target height.
     *
     * @param tStep
     *            the time step to be used in the calculation
     * @return the projectile's ending position [x, y] as a Double array
     */
    public Double[] calculateEndingPosition(double tStep) {
        Map<Double, Double[]> trajectory = new Map1L<Double, Double[]>();
        double elapsedTime = this.calculateTrajectory(trajectory, tStep);

        Double[] endingPosition = trajectory.value(elapsedTime);

        return endingPosition;
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

        while (this.calculatePosition(elapsedTime)[1] >= this
                .getTargetPosition()[1]) {
            trajectory.add(elapsedTime, this.calculatePosition(elapsedTime));
            elapsedTime += tStep;
        }

        return elapsedTime - tStep;
    }

    /**
     * Calculates the total flight time of the projectile.
     *
     * @param tStep
     *            the time step to be used in the calculation
     * @return the projectile flight time in seconds
     */
    public double calculateFlightTime(double tStep) {
        Map<Double, Double[]> trajectory = new Map1L<Double, Double[]>();

        return this.calculateTrajectory(trajectory, tStep);
    }

    // finds closest neighbor

    /**
     * Calculates the speed necessary to hit the target, given projectile
     * direction.
     *
     * @param tStep
     *            the time step to be used in the calculation
     * @param vStep
     *            the speed step to be used in the calculation
     * @return the speed required for projectile to hit the target
     */
    public double calculateRequiredSpeed(double tStep, double vStep) {
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
                Math.abs(
                        currentEndingPosition[0] - this.getTargetPosition()[0]),
                Math.abs(currentEndingPosition[1]
                        - this.getTargetPosition()[1]) };
        double[] nextPositionDifference = {
                Math.abs(nextEndingPosition[0] - this.getTargetPosition()[0]),
                Math.abs(nextEndingPosition[1] - this.getTargetPosition()[1]) };

        // find the nearest neighbor
        while (nextPositionDifference[0] < currentPositionDifference[0]
                || nextPositionDifference[1] < currentPositionDifference[1]) {
            currentSpeed += vStep;
            this.changeSpeed(currentSpeed);

            currentPositionDifference[0] = nextPositionDifference[0];
            nextEndingPosition = this.calculateEndingPosition(tStep);

            nextPositionDifference[0] = Math
                    .abs(nextEndingPosition[0] - this.getTargetPosition()[0]);
            nextPositionDifference[1] = Math
                    .abs(nextEndingPosition[1] - this.getTargetPosition()[1]);
        }

        // bc currentSpeed was increased for next guess, but current is better
        currentSpeed -= vStep;

        return currentSpeed;
    }

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
    public double calculateRequiredDirection(double tStep, double dPrecision) {
        double currentDirection = 0;

        // if requested target is too far...
        if (this.isAtMaxRange(this.getTargetPosition()[0], tStep)) {
            currentDirection = -1;
        } else {
            double upperBound = 90 * Math.PI / 180;
            double lowerBound = -90 * Math.PI / 180;

            this.changeDirection(currentDirection);
            Double[] currentEndingPosition = this
                    .calculateEndingPosition(tStep);

            // projectile overshoots target
            if (currentEndingPosition[0] > this.getTargetPosition()[0]) {
                upperBound = currentDirection;
                currentDirection = (lowerBound + currentDirection) / 2;
            } else {
                lowerBound = currentDirection;
                currentDirection = (upperBound + currentDirection) / 2;
            }

            this.changeDirection(currentDirection);
            Double[] nextEndingPosition = this.calculateEndingPosition(tStep);

            /*
             * calculate position differences against the target, and find
             * nearest neighbor using current guess and the next guess
             */
            double[] currentPositionDifference = {
                    Math.abs(currentEndingPosition[0]
                            - this.getTargetPosition()[0]),
                    Math.abs(currentEndingPosition[1]
                            - this.getTargetPosition()[1]) };
            double[] nextPositionDifference = {
                    Math.abs(nextEndingPosition[0]
                            - this.getTargetPosition()[0]),
                    Math.abs(nextEndingPosition[1]
                            - this.getTargetPosition()[1]) };

            while (Math.abs(nextPositionDifference[0]
                    - currentPositionDifference[0]) > dPrecision) {

                // projectile overshoots target
                if (nextEndingPosition[0] > this.getTargetPosition()[0]) {
                    upperBound = currentDirection;
                    currentDirection = (lowerBound + currentDirection) / 2;
                } else {
                    lowerBound = currentDirection;
                    currentDirection = (upperBound + currentDirection) / 2;
                }

                this.changeDirection(currentDirection);

                currentPositionDifference[0] = nextPositionDifference[0];
                nextEndingPosition = this.calculateEndingPosition(tStep);

                nextPositionDifference[0] = Math.abs(
                        nextEndingPosition[0] - this.getTargetPosition()[0]);
                nextPositionDifference[1] = Math.abs(
                        nextEndingPosition[1] - this.getTargetPosition()[1]);
            }
        }

        return currentDirection;
    }

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
    public double[] calculateMaxRange(double tStep) {
        double previousDirection = this.getDirection();

        // find theoretical max
        double vSqrOvrG = Math.pow(this.getSpeed(), 2) / 9.807;
        double heightDifference = this.getStartingPosition()[1]
                - this.getTargetPosition()[1];
        double maxRange = Math.pow(vSqrOvrG, 2)
                + 2 * vSqrOvrG * heightDifference;
        maxRange = Math.sqrt(maxRange);

        // find angle used for the theoretical max
        double angle = Math.atan(vSqrOvrG / maxRange);
        this.changeDirection(angle);

        Double[] calculatableMax = this.calculateEndingPosition(tStep);
        double[] maxRangeAndAngle = { calculatableMax[0], angle };
        this.changeDirection(previousDirection);
        return maxRangeAndAngle;
    }

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
    public boolean isAtMaxRange(double distance, double tStep) {
        return distance >= this.calculateMaxRange(tStep)[0];
    }

    /**
     * Main method.
     */
    public static void main(String[] args) {

        // configure projectile
        double tStep = 0.00001;
        ProjectileCalculatorPOC pc = new ProjectileCalculatorPOC(0.15, 30,
                45 * Math.PI / 180);
        pc.changeStartPosition(0, 4);
        pc.changeTargetPosition(96, 0);

        // calculate details
        Double[] endPosition = pc.calculateEndingPosition(tStep);
        double[] maxRangeAndAngle = pc.calculateMaxRange(tStep);

        // print details
        System.out.println("maximum range: " + maxRangeAndAngle[0]
                + " | launch angle: " + maxRangeAndAngle[1] * 180 / Math.PI);
        System.out.println(
                "end x: " + endPosition[0] + " | end y: " + endPosition[1]);

        if (pc.isAtMaxRange(pc.getTargetPosition()[0], tStep)) {
            System.out.println("At max range!");
        } else {
            System.out.println("Not at max range!");
        }

        System.out.println(
                "flight time: " + pc.calculateFlightTime(tStep) + " seconds");
        // System.out.println(
        //         "Required speed: " + pc.calculateRequiredSpeed(tStep, 1));
        // System.out.println("Required direction: "
        //         + pc.calculateRequiredDirection(tStep, 1));
        System.out.println("Required direction: "
                + pc.calculateRequiredDirection(tStep, 0.00001) * 180
                        / Math.PI);

    }
}