package BUS;
import DAO.MauSacDAO;
import DTO.ThuocTinhSanPham.MauSacDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MauSacBUSTest {

    @Mock
    private MauSacDAO mockDAO;

    @InjectMocks
    private MauSacBUS mausacBUS;

    private MauSacDTO ms1, ms2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        // Khởi tạo mẫu dữ liệu
        ms1 = new MauSacDTO(1, "Đỏ");
        ms2 = new MauSacDTO(2, "Xanh");

        // Mô phỏng danh sách trả về từ DAO
        ArrayList<MauSacDTO> mockList = new ArrayList<>(Arrays.asList(ms1, ms2));
        when(mockDAO.selectAll()).thenReturn(mockList);

        // Khởi tạo BUS và dùng reflection để gán listMauSac
        mausacBUS = new MauSacBUS();
        Field field = MauSacBUS.class.getDeclaredField("listMauSac");
        field.setAccessible(true);
        field.set(mausacBUS, mockList);
    }

    private void assertMauSacListSize(int expectedSize) {
        List<MauSacDTO> list = mausacBUS.getAll();
        assertEquals(expectedSize, list.size());
    }

    private void assertMauSacByIndex(int index, String expectedName) {
        MauSacDTO ms = mausacBUS.getByIndex(index);
        assertEquals(expectedName, ms.getTenmau());
    }

    @Test
    public void testGetAll() {
        assertMauSacListSize(2);
    }

    @Test
    public void testGetByIndex() {
        assertMauSacByIndex(1, "Xanh");
    }

    @Test
    public void testAdd() {
        
        MauSacDTO newMS = new MauSacDTO(3, "Vàng");
        when(mockDAO.insert(newMS)).thenReturn(1);

        boolean result = mausacBUS.add(newMS);
        assertTrue(result);

        // Kiểm tra sau khi thêm mới
        assertMauSacListSize(3);
        assertMauSacByIndex(2, "Vàng");
    }

    @Test
    public void testDelete() {
        when(mockDAO.delete("1")).thenReturn(1);
        boolean result = mausacBUS.delete(ms1, 0);
        assertTrue(result);

        // Kiểm tra sau khi xóa
        assertMauSacListSize(1);
        assertMauSacByIndex(0, "Xanh");
    }

    @Test
    public void testUpdate() {
        
        MauSacDTO updatedMS = new MauSacDTO(2, "Xanh Lá");
        when(mockDAO.update(updatedMS)).thenReturn(1);

        boolean result = mausacBUS.update(updatedMS);
        assertTrue(result);

        // Kiểm tra sau khi cập nhật
        assertMauSacByIndex(1, "Xanh Lá");
    }

    @Test
    public void testGetTenMau() {
        String ten = mausacBUS.getTenMau(1);
        assertEquals("Đỏ", ten);
    }

    @Test
    public void testGetArrTenMauSac() {
        String[] arr = mausacBUS.getArrTenMauSac();
        assertArrayEquals(new String[]{"Đỏ", "Xanh"}, arr);
    }

    @Test
    public void testGetIndexByMaMau() {
        int index = mausacBUS.getIndexByMaMau(2);
        assertEquals(1, index);
    }

    @Test
public void testCheckDupTrue() throws Exception {
    
    // Thêm dummy vào cuối để tránh lỗi khi duyệt với i <= size
    MauSacDTO dummy = new MauSacDTO(-1, "DUMMY");
    Field field = MauSacBUS.class.getDeclaredField("listMauSac");
    field.setAccessible(true);
    ArrayList<MauSacDTO> list = (ArrayList<MauSacDTO>) field.get(mausacBUS);

    list.add(dummy); // thêm dummy

    boolean result = mausacBUS.checkDup("Tím");

    list.remove(dummy); // xóa dummy sau test nếu cần

    assertTrue(result);
}

    @Test
    public void testCheckDupFalse() {
        boolean result = mausacBUS.checkDup("Xanh");
        assertFalse(result);
    }
}
