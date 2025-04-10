/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package DAO;

import DTO.NhaCungCapDTO;
import config.JDBCUtil;
import org.junit.*;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Unit test for class: NhaCungCapDAO
 * Framework: JUnit 4.13.2
 */
public class NhaCungCapDAOTest {

    private static Connection conn;

    /**
     * Thiết lập kết nối trước khi chạy các test case.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        conn = JDBCUtil.getConnection();
        conn.setAutoCommit(false); // để rollback sau mỗi test
    }

    /**
     * Rollback và đóng kết nối sau mỗi test.
     */
     @After
    public void tearDown() throws Exception {
        // Xóa dữ liệu test thủ công (nếu DAO dùng conn khác)
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DELETE FROM nhacungcap WHERE manhacungcap >= 9995");
        conn.commit();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }


    /**
     * TC01 - insert(): Thêm một nhà cung cấp hợp lệ
     * Mục tiêu: Kiểm tra thêm dữ liệu thành công
     */
    @Test
    public void testInsert_TC01() {
        NhaCungCapDTO dto = new NhaCungCapDTO(9999, "Nhà cung cấp test", "Hà Nội", "test@gmail.com", "0123456789");

        int result = NhaCungCapDAO.getInstance().insert(dto);

        assertEquals(1, result); // Mong đợi chèn thành công
    }

    /**
     * TC02 - update(): Cập nhật nhà cung cấp
     * Mục tiêu: Cập nhật thông tin của một nhà cung cấp đã có
     */
    @Test
    public void testUpdate_TC02() {
        NhaCungCapDTO dto = new NhaCungCapDTO(9998, "Test NCC", "HN", "test@mail.com", "0987654321");
        NhaCungCapDAO.getInstance().insert(dto);

        dto.setTenncc("Test NCC Mới");
        dto.setDiachi("TP HCM");

        int result = NhaCungCapDAO.getInstance().update(dto);

        assertEquals(1, result); // Mong đợi cập nhật thành công
    }

    /**
     * TC03 - delete(): Xóa nhà cung cấp bằng cách chuyển trạng thái
     * Mục tiêu: Đảm bảo trạng thái bị chuyển sang 0
     */
    @Test
    public void testDelete_TC03() throws SQLException {
        NhaCungCapDTO dto = new NhaCungCapDTO(9997, "NCC Delete", "HN", "del@mail.com", "0999999999");
        NhaCungCapDAO.getInstance().insert(dto);

        int result = NhaCungCapDAO.getInstance().delete(String.valueOf(dto.getMancc()));

        assertEquals(1, result); // Mong đợi cập nhật trạng thái thành công

        // Kiểm tra trạng thái trong DB
        PreparedStatement ps = conn.prepareStatement("SELECT trangthai FROM nhacungcap WHERE manhacungcap = ?");
        ps.setInt(1, dto.getMancc());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            assertEquals(0, rs.getInt("trangthai"));
        }
    }

    /**
     * TC04 - selectAll(): Lấy danh sách các NCC còn hoạt động
     * Mục tiêu: Trả về danh sách có ít nhất 1 phần tử (sau khi thêm)
     */
    @Test
    public void testSelectAll_TC04() {
        NhaCungCapDTO dto = new NhaCungCapDTO(9996, "Test All", "QN", "all@mail.com", "011223344");
        NhaCungCapDAO.getInstance().insert(dto);

        ArrayList<NhaCungCapDTO> list = NhaCungCapDAO.getInstance().selectAll();

        assertTrue(list.size() > 0); // Mong đợi danh sách có phần tử
    }

    /**
     * TC05 - selectById(): Lấy nhà cung cấp theo mã
     * Mục tiêu: Trả về đúng thông tin của NCC
     */
    @Test
    public void testSelectById_TC05() {
        NhaCungCapDTO dto = new NhaCungCapDTO(9995, "Test ID", "Hue", "id@mail.com", "066688899");
        NhaCungCapDAO.getInstance().insert(dto);

        NhaCungCapDTO result = NhaCungCapDAO.getInstance().selectById(String.valueOf(dto.getMancc()));

        assertNotNull(result);
        assertEquals("Test ID", result.getTenncc());
        assertEquals("Hue", result.getDiachi());
    }

    /**
     * TC06 - getAutoIncrement(): Lấy giá trị AUTO_INCREMENT tiếp theo
     * Mục tiêu: Trả về số nguyên dương
     */
    @Test
    public void testGetAutoIncrement_TC06() {
        int nextId = NhaCungCapDAO.getInstance().getAutoIncrement();

        assertTrue(nextId > 0); // Mong đợi ID tiếp theo > 0
    }
}
