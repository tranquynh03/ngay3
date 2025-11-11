/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Admin
 */
import database.DBConnection;
import model.NhanVien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
public class NhanVienDao {
    
    // --- 1. Phương thức Lấy TẤT CẢ Nhân viên (Status = 1) ---
    public List<NhanVien> getAllActiveNhanVien() {
        List<NhanVien> nhanVienList = new ArrayList<>();
        // Câu lệnh SQL (Chỉ lấy nhân viên đang hoạt động)
        String sql = "SELECT * FROM NHANVIEN WHERE Status = 1"; 
  
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Lấy kết nối từ DBConnection
            conn = DBConnection.getConnection(); 
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            // Duyệt qua từng hàng kết quả (ResultSet)
            while (rs.next()) {
                // Tạo một đối tượng NhanVien mới từ dữ liệu trong hàng
                NhanVien nv = new NhanVien();
                
                // Set dữ liệu từ các cột vào đối tượng NhanVien
                nv.setMaNV(rs.getString("MaNV"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                
                // Chuyển đổi java.sql.Date thành java.util.Date (Nếu cần)
                // Cẩn thận khi xử lý Date:
                java.sql.Date sqlDateOfBirth = rs.getDate("NgaySinh");
                if (sqlDateOfBirth != null) {
                    nv.setNgaySinh(new Date(sqlDateOfBirth.getTime()));
                }
                
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                
                java.sql.Date sqlDateStart = rs.getDate("NgayVaoLam");
                if (sqlDateStart != null) {
                    nv.setNgayVaoLam(new Date(sqlDateStart.getTime()));
                }
                
                nv.setMaPB(rs.getString("MaPB"));
                nv.setStatus(rs.getInt("Status")); // Status = 1

                // Thêm đối tượng vào danh sách
                nhanVienList.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Ghi log lỗi hoặc xử lý lỗi phù hợp
        } finally {
            // Đóng các tài nguyên
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return nhanVienList;
    }
    // Trong dao/NhanVienDao.java

public boolean addNhanVien(NhanVien nv) {
    // Không cần chèn cột Status vì nó có DEFAULT 1
    String sql = "INSERT INTO NHANVIEN (MaNV, HoTen, GioiTinh, NgaySinh, DiaChi, SoDienThoai, NgayVaoLam, MaPB) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    Connection conn = null;
    PreparedStatement ps = null;
    
    try {
        conn = DBConnection.getConnection();
        ps = conn.prepareStatement(sql);
        
        // Thiết lập các tham số cho câu lệnh SQL
        ps.setString(1, nv.getMaNV());
        ps.setString(2, nv.getHoTen());
        ps.setString(3, nv.getGioiTinh());
        
        // Chuyển java.util.Date sang java.sql.Date
        ps.setDate(4, new java.sql.Date(nv.getNgaySinh().getTime())); 
        
        ps.setString(5, nv.getDiaChi());
        ps.setString(6, nv.getSoDienThoai());
        
        // Chuyển java.util.Date sang java.sql.Date
        ps.setDate(7, new java.sql.Date(nv.getNgayVaoLam().getTime()));
        
        ps.setString(8, nv.getMaPB());
        
        // Thực thi câu lệnh (executeUpdate vì là INSERT)
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
        
    } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Lỗi khi thêm nhân viên: " + e.getMessage());
        return false;
    } finally {
        try {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
// Trong dao/NhanVienDao.java

public boolean softDeleteNhanVien(String maNV) {
    // Câu lệnh UPDATE Status = 0
    String sql = "UPDATE NHANVIEN SET Status = 0 WHERE MaNV = ?"; 
    
    Connection conn = null;
    PreparedStatement ps = null;
    
    try {
        conn = DBConnection.getConnection();
        ps = conn.prepareStatement(sql);
        
        // Thiết lập tham số MaNV
        ps.setString(1, maNV); 
        
        // Thực thi câu lệnh (executeUpdate vì là UPDATE)
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
        
    } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Lỗi khi xóa mềm nhân viên: " + e.getMessage());
        return false;
    } finally {
        try {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
   // Trong dao/NhanVienDao.java

public boolean updateNhanVien(NhanVien nv) {
    // Câu lệnh UPDATE Status = 0
    String sql = "UPDATE NHANVIEN SET HoTen = ?, GioiTinh = ?, NgaySinh = ?, DiaChi = ?, " +
                 "SoDienThoai = ?, NgayVaoLam = ?, MaPB = ?, Status = ? " +
                 "WHERE MaNV = ?"; // Dùng MaNV để xác định nhân viên cần cập nhật
    
    Connection conn = null;
    PreparedStatement ps = null;
    
    try {
        conn = DBConnection.getConnection();
        ps = conn.prepareStatement(sql);
        
        // Thiết lập các tham số cho câu lệnh SQL (theo thứ tự: 1 đến 8)
        ps.setString(1, nv.getHoTen());
        ps.setString(2, nv.getGioiTinh());
        ps.setDate(3, new java.sql.Date(nv.getNgaySinh().getTime())); 
        ps.setString(4, nv.getDiaChi());
        ps.setString(5, nv.getSoDienThoai());
        ps.setDate(6, new java.sql.Date(nv.getNgayVaoLam().getTime()));
        ps.setString(7, nv.getMaPB());
        ps.setInt(8, nv.getStatus());
        
        // Điều kiện WHERE: MaNV là tham số thứ 9
        ps.setString(9, nv.getMaNV()); 
        
        // Thực thi câu lệnh (executeUpdate vì là UPDATE)
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
        
    } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Lỗi khi cập nhật nhân viên: " + e.getMessage());
        return false;
    } finally {
        try {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    // Thêm vào lớp dao/NhanVienDao.java

// --- 5. Phương thức Lấy Nhân viên theo Mã NV ---
public NhanVien getNhanVienByMa(String maNV) {
    NhanVien nv = null;
    // Câu lệnh SQL (Tìm kiếm theo khóa chính MaNV)
    String sql = "SELECT * FROM NHANVIEN WHERE MaNV = ?"; 

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        conn = DBConnection.getConnection();
        ps = conn.prepareStatement(sql);
        
        // Thiết lập tham số cho câu lệnh SQL
        ps.setString(1, maNV); 
        
        rs = ps.executeQuery();

        if (rs.next()) {
            nv = new NhanVien();
            
            // LẤY DỮ LIỆU VÀ ĐIỀN VÀO TẤT CẢ CÁC TRƯỜNG CỦA ĐỐI TƯỢNG NV
            nv.setMaNV(rs.getString("MaNV"));
            nv.setHoTen(rs.getString("HoTen"));
            nv.setGioiTinh(rs.getString("GioiTinh"));
            
            // Xử lý Ngày sinh
            java.sql.Date sqlDateOfBirth = rs.getDate("NgaySinh");
            if (sqlDateOfBirth != null) {
                nv.setNgaySinh(new Date(sqlDateOfBirth.getTime()));
            }
            
            nv.setDiaChi(rs.getString("DiaChi"));
            nv.setSoDienThoai(rs.getString("SoDienThoai"));
            
            // Xử lý Ngày vào làm
            java.sql.Date sqlDateStart = rs.getDate("NgayVaoLam");
            if (sqlDateStart != null) {
                nv.setNgayVaoLam(new Date(sqlDateStart.getTime()));
            }
            
            nv.setMaPB(rs.getString("MaPB"));
            nv.setStatus(rs.getInt("Status")); 
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Đóng tài nguyên
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return nv;
}
// Trong dao/NhanVienDao.java

// Trong dao/NhanVienDao.java

public List<NhanVien> searchNhanVien(String keyword) {
    List<NhanVien> nhanVienList = new ArrayList<>();
    
    // 1. Mở rộng câu lệnh SQL để tìm kiếm trên tất cả các cột chuỗi
    String sql = "SELECT * FROM NHANVIEN WHERE Status = 1 AND ("
               + "MaNV LIKE ? OR "
               + "HoTen LIKE ? OR "
               + "GioiTinh LIKE ? OR "
               + "DiaChi LIKE ? OR "
               + "SoDienThoai LIKE ? OR "
               + "MaPB LIKE ?"
               + ")";
    
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        conn = DBConnection.getConnection();
        ps = conn.prepareStatement(sql);
        
        String searchParam = "%" + keyword.trim() + "%";
        
        // 2. Gán tham số cho TẤT CẢ các cột
        ps.setString(1, searchParam); // MaNV
        ps.setString(2, searchParam); // HoTen
        ps.setString(3, searchParam); // GioiTinh
        ps.setString(4, searchParam); // DiaChi
        ps.setString(5, searchParam); // SoDienThoai
        ps.setString(6, searchParam); // MaPB (Phòng ban)
        
        rs = ps.executeQuery();

        while (rs.next()) {
            NhanVien nv = new NhanVien();
            // Lấy dữ liệu từ ResultSet và set vào đối tượng nv
            nv.setMaNV(rs.getString("MaNV"));
            nv.setHoTen(rs.getString("HoTen"));
            nv.setGioiTinh(rs.getString("GioiTinh"));
            
            // Xử lý Ngày sinh
            java.sql.Date sqlDateOfBirth = rs.getDate("NgaySinh");
            if (sqlDateOfBirth != null) {
                nv.setNgaySinh(new Date(sqlDateOfBirth.getTime()));
            }
            
            nv.setDiaChi(rs.getString("DiaChi"));
            nv.setSoDienThoai(rs.getString("SoDienThoai"));
            
            // Xử lý Ngày vào làm
            java.sql.Date sqlDateStart = rs.getDate("NgayVaoLam");
            if (sqlDateStart != null) {
                nv.setNgayVaoLam(new Date(sqlDateStart.getTime()));
            }
            
            nv.setMaPB(rs.getString("MaPB"));
            nv.setStatus(rs.getInt("Status")); 

            nhanVienList.add(nv);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // ... (Logic đóng tài nguyên) ...
    }
    return nhanVienList;
}
}
