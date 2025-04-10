package DAO;

import DTO.KhuVucKhoDTO;
import config.JDBCUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KhuVucKhoDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private KhuVucKhoDAO dao;

    @BeforeEach
    void setUp() {
        dao = KhuVucKhoDAO.getInstance();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    /**
     * Kiểm thử insert(): thêm khu vực kho thành công
     */
    @Test
    void testInsert_Success() throws Exception {
        KhuVucKhoDTO dto = new KhuVucKhoDTO(1, "KV A", "OK");

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
     * Kiểm thử insert(): lỗi SQL khi thêm khu vực kho
     */
    @Test
    void testInsert_SQLException() throws Exception {
        KhuVucKhoDTO dto = new KhuVucKhoDTO(1, "KV B", "Note");

        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Insert failed"));

            int result = dao.insert(dto);

            System.out.println("Insert exception result: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Kiểm thử update(): cập nhật khu vực kho thành công
     */
    @Test
    void testUpdate_Success() throws Exception {
        KhuVucKhoDTO dto = new KhuVucKhoDTO(1, "KV C", "OK");

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
     * Kiểm thử update(): lỗi SQL khi cập nhật
     */
    @Test
    void testUpdate_SQLException() throws Exception {
        KhuVucKhoDTO dto = new KhuVucKhoDTO(1, "KV D", "Note");

        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Update failed"));

            int result = dao.update(dto);

            System.out.println("Update exception result: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Kiểm thử delete(): xoá khu vực kho thành công
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
     * Kiểm thử delete(): lỗi SQL khi xoá khu vực kho
     */
    @Test
    void testDelete_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Delete failed"));

            int result = dao.delete("1");

            System.out.println("Delete exception result: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Kiểm thử selectAll(): truy vấn tất cả khu vực kho thành công
     */
    @Test
    void testSelectAll_Success() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("makhuvuc")).thenReturn(1);
            when(mockResultSet.getString("tenkhuvuc")).thenReturn("KV A");

            ArrayList<KhuVucKhoDTO> result = dao.selectAll();

            System.out.println("SelectAll success result size: " + result.size());
            assertEquals(1, result.size());
        }
    }

    /**
     * Kiểm thử selectAll(): lỗi SQL khi truy vấn
     */
    @Test
    void testSelectAll_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Select failed"));

            ArrayList<KhuVucKhoDTO> result = dao.selectAll();

            System.out.println("SelectAll exception result size: " + result.size());
            assertTrue(result.isEmpty());
        }
    }

    /**
     * Kiểm thử selectById(): truy vấn khu vực kho theo mã thành công
     */
    @Test
    void testSelectById_Success() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("makhuvuc")).thenReturn(1);
            when(mockResultSet.getString("tenkhuvuc")).thenReturn("KV A");

            KhuVucKhoDTO dto = dao.selectById("1");

            System.out.println("SelectById success result: " + dto.getTenkhuvuc());
            assertNotNull(dto);
            assertEquals("KV A", dto.getTenkhuvuc());
        }
    }

    /**
     * Kiểm thử selectById(): lỗi SQL khi truy vấn theo mã
     */
    @Test
    void testSelectById_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("SelectById error"));

            KhuVucKhoDTO dto = dao.selectById("1");

            System.out.println("SelectById exception result: " + dto);
            assertNull(dto);
        }
    }

    /**
     * Kiểm thử getAutoIncrement(): lấy AUTO_INCREMENT thành công
     */
    @Test
    void testGetAutoIncrement_Success() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.isBeforeFirst()).thenReturn(true);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("AUTO_INCREMENT")).thenReturn(9);

            int result = dao.getAutoIncrement();

            System.out.println("AutoIncrement success result: " + result);
            assertEquals(9, result);
        }
    }

    /**
     * Kiểm thử getAutoIncrement(): lỗi SQL khi truy vấn AUTO_INCREMENT
     */
    @Test
    void testGetAutoIncrement_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("AutoIncrement error"));

            int result = dao.getAutoIncrement();

            System.out.println("AutoIncrement exception result: " + result);
            assertEquals(-1, result);
        }
    }
}
