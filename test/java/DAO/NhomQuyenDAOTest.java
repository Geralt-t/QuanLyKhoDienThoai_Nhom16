package DAO;

import DTO.NhomQuyenDTO;
import config.JDBCUtil;
import org.junit.*;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for NhomQuyenDAO
 * Framework: JUnit 4.13.2
 */
public class NhomQuyenDAOTest {

    private static Connection conn;
    private static NhomQuyenDAO dao;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        conn = JDBCUtil.getConnection();
        conn.setAutoCommit(false); // rollback sau mỗi test
        dao = NhomQuyenDAO.getInstance();
    }

    @After
    public void rollbackAfterTest() throws Exception {
        conn.rollback();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        conn.setAutoCommit(true);
        JDBCUtil.closeConnection(conn);
    }

    /**
     * TC01 - insert: Thêm nhóm quyền mới
     */
    @Test
    public void testInsert_TC01() {
        NhomQuyenDTO dto = new NhomQuyenDTO();
        dto.setTennhomquyen("Quản lý test");

        int result = dao.insert(dto);
        assertEquals("Thêm mới nhóm quyền thất bại", 1, result);
    }

    /**
     * TC02 - update: Cập nhật tên nhóm quyền
     */
    @Test
    public void testUpdate_TC02() {
        // Thêm mới để có dữ liệu cập nhật
        NhomQuyenDTO dto = new NhomQuyenDTO();
        dto.setTennhomquyen("Nhóm cập nhật");
        dao.insert(dto);

        int idMoi = dao.getAutoIncrement() - 1;
        dto.setManhomquyen(idMoi);
        dto.setTennhomquyen("Đã cập nhật");

        int result = dao.update(dto);
        assertEquals("Cập nhật nhóm quyền thất bại", 1, result);
    }

    /**
     * TC03 - selectById: Trả về đúng đối tượng theo mã
     */
    @Test
    public void testSelectById_TC03() {
        NhomQuyenDTO dto = new NhomQuyenDTO(9998,"Nhóm quyền kiểm tra ID" );
        dao.insert(dto);
        NhomQuyenDTO result = dao.selectById(String.valueOf(9998));

        assertNotNull("Không tìm thấy nhóm quyền", result);
        assertEquals("Tên không khớp", "Nhóm quyền kiểm tra ID", result.getTennhomquyen());
    }

    /**
     * TC04 - selectAll: Danh sách nhóm quyền đang hoạt động
     */
    @Test
    public void testSelectAll_TC04() {
        NhomQuyenDTO dto = new NhomQuyenDTO();
        dto.setTennhomquyen("Nhóm quyền hoạt động");
        dao.insert(dto);

        List<NhomQuyenDTO> list = dao.selectAll();

        assertNotNull("Danh sách null", list);
        assertTrue("Danh sách rỗng", list.size() > 0);
    }

    /**
     * TC05 - delete: Cập nhật trạng thái về 0
     */
    @Test
    public void testDelete_TC05() {
        NhomQuyenDTO dto = new NhomQuyenDTO(9999,"Nhóm quyền kiểm tra ID" );
        dao.insert(dto);
        int result = dao.delete(String.valueOf(9999));
        assertEquals("Xóa mềm thất bại", 1, result);

        // Kiểm tra trạng thái
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT trangthai FROM nhomquyen WHERE manhomquyen = ?");
            ps.setInt(1, 9999);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int status = rs.getInt("trangthai");
                assertEquals("Trạng thái không phải 0 sau khi xóa", 0, status);
            } else {
                fail("Không tìm thấy nhóm quyền trong DB");
            }
        } catch (SQLException e) {
            fail("Lỗi khi truy vấn trạng thái: " + e.getMessage());
        }
    }

    /**
     * TC06 - getAutoIncrement: ID tiếp theo phải > 0
     */
    @Test
    public void testGetAutoIncrement_TC06() {
        int autoInc = dao.getAutoIncrement();
        assertTrue("ID không hợp lệ", autoInc > 0);
    }
}
