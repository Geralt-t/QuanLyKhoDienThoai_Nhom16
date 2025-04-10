/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package BUS;

import DTO.ThuocTinhSanPham.DungLuongRamDTO;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DungLuongRamBUSTest {

    private DungLuongRamBUS dlRamBUS;

    private DungLuongRamDTO testRam;

    @Before
    public void setUp() {
        dlRamBUS = new DungLuongRamBUS();
        testRam = new DungLuongRamDTO();
        testRam.setDungluongram(64);
        dlRamBUS.add(testRam);
    }

   
    @Test
    public void testAdd() {
        DungLuongRamDTO newRam = new DungLuongRamDTO();
        newRam.setMadlram(1000);
        newRam.setDungluongram(128);

        boolean result = dlRamBUS.add(newRam);
        assertTrue(result);

        // Kiểm tra xem dữ liệu đã được thêm chưa
        assertNotNull(dlRamBUS.getByIndex(dlRamBUS.getIndexById(1000)));
    }

    @Test
    public void testGetAll() {
        ArrayList<DungLuongRamDTO> list = dlRamBUS.getAll();
        assertNotNull(list);
    }

    

    @Test
    public void testGetByIndex() {
        ArrayList<DungLuongRamDTO> list = dlRamBUS.getAll();
        if (!list.isEmpty()) {
            DungLuongRamDTO ram = dlRamBUS.getByIndex(0);
            assertEquals(list.get(0), ram);
        }
    }

    @Test
    public void testGetIndexByMaRam() {
        ArrayList<DungLuongRamDTO> list = dlRamBUS.getAll();
        if (!list.isEmpty()) {
            int ma = list.get(0).getMadlram();
            int index = dlRamBUS.getIndexByMaRam(ma);
            assertEquals(0, index);
        }
    }

    @Test
    public void testUpdate() {
        ArrayList<DungLuongRamDTO> list = dlRamBUS.getAll();
        if (!list.isEmpty()) {
            DungLuongRamDTO ram = list.get(0);
            int oldValue = ram.getDungluongram();
            ram.setDungluongram(oldValue + 1); // thay đổi giá trị

            boolean updated = dlRamBUS.update(ram);
            assertTrue(updated);
            assertEquals(oldValue + 1, dlRamBUS.getAll().get(0).getDungluongram());
        }
    }

    @Test
    public void testDelete() {
        DungLuongRamDTO newRam = new DungLuongRamDTO();
        newRam.setMadlram(998);
        newRam.setDungluongram(128);
        dlRamBUS.add(newRam);
        int index = dlRamBUS.getIndexById(998);

        boolean result = dlRamBUS.delete(newRam, index);
        assertTrue(result);
        assertEquals(-1, dlRamBUS.getIndexById(998));
    }

    @Test
    public void testCheckDup() {
        DungLuongRamDTO dto = new DungLuongRamDTO();
        dto.setMadlram(997);
        dto.setDungluongram(256);
        dlRamBUS.add(dto);

        boolean isDup = !dlRamBUS.checkDup(256);
        assertTrue(isDup);

        boolean isNotDup = dlRamBUS.checkDup(999);
        assertTrue(isNotDup);
    }

    @Test
    public void testGetKichThuocById() {
        ArrayList<DungLuongRamDTO> list = dlRamBUS.getAll();
        if (!list.isEmpty()) {
            DungLuongRamDTO dto = list.get(0);
            int kichthuoc = dlRamBUS.getKichThuocById(dto.getMadlram());
            assertEquals(dto.getDungluongram(), kichthuoc);
        }
    }

    @Test
    public void testGetArrKichThuoc() {
        String[] kichthuoc = dlRamBUS.getArrKichThuoc();
        assertNotNull(kichthuoc);
        assertEquals(dlRamBUS.getAll().size(), kichthuoc.length);
    }
     @After
    public void tearDown() {
    // Xóa dữ liệu test dựa trên ID cụ thể được dùng trong test
    int[] testIds = {997, 998, 999, 1000};
    for (int id : testIds) {
        int index = dlRamBUS.getIndexById(id);
        if (index != -1) {
            DungLuongRamDTO dto = dlRamBUS.getByIndex(index);
            dlRamBUS.delete(dto, index);
        }
    }
}

}
