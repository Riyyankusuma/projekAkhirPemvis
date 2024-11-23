/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package projectakhir1;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author ASUS
 */
public class projek_akhir extends javax.swing.JFrame {
    Connection conn;
    private DefaultTableModel modeldata;
    private DefaultTableModel modelspp;
    private DefaultTableModel modelnilai;

    /**
     * Creates new form projek_akhir
     */
    public projek_akhir() {
        initComponents(); 
        conn = koneksi_1.getConnection();
        initTableModels();
        loadDataSiswa();
        loadDataSpp();
        loadDataNilai();
        clearSiswaFields();
        clearSppFields();
        clearNilaiFields();  
         txtspp.setText("500000");
         txtkembalian.setText("");
          
        txtuang.getDocument().addDocumentListener(new DocumentListener() {
    public void changedUpdate(DocumentEvent e) {
        hitungKembalian();
    }
    public void removeUpdate(DocumentEvent e) {
        hitungKembalian();
    }
    public void insertUpdate(DocumentEvent e) {
        hitungKembalian(); 
    }
});
     
    } 
     private void initTableModels() {
        modeldata = new DefaultTableModel(new String[] { "NISN", "NAMA", "GENDER", "ttl", "ALAMAT", "NO HP" }, 0);
        tabledata.setModel(modeldata);

        modelspp = new DefaultTableModel(new String[]{"NISN", "Nama", "Tanggal Bayar", "Tagihan", "Uang Bayar", "KEMBALIAN"}, 0);
        tablespp.setModel(modelspp);

        modelnilai = new DefaultTableModel(new String[] { "NAMA", "ABSEN", "KELAS",  "NILAI AKHIR" }, 0);
        tablenilai.setModel(modelnilai);
    } 
     
        private void loadDataSiswa() {
        modeldata.setRowCount(0);  
        try {
            String sql = "SELECT * FROM datasiswa";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modeldata.addRow(new Object[]{
                    rs.getInt("nisn"),
                    rs.getString("nama"),
                    rs.getString("gender"),
                    rs.getString("ttl"),  
                    rs.getString("alamat"),
                    rs.getString("nohp")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error Load datasiswa: " + e.getMessage());
        }
    }

        private void loadDataSpp() {
        modelspp.setRowCount(0); 
        try {
        String sql = "SELECT * FROM sppsiswa";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString("nisn") + ", " + rs.getString("nama"));
            modelspp.addRow(new Object[]{
                rs.getString("nisn"),
                rs.getString("nama"),
                rs.getString("tanggalbyr"),
                rs.getString("tagihan"),
                rs.getString("uangbayar"),
                rs.getString("kembalian")
            });
        }
    } catch (SQLException e) {
        System.out.println("Error Load datasiswa: " + e.getMessage());
    }
    }

