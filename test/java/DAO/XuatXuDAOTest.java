/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package DAO;

import DTO.ThuocTinhSanPham.XuatXuDTO;
import config.JDBCUtil;
import org.junit.*;
import java.sql.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Unit test for XuatXuDAO.
 * Mục tiêu: Kiểm thử các chức năng CRUD và lấy ID tự tăng.
 * Tên lớp: XuatXuDAOTest
 * JUnit version: 4.13.2
 */
public class XuatXuDAOTest {

    private static Connection connection;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        connection = JDBCUtil.getConnection();
        connection.setAutoCommit(false); // Bật chế độ rollback sau mỗi test
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        connection.close();
    }

    @After
    public void rollbackAfterEachTest() throws Exception {
        connection.rollback(); // Rollback mọi thay đổi sau mỗi test
    }

    /**
     * TC_01
     * Mục tiêu: Kiểm tra thêm mới xuất xứ.
     * Input: new XuatXuDTO(id, "Test Country")
     * Expected: trả về 1 (số dòng ảnh hưởng)
     */
    @Test
    public void testInsert() throws Exception {
        XuatXuDTO dto = new XuatXuDTO(9999, "Test Country");
        int result = XuatXuDAO.getInstance().insert(dto);
        assertEquals(1, result);
    }

    /**
     * TC_02
     * Mục tiêu: Kiểm tra cập nhật thông tin xuất xứ.
     * Input: cập nhật tên "Updated Country"
     * Expected: trả về 1 (1 dòng bị ảnh hưởng)
     */
    @Test
    public void testUpdate() throws Exception {
        XuatXuDTO dto = new XuatXuDTO(9999, "Initial Country");
        XuatXuDAO.getInstance().insert(dto);
        dto.setTenxuatxu("Updated Country");
        int result = XuatXuDAO.getInstance().update(dto);
        assertEquals(1, result);
    }

    /**
     * TC_03
     * Mục tiêu: Kiểm tra xóa mềm (set trạng thái = 0).
     * Input: ID = 9999
     * Expected: trả về 1 (1 dòng bị ảnh hưởng)
     */
    @Test
    public void testDelete() throws Exception {
        XuatXuDTO dto = new XuatXuDTO(9999, "Delete Test");
        XuatXuDAO.getInstance().insert(dto);
        int result = XuatXuDAO.getInstance().delete("9999");
        assertEquals(1, result);
    }

    /**
     * TC_04
     * Mục tiêu: Kiểm tra lấy tất cả bản ghi còn hoạt động (trangthai = 1)
     * Expected: Danh sách không null và có size >= 0
     */
    @Test
    public void testSelectAll() throws Exception {
        ArrayList<XuatXuDTO> list = XuatXuDAO.getInstance().selectAll();
        assertNotNull(list);
        assertTrue(list.size() >= 0);
    }

    /**
     * TC_05
     * Mục tiêu: Kiểm tra tìm kiếm xuất xứ theo ID
     * Input: ID = "9999"
     * Expected: Trả về DTO có đúng ID và tên
     */
    @Test
    public void testSelectById() throws Exception {
        XuatXuDTO dto = new XuatXuDTO(9999, "Find Test");
        XuatXuDAO.getInstance().insert(dto);
        XuatXuDTO result = XuatXuDAO.getInstance().selectById("9999");
        assertNotNull(result);
        assertEquals("Find Test", result.getTenxuatxu());
    }

    /**
     * TC_06
     * Mục tiêu: Kiểm tra giá trị tự động tăng ID tiếp theo.
     * Expected: Giá trị > 0
     */
    @Test
    public void testGetAutoIncrement() {
        int autoIncrement = XuatXuDAO.getInstance().getAutoIncrement();
        assertTrue(autoIncrement > 0);
    }
}
