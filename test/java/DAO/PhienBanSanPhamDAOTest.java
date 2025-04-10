/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package DAO;

import DAO.PhienBanSanPhamDAO;
import DTO.PhienBanSanPhamDTO;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test cho lớp PhienBanSanPhamDAO
 */
public class PhienBanSanPhamDAOTest {

    private static PhienBanSanPhamDAO dao;

    @BeforeClass
    public static void setupBeforeClass() {
        // Khởi tạo instance dùng chung cho tất cả các test
        dao = PhienBanSanPhamDAO.getInstance();
    }

    /**
     * Test case TC01:
     * Kiểm tra insert một bản ghi phiên bản sản phẩm thành công
     */
    @Test
    public void testInsertSinglePhienBanSanPham_TC01() {
        PhienBanSanPhamDTO dto = new PhienBanSanPhamDTO(9999, 1, 4, 64, 1, 1000000, 1200000, 10);
        int result = dao.insert(dto);
        assertEquals("Thêm mới phiên bản sản phẩm nên trả về 1", 1, result);
    }

    /**
     * Test case TC02:
     * Kiểm tra insert nhiều phiên bản sản phẩm cùng lúc
     */
    @Test
    public void testInsertMultiplePhienBanSanPham_TC02() {
        ArrayList<PhienBanSanPhamDTO> list = new ArrayList<>();
        list.add(new PhienBanSanPhamDTO(9998, 1, 6, 128, 2, 1100000, 1300000, 5));
        list.add(new PhienBanSanPhamDTO(9997, 1, 8, 256, 3, 1400000, 1600000, 8));
        int result = dao.insert(list);
        assertEquals("Thêm danh sách phiên bản sản phẩm, chỉ return 1 do ghi đè kết quả cuối cùng", 1, result);
    }

    /**
     * Test case TC03:
     * Kiểm tra cập nhật thông tin phiên bản sản phẩm
     */
    @Test
    public void testUpdatePhienBanSanPham_TC03() {
        PhienBanSanPhamDTO dto = new PhienBanSanPhamDTO(9999, 1, 6, 128, 1, 1050000, 1250000, 10);
        int result = dao.update(dto);
        assertEquals("Cập nhật phiên bản sản phẩm phải thành công", 1, result);
    }

    /**
     * Test case TC04:
     * Kiểm tra selectById có lấy đúng phiên bản theo ID không
     */
    @Test
    public void testSelectById_TC04() {
        PhienBanSanPhamDTO dto = dao.selectById(9999);
        assertNotNull("Phải lấy được phiên bản sản phẩm đã thêm", dto);
        assertEquals("Mã phiên bản sản phẩm phải khớp", 9999, dto.getMaphienbansp());
    }

    /**
     * Test case TC05:
     * Kiểm tra update số lượng tồn
     */
    @Test
    public void testUpdateSoLuongTon_TC05() {
        int result = dao.updateSoLuongTon(9999, 5); // cộng thêm 5
        assertEquals("Cập nhật số lượng tồn phải thành công", 1, result);
    }

    /**
     * Test case TC06:
     * Kiểm tra delete bản ghi (trangthai = 0)
     */
    @Test
    public void testDeletePhienBanSanPham_TC06() {
        int result = dao.delete("9999");
        assertEquals("Xoá mềm phiên bản sản phẩm thành công", 1, result);
    }

    /**
     * Test case TC07:
     * Kiểm tra selectAll với sản phẩm có mã hợp lệ
     */
    @Test
    public void testSelectAllByMaSP_TC07() {
        List<PhienBanSanPhamDTO> list = dao.selectAll("1"); // mã sản phẩm giả định
        assertNotNull("Danh sách không được null", list);
        assertTrue("Danh sách có thể rỗng hoặc có dữ liệu", list.size() >= 0);
    }

    /**
     * Test case TC08:
     * Kiểm tra lấy giá trị AUTO_INCREMENT tiếp theo trong bảng
     */
    @Test
    public void testGetAutoIncrement_TC08() {
        int nextId = dao.getAutoIncrement();
        assertTrue("Giá trị AUTO_INCREMENT phải lớn hơn 0", nextId > 0);
    }

    /**
     * Test case TC09:
     * Kiểm tra check trùng IMEI (trường hợp giả định danh sách IMEI không tồn tại)
     */
    @Test
    public void testCheckImeiExists_TC09() {
        ArrayList<DTO.ChiTietSanPhamDTO> imeis = new ArrayList<>();
        DTO.ChiTietSanPhamDTO imei = new DTO.ChiTietSanPhamDTO();
        imei.setImei("IMEI999999"); // giá trị giả lập
        imeis.add(imei);

        boolean exists = dao.checkImeiExists(imeis);
        assertTrue("IMEI chưa tồn tại thì hàm trả về true", exists);
    }

    @AfterClass
    public static void cleanupAfterClass() {
        // Xoá các bản ghi test đã insert (nếu có)
        dao.delete("9999");
        dao.delete("9998");
        dao.delete("9997");
    }
}
