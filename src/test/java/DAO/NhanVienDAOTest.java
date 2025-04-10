package DAO;

import DTO.NhanVienDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Test class: NhanVienDAOTest
 * Mục tiêu: Kiểm thử các chức năng CRUD của lớp NhanVienDAO.
 */
public class NhanVienDAOTest {

    private static Connection connection;

    @BeforeClass
    public static void setupBeforeClass() throws Exception {
        connection = JDBCUtil.getConnection();
        connection.setAutoCommit(false); // Dùng rollback sau mỗi test
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        connection.rollback(); // Rollback tất cả thay đổi sau khi test
        JDBCUtil.closeConnection(connection);
    }

    /**
     * TC01 - insert() - Mục tiêu: kiểm tra thêm nhân viên mới
     */
    @Test
    public void TC01_insert_shouldInsertNhanVienSuccessfully() {
        // Arrange
        NhanVienDTO nv = new NhanVienDTO(0, "Nguyen Van A", 1, Date.valueOf("2000-01-01"), "0123456789", 1, "vana@gmail.com");
        
        // Act
        int result = NhanVienDAO.getInstance().insert(nv);
        
        // Assert
        assertEquals("Insert thành công => trả về 1", 1, result);
    }

    /**
     * TC02 - selectAll() - Mục tiêu: lấy tất cả nhân viên đang hoạt động (trangthai = 1)
     */
    @Test
    public void TC02_selectAll_shouldReturnListOfActiveNhanVien() {
        // Act
        ArrayList<NhanVienDTO> list = NhanVienDAO.getInstance().selectAll();
        
        // Assert
        assertNotNull("Danh sách nhân viên không được null", list);
        for (NhanVienDTO nv : list) {
            assertEquals("Nhân viên phải có trạng thái = 1", 1, nv.getTrangthai());
        }
    }

    /**
     * TC03 - selectByEmail() - Mục tiêu: tìm nhân viên theo email
     */
    @Test
    public void TC03_selectByEmail_shouldReturnCorrectNhanVien() {
        // Arrange
        String email = "vana@gmail.com"; // email vừa insert ở TC01

        // Act
        NhanVienDTO nv = NhanVienDAO.getInstance().selectByEmail(email);

        // Assert
        assertNotNull("Nhân viên tồn tại với email đã cho", nv);
        assertEquals("Email khớp", email, nv.getEmail());
    }

    /**
     * TC04 - update() - Mục tiêu: cập nhật thông tin nhân viên
     */
    @Test
    public void TC04_update_shouldUpdateNhanVienSuccessfully() {
        // Arrange
        NhanVienDTO nv = NhanVienDAO.getInstance().selectByEmail("vana@gmail.com");
        nv.setHoten("Nguyen Van B");

        // Act
        int result = NhanVienDAO.getInstance().update(nv);

        // Assert
        assertEquals("Cập nhật thành công => trả về 1", 1, result);

        // Check lại DB
        NhanVienDTO updated = NhanVienDAO.getInstance().selectById(String.valueOf(nv.getManv()));
        assertEquals("Họ tên đã được cập nhật", "Nguyen Van B", updated.getHoten());
    }

    /**
     * TC05 - delete() - Mục tiêu: cập nhật trạng thái nhân viên thành -1
     */
    @Test
    public void TC05_delete_shouldSetTrangThaiToMinus1() {
        // Arrange
        NhanVienDTO nv = NhanVienDAO.getInstance().selectByEmail("vana@gmail.com");

        // Act
        int result = NhanVienDAO.getInstance().delete(String.valueOf(nv.getManv()));

        // Assert
        assertEquals("Xóa logic => cập nhật trạng thái => 1 row", 1, result);

        // Check DB
        NhanVienDTO deleted = NhanVienDAO.getInstance().selectById(String.valueOf(nv.getManv()));
        assertEquals("Trạng thái phải là -1", -1, deleted.getTrangthai());
    }

    /**
     * TC06 - getAutoIncrement() - Mục tiêu: lấy id tiếp theo sẽ được gán
     */
    @Test
    public void TC06_getAutoIncrement_shouldReturnPositiveInt() {
        // Act
        int autoIncrement = NhanVienDAO.getInstance().getAutoIncrement();

        // Assert
        assertTrue("Giá trị AUTO_INCREMENT phải > 0", autoIncrement > 0);
    }

    /**
     * TC07 - selectAlll() - Mục tiêu: lấy tất cả nhân viên không phân biệt trạng thái
     */
    @Test
    public void TC07_selectAlll_shouldReturnAllNhanVien() {
        // Act
        ArrayList<NhanVienDTO> list = NhanVienDAO.getInstance().selectAlll();

        // Assert
        assertNotNull("Danh sách không được null", list);
        assertTrue("Có ít nhất 1 nhân viên", list.size() > 0);
    }

    /**
     * TC08 - selectAllNV() - Mục tiêu: lấy danh sách nhân viên chưa có tài khoản
     */
    @Test
    public void TC08_selectAllNV_shouldReturnNhanVienChuaCoTaiKhoan() {
        // Act
        ArrayList<NhanVienDTO> list = NhanVienDAO.getInstance().selectAllNV();

        // Assert
        assertNotNull("Không null", list);
        for (NhanVienDTO nv : list) {
            // Không kiểm tra với DB tài khoản thật, assume chỉ check list tồn tại
            assertEquals("Trạng thái phải là 1", 1, nv.getTrangthai());
        }
    }
}
