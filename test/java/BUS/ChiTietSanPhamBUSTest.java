/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package BUS;


import static org.junit.Assert.*;

/**
 *
 * @author admin
 */

import DTO.ChiTietSanPhamDTO;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;


public class ChiTietSanPhamBUSTest {

    private ChiTietSanPhamBUS chiTietSanPhamBUS;

    @Before
    public void setUp() {
        chiTietSanPhamBUS = new ChiTietSanPhamBUS();
         ChiTietSanPhamDTO dto1 = new ChiTietSanPhamDTO();
        dto1.setImei("IMEI001");

        ChiTietSanPhamDTO dto2 = new ChiTietSanPhamDTO();
        dto2.setImei("IMEI002");

        chiTietSanPhamBUS.listctsp.add(dto1);
        chiTietSanPhamBUS.listctsp.add(dto2);
    }

    
    @Test
    public void testGetAll() {
        ArrayList<ChiTietSanPhamDTO> result = chiTietSanPhamBUS.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("IMEI001", result.get(0).getImei());
        assertEquals("IMEI002", result.get(1).getImei());
    }

    @Test
    public void testGetByIndex() {
        ChiTietSanPhamDTO result = chiTietSanPhamBUS.getByIndex(1);
        assertNotNull(result);
        assertEquals("IMEI002", result.getImei());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetByIndex_OutOfBounds() {
        chiTietSanPhamBUS.getByIndex(5); // nên ném ra lỗi
    }
   @Test
public void testGetAllByMaPBSP() {
    int maPBSP = 57; 
    ArrayList<ChiTietSanPhamDTO> result = chiTietSanPhamBUS.getAllByMaPBSP(maPBSP);

    assertNotNull(result);
    assertEquals(7, result.size()); 

    for (ChiTietSanPhamDTO dto : result) {
        assertEquals(maPBSP, dto.getMaphienbansp());
    }
}


    @Test
    public void testGetAllCTSPbyMasp() {
        int maSP = 1; // Giả sử mã sản phẩm này có trong DB
        ArrayList<ChiTietSanPhamDTO> result = chiTietSanPhamBUS.getAllCTSPbyMasp(maSP);
        assertNotNull(result);
        for (ChiTietSanPhamDTO dto : result) {
            assertNotNull(dto.getImei());
        }
    }

    @Test
    public void testGetChiTietSanPhamFromMaPN() {
        int maPhieuNhap = 23; 
        HashMap<Integer, ArrayList<ChiTietSanPhamDTO>> result = chiTietSanPhamBUS.getChiTietSanPhamFromMaPN(maPhieuNhap);
        assertNotNull(result);
        for (Integer key : result.keySet()) {
            ArrayList<ChiTietSanPhamDTO> list = result.get(key);
            assertNotNull(list);
        }
    }

    @Test
    public void testGetChiTietSanPhamFromMaPX() {
        int maPhieuXuat = 25; // Phải có phiếu xuất thật trong DB
        HashMap<Integer, ArrayList<ChiTietSanPhamDTO>> result = chiTietSanPhamBUS.getChiTietSanPhamFromMaPX(maPhieuXuat);
        assertNotNull(result);
        
    }

    @Test
    public void testFilterPBvaTT() {
        int maSP = 1;
        int phienBan = 57;
        int tinhTrang = 0;
        String text = "107725056444797"; // đoạn text có thể match với imei thực tế

        ArrayList<ChiTietSanPhamDTO> result = chiTietSanPhamBUS.FilterPBvaTT(text, maSP, phienBan, tinhTrang);
        assertNotNull(result);
        for (ChiTietSanPhamDTO dto : result) {
            assertTrue(dto.getImei().contains(text));
            assertEquals(dto.getMaphienbansp(), phienBan);
            assertEquals(dto.getTinhtrang(), tinhTrang);
        }
    }

    @Test
    public void testFilterPBvaAll() {
        int maSP = 1;
        int phienBan = 57;
        String text = "107725056444797";

        ArrayList<ChiTietSanPhamDTO> result = chiTietSanPhamBUS.FilterPBvaAll(text, maSP, phienBan);
        assertNotNull(result);
        for (ChiTietSanPhamDTO dto : result) {
            assertTrue(dto.getImei().contains(text));
            assertEquals(dto.getMaphienbansp(), phienBan);
        }
    }
}


