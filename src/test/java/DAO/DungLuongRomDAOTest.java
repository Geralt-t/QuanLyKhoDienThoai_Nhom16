package DAO;

import DTO.ThuocTinhSanPham.DungLuongRomDTO;
import config.JDBCUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DungLuongRomDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private DungLuongRomDAO dao;

    @BeforeEach
    void setUp() {
        dao = DungLuongRomDAO.getInstance();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    /**
     * Kiểm thử insert(): thêm thành công
     */
    @Test
    void testInsert_Success() throws Exception {
        DungLuongRomDTO dto = new DungLuongRomDTO(1, 128);

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
        DungLuongRomDTO dto = new DungLuongRomDTO(1, 128);

        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Insert error"));

            int result = dao.insert(dto);

            System.out.println("Insert SQL exception result: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Kiểm thử update(): cập nhật thành công
     */
    @Test
    void testUpdate_Success() throws Exception {
        DungLuongRomDTO dto = new DungLuongRomDTO(1, 256);

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
     * Kiểm thử update(): lỗi SQL
     */
    @Test
    void testUpdate_SQLException() throws Exception {
        DungLuongRomDTO dto = new DungLuongRomDTO(1, 256);

        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Update error"));

            int result = dao.update(dto);

            System.out.println("Update SQL exception result: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Kiểm thử delete(): xoá thành công
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
     * Kiểm thử delete(): lỗi SQL
     */
    @Test
    void testDelete_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Delete error"));

            int result = dao.delete("1");

            System.out.println("Delete SQL exception result: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Kiểm thử selectAll(): truy vấn danh sách thành công
     */
    @Test
    void testSelectAll_Success() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("madlrom")).thenReturn(1);
            when(mockResultSet.getInt("kichthuocrom")).thenReturn(128);

            ArrayList<DungLuongRomDTO> result = dao.selectAll();

            System.out.println("SelectAll success result size: " + result.size());
            assertEquals(1, result.size());
        }
    }

    /**
     * Kiểm thử selectAll(): lỗi SQL
     */
    @Test
    void testSelectAll_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("SelectAll error"));

            ArrayList<DungLuongRomDTO> result = dao.selectAll();

            System.out.println("SelectAll SQL exception result size: " + result.size());
            assertTrue(result.isEmpty());
        }
    }

    /**
     * Kiểm thử selectById(): truy vấn một dòng thành công
     */
    @Test
    void testSelectById_Success() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("madlrom")).thenReturn(1);
            when(mockResultSet.getInt("kichthuocrom")).thenReturn(512);

            DungLuongRomDTO dto = dao.selectById("1");

            System.out.println("SelectById success result: " + dto.getDungluongrom());
            assertNotNull(dto);
            assertEquals(512, dto.getDungluongrom());
        }
    }

    /**
     * Kiểm thử selectById(): lỗi SQL
     */
    @Test
    void testSelectById_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("SelectById error"));

            DungLuongRomDTO dto = dao.selectById("1");

            System.out.println("SelectById SQL exception result: " + dto);
            assertNull(dto);
        }
    }

    /**
     * Kiểm thử getAutoIncrement(): trả về giá trị AUTO_INCREMENT
     */
    @Test
    void testGetAutoIncrement_Success() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.isBeforeFirst()).thenReturn(true);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("AUTO_INCREMENT")).thenReturn(7);

            int autoInc = dao.getAutoIncrement();

            System.out.println("AutoIncrement success result: " + autoInc);
            assertEquals(7, autoInc);
        }
    }

    /**
     * Kiểm thử getAutoIncrement(): lỗi SQL
     */
    @Test
    void testGetAutoIncrement_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> jdbc = mockStatic(JDBCUtil.class)) {
            jdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("AutoIncrement error"));

            int result = dao.getAutoIncrement();

            System.out.println("AutoIncrement SQL exception result: " + result);
            assertEquals(-1, result);
        }
    }
}
