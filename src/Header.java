import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Header{
    public static int N, M, P;
    public static String S;
    public static Color getColor(char c){
        return switch (c){
            case 'A' -> new Color(255, 0, 0);
            case 'B' -> new Color(0, 0, 255);
            case 'C' -> new Color(0, 255, 0);
            case 'D' -> new Color(255, 255, 0);
            case 'E' -> new Color(255, 165, 0);
            case 'F' -> new Color(128, 0, 128);
            case 'G' -> new Color(0, 255, 255);
            case 'H' -> new Color(255, 20, 147);
            case 'I' -> new Color(139, 69, 19);
            case 'J' -> new Color(0, 128, 128);
            case 'K' -> new Color(173, 255, 47);
            case 'L' -> new Color(75, 0, 130);
            case 'M' -> new Color(255, 192, 203);
            case 'N' -> new Color(0, 0, 139);
            case 'O' -> new Color(255, 140, 0);
            case 'P' -> new Color(153, 50, 204);
            case 'Q' -> new Color(34, 139, 34);
            case 'R' -> new Color(255, 99, 71);
            case 'S' -> new Color(47, 79, 79);
            case 'T' -> new Color(72, 61, 139);
            case 'U' -> new Color(220, 20, 60);
            case 'V' -> new Color(0, 191, 255);
            case 'W' -> new Color(255, 215, 0);
            case 'X' -> new Color(0, 250, 154);
            case 'Y' -> new Color(186, 85, 211);
            case 'Z' -> new Color(70, 130, 180);
            default -> Color.WHITE;
        };
    }
    //KONSTRUKTOR
    public static void setHeader(int n, int m, int p, String s){
        N = n;
        M = m;
        P = p;
        S = s;
    }
    public static void saveAsFile(char[][] board, String filename){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for(char[] b : board){
                writer.write(b);
                writer.newLine();
            }
            System.out.println("Solusi berhasil disimpan dengan nama " + filename);
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menulis ke file: " + e.getMessage());
        }
    }
    public static void saveAsImage(char[][] board, String filename){
        int width = M*50;
        int height = N*50;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D blockImage = image.createGraphics();
        blockImage.setColor(Color.WHITE);
        blockImage.fillRect(0, 0, width, height);
        
        for(int i=0; i<N; i++){
            for(int j=0; j<M; j++){
                if(board[i][j] == ' ') continue;
                blockImage.setColor(getColor(board[i][j]));
                blockImage.fillRect(j*50, i*50, 50, 50);
            }
        }
        blockImage.dispose();
        try {
            ImageIO.write(image, "png", new File(filename));
            System.out.println("Solusi berhasil disimpan sebagai " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
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