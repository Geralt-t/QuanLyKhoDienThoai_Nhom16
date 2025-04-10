package BUS;

import DAO.ChiTietPhieuXuatDAO;
import DAO.ChiTietSanPhamDAO;
import DAO.PhieuXuatDAO;
import DTO.ChiTietPhieuDTO;
import DTO.PhieuXuatDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PhieuXuatBUSTest {

    @Mock private PhieuXuatDAO mockPhieuXuatDAO;
    @Mock private ChiTietPhieuXuatDAO mockChiTietPhieuXuatDAO;
    @Mock private ChiTietSanPhamDAO mockChiTietSanPhamDAO;
    @Mock private NhanVienBUS mockNhanVienBUS;
    @Mock private KhachHangBUS mockKhachHangBUS;

    private PhieuXuatBUS phieuXuatBUS;
    private ArrayList<PhieuXuatDTO> mockData;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PhieuXuatDTO phieu = new PhieuXuatDTO(100, 1, 200, new Timestamp(System.currentTimeMillis()), 500000, 1);
        mockData = new ArrayList<>(Arrays.asList(phieu));

        when(mockPhieuXuatDAO.selectAll()).thenReturn(mockData);
        when(mockKhachHangBUS.getTenKhachHang(100)).thenReturn("Nguyen Van A");
        when(mockNhanVienBUS.getNameById(200)).thenReturn("Tran B");

        phieuXuatBUS = new PhieuXuatBUS(
            mockPhieuXuatDAO,
            mockChiTietPhieuXuatDAO,
            mockChiTietSanPhamDAO,
            mockNhanVienBUS,
            mockKhachHangBUS,
            mockData
        );
    }

    @Test
    public void testGetAll_ShouldReturnList() {
        assertEquals(1, phieuXuatBUS.getAll().size());
    }

    @Test
    public void testGetSelect_ShouldReturnCorrectItem() {
        assertEquals(1, phieuXuatBUS.getSelect(0).getMaphieu());
    }

    @Test
    public void testFillerPhieuXuat_ByCustomerName_ShouldReturnMatch() {
        Date now = new Date();
        ArrayList<PhieuXuatDTO> result = phieuXuatBUS.fillerPhieuXuat(2, "nguyen", 0, 0, now, now, "0", "1000000");
        for (PhieuXuatDTO dto : result) {
        System.out.println(">> Tên KH: " + mockKhachHangBUS.getTenKhachHang(dto.getMakh()));
    }
        assertEquals(1, result.size());
    }

    @Test
    public void testRemove_ShouldUpdateList() {
        phieuXuatBUS.remove(0);
        assertEquals(0, phieuXuatBUS.getAll().size());
    }

    @Test
    public void testInsert_ShouldCallDAOs() {
        PhieuXuatDTO px = new PhieuXuatDTO();
        ArrayList<ChiTietPhieuDTO> ct = new ArrayList<>();
        phieuXuatBUS.insert(px, ct);

        verify(mockPhieuXuatDAO).insert(px);
        verify(mockChiTietPhieuXuatDAO).insert(ct);
    }
}