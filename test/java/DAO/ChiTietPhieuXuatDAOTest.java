package DAO;

import DTO.ChiTietPhieuDTO;
import config.JDBCUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChiTietPhieuXuatDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private ChiTietPhieuXuatDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        dao = ChiTietPhieuXuatDAO.getInstance();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    /**
     * Kiểm thử insert(): thêm bản ghi thành công
     */
    @Test
    void testInsert_Success() throws Exception {
        ArrayList<ChiTietPhieuDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuDTO(1, 101, 5, 1000));
        list.add(new ChiTietPhieuDTO(1, 102, 10, 2000));

        try (MockedStatic<JDBCUtil> jdbcUtil = mockStatic(JDBCUtil.class);
             MockedStatic<PhienBanSanPhamDAO> pbspDaoMock = mockStatic(PhienBanSanPhamDAO.class)) {

            jdbcUtil.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            PhienBanSanPhamDAO mockPbspDao = mock(PhienBanSanPhamDAO.class);
            pbspDaoMock.when(PhienBanSanPhamDAO::getInstance).thenReturn(mockPbspDao);
            when(mockPbspDao.updateSoLuongTon(anyInt(), anyInt())).thenReturn(1);

            int result = dao.insert(list);

            System.out.println("Insert result: " + result);
            assertEquals(1, result);
        }
    }

    /**
     * Kiểm thử insert(): xảy ra SQLException
     */
    @Test
    void testInsert_SQLException() throws Exception {
        ArrayList<ChiTietPhieuDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuDTO(1, 101, 5, 1000));

        try (MockedStatic<JDBCUtil> jdbcUtil = mockStatic(JDBCUtil.class)) {
            jdbcUtil.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Insert failed"));

            int result = dao.insert(list);

            System.out.println("Insert exception result: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Kiểm thử delete(): xoá bản ghi thành công theo mã phiếu
     */
    @Test
    void testDelete_Success() throws Exception {
        try (MockedStatic<JDBCUtil> jdbcUtil = mockStatic(JDBCUtil.class)) {
            jdbcUtil.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.delete("1");

            System.out.println("Delete result: " + result);
            assertEquals(1, result);
        }
    }

    /**
     * Kiểm thử delete(): lỗi SQL xảy ra khi xoá
     */
    @Test
    void testDelete_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> jdbcUtil = mockStatic(JDBCUtil.class)) {
            jdbcUtil.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Delete failed"));

            int result = dao.delete("1");

            System.out.println("Delete exception result: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Kiểm thử update(): cập nhật số lượng thành công
     */
    @Test
    void testUpdate_Success() throws Exception {
        ArrayList<ChiTietPhieuDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuDTO(1, 101, 5, 1000));

        try (MockedStatic<JDBCUtil> jdbcUtil = mockStatic(JDBCUtil.class);
             MockedStatic<PhienBanSanPhamDAO> pbspDaoMock = mockStatic(PhienBanSanPhamDAO.class)) {

            jdbcUtil.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            PhienBanSanPhamDAO mockPbspDao = mock(PhienBanSanPhamDAO.class);
            pbspDaoMock.when(PhienBanSanPhamDAO::getInstance).thenReturn(mockPbspDao);
            when(mockPbspDao.updateSoLuongTon(anyInt(), anyInt())).thenReturn(1);

            int result = dao.update(list, "1");

            System.out.println("Update result: " + result);
            assertEquals(1, result);
        }
    }

    /**
     * Kiểm thử selectAll(): truy vấn thành công danh sách chi tiết phiếu xuất
     */
    @Test
    void testSelectAll_Success() throws Exception {
        try (MockedStatic<JDBCUtil> jdbcUtil = mockStatic(JDBCUtil.class)) {
            jdbcUtil.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("maphieuxuat")).thenReturn(1);
            when(mockResultSet.getInt("maphienbansp")).thenReturn(101);
            when(mockResultSet.getInt("soluong")).thenReturn(5);
            when(mockResultSet.getInt("dongia")).thenReturn(1000);

            List<ChiTietPhieuDTO> result = dao.selectAll("1");

            System.out.println("SelectAll result size: " + result.size());
            assertEquals(1, result.size());
        }
    }

    /**
     * Kiểm thử selectAll(): xảy ra SQLException khi truy vấn
     */
    @Test
    void testSelectAll_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> jdbcUtil = mockStatic(JDBCUtil.class)) {
            jdbcUtil.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Query error"));

            List<ChiTietPhieuDTO> result = dao.selectAll("1");

            System.out.println("SelectAll exception result size: " + result.size());
            assertTrue(result.isEmpty());
        }
    }
}
