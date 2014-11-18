package phase1;


public class PingballPhase1Benchmark {

    // length of time steps
    private static final double timeResolutionSec = 0.0005;
    private static final double timeResolutionMilli = timeResolutionSec * 1000; 
    
    public static void main(String[] args) {
        Board board;
        if (args.length == 0) {
            board = BenchmarkBoards.createBoard("default");
        }
        else if (args.length == 1) {
            String boardType = args[0];
            board = BenchmarkBoards.createBoard(boardType);
        }
        else {
            throw new RuntimeException("Too many arguments");
        }
        
        
        long startTime = System.currentTimeMillis();
        while (true) {
            long nextTime = System.currentTimeMillis();
            if (nextTime - startTime > timeResolutionMilli) {
                board.updateBoard(timeResolutionSec);
                startTime = nextTime;
            }
            System.out.println(board.toPrint());
            
        }
    }

}

