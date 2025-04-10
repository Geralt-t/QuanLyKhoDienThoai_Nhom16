package DAO;

import DTO.ChiTietPhieuNhapDTO;
import config.JDBCUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChiTietPhieuNhapDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private ChiTietPhieuNhapDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        dao = ChiTietPhieuNhapDAO.getInstance();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    /**
     * Kiểm thử insert(): thêm một bản ghi thành công
     */
    @Test
    void testInsert_Success() throws Exception {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(1, 101, 202, 5, 1000));
        list.add(new ChiTietPhieuNhapDTO(2, 102, 203, 10, 2000));

        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            // Mock PhienBanSanPhamDAO static call nếu cần
            try (MockedStatic<PhienBanSanPhamDAO> mockedPBSPDAO = mockStatic(PhienBanSanPhamDAO.class)) {
                PhienBanSanPhamDAO mockPbspDao = mock(PhienBanSanPhamDAO.class);
                mockedPBSPDAO.when(PhienBanSanPhamDAO::getInstance).thenReturn(mockPbspDao);
                when(mockPbspDao.updateSoLuongTon(anyInt(), anyInt())).thenReturn(1);

                int result = dao.insert(list);

                System.out.println("Insert result: " + result);
                assertEquals(1, result); // do dòng cuối sẽ ghi đè kết quả
                verify(mockPreparedStatement, times(2)).executeUpdate();
            }
        }
    }

    /**
     * Kiểm thử insert(): xảy ra lỗi SQL
     */
    @Test
    void testInsert_SQLException() throws Exception {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(1, 101, 202, 5, 1000));

        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("SQL error"));

            int result = dao.insert(list);

            System.out.println("Insert exception result: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Kiểm thử delete(): xoá thành công theo mã phiếu
     */
    @Test
    void testDelete_Success() throws Exception {
        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            int result = dao.delete("1");

            System.out.println("Delete result: " + result);
            assertEquals(1, result);
        }
    }

    /**
     * Kiểm thử delete(): lỗi SQL khi thực hiện xoá
     */
    @Test
    void testDelete_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("SQL error"));

            int result = dao.delete("1");

            System.out.println("Delete exception result: " + result);
            assertEquals(0, result);
        }
    }

    /**
     * Kiểm thử update(): cập nhật bản ghi thành công
     */
    @Test
    void testUpdate_Success() throws Exception {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(new ChiTietPhieuNhapDTO(1, 101, 202, 5, 1000));

        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            // Mock PhienBanSanPhamDAO để test insert
            try (MockedStatic<PhienBanSanPhamDAO> mockedPBSPDAO = mockStatic(PhienBanSanPhamDAO.class)) {
                PhienBanSanPhamDAO mockPbspDao = mock(PhienBanSanPhamDAO.class);
                mockedPBSPDAO.when(PhienBanSanPhamDAO::getInstance).thenReturn(mockPbspDao);
                when(mockPbspDao.updateSoLuongTon(anyInt(), anyInt())).thenReturn(1);

                int result = dao.update(list, "1");

                System.out.println("Update result: " + result);
                assertEquals(1, result);
            }
        }
    }

    /**
     * Kiểm thử selectAll(): truy vấn danh sách bản ghi chi tiết phiếu nhập
     */
    @Test
    void testSelectAll_Success() throws Exception {
        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("maphieunhap")).thenReturn(1);
            when(mockResultSet.getInt("maphienbansp")).thenReturn(101);
            when(mockResultSet.getInt("dongia")).thenReturn(1000);
            when(mockResultSet.getInt("soluong")).thenReturn(5);
            when(mockResultSet.getInt("hinhthucnhap")).thenReturn(1);

            List<ChiTietPhieuNhapDTO> list = dao.selectAll("1");

            System.out.println("SelectAll result size: " + list.size());
            assertEquals(1, list.size());
        }
    }

    /**
     * Kiểm thử selectAll(): lỗi SQL xảy ra
     */
    @Test
    void testSelectAll_SQLException() throws Exception {
        try (MockedStatic<JDBCUtil> mockedJdbc = mockStatic(JDBCUtil.class)) {
            mockedJdbc.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("SQL error"));

            List<ChiTietPhieuNhapDTO> list = dao.selectAll("1");

            System.out.println("SelectAll exception result size: " + list.size());
            assertTrue(list.isEmpty());
        }
    }
}
