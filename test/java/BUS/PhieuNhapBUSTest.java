/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package BUS;

import DAO.ChiTietPhieuNhapDAO;
import DAO.ChiTietSanPhamDAO;
import DAO.PhieuNhapDAO;
import DTO.ChiTietPhieuNhapDTO;
import DTO.ChiTietSanPhamDTO;
import DTO.PhieuNhapDTO;
import java.sql.Timestamp;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PhieuNhapBUSTest {

    private PhieuNhapDAO phieuNhapDAO;
    private ChiTietPhieuNhapDAO ctPhieuNhapDAO;
    private ChiTietSanPhamDAO ctSanPhamDAO;
    private NhaCungCapBUS nccBUS;
    private NhanVienBUS nvBUS;
    private PhieuNhapBUS bus;

    @Before
    public void setUp() {
        phieuNhapDAO = mock(PhieuNhapDAO.class);
        ctPhieuNhapDAO = mock(ChiTietPhieuNhapDAO.class);
        ctSanPhamDAO = mock(ChiTietSanPhamDAO.class);
        nccBUS = mock(NhaCungCapBUS.class);
        nvBUS = mock(NhanVienBUS.class);

        bus = new PhieuNhapBUS(phieuNhapDAO, ctPhieuNhapDAO, ctSanPhamDAO, nccBUS, nvBUS);
    }

    @Test
    public void testGetTongTien() {
        ChiTietPhieuNhapDTO item = new ChiTietPhieuNhapDTO();
        item.setDongia(500);
        item.setSoluong(3);

        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(item);

        assertEquals(1500, bus.getTongTien(list));
    }

    @Test
    public void testFindCT_Found() {
        ChiTietPhieuNhapDTO dto = new ChiTietPhieuNhapDTO();
        dto.setMaphienbansp(101);

        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        list.add(dto);

        ChiTietPhieuNhapDTO result = bus.findCT(list, 101);
        assertEquals(dto, result);
    }

    @Test
    public void testFindCT_NotFound() {
        ArrayList<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        ChiTietPhieuNhapDTO result = bus.findCT(list, 999);
        assertNull(result);
    }

    @Test
    public void testConvertHashMapToArray() {
        HashMap<Integer, ArrayList<ChiTietSanPhamDTO>> map = new HashMap<>();

        ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO();
        ArrayList<ChiTietSanPhamDTO> list = new ArrayList<>();
        list.add(dto);

        map.put(1, list);

        ArrayList<ChiTietSanPhamDTO> result = bus.convertHashMapToArray(map);
        assertEquals(1, result.size());
    }

    @Test
    public void testAdd_Success() {
        // Tạo đối tượng Phiếu nhập
        PhieuNhapDTO phieu = new PhieuNhapDTO(
                10, // mã nhà cung cấp
                1001, // mã phiếu
                2002, // mã người tạo
                new Timestamp(System.currentTimeMillis()), // thời gian tạo
                500000, // tổng tiền
                1 // trạng thái
        );

        // Chi tiết phiếu nhập (rỗng cũng được vì ta mock DAO insert(ctPhieu))
        ArrayList<ChiTietPhieuNhapDTO> ctPhieu = new ArrayList<>();

        // Tạo HashMap chứa chi tiết sản phẩm nhập
        ChiTietSanPhamDTO sp = new ChiTietSanPhamDTO(); // bạn có thể set thêm thuộc tính nếu cần
        ArrayList<ChiTietSanPhamDTO> listSP = new ArrayList<>();
        listSP.add(sp);
        HashMap<Integer, ArrayList<ChiTietSanPhamDTO>> map = new HashMap<>();
        map.put(1, listSP); // key không quan trọng, miễn sao có dữ liệu

        // Mock các phương thức DAO
        when(phieuNhapDAO.insert(phieu)).thenReturn(1);
        when(ctPhieuNhapDAO.insert(ctPhieu)).thenReturn(1);
        when(ctSanPhamDAO.insert_mutiple(listSP)).thenReturn(1);

        // Gọi phương thức thật (convertHashMapToArray), không cần mock
        boolean result = bus.add(phieu, ctPhieu, map);

        // Kết quả mong đợi: true
        assertTrue(result);
    }

    @Test
    public void testAdd_FailAtPhieu() {
        PhieuNhapDTO phieu = new PhieuNhapDTO();
        ArrayList<ChiTietPhieuNhapDTO> ctPhieu = new ArrayList<>();
        HashMap<Integer, ArrayList<ChiTietSanPhamDTO>> map = new HashMap<>();

        when(phieuNhapDAO.insert(phieu)).thenReturn(0);

        boolean result = bus.add(phieu, ctPhieu, map);
        assertFalse(result);
    }

    @Test
    public void testCheckCancelPn() {
        when(phieuNhapDAO.checkCancelPn(5)).thenReturn(true);
        assertTrue(bus.checkCancelPn(5));
    }

    @Test
    public void testCancelPhieuNhap() {
        when(phieuNhapDAO.cancelPhieuNhap(5)).thenReturn(1);
        assertEquals(1, bus.cancelPhieuNhap(5));
    }
}
