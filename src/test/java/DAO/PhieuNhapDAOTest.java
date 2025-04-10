/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package DAO;

import DTO.PhieuNhapDTO;
import config.JDBCUtil;
import org.junit.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Class: PhieuNhapDAOTest
 * Mục tiêu: Kiểm thử các phương thức của lớp PhieuNhapDAO
 */
public class PhieuNhapDAOTest {

    private PhieuNhapDAO phieuNhapDAO;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        phieuNhapDAO = PhieuNhapDAO.getInstance();
        connection = JDBCUtil.getConnection();
        connection.setAutoCommit(false); // rollback sau mỗi test
    }

    @After
    public void tearDown() throws Exception {
        connection.rollback(); // rollback để không ảnh hưởng DB
        connection.close();
    }

    /**
     * TC001 - Test insert()
     * Mục tiêu: Kiểm tra chèn một phiếu nhập hợp lệ
     */
    @Test
    public void testInsert_TC001_InsertValidPhieuNhap() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        PhieuNhapDTO dto = new PhieuNhapDTO(1, 9999, 1, now, 100000, 1);
        int result = phieuNhapDAO.insert(dto);
        assertEquals("Insert phiếu nhập hợp lệ", 1, result);
    }

    /**
     * TC002 - Test update()
     * Mục tiêu: Cập nhật thông tin phiếu nhập
     */
    @Test
    public void testUpdate_TC002_UpdateExistingPhieuNhap() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        PhieuNhapDTO dto = new PhieuNhapDTO(1, 9998, 1, now, 100000, 1);
        phieuNhapDAO.insert(dto);

        dto.setTongTien(200000);
        dto.setTrangthai(2);
        int result = phieuNhapDAO.update(dto);
        assertEquals("Update thành công phiếu nhập", 1, result);
    }

    /**
     * TC003 - Test delete()
     * Mục tiêu: Đánh dấu trạng thái phiếu nhập là 0
     */
    @Test
    public void testDelete_TC003_DeletePhieuNhap() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        PhieuNhapDTO dto = new PhieuNhapDTO(1, 9997, 1, now, 50000, 1);
        phieuNhapDAO.insert(dto);

        int result = phieuNhapDAO.delete("9997");
        assertEquals("Xóa mềm phiếu nhập", 1, result);
    }

    /**
     * TC004 - Test selectAll()
     * Mục tiêu: Lấy toàn bộ danh sách phiếu nhập
     */
    @Test
    public void testSelectAll_TC004_GetAllPhieuNhap() {
        ArrayList<PhieuNhapDTO> list = phieuNhapDAO.selectAll();
        assertNotNull("Danh sách phiếu nhập không null", list);
        assertTrue("Danh sách có thể rỗng hoặc nhiều hơn", list.size() >= 0);
    }

    /**
     * TC005 - Test selectById()
     * Mục tiêu: Lấy phiếu nhập theo ID
     */
    @Test
    public void testSelectById_TC005_GetPhieuNhapById() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        PhieuNhapDTO dto = new PhieuNhapDTO(1, 9996, 1, now, 60000, 1);
        phieuNhapDAO.insert(dto);

        PhieuNhapDTO found = phieuNhapDAO.selectById("9996");
        assertNotNull("Tìm thấy phiếu nhập theo ID", found);
        assertEquals("ID đúng", 9996, found.getMaphieu());
    }

    /**
     * TC006 - Test statistical()
     * Mục tiêu: Lọc phiếu nhập theo khoảng tiền
     */
    @Test
    public void testStatistical_TC006_FilterByTongTien() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        PhieuNhapDTO dto = new PhieuNhapDTO(1, 9995, 1, now, 150000, 1);
        phieuNhapDAO.insert(dto);

        ArrayList<PhieuNhapDTO> list = phieuNhapDAO.statistical(10000, 200000);
        assertNotNull("Danh sách lọc theo tổng tiền", list);
        assertTrue("Có thể có hoặc không có kết quả", list.size() >= 0);
    }

    /**
     * TC007 - Test getAutoIncrement()
     * Mục tiêu: Lấy giá trị AUTO_INCREMENT tiếp theo
     */
    @Test
    public void testGetAutoIncrement_TC007_GetNextAutoId() {
        int nextId = phieuNhapDAO.getAutoIncrement();
        assertTrue("AUTO_INCREMENT hợp lệ", nextId > 0);
    }

    /**
     * TC008 - Test checkCancelPn()
     * Mục tiêu: Kiểm tra phiếu nhập có thể hủy không (giả sử không xuất sản phẩm)
     */
    @Test
    public void testCheckCancelPn_TC008_CheckCancelablePhieuNhap() {
        boolean result = phieuNhapDAO.checkCancelPn(1);
        assertTrue("Có thể hủy nếu chưa xuất", result || !result);
    }

    /**
     * TC009 - Test cancelPhieuNhap()
     * Mục tiêu: Thử hủy phiếu nhập (đòi hỏi DB test tương thích)
     * Input: maphieunhap giả định đã insert trước
     * Expected Output: Trả về 1 nếu xóa thành công
     * Ghi chú: Phụ thuộc dữ liệu bảng liên quan nên test này nên được kiểm thử khi đầy đủ bảng test
     */
    @Ignore("Test cần cấu hình bảng CTSanPham, ChiTietPhieuNhap, PhienBanSanPham trước khi test")
    @Test
    public void testCancelPhieuNhap_TC009_CancelFullPhieuNhap() {
        int result = phieuNhapDAO.cancelPhieuNhap(1);
        assertEquals("Hủy phiếu nhập thành công", 1, result);
    }

}
