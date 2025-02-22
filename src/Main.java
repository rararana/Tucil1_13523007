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

            if(Header.S.equals("DEFAULT")){
                DefaultSolver solver = new DefaultSolver();
                // long start = System.currentTimeMillis();
                solver.readBlocks(blockLines);
                //long end = System.currentTimeMillis();

                System.out.println();
                System.out.println("Banyak kasus yang ditinjau: " + DefaultSolver.count);
                System.out.println("Waktu eksekusi: " + (DefaultSolver.end - DefaultSolver.start) + " ms");
                
                if(DefaultSolver.foundSolution){
                    System.out.print("Apakah anda ingin menyimpan solusi sebagai txt? (ya/tidak) ");
                    String save = sc.nextLine();
                    if(save.equalsIgnoreCase("ya")){
                        System.out.print("Masukkan nama file: ");
                        String saveFilename = sc.nextLine();
                        Header.saveAsFile(DefaultSolver.board, saveFilename);
                    } else {
                        System.out.println("Solusi tidak disimpan sebagai txt.");
                    }
                    System.out.print("Simpan solusi sebagai gambar? (ya/tidak) ");
                    String saveImg = sc.nextLine();
                    if(saveImg.equalsIgnoreCase("ya")){
                        System.out.print("Masukkan nama file: ");
                        String saveImgname = sc.nextLine();
                        Header.saveAsImage(DefaultSolver.board, saveImgname);
                    } else {
                        System.out.println("Solusi tidak disimpan sebagai gambar.");
                    }
                }
            } else if(Header.S.equals("CUSTOM")) {
                CustomSolver solver = new CustomSolver();
                // long start = System.currentTimeMillis();
                solver.readBlocks(blockLines);
                //long end = System.currentTimeMillis();

                System.out.println();
                System.out.println("Banyak kasus yang ditinjau: " + CustomSolver.count);
                System.out.println("Waktu eksekusi: " + (CustomSolver.end - CustomSolver.start) + " ms");
                
                if(CustomSolver.foundSolution){
                    System.out.print("Apakah anda ingin menyimpan solusi sebagai txt? (ya/tidak) ");
                    String save = sc.nextLine();
                    if(save.equalsIgnoreCase("ya")){
                        System.out.print("Masukkan nama file: ");
                        String saveFilename = sc.nextLine();
                        Header.saveAsFile(CustomSolver.board, saveFilename);
                    } else {
                        System.out.println("Solusi tidak disimpan sebagai txt.");
                    }
                    System.out.print("Simpan solusi sebagai gambar? (ya/tidak) ");
                    String saveImg = sc.nextLine();
                    if(saveImg.equalsIgnoreCase("ya")){
                        System.out.print("Masukkan nama file: ");
                        String saveImgname = sc.nextLine();
                        Header.saveAsImage(CustomSolver.board, saveImgname);
                    } else {
                        System.out.println("Solusi tidak disimpan sebagai gambar.");
                    }
                }
            } else {
                System.out.println("Mode bukan CUSTOM, Custom solver tidak dijalankan.");
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
        }
        sc.close();
    }
}
