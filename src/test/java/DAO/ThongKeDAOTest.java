/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
/*
 * File: ThongKeDAOTest.java
 * Class: ThongKeDAOTest
 * Description: Unit test class for ThongKeDAO using JUnit 4.13.2
 * Author: [Your Name]
 * Date: April 08, 2025
 */

package DAO;

// File: ThongKeDAOTest.java
// Mô tả: Unit test cho lớp ThongKeDAO sử dụng JUnit 4.13.2.
// Tên class: ThongKeDAOTest
// Tác giả: [Tên của bạn]
// Ngày tạo: [Ngày tạo file]


import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DAO.ThongKeDAO;
import DTO.ThongKe.ThongKeDoanhThuDTO;
import DTO.ThongKe.ThongKeKhachHangDTO;
import DTO.ThongKe.ThongKeNhaCungCapDTO;
import DTO.ThongKe.ThongKeTheoThangDTO;
import DTO.ThongKe.ThongKeTonKhoDTO;
import DTO.ThongKe.ThongKeTungNgayTrongThangDTO;
import config.JDBCUtil;

public class ThongKeDAOTest {

    // Biến kết nối dùng chung cho toàn bộ test case.
    // autoCommit được đặt thành false để sau test sử dụng rollback.
    private Connection connectionTest;

    /**
     * @Before: Phương thức thiết lập trước khi chạy mỗi test case.
     * Mục tiêu: Thiết lập kết nối đến CSDL và chuyển sang chế độ manual transaction.
     * (Kiểm tra DB: nếu không tạo được kết nối hoặc thiết lập autoCommit thì test sẽ fail)
     */
    @Before
    public void setUp() {
        try {
            connectionTest = JDBCUtil.getConnection();
            connectionTest.setAutoCommit(false);
        } catch (SQLException ex) {
            ex.printStackTrace();
            fail("Không thể thiết lập kết nối tới CSDL trong setUp().");
        }
    }

