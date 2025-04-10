package DAO;

import DTO.PhieuKiemKeDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit test for class PhieuKiemKeDAO
 * File/Class: PhieuKiemKeDAOTest.java
 * Author: DELL
 * 
 * Mỗi test case có mô tả rõ ràng (mã test, mục tiêu, input, expected output, ghi chú)
 */
public class PhieuKiemKeDAOTest {

    private static PhieuKiemKeDAO phieuKiemKeDAO;

    @BeforeClass
    public static void setUpClass() {
        phieuKiemKeDAO = PhieuKiemKeDAO.getInstance();
    }

    @AfterClass
    public static void tearDownClass() {
        // Cleanup resources if needed
    }

    @Before
    public void setUp() {
        // Setup before each test if needed
    }

    @After
    public void tearDown() {
        // Rollback nếu cần
    }

    /**
     * TC01 - Test getInstance()
     * Mục tiêu: kiểm tra getInstance trả về object khác null
     * Expected: không null
     */
    @Test
    public void test_TC01_getInstance_ReturnsNotNull() {
        PhieuKiemKeDAO result = PhieuKiemKeDAO.getInstance();
        assertNotNull("getInstance() should not return null", result);
    }

    /**
     * TC02 - Test insert()
     * Mục tiêu: kiểm tra chèn dữ liệu thành công
     * Input: một bản ghi PhieuKiemKeDTO hợp lệ
     * Expected: trả về 1 (số dòng thêm thành công)
     */
    @Test
    public void test_TC02_insert_ValidPhieuKiemKe_ReturnsOne() {
        int autoId = phieuKiemKeDAO.getAutoIncrement();
        PhieuKiemKeDTO dto = new PhieuKiemKeDTO(autoId, 1, new Timestamp(System.currentTimeMillis()));
        int result = phieuKiemKeDAO.insert(dto);
        assertEquals("Insert should return 1 row affected", 1, result);

        // Rollback manually
        phieuKiemKeDAO.delete(String.valueOf(autoId));
    }

    /**
     * TC03 - Test delete()
     * Mục tiêu: kiểm tra xóa bản ghi theo mã phiếu
     * Input: maphieu tồn tại
     * Expected: trả về 1 (xóa thành công)
     */
    @Test
    public void test_TC03_delete_ExistingId_ReturnsOne() {
        int autoId = phieuKiemKeDAO.getAutoIncrement();
        PhieuKiemKeDTO dto = new PhieuKiemKeDTO(autoId, 1, new Timestamp(System.currentTimeMillis()));
        phieuKiemKeDAO.insert(dto);

        int result = phieuKiemKeDAO.delete(String.valueOf(autoId));
        assertEquals("Delete should return 1 row affected", 1, result);
    }

    /**
     * TC04 - Test selectAll()
     * Mục tiêu: kiểm tra hàm trả về danh sách không null
     * Expected: danh sách (có thể rỗng) nhưng không null
     */
    @Test
    public void test_TC04_selectAll_ReturnsListNotNull() {
        ArrayList<PhieuKiemKeDTO> result = phieuKiemKeDAO.selectAll();
        assertNotNull("selectAll() should not return null", result);
    }

    /**
     * TC05 - Test getAutoIncrement()
     * Mục tiêu: kiểm tra giá trị AUTO_INCREMENT > 0
     */
    @Test
    public void test_TC05_getAutoIncrement_ReturnsPositive() {
        int result = phieuKiemKeDAO.getAutoIncrement();
        assertTrue("Auto increment should be positive", result > 0);
    }

    /**
     * TC06 - Test delete() với ID không tồn tại
     * Mục tiêu: đảm bảo delete không lỗi và trả về 0
     */
    @Test
    public void test_TC06_delete_InvalidId_ReturnsZero() {
        int result = phieuKiemKeDAO.delete("999999999");
        assertEquals("Delete with invalid ID should return 0", 0, result);
    }

    // Các test khác như update(), selectById() đang chưa được cài đặt nên chưa test
}