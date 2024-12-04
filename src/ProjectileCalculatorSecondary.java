import components.map.Map;
import components.map.Map1L;

public class ProjectileCalculatorSecondary implements ProjectileCalculator {
    /*
     * Public members ---------------------------------------------------------
     */

    /**
     * Acceptable margin of error for fields / variables.
     */
    public static final double EPSILON = 0.0000001;

    /**
     * Simply the number 10, for hashCode() implementation.
     */
    public static final int TEN = 10;

    /*
     * Common methods (from Object) -------------------------------------------
     */

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
    public String toString() {
        return "speed: " + this.getSpeed() + ", angle/direction: "
                + this.getDirection();
    }

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ProjectileCalculator)) {
            return false;
        }
        ProjectileCalculator pc = (ProjectileCalculator) obj;
        if (Math.abs(this.getSpeed() - pc.getSpeed()) > EPSILON) {
            return false;
        }
        if (Math.abs(this.getDirection() - pc.getDirection()) > EPSILON) {
            return false;
        }
        if (Math.abs(this.getStartingPosition()[0]
                - pc.getStartingPosition()[0]) > EPSILON
                || Math.abs(this.getStartingPosition()[1]
                        - pc.getStartingPosition()[1]) > EPSILON) {
            return false;
        }
        if (Math.abs(this.getTargetPosition()[0]
                - pc.getTargetPosition()[0]) > EPSILON
                || Math.abs(this.getTargetPosition()[1]
                        - pc.getTargetPosition()[1]) > EPSILON) {
            return false;
        }
        return true;
    }

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
    public int hashCode() {
        return (int) (this.getSpeed() + this.getDirection()) % TEN;
    }

    /*
     * Other non-kernel methods -----------------------------------------------
     */

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
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

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
    public Double[] calculateEndingPosition(double tStep) {
        Map<Double, Double[]> trajectory = new Map1L<Double, Double[]>();
        double elapsedTime = this.calculateTrajectory(trajectory, tStep);

        Double[] endingPosition = trajectory.value(elapsedTime);

        return endingPosition;
    }

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
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

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
    public double calculateFlightTime(double tStep) {
        Map<Double, Double[]> trajectory = new Map1L<Double, Double[]>();

        return this.calculateTrajectory(trajectory, tStep);
    }

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
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

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
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

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
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

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
    public boolean isAtMaxRange(double distance, double tStep) {
        return distance >= this.calculateMaxRange(tStep)[0];
    }
}
