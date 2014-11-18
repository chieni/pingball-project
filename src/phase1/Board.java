package phase1;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import phase1.Wall.WallDescription;
import physics.Geometry.DoublePair;
import physics.Vect;

/**
 * Board is an abstract datatype that represents the playing board of a Pingball game.
 * Board contains information regarding all of the balls and gadgets that exist on the board.
 * 
 *
 */
public class Board {
    
    // Rep invariant:
    //  - no two gadgets can ever occupy the same location
    //
    private static final double DEFAULT_GRAVITY = 25.0;
    private static final double DEFAULT_MU = 0.025;
    private static final double DEFAULT_MU2 = 0.025;
    
    public final int height;
    public final int width;
    private final double MU;
    private final double MU2;
    private final double GRAVITY;
    private final  Set<Gadget> board; // contains all gadgets in the board (including balls)
    private List<Ball> balls = new ArrayList<Ball>(); // contains all balls in the board
    private final Set<Wall> walls;
    
    public Board(Optional<File> file){
        this.height = 20;
        this.width = 20;
        this.MU = DEFAULT_MU;
        this.MU2 = DEFAULT_MU2;
        this.GRAVITY = DEFAULT_GRAVITY;
        board = new HashSet<Gadget>();
        walls=createWalls();
        board.addAll(createGadgetWalls());
        checkRep();
    }
    
    public Board(){
        this.height = 20;
        this.width = 20;
        this.MU = DEFAULT_MU;
        this.MU2 = DEFAULT_MU2;
        this.GRAVITY = DEFAULT_GRAVITY;
        board = new HashSet<Gadget>();
        walls=createWalls();
        board.addAll(createGadgetWalls());
        checkRep();
    }
    
    public Board(Optional<Double> gravity, Optional<Double> friction1, Optional<Double> friction2){
        this.height = 20;
        this.width = 20;
        if (gravity.isPresent()){
            this.GRAVITY = gravity.get();
        }else {
            this.GRAVITY = DEFAULT_GRAVITY;
        }
        
        if (friction1.isPresent()){
            this.MU = friction1.get();
        }
        else {
            this.MU = DEFAULT_MU;
        }
        
        if (friction2.isPresent()){
            this.MU2 = friction2.get();
        }
        else {
            this.MU2 = DEFAULT_MU2;
        }
        
        board = new HashSet<Gadget>();
        walls=createWalls();
        board.addAll(createGadgetWalls());
        checkRep();
    }
    
    
    private void checkRep() {
        List<DoublePair> myCoordinates= new ArrayList<DoublePair>();  
        for (Gadget gadgets: board){
            if (!gadgets.isBall()){
                List<DoublePair> location= gadgets.toConsoleCoordinates();
                for (DoublePair coordinate: location){
                    assert(!(myCoordinates.contains(coordinate)));
                }
                myCoordinates.addAll(location);
            }
        }
    }
    
    /**
     * Updates the correct wall to be invisible 
     * @param wallLocationMessage: a message from the server describing where the wall is located
     * (top, bottom, right, or left) and the name of the board its connected to
     */
    public void findAndUpdateInvisibleWall(String wallLocationMessage, String connectedBoardName){
        WallDescription wallDescription;
        if(wallLocationMessage=="top"){
            wallDescription=WallDescription.TOP;   
        }else if(wallLocationMessage=="bottom"){
            wallDescription=WallDescription.BOTTOM;
        }else if (wallLocationMessage=="right"){
            wallDescription=WallDescription.RIGHT;
        }else{
            wallDescription=WallDescription.LEFT;
        }
        for (Wall myWall: walls){
            if (myWall.getWallDescription()== wallDescription){
                myWall.setConnectedBoardName(connectedBoardName);
            }
        }
    }
    
    /**
     * Sets an invisible wall back to solid
     * @param connectedBoardName the name of the board the invisible wall is connected to
     */
    public void findAndUpdateSolidWall(String connectedBoardName){
        for (Wall myWall: walls){
            if (myWall.getConnectedBoardName().equals(connectedBoardName)){
                myWall.setConnectedBoardName("");
            }
        }
    }
    
