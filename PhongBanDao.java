/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.PhongBan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import database.DBConnection; // Dùng package 'database' cho DBConnection

public class PhongBanDao {

    // Helper method để ánh xạ ResultSet sang đối tượng PhongBan
    private PhongBan extractPhongBanFromResultSet(ResultSet rs) throws SQLException {
        PhongBan pb = new PhongBan();
        pb.setMaPB(rs.getString("MaPB"));
        pb.setTenPB(rs.getString("TenPB"));
        pb.setStatus(rs.getInt("Status"));
        return pb;
    }

    // 1. READ: Lấy tất cả phòng ban đang hoạt động (Status = 1)
    public List<PhongBan> getAllActivePhongBan() {
        List<PhongBan> list = new ArrayList<>();
        String sql = "SELECT MaPB, TenPB, Status FROM PHONGBAN WHERE Status = 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractPhongBanFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. CREATE: Thêm phòng ban mới
    public boolean addPhongBan(PhongBan pb) {
        String sql = "INSERT INTO PHONGBAN (MaPB, TenPB, Status) VALUES (?, ?, 1)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pb.getMaPB());
            ps.setString(2, pb.getTenPB());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. UPDATE: Cập nhật Tên và Status phòng ban
    public boolean updatePhongBan(PhongBan pb) {

        String sql = "UPDATE PHONGBAN SET TenPB = ?, Status = ? WHERE MaPB = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Sửa lỗi indexing:
            ps.setString(1, pb.getTenPB()); 
            ps.setInt(2, pb.getStatus()); // Tham số thứ 2 là Status
            ps.setString(3, pb.getMaPB()); // Tham số thứ 3 là MaPB (WHERE condition)

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. DELETE (Soft Delete): Đổi trạng thái Status = 0
    public boolean softDeletePhongBan(String maPB) {
        String sql = "UPDATE PHONGBAN SET Status = 0 WHERE MaPB = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPB);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. SEARCH: Tìm kiếm theo Mã PB, Tên PB
    public List<PhongBan> searchPhongBan(String keyword) {
        List<PhongBan> list = new ArrayList<>();

        String sql = "SELECT MaPB, TenPB, Status FROM PHONGBAN WHERE Status = 1 AND (MaPB LIKE ? OR TenPB LIKE ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchParam = "%" + keyword.trim() + "%";
            ps.setString(1, searchParam); // MaPB
            ps.setString(2, searchParam); // TenPB

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractPhongBanFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Phương thức hỗ trợ lấy 1 phòng ban theo mã (Đã đổi tên từ getPhongBanByMa thành getPhongBanByMaPB)
    public PhongBan getPhongBanByMaPB(String maPB) {
        String sql = "SELECT MaPB, TenPB, Status FROM PHONGBAN WHERE MaPB = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPB);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractPhongBanFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}