    /**
     * @After: Phương thức được chạy sau mỗi test case.
     * Mục tiêu: Rollback các thay đổi trong giao dịch và đóng kết nối (check DB).
     */
    @After
    public void tearDown() {
        if (connectionTest != null) {
            try {
                connectionTest.rollback();
                connectionTest.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // =========================================================================
    // Test Case 001: Kiểm tra phương thức getThongKeTonKho()
    // File: ThongKeDAOTest.java / Class: ThongKeDAOTest
    // Mã test case: TC_TK_001
    // Mục tiêu: Kiểm tra hàm getThongKeTonKho với input là text tìm kiếm "Vivo" và khoảng thời gian từ 2025-01-01 đến 2025-01-31.
    // Input:
    //    - Search Text: "SP"
    //    - Thời gian bắt đầu: "2025-01-01 00:00:00"
    //    - Thời gian kết thúc: "2025-01-31 23:59:00"
    // Expected output:
    //    - HashMap kết quả không null, chứa các key (mã sản phẩm) và danh sách đối tượng ThongKeTonKhoDTO có số liệu hợp lệ.
    // Ghi chú: CSDL phải có dữ liệu mẫu cho khoảng thời gian và điều kiện test.
    // =========================================================================
    @Test
    public void TC_TK_001_testGetThongKeTonKho() {
        String searchText = "Vivo";
        Date timeStart = Timestamp.valueOf("2025-01-01 00:00:00");
        // timeEnd sẽ được chuẩn hóa trong DAO với giờ 23:59
        Date timeEnd = Timestamp.valueOf("2025-01-31 23:59:00");

        // Gọi phương thức cần test
        HashMap<Integer, ArrayList<ThongKeTonKhoDTO>> resultMap = ThongKeDAO.getThongKeTonKho(searchText, timeStart, timeEnd);

        // Kiểm tra kết quả không được null
        assertNotNull("Kết quả trả về từ getThongKeTonKho() không được null", resultMap);
        // Kiểm tra có chứa ít nhất một sản phẩm
        assertTrue("Kết quả phải chứa ít nhất một sản phẩm", resultMap.size() > 0);

        // In ra thông tin để theo dõi (log)
        resultMap.forEach((maSanPham, listThongKe) -> {
            System.out.println("Mã sản phẩm: " + maSanPham + " - Số bản ghi: " + listThongKe.size());
        });
    }

    // =========================================================================
    // Test Case 002: Kiểm tra phương thức getDoanhThuTheoTungNam()
    // File: ThongKeDAOTest.java / Class: ThongKeDAOTest
    // Mã test case: TC_TK_002
    // Mục tiêu: Kiểm tra hàm getDoanhThuTheoTungNam() với input khoảng năm từ 2023 đến 2025.
    // Input:
    //    - Năm bắt đầu: 2023
    //    - Năm kết thúc: 2025
    // Expected output:
    //    - Danh sách có đúng số năm (2023, 2024, 2025; tổng cộng 3 năm),
    //      mỗi đối tượng có doanh thu, chi phí và lợi nhuận hợp lệ.
    // Ghi chú: CSDL cần có dữ liệu mẫu cho các năm này.
    // =========================================================================
    @Test
    public void TC_TK_002_testGetDoanhThuTheoTungNam() {
        int yearStart = 2023;
        int yearEnd = 2025;
        
        ArrayList<ThongKeDoanhThuDTO> doanhThuList = new ThongKeDAO().getDoanhThuTheoTungNam(yearStart, yearEnd);
        
        // Số năm mong đợi: 3 năm
        int expectedYears = yearEnd - yearStart + 1;
        assertNotNull("Danh sách doanh thu không được null", doanhThuList);
        assertEquals("Số bản ghi của doanh thu không bằng số năm mong đợi", expectedYears, doanhThuList.size());
        
    }

    // =========================================================================
    // Test Case 003: Kiểm tra phương thức getThongKeKhachHang()
    // File: ThongKeDAOTest.java / Class: ThongKeDAOTest
    // Mã test case: TC_TK_003
    // Mục tiêu: Kiểm tra hàm getThongKeKhachHang() với input tìm kiếm khách hàng "KH" và khoảng thời gian từ 2025-01-01 đến 2025-01-31.
    // Input:
    //    - Search Text: "KH"
    //    - Thời gian bắt đầu: 2025-01-01 00:00:00
    //    - Thời gian kết thúc: 2025-01-31 23:59:00
    // Expected output:
    //    - Danh sách khách hàng (DTO) có số phiếu giao dịch và tổng tiền hợp lệ.
    // Ghi chú: CSDL cần có dữ liệu mẫu cho khách hàng thỏa mãn điều kiện.
    // =========================================================================
    @Test
    public void TC_TK_003_testGetThongKeKhachHang() {
        String searchText = "KH";
        Date timeStart = Timestamp.valueOf("2025-01-01 00:00:00");
        Date timeEnd = Timestamp.valueOf("2025-01-31 23:59:00");
        
        ArrayList<ThongKeKhachHangDTO> khachHangList = ThongKeDAO.getThongKeKhachHang(searchText, timeStart, timeEnd);
        
        assertNotNull("Danh sách khách hàng không được null", khachHangList);
        
    }

    // =========================================================================
    // Test Case 004: Kiểm tra phương thức getThongKeNCC()
    // File: ThongKeDAOTest.java / Class: ThongKeDAOTest
    // Mã test case: TC_TK_004
    // Mục tiêu: Kiểm tra hàm getThongKeNCC() với input tìm kiếm NCC "NCC" và khoảng thời gian từ 2025-01-01 đến 2025-01-31.
    // Input:
    //    - Search Text: "NCC"
    //    - Thời gian bắt đầu: 2025-01-01 00:00:00
    //    - Thời gian kết thúc: 2025-01-31 23:59:00
    // Expected output:
    //    - Danh sách nhà cung cấp (DTO) với số phiếu và tổng tiền nhập hợp lệ.
    // Ghi chú: CSDL cần có dữ liệu mẫu cho các NCC.
    // =========================================================================
    @Test
    public void TC_TK_004_testGetThongKeNCC() {
        String searchText = "NCC";
        Date timeStart = Timestamp.valueOf("2025-01-01 00:00:00");
        Date timeEnd = Timestamp.valueOf("2025-01-31 23:59:00");
        
        ArrayList<ThongKeNhaCungCapDTO> nccList = ThongKeDAO.getThongKeNCC(searchText, timeStart, timeEnd);
        
        assertNotNull("Danh sách NCC không được null", nccList);
        for (ThongKeNhaCungCapDTO dto : nccList) {
            System.out.println("Mã NCC: " + dto.getMancc() + ", Tên NCC: " + dto.getTenncc() +
                    ", Số phiếu: " + dto.getSoluong() + ", Tổng tiền: " + dto.getTongtien());
            assertTrue("Số phiếu nhập NCC không hợp lệ", dto.getSoluong() >= 0);
        }
    }

    // =========================================================================
    // Test Case 005: Kiểm tra phương thức getThongKeTheoThang()
    // File: ThongKeDAOTest.java / Class: ThongKeDAOTest
    // Mã test case: TC_TK_005
    // Mục tiêu: Kiểm tra hàm getThongKeTheoThang() với input năm 2025.
    // Input:
    //    - Năm: 2025
    // Expected output:
    //    - Danh sách có đúng 12 bản ghi (1 bản ghi cho mỗi tháng) với doanh thu, chi phí và lợi nhuận hợp lệ.
    // Ghi chú: CSDL cần có dữ liệu cho đủ 12 tháng của năm được chọn.
    // =========================================================================
    @Test
    public void TC_TK_005_testGetThongKeTheoThang() {
        int nam = 2025;
        
        ArrayList<ThongKeTheoThangDTO> thangList = new ThongKeDAO().getThongKeTheoThang(nam);
        
        assertNotNull("Danh sách thống kê theo tháng không được null", thangList);
        assertEquals("Số bản ghi phải bằng 12 (một cho mỗi tháng)", 12, thangList.size());
        
        for (ThongKeTheoThangDTO dto : thangList) {
            System.out.println("Tháng: " + dto.getThang() + ", Chi phí: " + dto.getChiphi() +
                    ", Doanh thu: " + dto.getDoanhthu() + ", Lợi nhuận: " + dto.getLoinhuan());
            assertEquals("Lợi nhuận không bằng doanh thu - chi phí", dto.getDoanhthu() - dto.getChiphi(), dto.getLoinhuan());
        }
    }
    
    // =========================================================================
    // Test Case 006: Kiểm tra phương thức getThongKeTungNgayTrongThang()
    // File: ThongKeDAOTest.java / Class: ThongKeDAOTest
    // Mã test case: TC_TK_006
    // Mục tiêu: Kiểm tra hàm getThongKeTungNgayTrongThang() cho tháng 1 năm 2025.
    // Input:
    //    - Tháng: 1
    //    - Năm: 2025
    // Expected output:
    //    - Danh sách số liệu theo ngày với số bản ghi bằng số ngày của tháng 1 (ví dụ: 31 ngày).
    // Ghi chú: CSDL cần có dữ liệu mẫu cho tháng 1.
    // =========================================================================
    @Test
    public void TC_TK_006_testGetThongKeTungNgayTrongThang() {
        int thang = 1;
        int nam = 2025;
        
        ArrayList<ThongKeTungNgayTrongThangDTO> ngayList = new ThongKeDAO().getThongKeTungNgayTrongThang(thang, nam);
        
        assertNotNull("Danh sách thống kê theo ngày không được null", ngayList);
        // Tháng 1 luôn có 31 ngày
        assertEquals("Số bản ghi phải bằng 31 (số ngày của tháng 1)", 31, ngayList.size());
        
        for (ThongKeTungNgayTrongThangDTO dto : ngayList) {
            System.out.println("Ngày: " + dto.getNgay() + ", Chi phí: " + dto.getChiphi() +
                    ", Doanh thu: " + dto.getDoanhthu() + ", Lợi nhuận: " + dto.getLoinhuan());
            assertEquals("Lợi nhuận không bằng doanh thu - chi phí", dto.getDoanhthu() - dto.getChiphi(), dto.getLoinhuan());
        }
    }
    
    // =========================================================================
    // Test Case 007: Kiểm tra phương thức getThongKe7NgayGanNhat()
    // File: ThongKeDAOTest.java / Class: ThongKeDAOTest
    // Mã test case: TC_TK_007
    // Mục tiêu: Kiểm tra hàm getThongKe7NgayGanNhat() trả về 7 bản ghi cho 7 ngày gần nhất.
    // Input:
    //    - Không cần input cụ thể (sử dụng CURDATE() trong truy vấn).
    // Expected output:
    //    - Danh sách có đúng 7 bản ghi, mỗi bản ghi có dữ liệu doanh thu, chi phí và lợi nhuận hợp lệ.
    // Ghi chú: Kết quả phụ thuộc vào ngày hiện tại của hệ thống.
    // =========================================================================
    @Test
    public void TC_TK_007_testGetThongKe7NgayGanNhat() {
        ArrayList<ThongKeTungNgayTrongThangDTO> sevenDaysList = new ThongKeDAO().getThongKe7NgayGanNhat();
        
        assertNotNull("Danh sách 7 ngày gần nhất không được null", sevenDaysList);
        assertEquals("Số bản ghi phải bằng 7", 7, sevenDaysList.size());
        
        for (ThongKeTungNgayTrongThangDTO dto : sevenDaysList) {
            System.out.println("Ngày: " + dto.getNgay() + ", Chi phí: " + dto.getChiphi() +
                    ", Doanh thu: " + dto.getDoanhthu() + ", Lợi nhuận: " + dto.getLoinhuan());
            assertEquals("Lợi nhuận không bằng doanh thu - chi phí", dto.getDoanhthu() - dto.getChiphi(), dto.getLoinhuan());
        }
    }
    
    // =========================================================================
    // Test Case 008: Kiểm tra phương thức getThongKeTuNgayDenNgay()
    // File: ThongKeDAOTest.java / Class: ThongKeDAOTest
    // Mã test case: TC_TK_008
    // Mục tiêu: Kiểm tra hàm getThongKeTuNgayDenNgay() với input khoảng ngày từ 2025-01-10 đến 2025-01-20.
    // Input:
    //    - Ngày bắt đầu: "2025-01-10"
    //    - Ngày kết thúc: "2025-01-20"
    // Expected output:
    //    - Danh sách số bản ghi bằng số ngày trong khoảng [10,20] tức là 11 ngày.
    // Ghi chú: CSDL cần có dữ liệu mẫu cho khoảng ngày này.
    // =========================================================================
    @Test
    public void TC_TK_008_testGetThongKeTuNgayDenNgay() {
        String startDate = "2025-01-10";
        String endDate   = "2025-01-20";
        
        ArrayList<ThongKeTungNgayTrongThangDTO> dateRangeList = new ThongKeDAO().getThongKeTuNgayDenNgay(startDate, endDate);
        
        assertNotNull("Danh sách thống kê từ ngày đến ngày không được null", dateRangeList);
        // Từ ngày 10 đến 20 bao gồm 11 ngày
        assertEquals("Số bản ghi phải bằng 11", 11, dateRangeList.size());
        
        for (ThongKeTungNgayTrongThangDTO dto : dateRangeList) {
            System.out.println("Ngày: " + dto.getNgay() + ", Chi phí: " + dto.getChiphi() +
                    ", Doanh thu: " + dto.getDoanhthu() + ", Lợi nhuận: " + dto.getLoinhuan());
            assertEquals("Lợi nhuận không bằng doanh thu - chi phí", dto.getDoanhthu() - dto.getChiphi(), dto.getLoinhuan());
        }
    }
}
