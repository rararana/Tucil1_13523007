import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Masukkan nama file input: ");
        String filename = sc.nextLine();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String firstLine = br.readLine();
            String[] firstLineParts = firstLine.split(" ");
            int n = Integer.parseInt(firstLineParts[0]);
            int m = Integer.parseInt(firstLineParts[1]);
            int p = Integer.parseInt(firstLineParts[2]);
            String s = br.readLine().trim();
            Header.setHeader(n, m, p, s);
            List<String> blockLines = new ArrayList<>();
            String line;
            while((line = br.readLine()) != null){
                blockLines.add(line);
            }

            if (Header.S.equals("DEFAULT")) {
                DefaultSolver solver = new DefaultSolver();
                // long start = System.currentTimeMillis();
                solver.readBlocks(blockLines);
                //long end = System.currentTimeMillis();

                System.out.println();
                System.out.println("Banyak kasus yang ditinjau: " + DefaultSolver.count);
                System.out.println("Waktu eksekusi: " + (DefaultSolver.end - DefaultSolver.start) + " ms");
                
                if(DefaultSolver.foundSolution){
                    System.out.print("Apakah anda ingin menyimpan solusi? (ya/tidak) ");
                    String save = sc.nextLine();
                    if(save.equalsIgnoreCase("ya")){
                        System.out.print("Masukkan nama file: ");
                        String saveFilename = sc.nextLine();
                        Header.saveAsFile(DefaultSolver.board, saveFilename);
                    } else {
                        System.out.println("Solusi tidak disimpan.");
                    }
                }

            } else {
                System.out.println("Mode bukan DEFAULT, default solver tidak dijalankan.");
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
        }
        sc.close();
    }
}
