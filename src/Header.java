import java.io.*;

public class Header{
    public static int N, M, P;
    public static String S;
    
    //KONSTRUKTOR
    public static void setHeader(int n, int m, int p, String s){
        N = n;
        M = m;
        P = p;
        S = s;
    }
    public static void saveAsFile(char[][] board, String filename) {
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
}