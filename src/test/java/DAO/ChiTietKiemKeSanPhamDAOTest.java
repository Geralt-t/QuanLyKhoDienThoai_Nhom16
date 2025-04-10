package DAO;

import DTO.ChiTietKiemKeSanPhamDTO;
import config.JDBCUtil;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChiTietKiemKeSanPhamDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ChiTietKiemKeSanPhamDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        dao = ChiTietKiemKeSanPhamDAO.getInstance();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
    }

    /**
     * Test case: insert() - Thêm thành công nhiều bản ghi vào bảng ctkiemke_sanpham
     */
    @Test
    void testInsert_Successful() throws Exception {
        ArrayList<ChiTietKiemKeSanPhamDTO> list = new ArrayList<>();
        list.add(new ChiTietKiemKeSanPhamDTO(1, 2, "IMEI001", 0));
        list.add(new ChiTietKiemKeSanPhamDTO(1, 2, "IMEI002", 1));

        try (MockedStatic<JDBCUtil> mocked = mockStatic(JDBCUtil.class)) {
            mocked.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.insert(list);

            System.out.println("Số dòng đã insert: " + result);
            assertEquals(2, result);
            verify(mockPreparedStatement, times(2)).executeUpdate();
        }
    }

    /**
     * Test case: insert() - Xảy ra SQLException trong quá trình thêm dữ liệu
     */
    @Test
    void testInsert_SQLException() throws Exception {
        ArrayList<ChiTietKiemKeSanPhamDTO> list = new ArrayList<>();
        list.add(new ChiTietKiemKeSanPhamDTO(1, 2, "IMEI003", 1));

        try (MockedStatic<JDBCUtil> mocked = mockStatic(JDBCUtil.class)) {
            mocked.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Insert error"));

            int result = dao.insert(list);

            System.out.println("Kết quả khi có lỗi SQL: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Test case: delete() - Xoá thành công bản ghi theo maphieukiemke
     */
    @Test
    void testDelete_Successful() throws Exception {
        try (MockedStatic<JDBCUtil> mocked = mockStatic(JDBCUtil.class)) {
            mocked.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.delete("1");

            System.out.println("Kết quả xoá: " + result);
            assertEquals(1, result);
            verify(mockPreparedStatement).setString(1, "1");
        }
    }

    /**
     * Test case: delete() - Xảy ra SQLException khi xóa
     */
    @Test
    void testDelete_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> mocked = mockStatic(JDBCUtil.class)) {
            mocked.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Delete failed"));

            int result = dao.delete("1");

            System.out.println("Kết quả xoá khi lỗi SQL: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Test case: update() - Gọi update() chưa được hỗ trợ
     */
    @Test
    void testUpdate_Unsupported() {
        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class,
                () -> dao.update(new ArrayList<>(), "pk"));
        System.out.println("Thông báo lỗi update: " + ex.getMessage());
        assertEquals("Not supported yet.", ex.getMessage());
    }

    /**
     * Test case: selectAll() - Chưa hỗ trợ, ném UnsupportedOperationException
     */
    @Test
    void testSelectAll_Unsupported() {
        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class,
                () -> dao.selectAll("1"));
        System.out.println("Thông báo lỗi selectAll: " + ex.getMessage());
        assertEquals("Not supported yet.", ex.getMessage());
    }
}
