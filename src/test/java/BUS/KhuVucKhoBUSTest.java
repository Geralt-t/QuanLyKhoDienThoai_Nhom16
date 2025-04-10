package BUS;
import DAO.KhuVucKhoDAO;
import DTO.KhuVucKhoDTO;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class KhuVucKhoBUSTest {

    private KhuVucKhoBUS kvkBUS;
    private KhuVucKhoDTO kvk1;
    private KhuVucKhoDTO kvk2;
    
    private KhuVucKhoDAO mockDAO;
    @Before
    public void setUp() {
         mockDAO = mock(KhuVucKhoDAO.class);
        kvkBUS = new KhuVucKhoBUS() {
            { // khởi tạo list giả lập
                listKVK = new ArrayList<>();
                 kvkDAO = mockDAO;
            }
        };

        kvk1 = new KhuVucKhoDTO(1, "Khu A", "Ghi chú A");
        kvk2 = new KhuVucKhoDTO(2, "Khu B", "Ghi chú B");

        kvkBUS.getAll().add(kvk1);
        kvkBUS.getAll().add(kvk2);
    }

    @Test
    public void testGetByIndex() {
        assertEquals(kvk1, kvkBUS.getByIndex(0));
        assertEquals(kvk2, kvkBUS.getByIndex(1));
    }
    @Test
public void testGetIndexByMaLH() {
    // Trường hợp tồn tại
    assertEquals(0, kvkBUS.getIndexByMaLH(1));
    assertEquals(1, kvkBUS.getIndexByMaLH(2));

    // Trường hợp không tồn tại
    assertEquals(-1, kvkBUS.getIndexByMaLH(999));
}


    @Test
    public void testGetIndexByMaKVK() {
        assertEquals(0, kvkBUS.getIndexByMaKVK(1));
        assertEquals(1, kvkBUS.getIndexByMaKVK(2));
        assertEquals(-1, kvkBUS.getIndexByMaKVK(999));
    }

    @Test
    public void testGetTenKhuVuc() {
        assertEquals("Khu A", kvkBUS.getTenKhuVuc(1));
        assertEquals("Khu B", kvkBUS.getTenKhuVuc(2));
    }

    @Test
    public void testSearchTatCa() {
        ArrayList<KhuVucKhoDTO> result = kvkBUS.search("Khu", "Tất cả");
        assertEquals(2, result.size());

        result = kvkBUS.search("A", "Tất cả");
        assertEquals(1, result.size());
        assertEquals("Khu A", result.get(0).getTenkhuvuc());
    }

    @Test
    public void testSearchTheoMa() {
        ArrayList<KhuVucKhoDTO> result = kvkBUS.search("1", "Mã khu vực kho");
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getMakhuvuc());
    }

    @Test
    public void testSearchTheoTen() {
        ArrayList<KhuVucKhoDTO> result = kvkBUS.search("B", "Tên khu vực kho");
        assertEquals(1, result.size());
        assertEquals("Khu B", result.get(0).getTenkhuvuc());
    }

    @Test
    public void testGetArrTenKhuVuc() {
        String[] arr = kvkBUS.getArrTenKhuVuc();
        assertEquals(2, arr.length);
        assertEquals("Khu A", arr[0]);
        assertEquals("Khu B", arr[1]);
    }
      @Test
    public void testAddKhuVucKho_Success() {
        KhuVucKhoDTO newKVK = new KhuVucKhoDTO(3, "Khu C", "Ghi chú C");

        when(mockDAO.insert(newKVK)).thenReturn(1); // Giả lập insert thành công

        boolean result = kvkBUS.add(newKVK);

        assertTrue(result);
        assertTrue(kvkBUS.getAll().contains(newKVK));
        verify(mockDAO).insert(newKVK);
    }

    @Test
    public void testAddKhuVucKho_Fail() {
        KhuVucKhoDTO newKVK = new KhuVucKhoDTO(3, "Khu C", "Ghi chú C");

        when(mockDAO.insert(newKVK)).thenReturn(0); // Giả lập insert thất bại

        boolean result = kvkBUS.add(newKVK);

        assertFalse(result);
        assertFalse(kvkBUS.getAll().contains(newKVK));
        verify(mockDAO).insert(newKVK);
    }

    @Test
    public void testUpdateKhuVucKho_Success() {
        KhuVucKhoDTO updatedKVK = new KhuVucKhoDTO(1, "Khu A mới", "Ghi chú mới");

        when(mockDAO.update(updatedKVK)).thenReturn(1); // Giả lập update thành công

        boolean result = kvkBUS.update(updatedKVK);

        assertTrue(result);
        assertEquals("Khu A mới", kvkBUS.getAll().get(0).getTenkhuvuc());
        verify(mockDAO).update(updatedKVK);
    }

    @Test
    public void testUpdateKhuVucKho_Fail() {
        KhuVucKhoDTO updatedKVK = new KhuVucKhoDTO(1, "Khu A mới", "Ghi chú mới");

        when(mockDAO.update(updatedKVK)).thenReturn(0); // Giả lập update thất bại

        boolean result = kvkBUS.update(updatedKVK);

        assertFalse(result);
        assertEquals("Khu A", kvkBUS.getAll().get(0).getTenkhuvuc()); // Không đổi
        verify(mockDAO).update(updatedKVK);
    }

    @Test
    public void testDeleteKhuVucKho_Success() {
        when(mockDAO.delete("1")).thenReturn(1); // Giả lập delete thành công

        boolean result = kvkBUS.delete(kvk1, 0);

        assertTrue(result);
        assertEquals(1, kvkBUS.getAll().size());
        assertFalse(kvkBUS.getAll().contains(kvk1));
        verify(mockDAO).delete("1");
    }

    @Test
    public void testDeleteKhuVucKho_Fail() {
        when(mockDAO.delete("1")).thenReturn(0); // Giả lập delete thất bại

        boolean result = kvkBUS.delete(kvk1, 0);

        assertFalse(result);
        assertEquals(2, kvkBUS.getAll().size()); // Không xóa
        verify(mockDAO).delete("1");
    }
}

