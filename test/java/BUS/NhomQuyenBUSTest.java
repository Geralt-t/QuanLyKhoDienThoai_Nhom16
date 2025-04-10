package BUS;

import DAO.ChiTietQuyenDAO;
import DAO.NhomQuyenDAO;
import DTO.ChiTietQuyenDTO;
import DTO.NhomQuyenDTO;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NhomQuyenBUSTest {

    private NhomQuyenDAO nhomQuyenDAOMock;
    private ChiTietQuyenDAO chiTietQuyenDAOMock;
    private NhomQuyenBUS bus;

    @Before
    public void setUp() {
        nhomQuyenDAOMock = mock(NhomQuyenDAO.class);
        chiTietQuyenDAOMock = mock(ChiTietQuyenDAO.class);

        // Fake data
        ArrayList<NhomQuyenDTO> fakeList = new ArrayList<>();
        fakeList.add(new NhomQuyenDTO(1, "Admin"));

        when(nhomQuyenDAOMock.selectAll()).thenReturn(fakeList);

        // Inject mocks via new constructor
        bus = new NhomQuyenBUS(nhomQuyenDAOMock, chiTietQuyenDAOMock);
    }

    @Test
    public void testGetAll() {
        assertEquals(1, bus.getAll().size());
    }

    @Test
    public void testGetByIndex() {
        NhomQuyenDTO result = bus.getByIndex(0);
        assertEquals("Admin", result.getTennhomquyen());
    }

    @Test
    public void testAdd_Success() {
        when(nhomQuyenDAOMock.getAutoIncrement()).thenReturn(10);
        when(nhomQuyenDAOMock.insert(any())).thenReturn(1);
        when(chiTietQuyenDAOMock.insert(any())).thenReturn(1);

        ArrayList<ChiTietQuyenDTO> list = new ArrayList<>();
        list.add(new ChiTietQuyenDTO(10, "CN01", "VIEW"));

        boolean result = bus.add("Admin", list);
        assertTrue(result);
    }

    @Test
    public void testUpdate_Success() {
        NhomQuyenDTO nq = new NhomQuyenDTO(1, "Admin");
        ArrayList<ChiTietQuyenDTO> list = new ArrayList<>();
        list.add(new ChiTietQuyenDTO(1, "CN01", "EDIT"));

        when(nhomQuyenDAOMock.update(any())).thenReturn(1);
        when(chiTietQuyenDAOMock.delete("1")).thenReturn(1);
        when(chiTietQuyenDAOMock.insert(list)).thenReturn(1);

        boolean result = bus.update(nq, list, 0);
        assertTrue(result);
    }

    @Test
    public void testDelete_Success() {
        NhomQuyenDTO nq = new NhomQuyenDTO(1, "Admin");
        when(nhomQuyenDAOMock.delete("1")).thenReturn(1);

        boolean result = bus.delete(nq);
        assertTrue(result);
    }

    @Test
    public void testGetChiTietQuyen() {
        ArrayList<ChiTietQuyenDTO> list = new ArrayList<>();
        list.add(new ChiTietQuyenDTO(1, "CN01", "VIEW"));
        when(chiTietQuyenDAOMock.selectAll("1")).thenReturn(list);

        ArrayList<ChiTietQuyenDTO> result = bus.getChiTietQuyen("1");
        assertEquals(1, result.size());
    }

    @Test
    public void testAddChiTietQuyen_Success() {
        ArrayList<ChiTietQuyenDTO> list = new ArrayList<>();
        when(chiTietQuyenDAOMock.insert(list)).thenReturn(1);
        assertTrue(bus.addChiTietQuyen(list));
    }

    @Test
    public void testRemoveChiTietQuyen_Success() {
        when(chiTietQuyenDAOMock.delete("1")).thenReturn(1);
        assertTrue(bus.removeChiTietQuyen("1"));
    }

    @Test
    public void testCheckPermission_ReturnsTrue() {
        ArrayList<ChiTietQuyenDTO> list = new ArrayList<>();
        list.add(new ChiTietQuyenDTO(1, "CN01", "EDIT"));
        when(chiTietQuyenDAOMock.selectAll("1")).thenReturn(list);

        boolean result = bus.checkPermisson(1, "CN01", "EDIT");
        assertTrue(result);
    }

    @Test
    public void testSearch_ReturnsMatch() {
        ArrayList<NhomQuyenDTO> result = bus.search("Admin");
        assertEquals(1, result.size());
    }

    @Test
    public void testSearch_ReturnsEmpty() {
        ArrayList<NhomQuyenDTO> result = bus.search("NonExist");
        assertEquals(0, result.size());
    }
}
