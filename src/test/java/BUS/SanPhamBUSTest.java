package BUS;

import DAO.SanPhamDAO;
import DTO.PhienBanSanPhamDTO;
import DTO.SanPhamDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SanPhamBUSTest {

    @Mock private SanPhamDAO mockSanPhamDAO;
    @Mock private PhienBanSanPhamBUS mockPhienBanSanPhamBUS;

    private SanPhamBUS sanPhamBUS;
    private ArrayList<SanPhamDTO> mockList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        SanPhamDTO sp = new SanPhamDTO();
        sp.setMasp(1);
        sp.setTensp("Laptop Dell");
        sp.setKhuvuckho(101);
        sp.setSoluongton(10);

        mockList = new ArrayList<>(Arrays.asList(sp));
        when(mockSanPhamDAO.selectAll()).thenReturn(mockList);

        sanPhamBUS = new SanPhamBUS(mockSanPhamDAO, mockPhienBanSanPhamBUS);
    }

    @Test
    public void testGetAll_ShouldReturnList() {
        assertEquals(1, sanPhamBUS.getAll().size());
    }

    @Test
    public void testGetByIndex_ShouldReturnCorrectItem() {
        SanPhamDTO sp = sanPhamBUS.getByIndex(0);
        assertEquals(1, sp.getMasp());
        assertEquals("Laptop Dell", sp.getTensp());
    }

    @Test
    public void testGetByMaSP_ShouldReturnCorrectItem() {
        SanPhamDTO sp = sanPhamBUS.getByMaSP(1);
        assertNotNull(sp);
        assertEquals(1, sp.getMasp());
    }

    @Test
    public void testGetByMakhuvuc_ShouldReturnCorrectItem() {
        ArrayList<SanPhamDTO> result = sanPhamBUS.getByMakhuvuc(101);
        assertEquals(1, result.size());
    }

    @Test
    public void testSearch_ShouldReturnMatchingItem() {
        ArrayList<SanPhamDTO> result = sanPhamBUS.search("laptop");
        assertEquals(1, result.size());
    }

    @Test
    public void testGetQuantity_ShouldReturnCorrectSum() {
        int quantity = sanPhamBUS.getQuantity();
        assertEquals(10, quantity);
    }

    @Test
    public void testAdd_ShouldInsertAndAddToList() {
        SanPhamDTO sp = new SanPhamDTO();
        sp.setMasp(2);
        ArrayList<PhienBanSanPhamDTO> listpb = new ArrayList<>();

        when(mockSanPhamDAO.insert(sp)).thenReturn(1);

        boolean result = sanPhamBUS.add(sp, listpb);
        assertTrue(result);
        verify(mockPhienBanSanPhamBUS).add(listpb);
        assertEquals(2, sanPhamBUS.getAll().size());
    }

    @Test
    public void testDelete_ShouldRemoveFromList() {
        SanPhamDTO sp = mockList.get(0);
        when(mockSanPhamDAO.delete("1")).thenReturn(1);

        boolean result = sanPhamBUS.delete(sp);
        assertTrue(result);
        assertEquals(0, sanPhamBUS.getAll().size());
    }

    @Test
    public void testUpdate_ShouldModifyItem() {
        SanPhamDTO sp = new SanPhamDTO();
        sp.setMasp(1);
        sp.setTensp("Updated Dell");

        when(mockSanPhamDAO.update(sp)).thenReturn(1);

        boolean result = sanPhamBUS.update(sp);
        assertTrue(result);
        assertEquals("Updated Dell", sanPhamBUS.getByIndex(0).getTensp());
    }
}