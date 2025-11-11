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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    // Câu truy vấn INSERT (Đăng ký)
    private static final String REGISTER_SQL = 
        "INSERT INTO ACCOUNT (USERNAME, GMAIL, PASS, CONFIRM) VALUES (?, ?, ?, ?)";
        
    // Câu truy vấn SELECT (Đăng nhập)
    private static final String LOGIN_SQL = 
        "SELECT USERNAME FROM ACCOUNT WHERE USERNAME = ? AND PASS = ?";

    /**
     * Thực hiện đăng ký tài khoản mới vào database.
     * @return số hàng bị ảnh hưởng (1 nếu thành công).
     * @throws SQLException nếu có lỗi SQL (ví dụ: trùng lặp USERNAME).
     */
    public int register(String username, String email, String password, String confirm) throws SQLException {
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(REGISTER_SQL)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password); 
            stmt.setString(4, confirm);  

            // executeUpdate trả về số hàng bị ảnh hưởng
            return stmt.executeUpdate(); 
        }
    }

    /**
     * Kiểm tra thông tin đăng nhập trong database.
     * @return true nếu đăng nhập thành công, false nếu thất bại.
     * @throws SQLException nếu có lỗi SQL.
     */
    public boolean checkLogin(String username, String password) throws SQLException {
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(LOGIN_SQL)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true nếu tìm thấy dòng dữ liệu khớp
        }
    }

    
}