        private void loadDataNilai() {
        modelnilai.setRowCount(0); 
        try {
            String sql = "SELECT * FROM nilaisiswa";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelnilai.addRow(new Object[]{
                    rs.getString("nama_lengkap"),
                    rs.getInt("absen"),
                    rs.getString("kelas"),
                    rs.getString("nilai_akhir")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error Load nilai siswa: " + e.getMessage());
        }
    }

    private void clearSiswaFields() {
    
    txt1.setText(""); 
    txt2.setText(""); 
    txt5.setText(""); 
    txt6.setText(""); 
    txt7.setText(""); 
    txttempatlahir.setText("");
    dttglsiswa.setDate(null);

 
    ButtonGroup genderGroup = new ButtonGroup();
    genderGroup.add(cblaki);
    genderGroup.add(cbperempuan);

    genderGroup.clearSelection();

    txt1.requestFocus();
}




    private void tambahDataSiswa() {
    String nisn = txt1.getText().trim();
    String nama = txt2.getText().trim();
    String gender = "";
    String alamat = txt5.getText().trim();
    String nohp = txt6.getText().trim();
    String tempatLahir = txttempatlahir.getText().trim();  // Menambahkan input tempat lahir
    Date tanggalLahir = dttglsiswa.getDate();

    if (cblaki.isSelected()) {
        gender = "Laki-Laki";
    } else if (cbperempuan.isSelected()) {
        gender = "Perempuan";
    } else {
        JOptionPane.showMessageDialog(this, "Pilih gender (Laki-Laki atau Perempuan).", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (tanggalLahir == null || nisn.isEmpty() || nama.isEmpty() || gender.isEmpty() || alamat.isEmpty() || nohp.isEmpty() || tempatLahir.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Semua data harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String ttl = tempatLahir + ", " + sdf.format(tanggalLahir);  // Gabungkan tempat lahir dan tanggal lahir

    try {
        if (!nisn.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "NISN hanya boleh berisi angka.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!nohp.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Nomor HP hanya boleh berisi angka.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!nama.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, "Nama hanya boleh berisi huruf.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!tempatLahir.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, "Tempat Lahir hanya boleh berisi huruf.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cekSql = "SELECT COUNT(*) FROM datasiswa WHERE nisn = ?";
        PreparedStatement cekPs = conn.prepareStatement(cekSql);
        cekPs.setString(1, nisn);
        ResultSet rs = cekPs.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            JOptionPane.showMessageDialog(this, "NISN sudah ada. Data tidak boleh duplikat.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO datasiswa (nisn, nama, gender, ttl, alamat, nohp) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nisn);
        ps.setString(2, nama);
        ps.setString(3, gender);
        ps.setString(4, ttl);  
        ps.setString(5, alamat);
        ps.setString(6, nohp);

        int affectedRows = ps.executeUpdate();
        if (affectedRows > 0) {
            JOptionPane.showMessageDialog(this, "Data siswa berhasil ditambahkan.");
            loadDataSiswa();  
            clearSiswaFields(); 
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Kesalahan saat menambah data siswa: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
}

    private void searchDataSiswa() {
    String nisn = txt7.getText().trim();
    String tempatLahir = txttempatlahir.getText().trim(); 
    Date tanggalLahir = dttglsiswa.getDate(); 

    if (nisn.isEmpty() && tempatLahir.isEmpty() && tanggalLahir == null) {
        JOptionPane.showMessageDialog(this, "Masukkan NISN, Tempat Lahir atau Tanggal Lahir untuk mencari data.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        String sql = "SELECT * FROM datasiswa WHERE 1=1";

        if (!nisn.isEmpty()) {
            sql += " AND nisn = ?";
        }
        
        if (!tempatLahir.isEmpty()) {
            sql += " AND ttl LIKE ?";
        }

        if (tanggalLahir != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ttlTanggal = sdf.format(tanggalLahir); 
            sql += " AND ttl LIKE ?";
        }

        PreparedStatement ps = conn.prepareStatement(sql);

        int paramIndex = 1;
        if (!nisn.isEmpty()) {
            ps.setString(paramIndex++, nisn);
        }
        
        if (!tempatLahir.isEmpty()) {
            ps.setString(paramIndex++, "%" + tempatLahir + "%");  
        }

        if (tanggalLahir != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ttlTanggal = sdf.format(tanggalLahir);  
            ps.setString(paramIndex++, "%" + ttlTanggal + "%");  
        }

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            txt1.setText(rs.getString("nisn"));
            txt2.setText(rs.getString("nama"));
            txt5.setText(rs.getString("alamat"));
            txt6.setText(rs.getString("nohp"));
            
            String ttl = rs.getString("ttl");
            String[] ttlParts = ttl.split(", ");
            if (ttlParts.length == 2) {
                txttempatlahir.setText(ttlParts[0]); 
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    dttglsiswa.setDate(sdf.parse(ttlParts[1])); 
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            String gender = rs.getString("gender");
            ButtonGroup genderGroup = new ButtonGroup();
            genderGroup.add(cblaki);
            genderGroup.add(cbperempuan);
            if ("Laki-Laki".equals(gender)) {
                cblaki.setSelected(true);
            } else if ("Perempuan".equals(gender)) {
                cbperempuan.setSelected(true);
            }
        } else {
            clearSiswaFields();
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Kesalahan saat mencari data siswa: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
}


    private void updateDataSiswa() {
    String nisn = txt1.getText().trim();
    String nama = txt2.getText().trim();
    String alamat = txt5.getText().trim();
    String nohp = txt6.getText().trim();
    String tempatLahir = txttempatlahir.getText().trim();  
    Date tanggalLahir = dttglsiswa.getDate();

    String gender = "";
    if (cblaki.isSelected()) {
        gender = "Laki-Laki";
    } else if (cbperempuan.isSelected()) {
        gender = "Perempuan";
    } else {
        JOptionPane.showMessageDialog(this, "Pilih Data!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (tanggalLahir == null || nisn.isEmpty() || nama.isEmpty() || alamat.isEmpty() || nohp.isEmpty() || tempatLahir.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Semua data harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String ttl = tempatLahir + ", " + sdf.format(tanggalLahir);  

    try {
        String sql = "UPDATE datasiswa SET nama = ?, gender = ?, ttl = ?, alamat = ?, nohp = ? WHERE nisn = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nama);
        ps.setString(2, gender);
        ps.setString(3, ttl);  
        ps.setString(4, alamat);
        ps.setString(5, nohp);
        ps.setString(6, nisn);

        int affectedRows = ps.executeUpdate();
        if (affectedRows > 0) {
            JOptionPane.showMessageDialog(this, "Data siswa berhasil diperbarui.");
            loadDataSiswa();
            clearSiswaFields();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data siswa.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Kesalahan saat memperbarui data siswa: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
}



    private void deleteDataSiswa() {
    String nisn = txt1.getText().trim();
    if (nisn.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Masukkan NISN untuk menghapus data.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            String sql = "DELETE FROM datasiswa WHERE nisn = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nisn);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Data siswa berhasil dihapus.");
                loadDataSiswa();
                clearSiswaFields();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data siswa.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Kesalahan saat menghapus data siswa: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }
}

            private void clearSppFields() {
                txtnisn.setText("");
                txtnama.setText("");
                txtspp.setText("");
                txtuang.setText("");
                txtkembalian.setText("");
                txtcari.setText("");
                dttgl.setDate(null);
           }  
        
            private void hitungKembalian() {
    try {
        String uangText = txtuang.getText().trim();
        
        if (uangText.isEmpty()) {
            txtkembalian.setText("");  
            return;
        }

        if (!uangText.matches("\\d+")) {
            txtkembalian.setText("Data tidak valid!");  
            return;
        }

        int bayar = Integer.parseInt(uangText);
        int tagihan = 500000;  
        int kembalian = bayar - tagihan;

        if (kembalian < 0) {
            txtkembalian.setText("Uang kurang!"); 
        } else if (kembalian == 0) {
            txtkembalian.setText("Uang pas");  
        } else {
            txtkembalian.setText(String.valueOf(kembalian)); 
        }
    } catch (NumberFormatException e) {
        txtkembalian.setText("Data tidak valid!"); 
    }
}


        private void tambahSppSiswa() {
    String nisn = txtnisn.getText().trim();
    String nama = txtnama.getText().trim();
    String uangbayar = txtuang.getText().trim();
    String kembalian = txtkembalian.getText().trim();
    Date tanggalBayar = dttgl.getDate();

    if (tanggalBayar == null || nisn.isEmpty() || nama.isEmpty() || 
        uangbayar.isEmpty() || kembalian.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Semua data harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String tanggalbyr = sdf.format(tanggalBayar);

    try {
        if (!nisn.matches("\\d+")) { 
            JOptionPane.showMessageDialog(this, "NISN hanya boleh berisi angka.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!uangbayar.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "UANG BAYAR hanya boleh berisi angka.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String cekSql = "SELECT COUNT(*) FROM sppsiswa WHERE nisn = ?";
        PreparedStatement cekPs = conn.prepareStatement(cekSql);
        cekPs.setString(1, nisn);
        ResultSet rs = cekPs.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            JOptionPane.showMessageDialog(this, "NISN sudah ada. Data tidak boleh duplikat.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int tagihan = 500000;

        String sql = "INSERT INTO sppsiswa (nisn, nama, tanggalbyr, tagihan, uangbayar, kembalian) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nisn);
        ps.setString(2, nama);
        ps.setString(3, tanggalbyr);
        ps.setInt(4, tagihan);
        ps.setString(5, uangbayar);
        ps.setString(6, kembalian);

        int affectedRows = ps.executeUpdate();
        if (affectedRows > 0) {
            JOptionPane.showMessageDialog(this, "Data SPP siswa berhasil ditambahkan.");
            loadDataSpp();  
            clearSppFields();  
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Kesalahan saat menambah data SPP siswa: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
}

        private void searchSppSiswa() {
    String nisn = txtcari.getText().trim(); 
    if (nisn.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Masukkan NISN untuk mencari data.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        String sql = "SELECT * FROM sppsiswa WHERE nisn = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nisn); 
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            txtnisn.setText(rs.getString("nisn"));
            txtnama.setText(rs.getString("nama"));
            Date tanggalBayar = rs.getDate("tanggalbyr");
            if (tanggalBayar != null) {
                dttgl.setDate(tanggalBayar);
            } else {
                dttgl.setDate(null);
            }
            txtspp.setText(rs.getString("tagihan")); 
            txtuang.setText(rs.getString("uangbayar")); 
            txtkembalian.setText(rs.getString("kembalian")); // Tambahkan ini
        } else {
            clearSppFields(); 
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Kesalahan saat mencari data spp siswa: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
}


    private void updateSppSiswa() {
    String nisn = txtnisn.getText().trim();
    String nama = txtnama.getText().trim();
    String spp = txtspp.getText().trim();
    String uangbayar = txtuang.getText().trim();
    String kembalian = txtkembalian.getText().trim(); 
    Date tanggalbayar = dttgl.getDate();
    
    if (tanggalbayar == null || nisn.isEmpty() || nama.isEmpty() || 
        spp.isEmpty() || uangbayar.isEmpty() || kembalian.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Semua data harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String tanggalbyr = sdf.format(tanggalbayar);

    try {
        String sql = "UPDATE sppsiswa SET nama = ?, tanggalbyr = ?, tagihan = ?, uangbayar = ?, kembalian = ? WHERE nisn = ?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, nama);
        ps.setString(2, tanggalbyr);
        ps.setString(3, spp);         
        ps.setString(4, uangbayar);  
        ps.setString(5, kembalian);  
        ps.setString(6, nisn);        

        int affectedRows = ps.executeUpdate();

        if (affectedRows > 0) {
            JOptionPane.showMessageDialog(this, "Data SPP siswa berhasil diperbarui.");
            loadDataSpp();  
            clearSppFields();  
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data siswa. NISN tidak ditemukan.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Kesalahan saat memperbarui data SPP siswa: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
}
    private void deleteSppSiswa() {
        String nisn = txtnisn.getText().trim(); 
        if (nisn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan NISN untuk menghapus data.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data dengan NISN: " + nisn + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM sppsiswa WHERE nisn = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nisn);

                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Data siswa dengan NISN " + nisn + " berhasil dihapus.");
                    loadDataSpp(); 
                    clearSppFields(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus data spp siswa. Pastikan NISN benar.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Kesalahan saat menghapus data spp siswa: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        }
    } 

    private void clearNilaiFields() {
        txtnama2.setText("");
        txtabsen.setText("");
        txtkelas.setText("");
        txtnilaiakhir.setText("");
    } 

    private void tambahDataNilai() {
        String nama_lengkap = txtnama2.getText().trim();
        String absen = txtabsen.getText().trim();
        String kelas = txtkelas.getText().trim();
        String nilai_akhir = txtnilaiakhir.getText().trim();

        if ( nama_lengkap.isEmpty() || absen.isEmpty() || kelas.isEmpty() || nilai_akhir.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua data harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try { 
            if (!nama_lengkap.matches("[a-zA-Z\\s]+")) {
                JOptionPane.showMessageDialog(this, "NAMA hanya boleh berisi huruf.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!nilai_akhir.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "NILAI hanya boleh berisi angka.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                return;
            }
             if (!absen.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "ABSEN hanya boleh berisi angka.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String cekSql = "SELECT COUNT(*) FROM nilaisiswa WHERE nama_lengkap = ?";
            PreparedStatement cekPs = conn.prepareStatement(cekSql);
            cekPs.setString(1, nama_lengkap);
            ResultSet rs = cekPs.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "NISN sudah ada. Data tidak boleh duplikat.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "INSERT INTO nilaisiswa (nama_lengkap, absen, kelas, nilai_akhir) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nama_lengkap);
            ps.setString(2, absen);
            ps.setString(3, kelas);  
            ps.setString(4, nilai_akhir);


            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Data siswa berhasil ditambahkan.");
                loadDataNilai();  
                clearNilaiFields();  
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Kesalahan saat menambah data siswa: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchDataNilai() {
        String nama = txtcari2.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan nama untuk mencari data.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String sql = "SELECT * FROM nilaisiswa WHERE nama_lengkap = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nama);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtnama2.setText(rs.getString("nama_lengkap"));
                txtabsen.setText(String.valueOf(rs.getInt("absen")));
                txtkelas.setText(rs.getString("kelas"));
                txtnilaiakhir.setText(rs.getString("nilai_akhir"));
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Kesalahan saat mencari data nilai siswa: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    } 
    private void updateDataNilai() {
        String nama = txtnama.getText().trim();
        String absen = txtabsen.getText().trim();
        String kelas = txtkelas.getText().trim();
        String nilaiAkhir = txtnilaiakhir.getText().trim();

        if (nama.isEmpty() || absen.isEmpty() || kelas.isEmpty() || nilaiAkhir.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua data harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String sql = "UPDATE nilaisiswa SET absen = ?, kelas = ?, nilai_akhir = ? WHERE nama_lengkap = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(absen));
            ps.setString(2, kelas);
            ps.setString(3, nilaiAkhir);
            ps.setString(4, nama);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Data nilai siswa berhasil diperbarui.");
                loadDataNilai();
                clearNilaiFields();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui data nilai siswa.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Kesalahan saat memperbarui data nilai siswa: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    } 
    private void deleteDataNilai() {
        String nama = txtcari2.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan nama untuk menghapus data.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data nilai ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM nilaisiswa WHERE nama_lengkap = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nama);

                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Data nilai siswa berhasil dihapus.");
                    loadDataNilai();
                    clearNilaiFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus data nilai siswa.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Kesalahan saat menghapus data nilai siswa: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel1 = new java.awt.Panel();
        jPanel1 = new javax.swing.JPanel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabledata = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        btncari = new javax.swing.JButton();
        txt1 = new javax.swing.JTextField();
        txttempatlahir = new javax.swing.JTextField();
        cbperempuan = new javax.swing.JCheckBox();
        jLabel25 = new javax.swing.JLabel();
        cblaki = new javax.swing.JCheckBox();
        txt2 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txt6 = new javax.swing.JTextField();
        txt5 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txt7 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnpdf = new javax.swing.JButton();
        btnout = new javax.swing.JButton();
        dttglsiswa = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnsimpan = new javax.swing.JButton();
        btnedit = new javax.swing.JButton();
        btnhapus = new javax.swing.JButton();
        btnbatal = new javax.swing.JButton();
        txt8 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablespp = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtnisn = new javax.swing.JTextField();
        txtnama = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtspp = new javax.swing.JTextField();
        txtuang = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        dttgl = new com.toedter.calendar.JDateChooser();
        jPanel10 = new javax.swing.JPanel();
        btnpdf1 = new javax.swing.JButton();
        btnout1 = new javax.swing.JButton();
        txtkembalian = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btncari1 = new javax.swing.JButton();
        txtcari = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        btnedit1 = new javax.swing.JButton();
        btnsimpan1 = new javax.swing.JButton();
        btnhapus1 = new javax.swing.JButton();
        btnbatal1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtnama2 = new javax.swing.JTextField();
        txtabsen = new javax.swing.JTextField();
        txtnilaiakhir = new javax.swing.JTextField();
        cari2 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        txtcari2 = new javax.swing.JTextField();
        txtkelas = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablenilai = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        btnsimpan2 = new javax.swing.JButton();
        btnedit2 = new javax.swing.JButton();
        btnhapus2 = new javax.swing.JButton();
        btnbatal2 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        btnpdf2 = new javax.swing.JButton();
        btnout2 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setLayout(null);

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projectakhir1/abs.jpg"))); // NOI18N
        jLabel20.setText("jLabel20");
        jPanel2.add(jLabel20);
        jLabel20.setBounds(33, 24, 75, 75);

        jLabel21.setFont(new java.awt.Font("Segoe UI Black", 1, 45)); // NOI18N
        jLabel21.setText("PENGELOLA DATA SISWA");
        jPanel2.add(jLabel21);
        jLabel21.setBounds(158, 24, 593, 62);

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 830, 120));

        jTabbedPane1.setBackground(new java.awt.Color(153, 153, 153));

        jPanel4.setBackground(new java.awt.Color(0, 102, 204));
        jPanel4.setLayout(null);

        tabledata.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tabledata);

        jPanel4.add(jScrollPane3);
        jScrollPane3.setBounds(170, 170, 650, 230);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("NISN :");
        jPanel4.add(jLabel13);
        jLabel13.setBounds(220, 10, 40, 30);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("NAMA :");
        jPanel4.add(jLabel14);
        jLabel14.setBounds(210, 50, 50, 30);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("GENDER :");
        jPanel4.add(jLabel15);
        jLabel15.setBounds(200, 80, 60, 40);

        btncari.setText("cari");
        btncari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncariActionPerformed(evt);
            }
        });
        jPanel4.add(btncari);
        btncari.setBounds(400, 130, 72, 30);
        jPanel4.add(txt1);
        txt1.setBounds(270, 10, 197, 30);

        txttempatlahir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttempatlahirActionPerformed(evt);
            }
        });
        jPanel4.add(txttempatlahir);
        txttempatlahir.setBounds(590, 50, 197, 30);

        buttonGroup1.add(cbperempuan);
        cbperempuan.setText("Perempuan");
        jPanel4.add(cbperempuan);
        cbperempuan.setBounds(370, 90, 85, 20);

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("TEMPAT LAHIR :");
        jPanel4.add(jLabel25);
        jLabel25.setBounds(480, 50, 100, 30);

        buttonGroup1.add(cblaki);
        cblaki.setText("Laki-Laki");
        jPanel4.add(cblaki);
        cblaki.setBounds(280, 90, 70, 20);
        jPanel4.add(txt2);
        txt2.setBounds(270, 50, 197, 30);

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("TANGGAL LAHIR :");
        jPanel4.add(jLabel16);
        jLabel16.setBounds(470, 10, 110, 30);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("ALAMAT :");
        jPanel4.add(jLabel17);
        jLabel17.setBounds(510, 90, 70, 30);

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("NO HP :");
        jPanel4.add(jLabel18);
        jLabel18.setBounds(530, 130, 50, 30);
        jPanel4.add(txt6);
        txt6.setBounds(590, 130, 197, 30);

        txt5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt5ActionPerformed(evt);
            }
        });
        jPanel4.add(txt5);
        txt5.setBounds(590, 90, 197, 30);

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("CARI");
        jPanel4.add(jLabel19);
        jLabel19.setBounds(220, 130, 30, 30);
        jLabel19.getAccessibleContext().setAccessibleName("CARI :");

        jPanel4.add(txt7);
        txt7.setBounds(260, 130, 129, 30);

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        btnpdf.setBackground(new java.awt.Color(102, 255, 51));
        btnpdf.setText("TO PDF");
        btnpdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpdfActionPerformed(evt);
            }
        });
        jPanel3.add(btnpdf);

        btnout.setBackground(new java.awt.Color(255, 51, 255));
        btnout.setText("OUT");
        btnout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnoutActionPerformed(evt);
            }
        });
        jPanel3.add(btnout);

        jPanel4.add(jPanel3);
        jPanel3.setBounds(6, 294, 150, 80);
        jPanel4.add(dttglsiswa);
        dttglsiswa.setBounds(590, 10, 197, 30);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projectakhir1/askklh.jpg"))); // NOI18N
        jLabel9.setText("jLabel9");
        jPanel4.add(jLabel9);
        jLabel9.setBounds(160, 0, 670, 450);

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnsimpan.setText("simpan");
        btnsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpanActionPerformed(evt);
            }
        });
        jPanel7.add(btnsimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 30, 102, 40));

        btnedit.setText("edit");
        btnedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditActionPerformed(evt);
            }
        });
        jPanel7.add(btnedit, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 102, 40));

        btnhapus.setText("hapus");
        btnhapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhapusActionPerformed(evt);
            }
        });
        jPanel7.add(btnhapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 101, 40));

        btnbatal.setText("batal");
        btnbatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbatalActionPerformed(evt);
            }
        });
        jPanel7.add(btnbatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 102, 40));

        jPanel4.add(jPanel7);
        jPanel7.setBounds(0, 0, 160, 450);

        txt8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt8ActionPerformed(evt);
            }
        });
        jPanel4.add(txt8);
        txt8.setBounds(590, 90, 197, 30);

        jTabbedPane1.addTab("data siswa", jPanel4);

        jPanel5.setBackground(new java.awt.Color(0, 102, 255));
        jPanel5.setLayout(null);

        tablespp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tablespp);

        jPanel5.add(jScrollPane4);
        jScrollPane4.setBounds(170, 10, 650, 240);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("NISN : ");
        jPanel5.add(jLabel1);
        jLabel1.setBounds(210, 260, 40, 30);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("TANGGAL byr :");
        jPanel5.add(jLabel2);
        jLabel2.setBounds(160, 340, 90, 30);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("NAMA :");
        jPanel5.add(jLabel3);
        jLabel3.setBounds(200, 300, 50, 30);

        txtnisn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnisnActionPerformed(evt);
            }
        });
        jPanel5.add(txtnisn);
        txtnisn.setBounds(250, 260, 200, 30);

        txtnama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnamaActionPerformed(evt);
            }
        });
        jPanel5.add(txtnama);
        txtnama.setBounds(250, 300, 200, 30);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("TAGIHAN SPP : ");
        jPanel5.add(jLabel4);
        jLabel4.setBounds(480, 260, 90, 30);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("UANG BAYAR : ");
        jPanel5.add(jLabel5);
        jLabel5.setBounds(480, 300, 100, 30);

        txtspp.setEditable(false);
        jPanel5.add(txtspp);
        txtspp.setBounds(580, 260, 190, 30);

        txtuang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtuangActionPerformed(evt);
            }
        });
        jPanel5.add(txtuang);
        txtuang.setBounds(580, 300, 190, 30);

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("CARI :");
        jPanel5.add(jLabel24);
        jLabel24.setBounds(360, 400, 40, 30);
        jPanel5.add(dttgl);
        dttgl.setBounds(250, 340, 200, 30);

        jPanel10.setLayout(new java.awt.GridLayout(1, 0));

        btnpdf1.setBackground(new java.awt.Color(102, 255, 51));
        btnpdf1.setText("TO PDF");
        btnpdf1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpdf1ActionPerformed(evt);
            }
        });
        jPanel10.add(btnpdf1);

        btnout1.setBackground(new java.awt.Color(255, 51, 255));
        btnout1.setText("OUT");
        btnout1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnout1ActionPerformed(evt);
            }
        });
        jPanel10.add(btnout1);

        jPanel5.add(jPanel10);
        jPanel10.setBounds(6, 324, 150, 80);

        txtkembalian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtkembalianActionPerformed(evt);
            }
        });
        jPanel5.add(txtkembalian);
        txtkembalian.setBounds(580, 340, 190, 30);

        jLabel7.setFont(new java.awt.Font("Segoe UI Emoji", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("KEMBALIAN :");
        jPanel5.add(jLabel7);
        jLabel7.setBounds(490, 350, 80, 20);

        btncari1.setText("cari");
        btncari1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncari1ActionPerformed(evt);
            }
        });
        jPanel5.add(btncari1);
        btncari1.setBounds(550, 400, 80, 30);
        jPanel5.add(txtcari);
        txtcari.setBounds(410, 400, 126, 30);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projectakhir1/askklh.jpg"))); // NOI18N
        jLabel10.setText("jLabel10");
        jPanel5.add(jLabel10);
        jLabel10.setBounds(160, 0, 670, 450);

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnedit1.setText("edit");
        btnedit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnedit1ActionPerformed(evt);
            }
        });
        jPanel8.add(btnedit1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 102, 40));

        btnsimpan1.setText("simpan");
        btnsimpan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpan1ActionPerformed(evt);
            }
        });
        jPanel8.add(btnsimpan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 102, 40));

        btnhapus1.setText("hapus");
        btnhapus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhapus1ActionPerformed(evt);
            }
        });
        jPanel8.add(btnhapus1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 101, 40));

        btnbatal1.setText("batal");
        btnbatal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbatal1ActionPerformed(evt);
            }
        });
        jPanel8.add(btnbatal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 102, 40));

        jPanel5.add(jPanel8);
        jPanel8.setBounds(0, 0, 160, 450);

        jTabbedPane1.addTab("spp siswa", jPanel5);

        jPanel6.setLayout(null);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("NAMA LENGKAP  :");
        jPanel6.add(jLabel8);
        jLabel8.setBounds(170, 10, 110, 30);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("ABSEN  :");
        jPanel6.add(jLabel11);
        jLabel11.setBounds(230, 50, 50, 30);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("KELAS :");
        jPanel6.add(jLabel12);
        jLabel12.setBounds(450, 10, 43, 30);
        jPanel6.add(txtnama2);
        txtnama2.setBounds(280, 10, 126, 30);
        jPanel6.add(txtabsen);
        txtabsen.setBounds(280, 50, 126, 30);
        jPanel6.add(txtnilaiakhir);
        txtnilaiakhir.setBounds(500, 50, 126, 30);

        cari2.setText("cari");
        cari2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cari2ActionPerformed(evt);
            }
        });
        jPanel6.add(cari2);
        cari2.setBounds(670, 50, 80, 23);

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("NILAI AKHIR : ");
        jPanel6.add(jLabel22);
        jLabel22.setBounds(410, 50, 90, 30);
        jPanel6.add(txtcari2);
        txtcari2.setBounds(670, 20, 80, 22);
        jPanel6.add(txtkelas);
        txtkelas.setBounds(500, 10, 126, 30);

        tablenilai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablenilai);

        jPanel6.add(jScrollPane1);
        jScrollPane1.setBounds(190, 90, 590, 320);

        jPanel9.setBackground(new java.awt.Color(204, 204, 204));
        jPanel9.setLayout(null);

        btnsimpan2.setText("simpan");
        btnsimpan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpan2ActionPerformed(evt);
            }
        });
        jPanel9.add(btnsimpan2);
        btnsimpan2.setBounds(30, 30, 102, 40);

        btnedit2.setText("edit");
        btnedit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnedit2ActionPerformed(evt);
            }
        });
        jPanel9.add(btnedit2);
        btnedit2.setBounds(30, 90, 102, 40);

        btnhapus2.setText("hapus");
        btnhapus2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhapus2ActionPerformed(evt);
            }
        });
        jPanel9.add(btnhapus2);
        btnhapus2.setBounds(30, 150, 102, 40);

        btnbatal2.setText("batal");
        btnbatal2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbatal2ActionPerformed(evt);
            }
        });
        jPanel9.add(btnbatal2);
        btnbatal2.setBounds(30, 210, 102, 40);

        jPanel12.setLayout(new java.awt.GridLayout(1, 0));

        btnpdf2.setBackground(new java.awt.Color(102, 255, 51));
        btnpdf2.setText("TO PDF");
        btnpdf2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpdf2ActionPerformed(evt);
            }
        });
        jPanel12.add(btnpdf2);

        btnout2.setBackground(new java.awt.Color(255, 51, 255));
        btnout2.setText("OUT");
        btnout2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnout2ActionPerformed(evt);
            }
        });
        jPanel12.add(btnout2);

        jPanel9.add(jPanel12);
        jPanel12.setBounds(6, 294, 150, 80);

        jPanel6.add(jPanel9);
        jPanel9.setBounds(0, 0, 160, 450);

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projectakhir1/askklh.jpg"))); // NOI18N
        jLabel23.setText("jLabel23");
        jPanel6.add(jLabel23);
        jLabel23.setBounds(153, 0, 680, 450);

        jPanel13.setLayout(new java.awt.GridLayout(1, 0));

        jButton21.setBackground(new java.awt.Color(102, 255, 51));
        jButton21.setText("TO PDF");
        jPanel13.add(jButton21);

        jButton22.setBackground(new java.awt.Color(255, 51, 255));
        jButton22.setText("LOG OUT");
        jPanel13.add(jButton22);

        jPanel6.add(jPanel13);
        jPanel13.setBounds(6, 294, 150, 80);

        jTabbedPane1.addTab("nilai siswa", jPanel6);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 830, 480));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btneditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditActionPerformed
       updateDataSiswa();
    }//GEN-LAST:event_btneditActionPerformed

    private void btnhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhapusActionPerformed
        deleteDataSiswa();
    }//GEN-LAST:event_btnhapusActionPerformed

    private void btnbatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbatalActionPerformed
        
   

   
    
        clearSiswaFields();

        JOptionPane.showMessageDialog(
            this, 
            "Proses input/edit data telah dibatalkan.", 
            "Informasi", 
            JOptionPane.INFORMATION_MESSAGE
        );

        txt1.requestFocus(); 
    
 

    }//GEN-LAST:event_btnbatalActionPerformed

    private void btnsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpanActionPerformed
        tambahDataSiswa();
    }//GEN-LAST:event_btnsimpanActionPerformed

    private void txt5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt5ActionPerformed
        
    }//GEN-LAST:event_txt5ActionPerformed

    private void txtnamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnamaActionPerformed
       
    }//GEN-LAST:event_txtnamaActionPerformed

    private void txtuangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtuangActionPerformed
        txtuang.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            try {
                int tagihan = Integer.parseInt(txtspp.getText().trim());
                int uangBayar = Integer.parseInt(txtuang.getText().trim());
                int kembalian = uangBayar - tagihan;

                if (kembalian < 0) {
                    txtkembalian.setText("Kurang bayar: " + Math.abs(kembalian));
                } else {
                    txtkembalian.setText("Kembalian: " + kembalian);
                }
            } catch (NumberFormatException ex) {
                txtkembalian.setText("Input tidak valid");
            }
        }
    });
    }//GEN-LAST:event_txtuangActionPerformed

    private void txtnisnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnisnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnisnActionPerformed

    private void btnedit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnedit1ActionPerformed
        // TODO add your handling code here:
        updateSppSiswa();
    }//GEN-LAST:event_btnedit1ActionPerformed

    private void btnsimpan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpan1ActionPerformed
        // TODO add your handling code here:
        tambahSppSiswa();
    }//GEN-LAST:event_btnsimpan1ActionPerformed

    private void btnhapus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhapus1ActionPerformed
        // TODO add your handling code here:
        deleteSppSiswa();
    }//GEN-LAST:event_btnhapus1ActionPerformed

    private void btnbatal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbatal1ActionPerformed
         txtnisn.setText("");
         txtnama.setText("");
         txtspp.setText("");
         txtuang.setText("");
         txtcari.setText("");
         dttgl.setDate(null);
        JOptionPane.showMessageDialog(null, "Pilihan telah dibatalkan.", "Pembatalan", JOptionPane.INFORMATION_MESSAGE);
        
    }//GEN-LAST:event_btnbatal1ActionPerformed

    private void btnsimpan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpan2ActionPerformed
        // TODO add your handling code here:
        tambahDataNilai();
    }//GEN-LAST:event_btnsimpan2ActionPerformed

    private void btnedit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnedit2ActionPerformed
        // TODO add your handling code here:
        updateDataNilai();
    }//GEN-LAST:event_btnedit2ActionPerformed

    private void btnhapus2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhapus2ActionPerformed
        // TODO add your handling code here:
        deleteDataNilai();
    }//GEN-LAST:event_btnhapus2ActionPerformed

    private void btnbatal2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbatal2ActionPerformed
        // TODO add your handling code here:
        txtnama2.setText("");
        txtabsen.setText("");
        txtkelas.setText("");
        txtnilaiakhir.setText("");
        txtcari2.setText("");

        JOptionPane.showMessageDialog(null, "Pilihan telah dibatalkan.", "Pembatalan", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnbatal2ActionPerformed

    private void btnpdf2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpdf2ActionPerformed
        try {
        String filePath = "C:\\Users\\ASUS\\OneDrive\\문서\\datatable\\DataTabel.pdf";

        System.out.println("Saving PDF to: " + filePath);

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Data Tabel", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" "));

        PdfPTable pdfTable = new PdfPTable(tablenilai.getColumnCount());
        pdfTable.setWidthPercentage(100); 
        pdfTable.setSpacingBefore(10f);
        pdfTable.setSpacingAfter(10f);

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        PdfPCell headerCell;
        for (int i = 0; i < tablenilai.getColumnCount(); i++) {
            headerCell = new PdfPCell(new Phrase(tablenilai.getColumnName(i), headerFont));
            headerCell.setBackgroundColor(BaseColor.GRAY); 
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(8); 
            pdfTable.addCell(headerCell);
        }

        Font cellFont = new Font(Font.FontFamily.HELVETICA, 10);
        PdfPCell cell;
        for (int row = 0; row < tablenilai.getRowCount(); row++) {
            for (int col = 0; col < tablenilai.getColumnCount(); col++) {
                Object cellValue = tablenilai.getValueAt(row, col);
                cell = new PdfPCell(new Phrase(cellValue != null ? cellValue.toString() : "", cellFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
                cell.setPadding(5); 
                pdfTable.addCell(cell);
            }
        }

        document.add(pdfTable);
        document.close();

        JOptionPane.showMessageDialog(this, "PDF berhasil disimpan di: " + filePath);

        File pdfFile = new File(filePath);
        if (pdfFile.exists()) {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                JOptionPane.showMessageDialog(this, "Fitur Desktop tidak didukung pada sistem ini.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "File tidak ditemukan.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }

    }//GEN-LAST:event_btnpdf2ActionPerformed

    private void btnout2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnout2ActionPerformed
        int pilihan = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin keluar?", "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION);
    
        if (pilihan == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_btnout2ActionPerformed

    private void cari2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cari2ActionPerformed
        // TODO add your handling code here:
        searchDataNilai();
    }//GEN-LAST:event_cari2ActionPerformed

    private void btnpdf1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpdf1ActionPerformed
       try {
    String filePath = "C:\\Users\\ASUS\\OneDrive\\문서\\datatable\\DataTabel.pdf";

    System.out.println("Saving PDF to: " + filePath);

    Document document = new Document(PageSize.A4);
    PdfWriter.getInstance(document, new FileOutputStream(filePath));
    document.open();

    Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    Paragraph title = new Paragraph("Data Tabel", titleFont);
    title.setAlignment(Element.ALIGN_CENTER);
    document.add(title);

    document.add(new Paragraph(" "));

    PdfPTable pdfTable = new PdfPTable(tablespp.getColumnCount());
    pdfTable.setWidthPercentage(100); 
    pdfTable.setSpacingBefore(10f);
    pdfTable.setSpacingAfter(10f);

    Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    PdfPCell headerCell;
    for (int i = 0; i < tablespp.getColumnCount(); i++) {
        headerCell = new PdfPCell(new Phrase(tablespp.getColumnName(i), headerFont));
        headerCell.setBackgroundColor(BaseColor.GRAY); 
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER); 
        headerCell.setPadding(8); 
        pdfTable.addCell(headerCell);
    }

    Font cellFont = new Font(Font.FontFamily.HELVETICA, 10);
    PdfPCell cell;
    for (int row = 0; row < tablespp.getRowCount(); row++) {
        for (int col = 0; col < tablespp.getColumnCount(); col++) {
            Object cellValue = tablespp.getValueAt(row, col);
            cell = new PdfPCell(new Phrase(cellValue != null ? cellValue.toString() : "", cellFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
            cell.setPadding(5); 
            pdfTable.addCell(cell);
        }
    }

    document.add(pdfTable);
    document.close();

    JOptionPane.showMessageDialog(this, "PDF berhasil disimpan di: " + filePath);

    File pdfFile = new File(filePath);
    if (pdfFile.exists()) {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(pdfFile);
        } else {
            JOptionPane.showMessageDialog(this, "Fitur Desktop tidak didukung pada sistem ini.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "File tidak ditemukan.");
    }
} catch (Exception e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
}

    }//GEN-LAST:event_btnpdf1ActionPerformed

    private void btnout1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnout1ActionPerformed
         int pilihan = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin keluar?", "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION);
    
        if (pilihan == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_btnout1ActionPerformed

    private void btncariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncariActionPerformed
        // TODO add your handling code here:
        searchDataSiswa();
    }//GEN-LAST:event_btncariActionPerformed

    private void btnpdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpdfActionPerformed
       try {
    String filePath = "C:\\Users\\ASUS\\OneDrive\\문서\\datatable\\DataTabel.pdf";

    System.out.println("Saving PDF to: " + filePath);

    Document document = new Document(PageSize.A4, 36, 36, 36, 36); // Margin: 36pt
    PdfWriter.getInstance(document, new FileOutputStream(filePath));
    document.open();

    Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    Paragraph title = new Paragraph("Data Tabel", titleFont);
    title.setAlignment(Element.ALIGN_CENTER);
    title.setSpacingAfter(20);
    document.add(title);

    PdfPTable pdfTable = new PdfPTable(tabledata.getColumnCount());
    pdfTable.setWidthPercentage(100); 
    pdfTable.setSpacingBefore(10f);

    Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    for (int i = 0; i < tabledata.getColumnCount(); i++) {
        PdfPCell headerCell = new PdfPCell(new Phrase(tabledata.getColumnName(i), headerFont));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        pdfTable.addCell(headerCell);
    }

    Font cellFont = new Font(Font.FontFamily.TIMES_ROMAN, 12);
    for (int row = 0; row < tabledata.getRowCount(); row++) {
        for (int col = 0; col < tabledata.getColumnCount(); col++) {
            Object cellValue = tabledata.getValueAt(row, col);
            PdfPCell cell = new PdfPCell(new Phrase(cellValue != null ? cellValue.toString() : "", cellFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(cell);
        }
    }
    document.add(pdfTable);

    Font footerFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC);
    Paragraph footer = new Paragraph("Dokumen ini dicetak pada: " + new java.util.Date(), footerFont);
    footer.setAlignment(Element.ALIGN_RIGHT);
    footer.setSpacingBefore(20);
    document.add(footer);

    document.close();

    JOptionPane.showMessageDialog(this, "PDF berhasil disimpan di: " + filePath);
    File pdfFile = new File(filePath);
    if (pdfFile.exists()) {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(pdfFile);
        } else {
            JOptionPane.showMessageDialog(this, "Fitur Desktop tidak didukung pada sistem ini.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "File tidak ditemukan.");
    }
} catch (Exception e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
}

    }//GEN-LAST:event_btnpdfActionPerformed

    private void btnoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnoutActionPerformed
          int pilihan = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin keluar?", "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION);
    
        if (pilihan == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_btnoutActionPerformed

    private void btncari1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncari1ActionPerformed
        // TODO add your handling code here:
        searchSppSiswa();
    }//GEN-LAST:event_btncari1ActionPerformed

    private void txt8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt8ActionPerformed

    private void txttempatlahirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttempatlahirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttempatlahirActionPerformed

    private void txtkembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtkembalianActionPerformed
        // TODO add your handling code here:
         try {
        String tagihanStr = txtspp.getText().trim();
        String uangBayarStr = txtuang.getText().trim();

        if (!tagihanStr.isEmpty() && !uangBayarStr.isEmpty()) {
            double tagihan = Double.parseDouble(tagihanStr);
            double uangBayar = Double.parseDouble(uangBayarStr);

            if (uangBayar >= tagihan) {
                double kembalian = uangBayar - tagihan;
                JOptionPane.showMessageDialog(this, "Kembalian Anda: Rp" + kembalian, "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Uang bayar kurang dari tagihan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Masukkan angka yang valid untuk uang bayar!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_txtkembalianActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(projek_akhir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(projek_akhir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(projek_akhir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(projek_akhir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new projek_akhir().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnbatal;
    private javax.swing.JButton btnbatal1;
    private javax.swing.JButton btnbatal2;
    private javax.swing.JButton btncari;
    private javax.swing.JButton btncari1;
    private javax.swing.JButton btnedit;
    private javax.swing.JButton btnedit1;
    private javax.swing.JButton btnedit2;
    private javax.swing.JButton btnhapus;
    private javax.swing.JButton btnhapus1;
    private javax.swing.JButton btnhapus2;
    private javax.swing.JButton btnout;
    private javax.swing.JButton btnout1;
    private javax.swing.JButton btnout2;
    private javax.swing.JButton btnpdf;
    private javax.swing.JButton btnpdf1;
    private javax.swing.JButton btnpdf2;
    private javax.swing.JButton btnsimpan;
    private javax.swing.JButton btnsimpan1;
    private javax.swing.JButton btnsimpan2;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cari2;
    private javax.swing.JCheckBox cblaki;
    private javax.swing.JCheckBox cbperempuan;
    private com.toedter.calendar.JDateChooser dttgl;
    private com.toedter.calendar.JDateChooser dttglsiswa;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private java.awt.Panel panel1;
    private javax.swing.JTable tabledata;
    private javax.swing.JTable tablenilai;
    private javax.swing.JTable tablespp;
    private javax.swing.JTextField txt1;
    private javax.swing.JTextField txt2;
    private javax.swing.JTextField txt5;
    private javax.swing.JTextField txt6;
    private javax.swing.JTextField txt7;
    private javax.swing.JTextField txt8;
    private javax.swing.JTextField txtabsen;
    private javax.swing.JTextField txtcari;
    private javax.swing.JTextField txtcari2;
    private javax.swing.JTextField txtkelas;
    private javax.swing.JTextField txtkembalian;
    private javax.swing.JTextField txtnama;
    private javax.swing.JTextField txtnama2;
    private javax.swing.JTextField txtnilaiakhir;
    private javax.swing.JTextField txtnisn;
    private javax.swing.JTextField txtspp;
    private javax.swing.JTextField txttempatlahir;
    private javax.swing.JTextField txtuang;
    // End of variables declaration//GEN-END:variables
}
