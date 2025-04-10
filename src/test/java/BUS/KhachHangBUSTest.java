package BUS;

import DAO.KhachHangDAO;
import DTO.KhachHangDTO;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class KhachHangBUSTest {
    
    @Mock
    private KhachHangDAO mockDAO;
    
    // Instance của BUS cần test
    private KhachHangBUS khBUS;
    
    // Dữ liệu mẫu sử dụng cho test
    private ArrayList<KhachHangDTO> mockList;
    private KhachHangDTO kh1;
    private KhachHangDTO kh2;
    
    @Before
    public void setUp() throws Exception {
        // Khởi tạo các mock của Mockito
        MockitoAnnotations.initMocks(this);
        
        // Tạo dữ liệu mẫu cho Khách hàng
        kh1 = new KhachHangDTO();
        kh1.setMaKH(100);
        kh1.setHoten("Nguyen Van A");
        kh1.setDiachi("123 ABC, TP.HCM");
        kh1.setSdt("0909123456");
        
        kh2 = new KhachHangDTO();
        kh2.setMaKH(200);
        kh2.setHoten("Tran Thi B");
        kh2.setDiachi("456 DEF, HN");
        kh2.setSdt("0912345678");
        
        mockList = new ArrayList<>();
        mockList.add(kh1);
        mockList.add(kh2);
        
        // Giả lập DAO.selectAll() trả về danh sách mẫu
        when(mockDAO.selectAll()).thenReturn(mockList);
        
        // Tạo instance BUS (sử dụng constructor mặc định)
        khBUS = new KhachHangBUS();
        
        // Sử dụng Reflection để inject mockDAO vào trường private khDAO trong BUS
        Field field = KhachHangBUS.class.getDeclaredField("khDAO");
        field.setAccessible(true);
        field.set(khBUS, mockDAO);
        
        // Đảm bảo danh sách khách hàng trong BUS được cập nhật theo dữ liệu mẫu.
        // (Nếu constructor đã gọi khDAO.selectAll(), ta tiến hành cập nhật lại list theo mockList.)
        khBUS.getAll().clear();
        khBUS.getAll().addAll(mockList);
    }
    
    @Test
    public void testGetAll() {
        ArrayList<KhachHangDTO> list = khBUS.getAll();
        assertNotNull(list);
        assertEquals(2, list.size());
    }
    
    @Test
    public void testGetByIndex() {
        KhachHangDTO result = khBUS.getByIndex(1);
        assertNotNull(result);
        assertEquals(200, result.getMaKH());
        assertEquals("Tran Thi B", result.getHoten());
    }
    
    @Test
    public void testGetIndexByMaDV() {
        int index1 = khBUS.getIndexByMaDV(100);
        int index2 = khBUS.getIndexByMaDV(200);
        int notFound = khBUS.getIndexByMaDV(999);
        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(-1, notFound);
    }
    
    @Test
    public void testAdd() {
        // Tạo khách hàng mới
        KhachHangDTO newKH = new KhachHangDTO();
        newKH.setMaKH(300);
        newKH.setHoten("Le Van C");
        newKH.setDiachi("789 GHI, ĐN");
        newKH.setSdt("0923456789");
        
        // Giả lập DAO.insert(newKH) trả về số khác 0 (thành công)
        when(mockDAO.insert(newKH)).thenReturn(1);
        
        boolean result = khBUS.add(newKH);
        assertTrue(result);
        assertTrue(khBUS.getAll().contains(newKH));
        verify(mockDAO).insert(newKH);
    }
    
    @Test
    public void testDelete() {
        // Giả lập xoá khách hàng kh1 (MaKH = 100)
        when(mockDAO.delete("100")).thenReturn(1);
        boolean result = khBUS.delete(kh1);
        assertTrue(result);
        // Sau khi xoá, danh sách có kích thước giảm đi 1
        assertEquals(1, khBUS.getAll().size());
        verify(mockDAO).delete("100");
    }
    
    @Test
    public void testUpdate() {
        // Tạo DTO cập nhật cho kh1 (MaKH = 100)
        KhachHangDTO updatedKH = new KhachHangDTO();
        updatedKH.setMaKH(100);
        updatedKH.setHoten("Nguyen Van A Updated");
        updatedKH.setDiachi("New Address");
        updatedKH.setSdt("0987654321");
        
        when(mockDAO.update(updatedKH)).thenReturn(1);
        boolean result = khBUS.update(updatedKH);
        assertTrue(result);
        // Kiểm tra phần tử có MaKH = 100 đã được cập nhật
        KhachHangDTO testKH = khBUS.getByIndex(khBUS.getIndexByMaDV(100));
        assertEquals("Nguyen Van A Updated", testKH.getHoten());
        assertEquals("New Address", testKH.getDiachi());
        assertEquals("0987654321", testKH.getSdt());
        verify(mockDAO).update(updatedKH);
    }
    
    @Test
    public void testSearch_TatCa() {
        // Tìm kiếm với kiểu "Tất cả": nếu text có chứa trong bất kỳ thuộc tính nào.
        ArrayList<KhachHangDTO> result = khBUS.search("100", "Tất cả");
        // Vì kh1 có MaKH = 100 nên result chứa kh1
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getMaKH());
        
        // Tìm kiếm theo tên (kh2 có tên "Tran Thi B")
        result = khBUS.search("thi", "Tất cả");
        assertEquals(1, result.size());
        assertEquals(200, result.get(0).getMaKH());
    }
    
    @Test
    public void testSearch_MaKhachHang() {
        // Kiểm tra tìm kiếm theo "Mã khách hàng"
        ArrayList<KhachHangDTO> result = khBUS.search("200", "Mã khách hàng");
        assertEquals(1, result.size());
        assertEquals(200, result.get(0).getMaKH());
    }
    
    @Test
    public void testSearch_TenKhachHang() {
        // Kiểm tra tìm kiếm theo tên
        ArrayList<KhachHangDTO> result = khBUS.search("Nguyen", "Tên khách hàng");
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getMaKH());
    }
    
    @Test
    public void testSearch_DiaChi() {
        // Kiểm tra tìm kiếm theo địa chỉ
        ArrayList<KhachHangDTO> result = khBUS.search("TP.HCM", "Địa chỉ");
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getMaKH());
    }
    
    @Test
    public void testSearch_SoDienThoai() {
        // Kiểm tra tìm kiếm theo số điện thoại
        ArrayList<KhachHangDTO> result = khBUS.search("0912345678", "Số điện thoại");
        assertEquals(1, result.size());
        assertEquals(200, result.get(0).getMaKH());
    }
    
    @Test
    public void testGetTenKhachHang() {
        String name = khBUS.getTenKhachHang(100);
        assertEquals("Nguyen Van A", name);
        
        // Nếu không tìm thấy, phương thức sẽ trả về chuỗi rỗng
        name = khBUS.getTenKhachHang(999);
        assertEquals("", name);
    }
    
    @Test
    public void testGetArrTenKhachHang() {
        String[] expected = new String[]{"Nguyen Van A", "Tran Thi B"};
        String[] actual = khBUS.getArrTenKhachHang();
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testSelectKh() {
        // Giả lập DAO.selectById trả về một đối tượng cụ thể với MaKH = 100
        when(mockDAO.selectById("100")).thenReturn(kh1);
        KhachHangDTO result = khBUS.selectKh(100);
        assertNotNull(result);
        assertEquals(100, result.getMaKH());
        verify(mockDAO).selectById("100");
    }
}
