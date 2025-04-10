package BUS;

import DAO.PhienBanSanPhamDAO;
import DTO.ChiTietSanPhamDTO;
import DTO.PhienBanSanPhamDTO;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PhienBanSanPhamBUSTest {

    private PhienBanSanPhamDAO daoMock;
    private PhienBanSanPhamBUS bus;

    @Before
    public void setUp() {
        daoMock = mock(PhienBanSanPhamDAO.class);
        bus = new PhienBanSanPhamBUS(daoMock);
    }

    @Test
    public void testGetAll() {
        ArrayList<PhienBanSanPhamDTO> list = new ArrayList<>();
        list.add(new PhienBanSanPhamDTO());
        when(daoMock.selectAllpb("1")).thenReturn(list);

        ArrayList<PhienBanSanPhamDTO> result = bus.getAll(1);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetByMaPhienBan() {
        PhienBanSanPhamDTO pb = new PhienBanSanPhamDTO();
        when(daoMock.selectById(1)).thenReturn(pb);

        PhienBanSanPhamDTO result = bus.getByMaPhienBan(1);
        assertEquals(pb, result);
    }

    @Test
    public void testGetIndexByMaPhienBan() {
        ArrayList<PhienBanSanPhamDTO> list = new ArrayList<>();
        PhienBanSanPhamDTO pb = new PhienBanSanPhamDTO();
        pb.setMaphienbansp(5);
        list.add(pb);

        int index = bus.getIndexByMaPhienBan(list, 5);
        assertEquals(0, index);
    }

    @Test
    public void testCheckDuplicate_ReturnsTrue() {
        PhienBanSanPhamDTO pb = new PhienBanSanPhamDTO();
        ArrayList<PhienBanSanPhamDTO> list = new ArrayList<>();
        list.add(pb);

        assertTrue(PhienBanSanPhamBUS.checkDuplicate(list, pb));
    }

//    @Test
//    public void testCheckDuplicate_ReturnsFalse1() {
//        PhienBanSanPhamDTO pb1 = new PhienBanSanPhamDTO();
//        PhienBanSanPhamDTO pb2 = new PhienBanSanPhamDTO();
//        ArrayList<PhienBanSanPhamDTO> list = new ArrayList<>();
//        list.add(pb1);
//        // Nen coi 2 doi tuong duoc khoi tao khong tham so la bang nhau hay khac nhau????
//        assertFalse(PhienBanSanPhamBUS.checkDuplicate(list, pb2));
//    }
    @Test
    public void testCheckDuplicate_ReturnsFalse2() {
        PhienBanSanPhamDTO pb1 = new PhienBanSanPhamDTO();
        pb1.setRam(4);
        pb1.setRom(64);
        pb1.setMausac(1);

        PhienBanSanPhamDTO pb2 = new PhienBanSanPhamDTO();
        pb2.setRam(6); // khác RAM

        ArrayList<PhienBanSanPhamDTO> list = new ArrayList<>();
        list.add(pb1);

        assertFalse(PhienBanSanPhamBUS.checkDuplicate(list, pb2));
    }

    @Test
    public void testAdd() {
        ArrayList<PhienBanSanPhamDTO> list = new ArrayList<>();
        when(daoMock.insert(list)).thenReturn(1);

        assertTrue(bus.add(list));
    }

    @Test
    public void testGetSoluong() {
        PhienBanSanPhamDTO pb = new PhienBanSanPhamDTO();
        pb.setSoluongton(99);
        when(daoMock.selectById(1)).thenReturn(pb);

        assertEquals(99, bus.getSoluong(1));
    }

    @Test
    public void testCheckImeiExists() {
        ArrayList<ChiTietSanPhamDTO> arr = new ArrayList<>();
        when(daoMock.checkImeiExists(arr)).thenReturn(true);

        assertTrue(bus.checkImeiExists(arr));
    }
}
