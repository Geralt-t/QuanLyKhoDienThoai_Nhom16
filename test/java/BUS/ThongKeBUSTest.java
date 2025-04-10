package BUS;

import DAO.ThongKeDAO;
import DTO.ThongKe.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ThongKeBUSTest {

    private ThongKeDAO mockDAO;
    private ThongKeBUS thongKeBUS;

    @Before
    public void setUp() {
        mockDAO = mock(ThongKeDAO.class);
        thongKeBUS = new ThongKeBUS();
        thongKeBUS.thongkeDAO = mockDAO;
    }

    @Test
    public void testGetAllKhachHang() {
        ArrayList<ThongKeKhachHangDTO> expected = new ArrayList<>();
        when(mockDAO.getThongKeKhachHang(anyString(), any(Date.class), any(Date.class))).thenReturn(expected);

        ArrayList<ThongKeKhachHangDTO> result = thongKeBUS.getAllKhachHang();
        assertEquals(expected, result);
    }

    @Test
    public void testFilterKhachHang() {
        Date start = new Date();
        Date end = new Date();
        ArrayList<ThongKeKhachHangDTO> expected = new ArrayList<>();
        when(mockDAO.getThongKeKhachHang(eq("keyword"), eq(start), eq(end))).thenReturn(expected);

        ArrayList<ThongKeKhachHangDTO> result = thongKeBUS.FilterKhachHang("keyword", start, end);
        assertEquals(expected, result);
    }

    @Test
    public void testGetAllNCC() {
        ArrayList<ThongKeNhaCungCapDTO> expected = new ArrayList<>();
        when(mockDAO.getThongKeNCC(anyString(), any(Date.class), any(Date.class))).thenReturn(expected);

        ArrayList<ThongKeNhaCungCapDTO> result = thongKeBUS.getAllNCC();
        assertEquals(expected, result);
    }

    @Test
    public void testFilterNCC() {
        Date start = new Date();
        Date end = new Date();
        ArrayList<ThongKeNhaCungCapDTO> expected = new ArrayList<>();
        when(mockDAO.getThongKeNCC(eq("keyword"), eq(start), eq(end))).thenReturn(expected);

        ArrayList<ThongKeNhaCungCapDTO> result = thongKeBUS.FilterNCC("keyword", start, end);
        assertEquals(expected, result);
    }

    @Test
    public void testFilterTonKho() {
        Date start = new Date();
        Date end = new Date();
        HashMap<Integer, ArrayList<ThongKeTonKhoDTO>> expected = new HashMap<>();
        when(mockDAO.getThongKeTonKho(eq("test"), eq(start), eq(end))).thenReturn(expected);

        HashMap<Integer, ArrayList<ThongKeTonKhoDTO>> result = thongKeBUS.filterTonKho("test", start, end);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSoluong() {
        ThongKeTonKhoDTO item = mock(ThongKeTonKhoDTO.class);
        when(item.getTondauky()).thenReturn(1);
        when(item.getNhaptrongky()).thenReturn(2);
        when(item.getXuattrongky()).thenReturn(3);
        when(item.getToncuoiky()).thenReturn(4);

        ArrayList<ThongKeTonKhoDTO> list = new ArrayList<>();
        list.add(item);

        int[] result = thongKeBUS.getSoluong(list);
        assertArrayEquals(new int[]{1, 2, 3, 4}, result);
    }

    @Test
    public void testGetDoanhThuTheoTungNam() {
        ArrayList<ThongKeDoanhThuDTO> expected = new ArrayList<>();
        when(mockDAO.getDoanhThuTheoTungNam(2022, 2023)).thenReturn(expected);

        ArrayList<ThongKeDoanhThuDTO> result = thongKeBUS.getDoanhThuTheoTungNam(2022, 2023);
        assertEquals(expected, result);
    }

    @Test
    public void testGetThongKeTheoThang() {
        ArrayList<ThongKeTheoThangDTO> expected = new ArrayList<>();
        when(mockDAO.getThongKeTheoThang(2023)).thenReturn(expected);

        ArrayList<ThongKeTheoThangDTO> result = thongKeBUS.getThongKeTheoThang(2023);
        assertEquals(expected, result);
    }

    @Test
    public void testGetThongKeTungNgayTrongThang() {
        ArrayList<ThongKeTungNgayTrongThangDTO> expected = new ArrayList<>();
        when(mockDAO.getThongKeTungNgayTrongThang(4, 2023)).thenReturn(expected);

        ArrayList<ThongKeTungNgayTrongThangDTO> result = thongKeBUS.getThongKeTungNgayTrongThang(4, 2023);
        assertEquals(expected, result);
    }

    @Test
    public void testGetThongKeTuNgayDenNgay() {
        ArrayList<ThongKeTungNgayTrongThangDTO> expected = new ArrayList<>();
        when(mockDAO.getThongKeTuNgayDenNgay("2023-01-01", "2023-01-31")).thenReturn(expected);

        ArrayList<ThongKeTungNgayTrongThangDTO> result = thongKeBUS.getThongKeTuNgayDenNgay("2023-01-01", "2023-01-31");
        assertEquals(expected, result);
    }

    @Test
    public void testGetThongKe7NgayGanNhat() {
        ArrayList<ThongKeTungNgayTrongThangDTO> expected = new ArrayList<>();
        when(mockDAO.getThongKe7NgayGanNhat()).thenReturn(expected);

        ArrayList<ThongKeTungNgayTrongThangDTO> result = thongKeBUS.getThongKe7NgayGanNhat();
        assertEquals(expected, result);
    }
}