package DAO;

import DTO.DanhMucChucNangDTO;
import config.JDBCUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DanhMucChucNangDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private DanhMucChucNangDAO dao;

    @BeforeEach
    void setUp() {
        dao = DanhMucChucNangDAO.getInstance();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    /**
     * Kiểm thử selectAll(): truy vấn tất cả danh mục chức năng thành công
     */
    @Test
    void testSelectAll_Success() throws Exception {
        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {

            // Giả lập hành vi của JDBCUtil
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            // Trả về một dòng dữ liệu
            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getString("machucnang")).thenReturn("CN001");
            when(mockResultSet.getString("tenchucnang")).thenReturn("Quản lý người dùng");

            List<DanhMucChucNangDTO> result = dao.selectAll();

            System.out.println("SelectAll result size: " + result.size());
            assertEquals(1, result.size());
            assertEquals("CN001", result.get(0).getMachucnang());
        }
    }

    /**
     * Kiểm thử selectAll(): lỗi SQL xảy ra
     */
    @Test
    void testSelectAll_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {

            // Giả lập hành vi lỗi khi truy vấn
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("SQL error"));

            List<DanhMucChucNangDTO> result = dao.selectAll();

            System.out.println("SelectAll exception result size: " + result.size());
            assertTrue(result.isEmpty());
        }
    }
}
