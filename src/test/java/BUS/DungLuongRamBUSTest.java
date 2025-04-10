package BUS;

import DAO.DungLuongRamDAO;
import DTO.ThuocTinhSanPham.DungLuongRamDTO;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DungLuongRamBUSTest {

    // Tạo mock cho DAO
    @Mock
    private DungLuongRamDAO mockDAO;

    // Lớp BUS cần test
    private DungLuongRamBUS ramBUS;

    @Before
    public void setUp() throws Exception {
        // Khởi tạo các mock
        MockitoAnnotations.initMocks(this);
        
        // Tạo instance BUS thông qua constructor mặc định
        ramBUS = new DungLuongRamBUS();
        
        // Dùng reflection để inject mockDAO vào trường private final 'dlramDAO'
        Field field = DungLuongRamBUS.class.getDeclaredField("dlramDAO");
        field.setAccessible(true);
        field.set(ramBUS, mockDAO);
        
        // Giả lập trả về list khi gọi dlramDAO.selectAll()
        ArrayList<DungLuongRamDTO> mockList = new ArrayList<>();
        DungLuongRamDTO dto1 = new DungLuongRamDTO();
        dto1.setMadlram(1);
        dto1.setDungluongram(8);
        DungLuongRamDTO dto2 = new DungLuongRamDTO();
        dto2.setMadlram(2);
        dto2.setDungluongram(16);
        mockList.add(dto1);
        mockList.add(dto2);
        
        when(mockDAO.selectAll()).thenReturn(mockList);
        // Cập nhật listDLRam trong BUS theo giá trị từ DAO (để test những method không gọi DAO nữa)
        ramBUS.getAll().clear();
        ramBUS.getAll().addAll(mockList);
    }

    @Test
    public void testGetAll() {
        ArrayList<DungLuongRamDTO> list = ramBUS.getAll();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void testGetByIndex() {
        DungLuongRamDTO dto = ramBUS.getByIndex(1);
        assertNotNull(dto);
        assertEquals(2, dto.getMadlram());
        assertEquals(16, dto.getDungluongram());
    }

    @Test
    public void testGetIndexByMaRam() {
        int index1 = ramBUS.getIndexByMaRam(1);
        int index2 = ramBUS.getIndexByMaRam(2);
        int indexNotFound = ramBUS.getIndexByMaRam(99);
        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(-1, indexNotFound);
    }

    @Test
    public void testAdd() {
        DungLuongRamDTO newDTO = new DungLuongRamDTO();
        newDTO.setMadlram(3);
        newDTO.setDungluongram(32);
        when(mockDAO.insert(newDTO)).thenReturn(1);

        boolean result = ramBUS.add(newDTO);
        assertTrue(result);
        assertTrue(ramBUS.getAll().contains(newDTO));
        verify(mockDAO).insert(newDTO);
    }

    @Test
    public void testDelete() {
        // Giả lập xoá dto1 (madlram = 1) từ list
        when(mockDAO.delete("1")).thenReturn(1);
        // Lấy index của dto1 phải bằng 0 theo dữ liệu đã setup
        boolean result = ramBUS.delete(ramBUS.getByIndex(0), 0);
        assertTrue(result);
        assertEquals(1, ramBUS.getAll().size());
        verify(mockDAO).delete("1");
    }

    @Test
    public void testUpdate() {
        // Tạo DTO cập nhật cho madlram = 1
        DungLuongRamDTO updatedDTO = new DungLuongRamDTO();
        updatedDTO.setMadlram(1);
        updatedDTO.setDungluongram(12);
        when(mockDAO.update(updatedDTO)).thenReturn(1);

        boolean result = ramBUS.update(updatedDTO);
        assertTrue(result);
        // Kiểm tra rằng phần tử tại index 0 đã được cập nhật dungluongram = 12
        assertEquals(12, ramBUS.getByIndex(0).getDungluongram());
        verify(mockDAO).update(updatedDTO);
    }

    @Test
    public void testGetIndexById() {
        int index = ramBUS.getIndexById(1);
        assertEquals(0, index);
        int notFound = ramBUS.getIndexById(99);
        assertEquals(-1, notFound);
    }

    @Test
    public void testCheckDup() {
        
        assertFalse(ramBUS.checkDup(8));
        assertTrue(ramBUS.checkDup(64));
    }

    @Test
    public void testGetKichThuocById() {
        int size = ramBUS.getKichThuocById(1);
        assertEquals(8, size);
    }

    @Test
    public void testGetArrKichThuoc() {
        String[] arr = ramBUS.getArrKichThuoc();
        assertArrayEquals(new String[]{"8GB", "16GB"}, arr);
    }
}
