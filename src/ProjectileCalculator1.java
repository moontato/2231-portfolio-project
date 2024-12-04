/**
 * {@code Projectilecalculator} with implementations of primary methods.
 *
 * @convention $this.speed > 0 and $this.startingPosition !=
 *             $this.targetPosition
 * @correspondence if $this.speed > 0 and $this.startingPosition !=
 *                 $this.targetPosition then this = ($this.startingPosition,
 *                 $this.targetPosition, $this.speed, $this.direction)
 */
public class ProjectileCalculator1 extends ProjectileCalculatorSecondary {
    /*
     * Private members --------------------------------------------------------
     */

    /** Starting [x, y] coordinates for the projectile. */
    private Double[] startingCoordinates;

    /** Target [x, y] coordinates for the projectile. */
    private Double[] targetCoordinates;

    /** Speed (m/s) of the projectile. */
    private double speed;

    /** Direction (radians) of the projectile. */
    private double direction;

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {
        this.startingCoordinates = new Double[2];
        this.targetCoordinates = new Double[2];
        this.speed = 0;
        this.direction = 0;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public ProjectileCalculator1() {
        this.createNewRep();
    }

    /**
     * Constructor from {@code speed} and {@code direction}.
     *
     * @param speed
     *            {@code double} to initialize the speed from
     * @param direction
     *            {@code double} to initialize the direction from
     */
    public ProjectileCalculator1(double speed, double direction) {
        assert speed >= 0 : "Violation of: speed >= 0";
        this.startingCoordinates = new Double[2];
        this.targetCoordinates = new Double[2];
        this.speed = speed;
        this.direction = direction;
    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @Override
    public final ProjectileCalculator newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(ProjectileCalculator source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof ProjectileCalculator1 : ""
                + "Violation of: source is of dynamic type NaturalNumber1L";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case.
         */
        ProjectileCalculator1 localSource = (ProjectileCalculator1) source;
        this.startingCoordinates = localSource.startingCoordinates;
        this.targetCoordinates = localSource.targetCoordinates;
        this.speed = localSource.speed;
        this.direction = localSource.direction;
        localSource.createNewRep();
    }

    /*
     * Kernel methods ---------------------------------------------------------
     */

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
    public Double[] getStartingPosition() {
        Double[] newCoordinates = new Double[2];
        newCoordinates[0] = this.startingCoordinates[0].doubleValue();
        newCoordinates[1] = this.startingCoordinates[1].doubleValue();
        return newCoordinates;
    }

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
    public Double[] getTargetPosition() {
        Double[] newCoordinates = new Double[2];
        newCoordinates[0] = this.targetCoordinates[0].doubleValue();
        newCoordinates[1] = this.targetCoordinates[1].doubleValue();
        return newCoordinates;
    }

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
    public double getSpeed() {
        return this.speed;
    }

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
    public double getDirection() {
        return this.direction;
    }

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
    public void changeStartPosition(double newX, double newY) {
        this.startingCoordinates[0] = newX;
        this.startingCoordinates[1] = newY;
    }

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
    public void changeTargetPosition(double newX, double newY) {
        this.targetCoordinates[0] = newX;
        this.targetCoordinates[1] = newY;
    }

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
    public void changeSpeed(double newSpeed) {
        this.speed = newSpeed;
    }

    // CHECKSTYLE: ALLOW THIS METHOD TO BE OVERRIDDEN
    @Override
    public void changeDirection(double newDirection) {
        this.direction = newDirection;
    }

}
