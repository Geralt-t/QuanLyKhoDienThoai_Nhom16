package DAO;

import DTO.ChiTietSanPhamDTO;
import config.JDBCUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChiTietSanPhamDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private ChiTietSanPhamDAO dao;

    @BeforeEach
    void setUp() {
        dao = ChiTietSanPhamDAO.getInstance();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    /**
     * Kiểm thử insert(): thêm bản ghi thành công
     */
    @Test
    void testInsert_Success() throws Exception {
        ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO("IMEI123", 3, 1, 2, 0);

        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.insert(dto);

            System.out.println("Insert result: " + result);
            assertEquals(1, result);
        }
    }

    /**
     * Kiểm thử insert(): xảy ra SQLException
     */
    @Test
    void testInsert_SQLException() throws Exception {
        ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO("IMEI123", 3, 1, 2, 0);

        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Insert failed"));

            int result = dao.insert(dto);

            System.out.println("Insert exception result: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Kiểm thử insertMultiple(): thêm nhiều bản ghi thành công
     */
    @Test
    void testInsertMultiple_Success() throws Exception {
        ArrayList<ChiTietSanPhamDTO> list = new ArrayList<>();
        list.add(new ChiTietSanPhamDTO("IMEI001", 3, 1, 2, 0));
        list.add(new ChiTietSanPhamDTO("IMEI002", 3, 1, 2, 0));

        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.insert_mutiple(list);

            System.out.println("Insert multiple result: " + result);
            assertEquals(2, result);
        }
    }

    /**
     * Kiểm thử update(): cập nhật thông tin sản phẩm thành công
     */
    @Test
    void testUpdate_Success() throws Exception {
        ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO("IMEI123", 1, 2, 3, 1);

        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.update(dto);

            System.out.println("Update result: " + result);
            assertEquals(1, result);
        }
    }

    /**
     * Kiểm thử delete(): xoá theo mã chi tiết sản phẩm thành công
     */
    @Test
    void testDelete_Success() throws Exception {
        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.delete("IMEI123");

            System.out.println("Delete result: " + result);
            assertEquals(1, result);
        }
    }

    /**
     * Kiểm thử selectAll(): truy vấn thành công danh sách chi tiết sản phẩm
     */
    @Test
    void testSelectAll_Success() throws Exception {
        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getString("maimei")).thenReturn("IMEI123");
            when(mockResultSet.getInt("phienbansp")).thenReturn(1);
            when(mockResultSet.getInt("maphieunhap")).thenReturn(2);
            when(mockResultSet.getInt("maphieuxuat")).thenReturn(3);
            when(mockResultSet.getInt("tinhtrang")).thenReturn(1);

            List<ChiTietSanPhamDTO> result = dao.selectAll();

            System.out.println("SelectAll result size: " + result.size());
            assertEquals(1, result.size());
        }
    }

    /**
     * Kiểm thử selectAll(): xảy ra SQLException khi truy vấn
     */
    @Test
    void testSelectAll_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Select failed"));

            List<ChiTietSanPhamDTO> result = dao.selectAll();

            System.out.println("SelectAll exception result size: " + result.size());
            assertTrue(result.isEmpty());
        }
    }
}
