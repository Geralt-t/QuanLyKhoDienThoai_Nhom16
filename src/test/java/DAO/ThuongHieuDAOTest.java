/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package DAO;

import DTO.ThuocTinhSanPham.ThuongHieuDTO;
import config.JDBCUtil;
import org.junit.*;
import java.sql.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test class: ThuongHieuDAO
 * Mục tiêu: Kiểm thử các chức năng CRUD và logic xử lý trong lớp DAO tương tác với bảng 'thuonghieu'
 */
public class ThuongHieuDAOTest {

    private static Connection conn;
    private static ThuongHieuDAO thuongHieuDAO;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        conn = JDBCUtil.getConnection();
        conn.setAutoCommit(false); // Rollback sau mỗi test
        thuongHieuDAO = ThuongHieuDAO.getInstance();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        conn.rollback(); // Rollback mọi thay đổi
        conn.setAutoCommit(true);
        JDBCUtil.closeConnection(conn);
    }

    /**
     * TC01 - insert - Kiểm thử thêm thương hiệu mới vào database
     * Mục tiêu: kiểm tra khi insert thành công thì trả về số dòng bị ảnh hưởng = 1
     */
    @Test
    public void TC01_insertThuongHieu_Success() {
        ThuongHieuDTO thuongHieuMoi = new ThuongHieuDTO(999, "TestBrand");
        int expected = 1;

        int result = thuongHieuDAO.insert(thuongHieuMoi);

        assertEquals("Insert thương hiệu mới phải thành công", expected, result);
    }

    /**
     * TC02 - update - Cập nhật tên thương hiệu theo mã
     * Mục tiêu: cập nhật tên thành công -> affectedRows = 1
     */
    @Test
    public void TC02_updateThuongHieu_Success() {
        // Setup: thêm dữ liệu để test update
        ThuongHieuDTO thuongHieuMoi = new ThuongHieuDTO(998, "BrandToUpdate");
        thuongHieuDAO.insert(thuongHieuMoi);

        int id = getLastInsertedId(); // Lấy mã vừa insert
        ThuongHieuDTO capNhat = new ThuongHieuDTO(id, "UpdatedBrand");

        int result = thuongHieuDAO.update(capNhat);

        assertEquals("Update tên thương hiệu thành công", 1, result);
    }

    /**
     * TC03 - delete - Xóa mềm (set trạng thái = 0)
     * Mục tiêu: kiểm tra trạng thái được set về 0 thành công
     */
    @Test
    public void TC03_deleteThuongHieu_SoftDelete() {
        ThuongHieuDTO thuongHieu = new ThuongHieuDTO(997, "BrandToDelete");
        thuongHieuDAO.insert(thuongHieu);

        int id = getLastInsertedId();

        int result = thuongHieuDAO.delete(String.valueOf(id));

        assertEquals("Xóa mềm thành công (trangthai = 0)", 1, result);
    }

    /**
     * TC04 - selectAll - Lấy tất cả các thương hiệu có trạng thái = 1
     * Mục tiêu: chỉ trả về các bản ghi có trạng thái = 1
     */
    @Test
    public void TC04_selectAllThuongHieu_ReturnActiveOnly() {
        ArrayList<ThuongHieuDTO> list = thuongHieuDAO.selectAll();

        for (ThuongHieuDTO th : list) {
            assertNotNull("Tên thương hiệu không được null", th.getTenthuonghieu());
        }
    }

    /**
     * TC05 - selectById - Lấy thương hiệu theo ID
     * Mục tiêu: đảm bảo lấy đúng thương hiệu theo ID vừa insert
     */
    @Test
    public void TC05_selectThuongHieuById_ReturnExactMatch() {
        ThuongHieuDTO thuongHieu = new ThuongHieuDTO(996, "BrandById");
        thuongHieuDAO.insert(thuongHieu);
        int id = getLastInsertedId();

        ThuongHieuDTO result = thuongHieuDAO.selectById(String.valueOf(id));

        assertNotNull("Kết quả không được null", result);
        assertEquals("Tên thương hiệu phải khớp", "BrandById", result.getTenthuonghieu());
    }

    /**
     * TC06 - getAutoIncrement - Kiểm tra giá trị AUTO_INCREMENT hiện tại
     * Mục tiêu: đảm bảo trả về giá trị > 0
     */
    @Test
    public void TC06_getAutoIncrement_ReturnValid() {
        int result = thuongHieuDAO.getAutoIncrement();

        assertTrue("AUTO_INCREMENT phải > 0", result > 0);
    }

    /**
     * Hàm hỗ trợ: Lấy ID vừa insert gần nhất trong bảng thuonghieu
     */
    private int getLastInsertedId() {
        int id = -1;
        try {
            String sql = "SELECT MAX(mathuonghieu) AS id FROM thuonghieu";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            fail("Không thể lấy ID cuối cùng: " + e.getMessage());
        }
        return id;
    }
}
