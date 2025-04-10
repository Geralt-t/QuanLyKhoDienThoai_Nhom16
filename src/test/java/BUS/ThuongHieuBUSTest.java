package BUS;

import DAO.ThuongHieuDAO;
import DTO.ThuocTinhSanPham.ThuongHieuDTO;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ThuongHieuBUSTest {

    private ThuongHieuDAO mockDAO;
    private ThuongHieuBUS bus;
    private ArrayList<ThuongHieuDTO> mockList;

    @Before
    public void setUp() {
        mockDAO = mock(ThuongHieuDAO.class);
        mockList = new ArrayList<>();
        mockList.add(new ThuongHieuDTO(1, "Nike"));
        mockList.add(new ThuongHieuDTO(2, "Adidas"));

        when(mockDAO.selectAll()).thenReturn(mockList);

        // Sử dụng constructor test-friendly
        bus = new ThuongHieuBUS(mockDAO);
    }

    @Test
    public void testGetAll() {
        assertEquals(mockList, bus.getAll());
    }

    @Test
    public void testGetByIndex() {
        assertEquals("Nike", bus.getByIndex(0).getTenthuonghieu());
    }

    @Test
    public void testGetIndexByMaLH() {
        assertEquals(1, bus.getIndexByMaLH(2));
    }

    @Test
    public void testAdd() {
        when(mockDAO.getAutoIncrement()).thenReturn(3);
        when(mockDAO.insert(any(ThuongHieuDTO.class))).thenReturn(1);

        boolean result = bus.add("Puma");
        assertTrue(result);
    }

    @Test
    public void testDelete() {
        ThuongHieuDTO dto = mockList.get(0);
        when(mockDAO.delete("1")).thenReturn(1);

        boolean result = bus.delete(dto);
        assertTrue(result);
    }

    @Test
    public void testUpdate() {
        ThuongHieuDTO dto = new ThuongHieuDTO(1, "ReNike");
        when(mockDAO.update(dto)).thenReturn(1);

        boolean result = bus.update(dto);
        assertTrue(result);
    }

    @Test
    public void testSearch() {
        ArrayList<ThuongHieuDTO> result = bus.search("nike");
        assertEquals(1, result.size());
    }

    @Test
    public void testGetArrTenThuongHieu() {
        String[] arr = bus.getArrTenThuongHieu();
        assertArrayEquals(new String[]{"Nike", "Adidas"}, arr);
    }

    @Test
    public void testGetTenThuongHieu() {
        assertEquals("Adidas", bus.getTenThuongHieu(2));
    }

    @Test
    public void testCheckDup() {
        assertFalse(bus.checkDup("nike"));
        assertTrue(bus.checkDup("puma"));
    }
}
