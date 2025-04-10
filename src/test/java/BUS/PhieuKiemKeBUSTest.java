package BUS;

import DAO.ChiTietKiemKeDAO;
import DAO.ChiTietKiemKeSanPhamDAO;
import DAO.PhieuKiemKeDAO;
import DTO.ChiTietKiemKeDTO;
import DTO.ChiTietKiemKeSanPhamDTO;
import DTO.PhieuKiemKeDTO;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PhieuKiemKeBUSTest {

    private PhieuKiemKeDAO pkDAO;
    private ChiTietKiemKeDAO ctkkDAO;
    private ChiTietKiemKeSanPhamDAO ctkkspDAO;
    private NhanVienBUS nvBUS;
    private PhieuKiemKeBUS bus;

    @Before
    public void setUp() {
        pkDAO = mock(PhieuKiemKeDAO.class);
        ctkkDAO = mock(ChiTietKiemKeDAO.class);
        ctkkspDAO = mock(ChiTietKiemKeSanPhamDAO.class);
        nvBUS = mock(NhanVienBUS.class);

        when(pkDAO.selectAll()).thenReturn(new ArrayList<>());

        bus = new PhieuKiemKeBUS(pkDAO, ctkkDAO, ctkkspDAO, nvBUS);
    }

    @Test
    public void testConstructorInitializesDanhSachPhieu() {
        ArrayList<PhieuKiemKeDTO> mockList = new ArrayList<>();
        mockList.add(new PhieuKiemKeDTO());
        when(pkDAO.selectAll()).thenReturn(mockList);

        PhieuKiemKeBUS localBus = new PhieuKiemKeBUS(pkDAO, ctkkDAO, ctkkspDAO, nvBUS);

        assertEquals(mockList, localBus.getDanhSachPhieu());
    }

    @Test
    public void testInsert() {
        PhieuKiemKeDTO phieu = new PhieuKiemKeDTO();
        ArrayList<ChiTietKiemKeDTO> dsPhieu = new ArrayList<>();
        ArrayList<ChiTietKiemKeSanPhamDTO> ctPhieu = new ArrayList<>();

        bus.insert(phieu, dsPhieu, ctPhieu);

        verify(pkDAO).insert(phieu);
        verify(ctkkDAO).insert(dsPhieu);
        verify(ctkkspDAO).insert(ctPhieu);
    }

    @Test
    public void testGetDanhSachPhieu() {
        assertNotNull(bus.getDanhSachPhieu());
    }

    @Test
    public void testSetDanhSachPhieu() {
        ArrayList<PhieuKiemKeDTO> newList = new ArrayList<>();
        bus.setDanhSachPhieu(newList);
        assertEquals(newList, bus.getDanhSachPhieu());
    }

    @Test
    public void testGetAutoIncrement() {
        when(pkDAO.getAutoIncrement()).thenReturn(123);
        assertEquals(123, bus.getAutoIncrement());
    }

    @Test
    public void testSelectAll() {
        ArrayList<PhieuKiemKeDTO> list = new ArrayList<>();
        when(pkDAO.selectAll()).thenReturn(list);
        assertEquals(list, bus.selectAll());
    }

    @Test
    public void testCancel() {
        PhieuKiemKeDTO dto = new PhieuKiemKeDTO();
        dto.setMaphieukiemke(1);
        ArrayList<PhieuKiemKeDTO> list = new ArrayList<>();
        list.add(dto);
        bus.setDanhSachPhieu(list);

        bus.cancel(0);

        verify(ctkkspDAO).delete("1");
        verify(ctkkDAO).delete("1");
        verify(pkDAO).delete("1");
        assertTrue(bus.getDanhSachPhieu().isEmpty());
    }

    @Test
    public void testGetChitietTiemKe() {
        ArrayList<ChiTietKiemKeDTO> list = new ArrayList<>();
        when(ctkkDAO.selectAll("5")).thenReturn(list);
        assertEquals(list, bus.getChitietTiemKe(5));
    }

    @Test
    public void testFillerPhieuKiemKe_AllMatch() {
        PhieuKiemKeDTO dto = new PhieuKiemKeDTO();
        dto.setMaphieukiemke(10);
        dto.setNguoitao(2);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        dto.setThoigiantao(now);

        ArrayList<PhieuKiemKeDTO> list = new ArrayList<>();
        list.add(dto);
        when(nvBUS.getNameById(2)).thenReturn("nguyenvana");
        bus.setDanhSachPhieu(list);

        ArrayList<PhieuKiemKeDTO> result = bus.fillerPhieuKiemKe(
                0, "10", 2, new Date(now.getTime() - 1000), new Date(now.getTime() + 1000));

        assertEquals(1, result.size());
    }

    @Test
    public void testFillerPhieuKiemKe_NoMatch() {
        PhieuKiemKeDTO dto = new PhieuKiemKeDTO();
        dto.setMaphieukiemke(11);
        dto.setNguoitao(3);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        dto.setThoigiantao(now);

        ArrayList<PhieuKiemKeDTO> list = new ArrayList<>();
        list.add(dto);
        when(nvBUS.getNameById(3)).thenReturn("nguyenvanb");
        bus.setDanhSachPhieu(list);

        ArrayList<PhieuKiemKeDTO> result = bus.fillerPhieuKiemKe(
                1, "999", 99, new Date(now.getTime() - 1000), new Date(now.getTime() + 1000));

        assertEquals(0, result.size());
    }
}