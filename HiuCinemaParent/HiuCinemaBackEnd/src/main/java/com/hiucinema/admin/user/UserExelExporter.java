package com.hiucinema.admin.user;

import com.hiucinema.common.entity.User;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class UserExelExporter extends AbstractExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public UserExelExporter() {
        workbook = new XSSFWorkbook();
    }

    //Tạo 1 sheet tên là Users => tạo dòng đầu tiên => chỉnh style => điền giá trị
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row, 0, "User Id", cellStyle);
        createCell(row, 1, "E-mail", cellStyle);
        createCell(row, 2, "First Name", cellStyle);
        createCell(row, 3, "Last Name", cellStyle);
        createCell(row, 4, "Roles", cellStyle);
        createCell(row, 5, "Enabled", cellStyle);
    }


    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle cellStyle) {
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);
        //Nếu mà value là kiểu integer => ép kiểu object thành integer ,...
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(cellStyle);
    }

    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/octet-stream", ".xlsx");
        writeHeaderLine();

        writeDataLines(listUsers);

        //chỉnh size các cột
//        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
//        autoResize(sheet, numberOfColumn);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void writeDataLines(List<User> listUser)
    {
        int rowIndex = 1 ;

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        for (User user : listUser)
        {
            XSSFRow row = sheet.createRow(rowIndex);

            int columnIndex = 0;
            createCell(row, columnIndex++, user.getId(),cellStyle);
            createCell(row, columnIndex++, user.getEmail(),cellStyle);
            createCell(row, columnIndex++, user.getFirstName(),cellStyle);
            createCell(row, columnIndex++, user.getLastName(),cellStyle);
            createCell(row, columnIndex++, user.getRoles().toString(),cellStyle);
            createCell(row, columnIndex, user.isEnabled(),cellStyle);

            rowIndex++;
        }


    }

//    private void autoResize(XSSFSheet sheet, int numberOfColumn) {
//        for (int i = 0; i < numberOfColumn; i++) {
//            sheet.autoSizeColumn(i);
//        }
//
//    }
}
