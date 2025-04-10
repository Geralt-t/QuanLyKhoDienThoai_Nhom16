/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package DAO;

/**
 * File: SanPhamDAOTest.java
 * Class: SanPhamDAOTest
 * Description: Kiểm thử các phương thức trong lớp SanPhamDAO sử dụng JUnit 4.13.2
 * Tester: [Your Name]
 */

import DAO.SanPhamDAO;
import DTO.SanPhamDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SanPhamDAOTest {

    private SanPhamDAO dao;
    private SanPhamDTO spTest;

    /**
     * Setup - Insert một sản phẩm mẫu để sử dụng trong các test case.
     */
    @Before
    public void setUp() {
        dao = SanPhamDAO.getInstance();
        spTest = new SanPhamDTO(
                9999,                 // masp
                "Test Phone",        // tensp
                "test.jpg",          // hinhanh
                1,                   // madm
                "Snapdragon 888",    // chip
                4000,                // pin
                6.5,                 // manhinh
                1,                   // rom
                1,                   // ram
                "108MP",             // camsau
                "32MP",              // camtruoc
                12,                  // thoigianbaohanh
                1,                   // trangthai
                1,                   // manhinhid
                10                   // soluongton
        );
        dao.insert(spTest);
    }

    /**
     * Cleanup - Xoá sản phẩm sau khi mỗi test case chạy xong.
     */
    @After
    public void tearDown() {
        dao.delete(Integer.toString(spTest.getMasp()));
    }

    /**
     * Test Case ID: TC001
     * Mục tiêu: Kiểm thử phương thức insert()
     * Input: Đối tượng SanPhamDTO với mã 9999
     * Expected Output: selectById() trả về sản phẩm không null, tên sản phẩm là "Test Phone"
     * Ghi chú: Kiểm tra sản phẩm được thêm thành công vào CSDL.
     */
    @Test
    public void testInsert_TC001() {
        SanPhamDTO sp = dao.selectById(Integer.toString(spTest.getMasp()));
        assertNotNull(sp);
        assertEquals("Test Phone", sp.getTensp());
    }

    /**
     * Test Case ID: TC002
     * Mục tiêu: Kiểm thử phương thức update()
     * Input: Cập nhật tên sản phẩm thành "Updated Phone"
     * Expected Output: update() trả về 1; selectById() trả về sản phẩm có tên mới
     * Ghi chú: Kiểm tra sản phẩm được cập nhật đúng.
     */
    @Test
    public void testUpdate_TC002() {
        spTest.setTensp("Updated Phone");
        int result = dao.update(spTest);
        assertEquals(1, result);

        SanPhamDTO sp = dao.selectById(Integer.toString(spTest.getMasp()));
        assertEquals("Updated Phone", sp.getTensp());
    }

    /**
     * Test Case ID: TC003
     * Mục tiêu: Kiểm thử phương thức selectById()
     * Input: Mã sản phẩm 9999
     * Expected Output: Trả về đối tượng SanPhamDTO không null, có mã đúng
     * Ghi chú: Đảm bảo sản phẩm có thể được truy xuất chính xác theo ID.
     */
    @Test
    public void testSelectById_TC003() {
        SanPhamDTO sp = dao.selectById(Integer.toString(spTest.getMasp()));
        assertNotNull(sp);
        assertEquals(spTest.getMasp(), sp.getMasp());
    }

    /**
     * Test Case ID: TC004
     * Mục tiêu: Kiểm thử phương thức delete() (xoá mềm)
     * Input: Mã sản phẩm 9999
     * Expected Output: Trả về 1; selectById() vẫn trả về sản phẩm (do xoá mềm)
     * Ghi chú: Xoá mềm sản phẩm, kiểm tra trạng thái không ảnh hưởng đến việc truy xuất.
     */
    @Test
    public void testDelete_TC004() {
        int deleted = dao.delete(Integer.toString(spTest.getMasp()));
        assertEquals(1, deleted);

        SanPhamDTO sp = dao.selectById(Integer.toString(spTest.getMasp()));
        assertNotNull(sp); // Xoá mềm => vẫn truy xuất được
    }

    /**
     * Test Case ID: TC005
     * Mục tiêu: Kiểm thử phương thức getAutoIncrement()
     * Input: Không có
     * Expected Output: Trả về số nguyên dương > 0
     * Ghi chú: Kiểm tra hệ thống lấy đúng ID tiếp theo.
     */
    @Test
    public void testGetAutoIncrement_TC005() {
        int id = dao.getAutoIncrement();
        assertTrue(id > 0);
    }

    /**
     * Test Case ID: TC006
     * Mục tiêu: Kiểm thử phương thức updateSoLuongTon()
     * Input: Mã sản phẩm 9999, số lượng tăng thêm 5
     * Expected Output: Số lượng tồn tăng thêm 5 đơn vị
     * Ghi chú: Đảm bảo hệ thống cập nhật đúng số lượng tồn kho.
     */
    @Test
    public void testUpdateSoLuongTon_TC006() {
        int original = dao.selectById(Integer.toString(spTest.getMasp())).getSoluongton();
        dao.updateSoLuongTon(spTest.getMasp(), 5);
        int updated = dao.selectById(Integer.toString(spTest.getMasp())).getSoluongton();
        assertEquals(original + 5, updated);
    }
}
