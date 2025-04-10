/*package BUS;
import DAO.NhanVienDAO;
import DAO.TaiKhoanDAO;
import DTO.NhanVienDTO;
import GUI.Panel.NhanVien;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class NhanVienBUSTest {

    @Mock
    private NhanVien mockPanel;

    @Mock
    private NhanVienDAO nhanVienDAOMock;

    @Mock
    private TaiKhoanDAO taiKhoanDAOMock;

    @InjectMocks
    private NhanVienBUS bus;

    private NhanVienDTO nv1, nv2;

    @Before
    public void setUp() {
        nv1 = new NhanVienDTO(1, "Nguyễn Văn A", "a@gmail.com", "0123456789", "Nam", "1995-01-01");
        nv2 = new NhanVienDTO(2, "Trần Thị B", "b@gmail.com", "0987654321", "Nữ", "1997-05-01");

        bus = new NhanVienBUS(mockPanel);
        bus.listNv = new ArrayList<>(Arrays.asList(nv1, nv2));
    }

    @Test
    public void testGetAll() {
        List<NhanVienDTO> result = bus.getAll();
        assertEquals(2, result.size());
        assertEquals("Nguyễn Văn A", result.get(0).getHoten());
    }

    @Test
    public void testGetByIndex() {
        NhanVienDTO result = bus.getByIndex(1);
        assertEquals("Trần Thị B", result.getHoten());
    }

    @Test
    public void testGetIndexById_Found() {
        int index = bus.getIndexById(1);
        assertEquals(0, index);
    }

    @Test
    public void testGetIndexById_NotFound() {
        int index = bus.getIndexById(999);
        assertEquals(-1, index);
    }

    @Test
    public void testGetArrTenNhanVien() {
        String[] names = bus.getArrTenNhanVien();
        assertArrayEquals(new String[]{"Nguyễn Văn A", "Trần Thị B"}, names);
    }

    @Test
    public void testInsertNv() {
        NhanVienDTO nv3 = new NhanVienDTO(3, "Lê Văn C", "c@gmail.com", "0111222333", "Nam", "2000-01-01");
        bus.insertNv(nv3);
        assertEquals(3, bus.listNv.size());
        assertEquals("Lê Văn C", bus.listNv.get(2).getHoten());
    }

    @Test
    public void testUpdateNv() {
        NhanVienDTO updated = new NhanVienDTO(2, "Trần B Updated", "updated@gmail.com", "0000000000", "Nữ", "1997-05-01");
        bus.updateNv(1, updated);
        assertEquals("Trần B Updated", bus.listNv.get(1).getHoten());
    }

    @Test
    public void testDeleteNv() {
        NhanVienDTO toDelete = nv1;

        // Mock DAO behavior
        NhanVienDAO mockedDao = mock(NhanVienDAO.class);
        TaiKhoanDAO mockedTaiKhoanDao = mock(TaiKhoanDAO.class);
        NhanVienBUS busWithMocks = new NhanVienBUS(mockPanel);
        busWithMocks.listNv = new ArrayList<>(Arrays.asList(nv1, nv2));
        busWithMocks.nhanVienDAO = mockedDao;

        // Inject mock TaiKhoanDAO
        TaiKhoanDAO.setInstance(mockedTaiKhoanDao); // Nếu setInstance tồn tại
        busWithMocks.deleteNv(toDelete);

        verify(mockedDao).delete("1");
        verify(mockedTaiKhoanDao).delete("1");
        assertEquals(1, busWithMocks.listNv.size());
        assertFalse(busWithMocks.listNv.contains(toDelete));
    }

    @Test
    public void testSearch_All() {
        when(mockPanel.search.cbxChoose.getSelectedItem()).thenReturn("Tất cả");

        ArrayList<NhanVienDTO> result = bus.search("nguyễn");
        assertEquals(1, result.size());
        assertEquals("Nguyễn Văn A", result.get(0).getHoten());
    }

    @Test
    public void testSearch_ByHoTen() {
        when(mockPanel.search.cbxChoose.getSelectedItem()).thenReturn("Họ tên");

        ArrayList<NhanVienDTO> result = bus.search("b");
        assertEquals(1, result.size());
        assertEquals("Trần Thị B", result.get(0).getHoten());
    }

    @Test
    public void testSearch_ByEmail() {
        when(mockPanel.search.cbxChoose.getSelectedItem()).thenReturn("Email");

        ArrayList<NhanVienDTO> result = bus.search("gmail");
        assertEquals(2, result.size());
    }
}
*/