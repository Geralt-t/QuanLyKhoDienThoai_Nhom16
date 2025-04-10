package BUS;

import DAO.DungLuongRomDAO;
import DTO.ThuocTinhSanPham.DungLuongRomDTO;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DungLuongRomBUSTest {

    @Mock
    private DungLuongRomDAO mockDAO;

    // BUS cần test
    private DungLuongRomBUS romBUS;
    
    // Dữ liệu giả lập
    private ArrayList<DungLuongRomDTO> mockList;
    private DungLuongRomDTO dto1;
    private DungLuongRomDTO dto2;

    @Before
    public void setUp() throws Exception {
        // Khởi tạo các mock với Mockito
        MockitoAnnotations.initMocks(this);

        // Tạo dữ liệu mẫu cho ROM
        dto1 = new DungLuongRomDTO();
        dto1.setMadungluongrom(1);
        dto1.setDungluongrom(32); // 32 GB

        dto2 = new DungLuongRomDTO();
        dto2.setMadungluongrom(2);
        dto2.setDungluongrom(64); // 64 GB

        // Tạo list giả lập
        mockList = new ArrayList<>();
        mockList.add(dto1);
        mockList.add(dto2);

        // Giả lập trả về cho selectAll() từ DAO
        when(mockDAO.selectAll()).thenReturn(mockList);

        // Khởi tạo BUS (sử dụng constructor mặc định)
        romBUS = new DungLuongRomBUS();
        // Dùng reflection để inject mockDAO vào trường private final 'dlromDAO'
        Field field = DungLuongRomBUS.class.getDeclaredField("dlromDAO");
        field.setAccessible(true);
        field.set(romBUS, mockDAO);

        // Cập nhật lại list trong BUS theo dữ liệu giả lập
        ArrayList<DungLuongRomDTO> listInBUS = romBUS.getAll();
        listInBUS.clear();
        listInBUS.addAll(mockList);
    }

    @Test
    public void testGetAll() {
        ArrayList<DungLuongRomDTO> list = romBUS.getAll();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void testGetByIndex() {
        DungLuongRomDTO result = romBUS.getByIndex(1);
        assertNotNull(result);
        assertEquals(2, result.getMadungluongrom());
        assertEquals(64, result.getDungluongrom());
    }

    @Test
    public void testGetIndexByMaRom() {
        int index1 = romBUS.getIndexByMaRom(1);
        int index2 = romBUS.getIndexByMaRom(2);
        int indexNotFound = romBUS.getIndexByMaRom(999);
        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(-1, indexNotFound);
    }

    @Test
    public void testAdd() {
        // Tạo DTO mới
        DungLuongRomDTO newDTO = new DungLuongRomDTO();
        newDTO.setMadungluongrom(3);
        newDTO.setDungluongrom(128);
        
        // Giả lập thành công khi gọi DAO.insert()
        when(mockDAO.insert(newDTO)).thenReturn(1);
        
        boolean result = romBUS.add(newDTO);
        assertTrue(result);
        assertTrue(romBUS.getAll().contains(newDTO));
        verify(mockDAO).insert(newDTO);
    }

    @Test
    public void testDelete() {
        // Giả lập xoá dto1 (madungluongrom = 1)
        when(mockDAO.delete("1")).thenReturn(1);
        // Lấy index của dto1 (theo dữ liệu mẫu, index = 0)
        boolean result = romBUS.delete(dto1, 0);
        assertTrue(result);
        assertEquals(1, romBUS.getAll().size());
        verify(mockDAO).delete("1");
    }

    @Test
    public void testUpdate() {
        // Tạo DTO cập nhật cho madungluongrom = 1
        DungLuongRomDTO updatedDTO = new DungLuongRomDTO();
        updatedDTO.setMadungluongrom(1);
        updatedDTO.setDungluongrom(256);
        when(mockDAO.update(updatedDTO)).thenReturn(1);
        
        boolean result = romBUS.update(updatedDTO);
        assertTrue(result);
        // Kiểm tra lại phần tử tại index 0 đã được cập nhật dung lượng thành 256
        assertEquals(256, romBUS.getByIndex(0).getDungluongrom());
        verify(mockDAO).update(updatedDTO);
    }

    @Test
    public void testGetIndexById() {
        int index = romBUS.getIndexById(1);
        assertEquals(0, index);
        int notFound = romBUS.getIndexById(999);
        assertEquals(-1, notFound);
    }

    @Test
    public void testGetKichThuocById() {
        int size = romBUS.getKichThuocById(2);
        assertEquals(64, size);
    }

    @Test
    public void testGetArrKichThuoc() {
        String[] expected = new String[]{"32GB", "64GB"};
        String[] actual = romBUS.getArrKichThuoc();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCheckDup() {
        
        // Trong dữ liệu mẫu có 32GB và 64GB, nên checkDup(32) -> false, checkDup(128) -> true.
        boolean dupExists = romBUS.checkDup(32);
        boolean dupNotExists = romBUS.checkDup(128);
        assertFalse(dupExists);
        assertTrue(dupNotExists);
    }
}
