import java.util.*;

public class DefaultSolver{
    static char[][] board;
    List<Shape> shapes;
    Map<Character, List<Pair>> blockMap;  
    static long count = 0;

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
        int i = 0, blockSum = 0;
        char prev = ' ';
        boolean flag = true;
        for(String line : blockLines){
            for(int j=0; j <line.length(); j++){
                char a = line.charAt(j);
                if(a == ' ') continue;
                if(flag || a == prev){
                    flag = false;
                    blockSum++;
                    blockMap.computeIfAbsent(a, key -> new ArrayList<>()).add(new Pair(i, j));
                }else{
                    shapes.add(new Shape(prev));
                    blockSum++;
                    blockMap.computeIfAbsent(a, key -> new ArrayList<>()).add(new Pair(i, j));
                }
                prev = a;
            }
            i++;
        }
        shapes.add(new Shape(prev));

        if(blockSum != Header.N*Header.M){
            System.out.println("Tidak ada solusi.");
        } else {
            if(solve(0)){
                System.out.println("Solusi ditemukan!");
                printBoard();
            }else{
                System.out.println("Tidak ada solusi.");
            }
        }
    }
    private void printBoard(){
        for(char[] row : board){
            System.out.println(new String(row));
        }
    }
}

class Pair{
    int x, y;
    Pair(int x, int y){
        this.x = x;
        this.y = y;
    }
}

class Shape{
    private final char symbol;
    private boolean placed;

    Shape(char symbol){
        this.symbol = symbol;
        this.placed = false;
    }

    char getSymbol(){
        return symbol;
    }

    boolean isPlaced(){
        return placed;
    }

    void setPlaced(boolean placed){
        this.placed = placed;
    }
}