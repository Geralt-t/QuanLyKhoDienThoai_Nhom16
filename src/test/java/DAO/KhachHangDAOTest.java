package DAO;

import DTO.KhachHangDTO;
import config.JDBCUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KhachHangDAOTest {
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private KhachHangDAO dao;

    @BeforeEach
    void setUp() {
        dao = KhachHangDAO.getInstance();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    /**
     * Kiểm thử insert(): thêm khách hàng thành công
     */
    @Test
    void testInsert_Success() throws Exception {
        KhachHangDTO dto = new KhachHangDTO(1, "Nguyen Van A", "0123456789", "Hanoi", new Date());

        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.insert(dto);
            System.out.println("Insert success result: " + result);
            assertEquals(1, result);
        }
    }

    /**
     * Kiểm thử insert(): lỗi SQL
     */
    @Test
    void testInsert_SQLException() throws Exception {
        KhachHangDTO dto = new KhachHangDTO(1, "Nguyen Van A", "0123456789", "Hanoi", new Date());

        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Insert error"));

            int result = dao.insert(dto);
            System.out.println("Insert exception result: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Kiểm thử update(): cập nhật thông tin khách hàng thành công
     */
    @Test
    void testUpdate_Success() throws Exception {
        KhachHangDTO dto = new KhachHangDTO(1, "Nguyen Van B", "0987654321", "Saigon", new Date());

        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.update(dto);
            System.out.println("Update success result: " + result);
            assertEquals(1, result);
        }
    }

    /**
     * Kiểm thử delete(): xoá mềm khách hàng thành công
     */
    @Test
    void testDelete_Success() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.delete("1");
            System.out.println("Delete success result: " + result);
            assertEquals(1, result);
        }
    }

    /**
     * Kiểm thử selectAll(): truy vấn danh sách khách hàng thành công
     */
    @Test
    void testSelectAll_Success() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("makh")).thenReturn(1);
            when(mockResultSet.getString("tenkhachhang")).thenReturn("Nguyen Van A");
            when(mockResultSet.getString("diachi")).thenReturn("Hanoi");
            when(mockResultSet.getString("sdt")).thenReturn("0123456789");
            when(mockResultSet.getDate("ngaythamgia")).thenReturn(new java.sql.Date(System.currentTimeMillis()));

            ArrayList<KhachHangDTO> result = dao.selectAll();
            System.out.println("SelectAll result size: " + result.size());
            assertEquals(1, result.size());
        }
    }

    /**
     * Kiểm thử selectById(): truy vấn theo mã khách hàng thành công
     */
    @Test
    void testSelectById_Success() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("makh")).thenReturn(1);
            when(mockResultSet.getString("tenkhachhang")).thenReturn("Nguyen Van A");
            when(mockResultSet.getString("diachi")).thenReturn("Hanoi");
            when(mockResultSet.getString("sdt")).thenReturn("0123456789");
            when(mockResultSet.getDate("ngaythamgia")).thenReturn(new java.sql.Date(System.currentTimeMillis()));

            KhachHangDTO dto = dao.selectById("1");
            System.out.println("SelectById result: " + (dto != null ? dto.getHoten() : "null"));
            assertNotNull(dto);
        }
    }

    /**
     * Kiểm thử getAutoIncrement(): lấy giá trị AUTO_INCREMENT thành công
     */
    @Test
    void testGetAutoIncrement_Success() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.isBeforeFirst()).thenReturn(true);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("AUTO_INCREMENT")).thenReturn(99);

            int autoInc = dao.getAutoIncrement();
            System.out.println("AutoIncrement result: " + autoInc);
            assertEquals(99, autoInc);
        }
    }

    /**
     * Kiểm thử getAutoIncrement(): lỗi SQL
     */
    @Test
    void testGetAutoIncrement_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("AutoInc error"));

            int autoInc = dao.getAutoIncrement();
            System.out.println("AutoIncrement exception result: " + autoInc);
            assertEquals(-1, autoInc);
        }
    }
}
