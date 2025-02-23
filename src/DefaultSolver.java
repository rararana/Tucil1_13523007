import java.util.*;

public class DefaultSolver{
    public static final String RESET = "\033[0m";  // Text Reset
    static final String[] ANSI = new String[26];
    static long start, end;
    static{
        ANSI[0] = "\033[0;30m";   // BLACK
        ANSI[1] = "\033[0;31m";     // RED
        ANSI[2] = "\033[0;32m";   // GREEN
        ANSI[3] = "\033[0;33m";  // YELLOW
        ANSI[4] = "\033[0;34m";    // BLUE
        ANSI[5] = "\033[0;35m";  // PURPLE
        ANSI[6] = "\033[0;36m";    // CYAN
        ANSI[7] = "\033[0;37m";   // WHITE
        ANSI[8] = "\033[1;30m";  // BLACK BOLD
        ANSI[9] = "\033[1;31m";    // RED BOLD
        ANSI[10] = "\033[1;32m";  // GREEN BOLD
        ANSI[11] = "\033[1;33m"; // YELLOW BOLD
        ANSI[12] = "\033[1;34m";   // BLUE BOLD
        ANSI[13] = "\033[1;35m"; // PURPLE BOLD
        ANSI[14] = "\033[1;36m";   // CYAN BOLD
        ANSI[15] = "\033[1;37m";  // WHITE BOLD
        ANSI[16] = "\033[4;30m";  // BLACK UNDERLINE
        ANSI[17] = "\033[4;31m";    // RED UNDERLINE
        ANSI[18] = "\033[4;32m";  // GREEN UNDERLINE
        ANSI[19] = "\033[4;33m"; // YELLOW UNDERLINE
        ANSI[20] = "\033[4;34m";   // BLUE UNDERLINE
        ANSI[21] = "\033[4;35m"; // PURPLE UNDERLINE
        ANSI[22] = "\033[4;36m";   // CYAN UNDERLINE
        ANSI[23] = "\033[4;37m";  // WHITE UNDERLINE
        ANSI[24] = "\033[40m";  // BLACK BG
        ANSI[25] = "\033[41m";    // RED BG
    }

    static char[][] board;
    List<Shape> shapes;
    Map<Character, List<Pair>> blockMap;  
    static long count = 0;
    static boolean foundSolution;

    //KONSTRUKTOR
    public DefaultSolver(){
        board = new char[Header.N][Header.M];
        this.shapes = new ArrayList<>();
        this.blockMap = new HashMap<>();

        for(int i=0; i<Header.N; i++){
            for(int j=0; j<Header.M; j++){
                board[i][j] = '.';
            }
        }
    }
    private boolean isFilled() {
        for(int i=0; i<Header.N; i++){
            for(int j=0; j<Header.M; j++){
                if(board[i][j] == '.'){
                    return false;
                }
            }
        }
        return true;
    }
    private List<Pair> normalize(List<Pair> shape){
        int minRow = Integer.MAX_VALUE, minCol = Integer.MAX_VALUE;
        for(Pair p : shape){
            minRow = Math.min(minRow, p.x);
            minCol = Math.min(minCol, p.y);
        }
        List<Pair> normalized = new ArrayList<>();
        for(Pair p : shape){
            normalized.add(new Pair(p.x-minRow, p.y-minCol));
        }
        return normalized;
    }
    private List<Pair> rotate(List<Pair> shape){
        List<Pair> rotated = new ArrayList<>();
        for(Pair p : shape){
            rotated.add(new Pair(p.y, -p.x));
        }
        return normalize(rotated);
    }
    private List<Pair> reflectX(List<Pair> shape){
        List<Pair> reflected = new ArrayList<>();
        for(Pair p : shape){
            reflected.add(new Pair(p.x, -p.y));
        }
        return normalize(reflected);
    }
    private boolean canPlace(int x, int y, List<Pair> shape){
        for(Pair p : shape){
            int nx = x + p.x, ny = y + p.y;
            if(nx >= Header.N || ny >= Header.M || board[nx][ny] != '.'){
                return false;
            }
        }
        return true;
    }
    private void placeBlock(int x, int y, List<Pair> shape, char c, boolean place){
        for(Pair p : shape){
            int nx = x + p.x, ny = y + p.y;
            board[nx][ny] = place ? c : '.';
        }
    }
    private List<List<Pair>> transform(List<Pair> shape){
        List<List<Pair>> transformations = new ArrayList<>();
        transformations.add(shape);
        transformations.add(reflectX(shape));

        shape = rotate(shape); //90 deg clockwise
        transformations.add(shape);
        transformations.add(reflectX(shape));

        shape = rotate(shape); //180 deg clockwise
        transformations.add(shape);
        transformations.add(reflectX(shape));
        
        shape = rotate(shape); //270 deg clockwise
        transformations.add(shape);
        transformations.add(reflectX(shape));
        
        return transformations;
    }
    private boolean solve(int idx){
        if(isFilled()) return true;
        if(idx >= Header.P) return false;

        Shape currentShape = shapes.get(idx);
        if(currentShape.isPlaced()) return solve(idx + 1);

        List<Pair> shapeBlock = blockMap.get(currentShape.getSymbol());
        List<List<Pair>> allTransforms = transform(shapeBlock);

        for(List<Pair> transformed : allTransforms){
            for(int i=0; i<Header.N; i++) {
                for (int j=0; j<Header.M; j++) {
                    if(canPlace(i, j, transformed)){
                        placeBlock(i, j, transformed, currentShape.getSymbol(), true);
                        currentShape.setPlaced(true);
                        count++;
                        if(solve(idx + 1)) return true;
                        currentShape.setPlaced(false);
                        placeBlock(i, j, transformed, currentShape.getSymbol(), false);
                    }
                }
            }
        }
        return false;
    }
    public void readBlocks(List<String> blockLines){
        int i = 0, blockSum = 0, cnt = 1;
        char prev = ' ';
        boolean flag = true;
        for(String line : blockLines){
            for(int j=0; j<line.length(); j++){
                char a = line.charAt(j);
                if(a == ' ') continue;
                if(flag || a == prev){
                    flag = false;
                    blockSum++;
                    blockMap.computeIfAbsent(a, key -> new ArrayList<>()).add(new Pair(i, j));
                }else{
                    cnt++;
                    shapes.add(new Shape(prev));
                    blockSum++;
                    i = 0;
                    blockMap.computeIfAbsent(a, key -> new ArrayList<>()).add(new Pair(i, j));
                }
                prev = a;
            }
            i++;
        }
        shapes.add(new Shape(prev));
        start = System.currentTimeMillis();
        if(cnt != Header.P){
            System.out.println("Jumlah blok puzzle tidak sesuai.");
            foundSolution = false;
        } else if(blockSum != Header.N*Header.M) {
            System.out.println("Tidak ada solusi.");
            foundSolution = false;
        } else {
            if(solve(0)){
                System.out.println("Solusi ditemukan!");
                printBoard();
                foundSolution = true;
            }else{
                System.out.println("Tidak ada solusi.");
                foundSolution = false;
            }
        }
        end = System.currentTimeMillis();
    }
    private void printBoard(){
        for(int i=0; i<Header.N; i++){
            for(int j=0; j<Header.M; j++){
                System.out.print(ANSI[board[i][j]-'A']+board[i][j]+RESET);
            }
            System.out.println();
        }
    }
}