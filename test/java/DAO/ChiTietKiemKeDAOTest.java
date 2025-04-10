package DAO;

import DTO.ChiTietKiemKeDTO;
import config.JDBCUtil;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ChiTietKiemKeDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private ChiTietKiemKeDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        dao = ChiTietKiemKeDAO.getInstance();

        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    /**
     * Test Case: insert() - Thêm nhiều dòng thành công
     */
    @Test
    void testInsert_SuccessfulInsert() throws Exception {
        ArrayList<ChiTietKiemKeDTO> list = new ArrayList<>();
        list.add(new ChiTietKiemKeDTO(1, 2, 10, 0, "OK"));
        list.add(new ChiTietKiemKeDTO(1, 3, 5, 1, "Note"));

        try (MockedStatic<JDBCUtil> jdbcUtil = mockStatic(JDBCUtil.class)) {
            jdbcUtil.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.insert(list);

            System.out.println("testInsert_SuccessfulInsert: Insert thành công với " + result + " dòng bị ảnh hưởng");
            assertEquals(1, result);
            verify(mockPreparedStatement, times(2)).executeUpdate();
        }
    }

    /**
     * Test Case: insert() - Xảy ra SQLException
     */
    @Test
    void testInsert_SQLException() throws Exception {
        ArrayList<ChiTietKiemKeDTO> list = new ArrayList<>();
        list.add(new ChiTietKiemKeDTO(1, 2, 10, 0, "Test"));

        try (MockedStatic<JDBCUtil> jdbcUtil = mockStatic(JDBCUtil.class)) {
            jdbcUtil.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

            int result = dao.insert(list);

            System.out.println("testInsert_SQLException: Lỗi SQL xảy ra, kết quả insert = " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Test Case: delete() - Xoá thành công theo mã phiếu kiểm kê
     */
    @Test
    void testDelete_Success() throws Exception {
        try (MockedStatic<JDBCUtil> jdbcUtil = mockStatic(JDBCUtil.class)) {
            jdbcUtil.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.delete("1");

            System.out.println("testDelete_Success: Xoá thành công, kết quả = " + result);
            assertEquals(1, result);
            verify(mockPreparedStatement).setString(1, "1");
        }
    }

    /**
     * Test Case: delete() - Xảy ra SQLException
     */
    @Test
    void testDelete_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> jdbcUtil = mockStatic(JDBCUtil.class)) {
            jdbcUtil.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Delete failed"));

            int result = dao.delete("1");

            System.out.println("testDelete_SQLException: Gặp lỗi khi xoá, kết quả = " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Test Case: selectAll() - Truy vấn trả về danh sách chi tiết kiểm kê
     */
    @Test
    void testSelectAll_ReturnsList() throws Exception {
        try (MockedStatic<JDBCUtil> jdbcUtil = mockStatic(JDBCUtil.class)) {
            jdbcUtil.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("maphieukiemke")).thenReturn(1);
            when(mockResultSet.getInt("maphienban")).thenReturn(2);
            when(mockResultSet.getInt("soluong")).thenReturn(10);
            when(mockResultSet.getInt("chenhlech")).thenReturn(0);
            when(mockResultSet.getString("ghichu")).thenReturn("Test");

            List<ChiTietKiemKeDTO> list = dao.selectAll("1");

            System.out.println("testSelectAll_ReturnsList: Truy vấn trả về " + list.size() + " dòng");
            assertEquals(1, list.size());
            assertEquals("Test", list.get(0).getGhichu());
        }
    }

    /**
     * Test Case: selectAll() - Gặp SQLException
     */
    @Test
    void testSelectAll_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> jdbcUtil = mockStatic(JDBCUtil.class)) {
            jdbcUtil.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Query failed"));

            List<ChiTietKiemKeDTO> result = dao.selectAll("1");

            System.out.println("testSelectAll_SQLException: Truy vấn thất bại, danh sách trả về rỗng? " + result.isEmpty());
            assertTrue(result.isEmpty());
        }
    }

    /**
     * Test Case: update() - Không được hỗ trợ
     */
    @Test
    void testUpdate_NotImplemented() {
        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class,
                () -> dao.update(new ArrayList<>(), "pk"));

        System.out.println("testUpdate_NotImplemented: Đã bắt được exception như mong đợi -> " + ex.getMessage());
        assertEquals("Not supported yet.", ex.getMessage());
    }
}
