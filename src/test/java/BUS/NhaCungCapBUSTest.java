package BUS;

import DAO.NhaCungCapDAO;
import DTO.NhaCungCapDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NhaCungCapBUSTest {

    @Mock
    private NhaCungCapDAO mockDAO;

    @InjectMocks
    private NhaCungCapBUS nccBUS;

    private NhaCungCapDTO ncc1, ncc2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ncc1 = new NhaCungCapDTO(1, "Samsung", "HCM", "samsung@mail.com", "0123456789");
        ncc2 = new NhaCungCapDTO(2, "Sony", "HN", "sony@mail.com", "0987654321");

        ArrayList<NhaCungCapDTO> mockList = new ArrayList<>(Arrays.asList(ncc1, ncc2));
        when(mockDAO.selectAll()).thenReturn(mockList);

        nccBUS = new NhaCungCapBUS();

        Field field = NhaCungCapBUS.class.getDeclaredField("listNcc");
        field.setAccessible(true);
        field.set(nccBUS, mockList);
    }

    @Test
    public void testGetAll() {
        assertEquals(2, nccBUS.getAll().size());
    }

    @Test
    public void testGetByIndex() {
        assertEquals("Sony", nccBUS.getByIndex(1).getTenncc());
    }

    @Test
    public void testAddSuccess() {
        
        NhaCungCapDTO newNcc = new NhaCungCapDTO(3, "Apple", "Đà Nẵng", "apple@mail.com", "0912345678");
        when(mockDAO.insert(newNcc)).thenReturn(1);

        boolean result = nccBUS.add(newNcc);
        assertTrue(result);
        assertEquals(3, nccBUS.getAll().size());
        assertEquals("Apple", nccBUS.getByIndex(2).getTenncc());
    }

    @Test
    public void testAddFail() {
        NhaCungCapDTO newNcc = new NhaCungCapDTO(3, "Apple", "Đà Nẵng", "apple@mail.com", "0912345678");
        when(mockDAO.insert(newNcc)).thenReturn(0);

        boolean result = nccBUS.add(newNcc);
        assertFalse(result);
        assertEquals(2, nccBUS.getAll().size());
    }

    @Test
    public void testDeleteSuccess() {
        when(mockDAO.delete("1")).thenReturn(1);

        boolean result = nccBUS.delete(ncc1, 0);
        assertTrue(result);
        assertEquals(1, nccBUS.getAll().size());
    }

    @Test
    public void testDeleteFail() {
        when(mockDAO.delete("1")).thenReturn(0);

        boolean result = nccBUS.delete(ncc1, 0);
        assertFalse(result);
        assertEquals(2, nccBUS.getAll().size());
    }

    @Test
    public void testUpdateSuccess() {
        NhaCungCapDTO updated = new NhaCungCapDTO(2, "Sony Updated", "HN", "sony@mail.com", "0987654321");
        when(mockDAO.update(updated)).thenReturn(1);

        boolean result = nccBUS.update(updated);
        assertTrue(result);
        assertEquals("Sony Updated", nccBUS.getByIndex(1).getTenncc());
    }

    @Test
    public void testUpdateFail() {
        NhaCungCapDTO updated = new NhaCungCapDTO(2, "Sony Updated", "HN", "sony@mail.com", "0987654321");
        when(mockDAO.update(updated)).thenReturn(0);

        boolean result = nccBUS.update(updated);
        assertFalse(result);
        assertEquals("Sony", nccBUS.getByIndex(1).getTenncc());
    }

    @Test
    public void testGetIndexByMaNCC() {
        int index = nccBUS.getIndexByMaNCC(2);
        assertEquals(1, index);
    }

    @Test
    public void testGetIndexByMaNCC_NotFound() {
        int index = nccBUS.getIndexByMaNCC(999);
        assertEquals(-1, index);
    }

    @Test
    public void testSearch_TatCa() {
        ArrayList<NhaCungCapDTO> result = nccBUS.search("sony", "Tất cả");
        assertEquals(1, result.size());
        assertEquals("Sony", result.get(0).getTenncc());
    }

    @Test
    public void testSearch_Email() {
        ArrayList<NhaCungCapDTO> result = nccBUS.search("samsung", "Email");
        assertEquals(1, result.size());
        assertEquals("Samsung", result.get(0).getTenncc());
    }
    @Test
    public void testSearch_MaNhaCungCap() {
        ArrayList<NhaCungCapDTO> result = nccBUS.search("1", "Mã nhà cung cấp");
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getMancc());
    }

    @Test
    public void testSearch_DiaChi() {
        ArrayList<NhaCungCapDTO> result = nccBUS.search("HN", "Địa chỉ");
        assertEquals(1, result.size());
        assertEquals("HN", result.get(0).getDiachi());
    }

    @Test
    public void testSearch_SoDienThoai() {
        ArrayList<NhaCungCapDTO> result = nccBUS.search("098", "Số điện thoại");
        assertEquals(1, result.size());
        assertEquals("0987654321", result.get(0).getSdt());
    }
    @Test
    public void testSearch_TenNCC_KhongTimThay() {
        ArrayList<NhaCungCapDTO> result = nccBUS.search("Apple", "Tên nhà cung cấp");
        assertEquals(0, result.size());
    }

    @Test
    public void testGetArrTenNhaCungCap() {
        String[] arr = nccBUS.getArrTenNhaCungCap();
        assertArrayEquals(new String[]{"Samsung", "Sony"}, arr);
    }

    @Test
    public void testGetTenNhaCungCap() {
        String ten = nccBUS.getTenNhaCungCap(1);
        assertEquals("Samsung", ten);
    }

    @Test
    public void testFindCT_Success() {
        NhaCungCapDTO result = nccBUS.findCT(nccBUS.getAll(), "Sony");
        assertNotNull(result);
        assertEquals("Sony", result.getTenncc());
    }

    @Test
    public void testFindCT_NotFound() {
        NhaCungCapDTO result = nccBUS.findCT(nccBUS.getAll(), "Apple");
        assertNull(result);
    }
}