    /**
     * Removes a ball from the board (from balls)
     * @param myBall: the ball that is being removed
     */
    public void removeBallfromBoard(Ball myBall){
        balls.remove(myBall);
    }
    
    /**
     * Adds a ball to the board (to balls)
     * @param myBall: the ball that is being added
     */
    public void addBalltoBoard(Ball myBall){
        balls.add(myBall);  
    }
    
    /**
     * Takes in a list of gadgets and adds them to the board. If a gadget is passed
     * with a location within another gadget's bounding box, the gadget is not inserted
     * 
     * @param gadgets list of gadgets to be added to the board
     * Precondition: does not contain any walls
     */
    public void insertGadgets(List<Gadget> gadgets) {
        for (Gadget gadget : gadgets) {
            if (gadget.isBall()) {
                Ball ball = (Ball) gadget;
                balls.add(ball);
            }else{
                board.add(gadget);
            }
        }
    }
    
    
    public List<Ball> getBalls() {
        return this.balls; 
    }
    
    public Set<Gadget> testHelperGetGadgets() {
        return this.board; 
    }
    
    /**
     * @return Set of Gadgets representing the walls for this board
     */
    private Set<Gadget> createGadgetWalls(){
        Set<Gadget> gadgetWalls = new HashSet<Gadget>();
        for (Wall myWall: walls){
            gadgetWalls.add(myWall);
        }
        return gadgetWalls;
    }
    
    /**
     * @return Set of Walls representing the walls for this board
     */
    private Set<Wall> createWalls(){
        Set<Wall> walls = new HashSet<Wall>();
        walls.add(new Wall(0, 0, 0, height, WallDescription.LEFT, this));   // left wall
        walls.add(new Wall(1, 0, width+1, 0, WallDescription.TOP, this));    // top wall
        walls.add(new Wall(0, height+1, width, height+1, WallDescription.BOTTOM, this)); // bottom wall
        walls.add(new Wall(width+1, 1, width+1, height+1, WallDescription.RIGHT, this)); // right wall
        return walls;
    }
    
    /**
     * Checks every ball in board to see if its is about to collide with a gadget
     * if it is, then it "reflects" the ball and triggers the action of the
     * hit gadget.
     * 
     * @param balls2 list of the Gadgets representing the balls in this board 
     * @param time double that represents time resolution
     */
    public void updateBallVelocities(List<Ball> balls, double time) {
        // original set of gadgets before updating any velocities
        Set<Gadget> unUpdatedGadgets = new HashSet<Gadget>();
        unUpdatedGadgets.addAll(board);
      
        List<Ball> ballsCopy = new ArrayList<Ball>(balls);
        
        for (Ball ball : ballsCopy) {
            Gadget otherGadget = getGadgetWithMinimumTimeToCollision(ball, unUpdatedGadgets);
            // collides with gadget and ball reflects
            if (otherGadget.getTimeUntilCollision(ball) < time) {
                Vect reflectionVector = otherGadget.getReflectionVector(ball);
                ball.setVelocity(applyFriction(reflectionVector, time));
            }
            // ball accelerates due to gravity
            ball.accelerateDueToGravity(time, this.GRAVITY);
        }       
    }
    
    /**
     * Gets the gadget among all gadgets in board that a single ball is first going
     * to collide with. The single ball is excluded from the set of gadgets in the board, so
     * return value cannot be equal to ball.
     * 
     * @param ball
     * @return the gadget the ball is predicted to collide with the soonest
     */
    public Gadget getGadgetWithMinimumTimeToCollision(Ball ball, Set<Gadget> originalGadgets) {
        // do not let a ball collide with itself        
        List<Ball> ballsExcludingThisBall = new ArrayList<Ball>();
        ballsExcludingThisBall.addAll(balls);
        ballsExcludingThisBall.remove(ball);
        
        List<Gadget> allGadgets= new ArrayList<Gadget>();
        allGadgets.addAll(originalGadgets);
        
        // find the gadget with minimum time till collision
        Gadget minimumTimeGadget = (allGadgets).get(0); 
        double minimumTime = Double.POSITIVE_INFINITY;
        for (Gadget gadget : board) {
            if (gadget.getTimeUntilCollision(ball) < minimumTime) {
                minimumTimeGadget = gadget;
                minimumTime = minimumTimeGadget.getTimeUntilCollision(ball);
            }
        }
        for (Ball otherBall: ballsExcludingThisBall){
            if (otherBall.getTimeUntilCollision(ball) < minimumTime) {
                minimumTimeGadget = otherBall;
                minimumTime = minimumTimeGadget.getTimeUntilCollision(ball);
            }
        }
        return minimumTimeGadget;
    }
    
