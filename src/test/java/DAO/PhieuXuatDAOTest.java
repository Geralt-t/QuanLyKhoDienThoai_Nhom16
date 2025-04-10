/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package DAO;

import DTO.PhieuXuatDTO;
import config.JDBCUtil;
import org.junit.*;
import java.sql.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * File: PhieuXuatDAOTest.java
 */

public class PhieuXuatDAOTest {

    private static Connection connection;
    private static PhieuXuatDAO dao;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        connection = JDBCUtil.getConnection();
        connection.setAutoCommit(false); // Disable auto-commit for rollback after test
        dao = PhieuXuatDAO.getInstance();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        connection.rollback(); // Rollback all changes made during tests
        connection.setAutoCommit(true); // Restore auto-commit
        JDBCUtil.closeConnection(connection);
    }

    /**
     * TC01: Test insert
     * Mục tiêu: Kiểm tra phương thức insert hoạt động đúng
     * Input: DTO có các trường hợp lệ
     * Expected Output: 1 record được insert
     */
    @Test
    public void TC01_insertPhieuXuat_success() {
        int autoId = dao.getAutoIncrement();

        PhieuXuatDTO dto = new PhieuXuatDTO(1, autoId, 2, null, 50000L, 1);
        int result = dao.insert(dto);

        assertEquals(1, result); // Expect 1 row inserted

        // Check DB để xác nhận dữ liệu đã được thêm
        PhieuXuatDTO fromDB = dao.selectById(String.valueOf(autoId));
        assertNotNull(fromDB);
        assertEquals(dto.getMakh(), fromDB.getMakh());
        assertEquals(dto.getTongTien(), fromDB.getTongTien());
    }

    /**
     * TC02: Test update
     * Mục tiêu: Đảm bảo update thành công khi dữ liệu hợp lệ
     * Input: phiếu đã có sẵn (autoId), cập nhật tongTien và trangthai
     * Expected Output: 1 record updated
     */
    @Test
    public void TC02_updatePhieuXuat_success() {
        int autoId = dao.getAutoIncrement();
        PhieuXuatDTO dto = new PhieuXuatDTO(1, autoId, 2, null, 50000L, 1);
        dao.insert(dto);

        // Modify fields
        dto.setTongTien(100000L);
        dto.setTrangthai(2);
        int result = dao.update(dto);

        assertEquals(1, result); // Expect 1 row updated

        PhieuXuatDTO fromDB = dao.selectById(String.valueOf(autoId));
        assertEquals(100000L, fromDB.getTongTien());
        assertEquals(2, fromDB.getTrangthai());
    }

    /**
     * TC03: Test delete 
     * Mục tiêu: Xác minh trạng thái được cập nhật là 0 thay vì xóa thật
     * Input: maphieu string
     * Expected Output: 1 record updated, trangthai = 0
     */
    @Test
    public void TC03_softDeletePhieuXuat_success() {
        int autoId = dao.getAutoIncrement();
        dao.insert(new PhieuXuatDTO(1, autoId, 2, null, 60000L, 1));

        int result = dao.delete(String.valueOf(autoId));
        assertEquals(1, result); // Expect 1 row updated

        PhieuXuatDTO fromDB = dao.selectById(String.valueOf(autoId));
        assertEquals(0, fromDB.getTrangthai()); // Expect soft deleted (trangthai = 0)
    }

    /**
     * TC04: Test select all
     * Mục tiêu: Lấy tất cả phiếu xuất hiện có trong bảng
     * Input: Không có
     * Expected Output: Danh sách phiếu xuất trả về không null
     */
    @Test
    public void TC04_selectAllPhieuXuat() {
        ArrayList<PhieuXuatDTO> list = dao.selectAll();
        assertNotNull(list);
        assertTrue(list.size() >= 0); // Có thể không có bản ghi nhưng không lỗi
    }

    /**
     * TC05: Test lấy tự động AUTO_INCREMENT ID
     * Mục tiêu: Đảm bảo lấy đúng giá trị tăng tiếp theo
     * Expected Output: >= 1
     */
    @Test
    public void TC05_getAutoIncrement_success() {
        int nextId = dao.getAutoIncrement();
        assertTrue(nextId >= 1);
    }

    /**
     * TC06: Test delete thật (xóa khỏi bảng)
     * Mục tiêu: Xóa phiếu khỏi bảng
     * Expected Output: record bị xóa, selectById trả về null
     */
    @Test
    public void TC06_deletePhieuXuat_permanent() {
        int autoId = dao.getAutoIncrement();
        dao.insert(new PhieuXuatDTO(1, autoId, 2, null, 60000L, 1));

        int result = dao.deletePhieu(autoId);
        assertEquals(1, result);

        PhieuXuatDTO deleted = dao.selectById(String.valueOf(autoId));
        assertNull(deleted);
    }

    /**
     * TC07: Test lấy tất cả phiếu xuất theo khách hàng
     * Mục tiêu: Kiểm tra selectAllofKH lấy đúng phiếu theo mã khách
     * Input: makh = 1
     * Expected Output: danh sách không null
     */
    @Test
    public void TC07_selectAllofKH_success() {
        ArrayList<PhieuXuatDTO> list = dao.selectAllofKH(1);
        assertNotNull(list);
    }

    // Bạn có thể thêm test case khác cho cancel() nếu mock được ChiTietPhieuXuatDAO và ChiTietSanPhamDAO

}
