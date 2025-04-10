package DAO;

import DTO.ChiTietQuyenDTO;
import config.JDBCUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChiTietQuyenDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private ChiTietQuyenDAO dao;

    @BeforeEach
    void setUp() {
        dao = ChiTietQuyenDAO.getInstance();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    /**
     * Kiểm thử insert(): thêm bản ghi thành công
     */
    @Test
    void testInsert_Success() throws Exception {
        ArrayList<ChiTietQuyenDTO> list = new ArrayList<>();
        list.add(new ChiTietQuyenDTO(1, "CN001", "READ"));
        list.add(new ChiTietQuyenDTO(1, "CN002", "WRITE"));

        try (MockedStatic<JDBCUtil> jdbcMock = mockStatic(JDBCUtil.class)) {
            jdbcMock.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.insert(list);

            System.out.println("Insert result: " + result);
            assertEquals(1, result);
            verify(mockPreparedStatement, times(2)).executeUpdate();
        }
    }

    /**
     * Kiểm thử insert(): xảy ra SQLException
     */
    @Test
    void testInsert_SQLException() throws Exception {
        ArrayList<ChiTietQuyenDTO> list = new ArrayList<>();
        list.add(new ChiTietQuyenDTO(1, "CN001", "READ"));

        try (MockedStatic<JDBCUtil> jdbcMock = mockStatic(JDBCUtil.class)) {
            jdbcMock.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Insert error"));

            int result = dao.insert(list);

            System.out.println("Insert exception result: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Kiểm thử delete(): xoá thành công theo mã quyền
     */
    @Test
    void testDelete_Success() throws Exception {
        try (MockedStatic<JDBCUtil> jdbcMock = mockStatic(JDBCUtil.class)) {
            jdbcMock.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.delete("1");

            System.out.println("Delete result: " + result);
            assertEquals(1, result);
        }
    }

    /**
     * Kiểm thử delete(): lỗi SQL khi xoá
     */
    @Test
    void testDelete_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> jdbcMock = mockStatic(JDBCUtil.class)) {
            jdbcMock.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Delete error"));

            int result = dao.delete("1");

            System.out.println("Delete exception result: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Kiểm thử update(): cập nhật quyền thành công
     */
    @Test
    void testUpdate_Success() throws Exception {
        ArrayList<ChiTietQuyenDTO> list = new ArrayList<>();
        list.add(new ChiTietQuyenDTO(1, "CN001", "READ"));

        try (MockedStatic<JDBCUtil> jdbcMock = mockStatic(JDBCUtil.class)) {
            jdbcMock.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.update(list, "1");

            System.out.println("Update result: " + result);
            assertEquals(1, result);
        }
    }

    /**
     * Kiểm thử selectAll(): truy vấn thành công trả về danh sách quyền
     */
    @Test
    void testSelectAll_Success() throws Exception {
        try (MockedStatic<JDBCUtil> jdbcMock = mockStatic(JDBCUtil.class)) {
            jdbcMock.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("manhomquyen")).thenReturn(1);
            when(mockResultSet.getString("machucnang")).thenReturn("CN001");
            when(mockResultSet.getString("hanhdong")).thenReturn("READ");

            List<ChiTietQuyenDTO> result = dao.selectAll("1");

            System.out.println("SelectAll result size: " + result.size());
            assertEquals(1, result.size());
        }
    }

    /**
     * Kiểm thử selectAll(): lỗi SQL khi truy vấn
     */
    @Test
    void testSelectAll_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> jdbcMock = mockStatic(JDBCUtil.class)) {
            jdbcMock.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Select error"));

            List<ChiTietQuyenDTO> result = dao.selectAll("1");

            System.out.println("SelectAll exception result size: " + result.size());
            assertTrue(result.isEmpty());
        }
    }
}
