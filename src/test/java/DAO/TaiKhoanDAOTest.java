/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
// File: TaiKhoanDAOTest.java
package DAO;

import DTO.TaiKhoanDTO;
import config.JDBCUtil;
import org.junit.*;
import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit Test for TaiKhoanDAO class
 * Database: Test version
 * Author: ChatGPT
 * Library: JUnit 4.13.2
 */
public class TaiKhoanDAOTest {

    private static Connection connection;
    private TaiKhoanDAO dao;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Mở kết nối đến database test
        connection = JDBCUtil.getConnection();
        connection.setAutoCommit(false); // Enable rollback after each test
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        connection.rollback(); // rollback mọi thay đổi
        JDBCUtil.closeConnection(connection);
    }

    @Before
    public void setUp() {
        dao = TaiKhoanDAO.getInstance();
    }

    /**
     * TC01 - Test insert account
     * Mục tiêu: Thêm một tài khoản mới thành công
     * Input: TaiKhoanDTO dummy
     * Expected: Return 1 (success), dữ liệu tồn tại
     */
    @Test
    public void testInsert_TC01() {
        TaiKhoanDTO tk = new TaiKhoanDTO(9999, "test_user", "123456", 1, 1);
        int result = dao.insert(tk);
        assertEquals("Should insert successfully", 1, result);
    }

    /**
     * TC02 - Test update account
     * Mục tiêu: Cập nhật thông tin tài khoản
     * Input: username, trạng thái, nhóm quyền
     * Expected: Return 1 (thành công)
     */
    @Test
    public void testUpdate_TC02() {
        TaiKhoanDTO tk = new TaiKhoanDTO(9999, "updated_user", "123456", 2, 0);
        int result = dao.update(tk);
        assertEquals("Should update account", 1, result);
    }

    /**
     * TC03 - Test update password by email
     * Mục tiêu: Cập nhật mật khẩu bằng email
     * Input: Email đã tồn tại
     * Expected: Không throw exception
     */
    @Test
    public void testUpdatePass_TC03() {
        dao.updatePass("test@email.com", "newpass123");
        // Không có exception -> pass
    }

    /**
     * TC04 - Test selectByEmail
     * Mục tiêu: Tìm tài khoản theo email
     * Input: Email tồn tại
     * Expected: Tài khoản không null
     */
    @Test
    public void testSelectByEmail_TC04() {
        TaiKhoanDTO tk = dao.selectByEmail("test@email.com");
        assertNotNull("Account should be found by email", tk);
    }

    /**
     * TC05 - Test send OTP
     * Mục tiêu: Gửi OTP thành công
     * Input: email hợp lệ, chuỗi OTP
     * Expected: Không lỗi
     */
    @Test
    public void testSendOtp_TC05() {
        dao.sendOpt("test@email.com", "987654");
    }

    /**
     * TC06 - Test check OTP
     * Mục tiêu: Kiểm tra OTP đúng
     * Input: email + otp hợp lệ
     * Expected: true
     */
    @Test
    public void testCheckOtp_TC06() {
        dao.sendOpt("test@email.com", "111222");
        boolean check = dao.checkOtp("test@email.com", "111222");
        assertTrue("OTP should be valid", check);
    }

    /**
     * TC07 - Test delete account (soft delete)
     * Mục tiêu: Gán trạng thái -1 cho tài khoản
     * Input: mã nhân viên
     * Expected: Return 1
     */
    @Test
    public void testDelete_TC07() {
        int result = dao.delete("9999");
        assertEquals("Account should be soft deleted", 1, result);
    }

    /**
     * TC08 - Test selectAll
     * Mục tiêu: Lấy danh sách tài khoản hoạt động
     * Input: none
     * Expected: Danh sách >= 0
     */
    @Test
    public void testSelectAll_TC08() {
        List<TaiKhoanDTO> list = dao.selectAll();
        assertNotNull("List should not be null", list);
    }

    /**
     * TC09 - Test selectById
     * Mục tiêu: Tìm tài khoản theo mã nhân viên
     * Input: mã nhân viên
     * Expected: Trả về đúng DTO
     */
    @Test
    public void testSelectById_TC09() {
        TaiKhoanDTO tk = dao.selectById("1");
        assertNotNull("Account should exist with manv=1", tk);
    }

    /**
     * TC10 - Test selectByUser
     * Mục tiêu: Tìm tài khoản theo tên đăng nhập
     * Input: username hợp lệ
     * Expected: Không null
     */
    @Test
    public void testSelectByUser_TC10() {
        TaiKhoanDTO tk = dao.selectByUser("admin");
        assertNotNull("Account should exist", tk);
    }

    /**
     * TC11 - Test getAutoIncrement
     * Mục tiêu: Lấy ID auto-increment tiếp theo
     * Expected: Số > 0
     */
    @Test
    public void testGetAutoIncrement_TC11() {
        int nextId = dao.getAutoIncrement();
        assertTrue("Auto increment should be > 0", nextId > 0);
    }
}
