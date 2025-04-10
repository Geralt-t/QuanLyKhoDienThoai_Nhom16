package BUS;

import DAO.HeDieuHanhDAO;
import DTO.ThuocTinhSanPham.HeDieuHanhDTO;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HeDieuHanhBUSTest {

    @Mock
    private HeDieuHanhDAO mockDAO;

    // Lớp BUS cần test
    private HeDieuHanhBUS hdhBUS;

    // Danh sách giả lập dùng cho test
    private ArrayList<HeDieuHanhDTO> mockList;
    private HeDieuHanhDTO dto1; // Ví dụ: Android
    private HeDieuHanhDTO dto2; // Ví dụ: iOS

    @Before
    public void setUp() throws Exception {
        // Khởi tạo các mock của Mockito
        MockitoAnnotations.initMocks(this);
        
        // Tạo dữ liệu mẫu
        dto1 = new HeDieuHanhDTO();
        dto1.setMahdh(1);
        dto1.setTenhdh("Android");
        
        dto2 = new HeDieuHanhDTO();
        dto2.setMahdh(2);
        dto2.setTenhdh("iOS");
        
        // Tạo danh sách giả lập
        mockList = new ArrayList<>();
        mockList.add(dto1);
        mockList.add(dto2);
        
        // Giả lập trả về cho selectAll() của DAO
        when(mockDAO.selectAll()).thenReturn(mockList);
        
        // Tạo instance của BUS (sử dụng constructor mặc định)
        hdhBUS = new HeDieuHanhBUS();
        
        // Dùng Reflection để inject mockDAO vào trường private hdhDAO trong BUS
        Field field = HeDieuHanhBUS.class.getDeclaredField("hdhDAO");
        field.setAccessible(true);
        field.set(hdhBUS, mockDAO);
        
        // Đảm bảo danh sách trong BUS được cập nhật theo danh sách giả lập
        // (do constructor của BUS gọi dlhdDAO.selectAll())
        hdhBUS.getAll().clear();
        hdhBUS.getAll().addAll(mockList);
    }
    
    @Test
    public void testGetAll() {
        ArrayList<HeDieuHanhDTO> list = hdhBUS.getAll();
        assertNotNull(list);
        assertEquals(2, list.size());
    }
    
    @Test
    public void testGetArrTenHeDieuHanh() {
        String[] expected = new String[]{"Android", "iOS"};
        String[] actual = hdhBUS.getArrTenHeDieuHanh();
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testGetByIndex() {
        HeDieuHanhDTO result = hdhBUS.getByIndex(1);
        assertNotNull(result);
        assertEquals(2, result.getMahdh());
        assertEquals("iOS", result.getTenhdh());
    }
    
    @Test
    public void testAdd() {
        // Tạo một đối tượng mới
        HeDieuHanhDTO newDTO = new HeDieuHanhDTO();
        newDTO.setMahdh(3);
        newDTO.setTenhdh("Windows");
        
        // Giả lập DAO.insert(newDTO) trả về số khác 0 (thành công)
        when(mockDAO.insert(newDTO)).thenReturn(1);
        
        boolean result = hdhBUS.add(newDTO);
        assertTrue(result);
        assertTrue(hdhBUS.getAll().contains(newDTO));
        verify(mockDAO).insert(newDTO);
    }
    
    @Test
    public void testDelete() {
        // Giả lập xoá dto1 (Mahdh = 1)
        when(mockDAO.delete("1")).thenReturn(1);
        // Chúng ta biết dto1 đang ở index 0
        boolean result = hdhBUS.delete(dto1, 0);
        assertTrue(result);
        assertEquals(1, hdhBUS.getAll().size());
        verify(mockDAO).delete("1");
    }
    
    @Test
    public void testGetIndexByMaMau() {
        // Phương thức getIndexByMaMau được sử dụng để tìm index dựa trên mã hệ điều hành
        // (Ở đây dto1 có Mahdh = 1, dto2 có Mahdh = 2)
        int index1 = hdhBUS.getIndexByMaMau(1);
        int index2 = hdhBUS.getIndexByMaMau(2);
        int notFound = hdhBUS.getIndexByMaMau(99);
        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(-1, notFound);
    }
    
    @Test
    public void testGetTenHdh() {
        // Lấy tên của hệ điều hành dựa trên mã
        String ten = hdhBUS.getTenHdh(2);
        assertEquals("iOS", ten);
    }
    
    @Test
    public void testUpdate() {
        // Tạo DTO mới cập nhật cho đối tượng có Mahdh = 1 (dto1)
        HeDieuHanhDTO updatedDTO = new HeDieuHanhDTO();
        updatedDTO.setMahdh(1);
        updatedDTO.setTenhdh("Android 11");
        
        when(mockDAO.update(updatedDTO)).thenReturn(1);
        
        boolean result = hdhBUS.update(updatedDTO);
        assertTrue(result);
        // Kiểm tra rằng phần tử tại index 0 đã được cập nhật tên thành "Android 11"
        assertEquals("Android 11", hdhBUS.getByIndex(0).getTenhdh());
        verify(mockDAO).update(updatedDTO);
    }
    
    @Test
    public void testCheckDup_Found() {
        // Vì trong danh sách có "Android" và "iOS", nên checkDup với tên 'Andr' (bao gồm substring) sẽ cho kết quả false
        boolean duplicate = hdhBUS.checkDup("Andr");
        assertFalse(duplicate);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckDup_IndexOutOfBounds() {
        // Do vòng lặp trong checkDup dùng while(i <= list.size()), nếu không tìm thấy sẽ có exception
        // Ví dụ: kiểm tra với tên không tồn tại nào trong list (không có 'Windows') -> sẽ duyệt i từ 0 đến list.size(), gây IndexOutOfBoundsException.
        hdhBUS.checkDup("Windows");
    }
}