    /**
     * Decreases magnitude of velocity vector using suggested approximation in
     * problem set specification.
     * Vnew = Vold × ( 1 - mu × deltat - mu2 × |Vold| × deltat )
     * 
     * @param velocity a Vector representing current velocity of a ball that
     *      has collided
     * @return new velocity adjusted for friction
     */
    public Vect applyFriction(Vect velocity, double deltaTime) {
        double x= Math.abs(velocity.x());
        double y= Math.abs(velocity.y());
        Double length=(new Vect(x,y)).length();
        Vect newVelocity = velocity.times(1 - this.MU * deltaTime - this.MU2 * length * deltaTime);
        return newVelocity;
    }
    
    /**
     * Makes every gadget in the board update its position based on their current velocities.
     * 
     * @param gadgets Set of all Gadgets in the board
     */
    public void updateGadgetPositions(Set<Gadget> gadgets, double time) {
        for (Gadget gadget : gadgets) {
            gadget.changePosition(time);
        }
    }
    
    /**
     * Makes every ball in the board update its position based on their current velocities.
     * 
     * @param gadgets Set of all Gadgets in the board
     */
    public void updateBallPositions(List<Ball> balls, double time) {
        for (Ball ball : balls) {
            ball.changePosition(time);
        }
    }
    
    /**
     * Updates the board by updating all gadgets based on current positions and
     * velocities
     * 
     * @param time the time that has passed since previous update has been called
     * 0 if this is the first time updateBoard is being called
     */
    public void updateBoard(double time) {
        updateBallVelocities(balls, time);
        updateBallPositions(balls, time);
        updateGadgetPositions(board, time);
    }
    
    /**
     * Creates string representation of the board. 
     *
     * @return String representing the string representation of this board to 
     * be printed to the console
     */
    public String toPrint() {
        // initialize 2D array with spaces
        String[][] grid = new String[22][22];
        for (int i = 0; i < 22; i++) {
            for (int j = 0; j < 22; j++){
                grid[i][j] = " "; 
            }
        }
        
        // write respective symbols in the coordinates that gadgets occupy 
        for (Gadget item: board) {
            List<DoublePair> gadgetCoordinates = item.toConsoleCoordinates();
            for (DoublePair coord: gadgetCoordinates) {
                grid[(int)coord.d1][(int)coord.d2] = item.getSymbol();
            }
        }
        
        for (Ball item: balls) {
            List<DoublePair> gadgetCoordinates = item.toConsoleCoordinates();
            for (DoublePair coord: gadgetCoordinates) {
                grid[(int)coord.d1][(int)coord.d2] = item.getSymbol();
            }
        }
        
        for (Wall item: walls){
            if (item.getIsInvisible()){
                List<DoublePair> gadgetCoordinates = item.toConsoleCoordinates();
                String wallRepresentation=((Wall) item).getStringRepresentationInvsibleWall();
                int wallRepresentationIndex=0;
                for (DoublePair coord: gadgetCoordinates) {
                    grid[(int)coord.d1][(int)coord.d2] = Character.toString(wallRepresentation.charAt(wallRepresentationIndex));
                    wallRepresentationIndex+=1;
                }
            }
        }

        // print each row in the grid on a new line
        String output = "";
        for (int i = 0; i < 22; i++) {
            for (int j = 0; j < 22; j++){
                output += grid[j][i];
            }
            output+="\n";
        }
        return output;
    }
    
    @Override
    public String toString() {
        return toPrint();
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Board)) {
            return false;
        }
        Board thatBoard = (Board) thatObject;
        return board.equals(thatBoard.board);
    }
    
    
}

