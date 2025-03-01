import javax.swing.*;
import java.awt.*;
import java.io.*;

public class GUI extends JFrame {
    private JTextField filePath;
    private JButton process, saveTxt, saveImg;
    private JPanel boardPanel;
    private JScrollPane scrollPane;
    private boolean hasSolution = false;
    private JLabel timeLabel, caseLabel;

    public GUI(){
        setTitle("IQ Puzzler Pro");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        filePath = new JTextField(30);
        JButton browseButton = new JButton("Cari");
        process = new JButton("Proses");
        browseButton.addActionListener(e -> chooseFile());
        process.addActionListener(e -> processFile());
        top.add(new JLabel("File Input: "));
        top.add(filePath);
        top.add(browseButton);
        top.add(process);
        add(top, BorderLayout.NORTH);
        boardPanel = new JPanel();
        scrollPane = new JScrollPane(boardPanel);
        add(scrollPane, BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        saveTxt = new JButton("Simpan sebagai TXT");
        saveImg = new JButton("Simpan sebagai Gambar");
        saveTxt.setEnabled(false);
        saveImg.setEnabled(false);
        saveTxt.addActionListener(e -> saveAsTxt());
        saveImg.addActionListener(e -> saveAsImage());
        buttonPanel.add(saveTxt);
        buttonPanel.add(saveImg);
        JPanel infoPanel = new JPanel();
        timeLabel = new JLabel("Waktu Eksekusi: -");
        caseLabel = new JLabel("Jumlah Kasus: -");
        infoPanel.add(timeLabel);
        infoPanel.add(caseLabel);
        bottom.add(buttonPanel, BorderLayout.NORTH);
        bottom.add(infoPanel, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);
        setVisible(true);
    }
    private void chooseFile(){
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if(returnValue == JFileChooser.APPROVE_OPTION){
            filePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }
    private void processFile() {
        String filename = filePath.getText();
        if(filename.isEmpty()){
            JOptionPane.showMessageDialog(this, "Pilih file terlebih dahulu! >:(");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String firstLine = br.readLine();
            String[] parts = firstLine.split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            int p = Integer.parseInt(parts[2]);
            String s = br.readLine().trim();
            Header.setHeader(n, m, p, s);
            java.util.List<String> blockLines = new java.util.ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                blockLines.add(line);
            }
            char[][] board = null;
            hasSolution = false;
            long startTime = 0, endTime = 0;
            if(Header.S.equals("DEFAULT")){
                DefaultSolver solver = new DefaultSolver();
                startTime = System.currentTimeMillis();
                solver.readBlocks(blockLines);
                endTime = System.currentTimeMillis();
                if (DefaultSolver.foundSolution) {
                    board = DefaultSolver.board;
                    hasSolution = true;
                }
            } else if(Header.S.equals("CUSTOM")){
                CustomSolver solver = new CustomSolver();
                startTime = System.currentTimeMillis();
                solver.readBlocks(blockLines);
                endTime = System.currentTimeMillis();
                if (CustomSolver.foundSolution) {
                    board = CustomSolver.board;
                    hasSolution = true;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Mode tidak dikenali: " + Header.S + ". Solver tidak dijalankan.");
                saveTxt.setEnabled(false);
                saveImg.setEnabled(false);
                return;
            }
            if(hasSolution){
                displayBoard(board);
                saveTxt.setEnabled(true);
                saveImg.setEnabled(true);
                if(Header.S.equals("DEFAULT")){
                    timeLabel.setText("Waktu Eksekusi: " + (endTime - startTime) + " ms");
                    caseLabel.setText("Jumlah Kasus: " + DefaultSolver.count);
                } else {
                    timeLabel.setText("Waktu Eksekusi: " + (endTime - startTime) + " ms");
                    caseLabel.setText("Jumlah Kasus: " + CustomSolver.count);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ditemukan solusi.");
                saveTxt.setEnabled(false);
                saveImg.setEnabled(false);
                timeLabel.setText("Waktu Eksekusi: 0 ms");
                caseLabel.setText("Jumlah Kasus: 0");
            }
        } catch(IOException e){
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat membaca file.");
        }
    }
    private void displayBoard(char[][] board){
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(Header.N, Header.M));
        for(char[] row : board){
            for(char c : row){
                JPanel cell = new JPanel();
                cell.setBackground(Header.getColor(c));
                boardPanel.add(cell);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }
    private void saveAsTxt(){
        if(!hasSolution) return;
        String filename = JOptionPane.showInputDialog(this, "Masukkan nama file txt:");
        if(filename != null && !filename.trim().isEmpty()){
            filename = filename.trim() + ".txt";
            try {
                char[][] board = getCurrentBoard();
                if (board != null) {
                    Header.saveAsFile(board, filename);
                    JOptionPane.showMessageDialog(this, "Berhasil disimpan sebagai " + filename);
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Gagal menyimpan file: " + ex.getMessage());
            }
        }
    }
    private void saveAsImage(){
        if(!hasSolution) return;
        String filename = JOptionPane.showInputDialog(this, "Masukkan nama file gambar:");
        if(filename != null && !filename.trim().isEmpty()){
            filename = filename.trim() + ".png";
            try {
                char[][] board = getCurrentBoard();
                if (board != null) {
                    Header.saveAsImage(board, filename);
                    JOptionPane.showMessageDialog(this, "Berhasil disimpan sebagai " + filename);
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Gagal menyimpan gambar: " + ex.getMessage());
            }
        }
    }
    private char[][] getCurrentBoard(){
        if(Header.S.equals("DEFAULT")) return DefaultSolver.board;
        else if(Header.S.equals("CUSTOM")) return CustomSolver.board;
        else return null;
    }
}