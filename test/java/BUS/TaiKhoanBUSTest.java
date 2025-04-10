package BUS;

import DAO.NhomQuyenDAO;
import DAO.TaiKhoanDAO;
import DTO.NhomQuyenDTO;
import DTO.TaiKhoanDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TaiKhoanBUSTest {

    @Mock private TaiKhoanDAO mockTaiKhoanDAO;
    @Mock private NhomQuyenDAO mockNhomQuyenDAO;

    private TaiKhoanBUS taiKhoanBUS;
    private ArrayList<TaiKhoanDTO> taiKhoanList;
    private ArrayList<NhomQuyenDTO> nhomQuyenList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        TaiKhoanDTO tk = new TaiKhoanDTO();
        tk.setManv(101);
        tk.setUsername("admin");

        NhomQuyenDTO nq = new NhomQuyenDTO();
        nq.setManhomquyen(1);

        taiKhoanList = new ArrayList<>(Arrays.asList(tk));
        nhomQuyenList = new ArrayList<>(Arrays.asList(nq));

        when(mockTaiKhoanDAO.selectAll()).thenReturn(taiKhoanList);
        when(mockNhomQuyenDAO.selectAll()).thenReturn(nhomQuyenList);
        when(mockNhomQuyenDAO.selectById("1")).thenReturn(nq);

        taiKhoanBUS = new TaiKhoanBUS(mockTaiKhoanDAO, mockNhomQuyenDAO);
    }

    @Test
    public void testGetTaiKhoanAll_ShouldReturnList() {
        assertEquals(1, taiKhoanBUS.getTaiKhoanAll().size());
    }

    @Test
    public void testGetTaiKhoan_ShouldReturnCorrectItem() {
        assertEquals("admin", taiKhoanBUS.getTaiKhoan(0).getUsername());
    }

    @Test
    public void testGetTaiKhoanByMaNV_ShouldReturnIndex() {
        int index = taiKhoanBUS.getTaiKhoanByMaNV(101);
        assertEquals(0, index);
    }

    @Test
    public void testGetNhomQuyenDTO_ShouldReturnCorrectGroup() {
        NhomQuyenDTO nq = taiKhoanBUS.getNhomQuyenDTO(1);
        assertEquals(1, nq.getManhomquyen());
    }

    @Test
    public void testSearch_ByUsername_ShouldReturnMatch() {
        ArrayList<TaiKhoanDTO> result = taiKhoanBUS.search("admin", "Username");
        assertEquals(1, result.size());
    }

    @Test
    public void testAddAcc_ShouldAddSuccessfully() {
        TaiKhoanDTO newTk = new TaiKhoanDTO();
        newTk.setManv(102);
        newTk.setUsername("newuser");

        taiKhoanBUS.addAcc(newTk);
        assertEquals(2, taiKhoanBUS.getTaiKhoanAll().size());
    }

    @Test
    public void testUpdateAcc_ShouldReplaceSuccessfully() {
        TaiKhoanDTO updated = new TaiKhoanDTO();
        updated.setManv(101);
        updated.setUsername("updateduser");

        taiKhoanBUS.updateAcc(0, updated);
        assertEquals("updateduser", taiKhoanBUS.getTaiKhoan(0).getUsername());
    }
} 
