package com.yangskull.admin.user.export;

import com.yangskull.admin.AbstractExporter;
import com.yangskull.common.entity.User;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPDFExporter extends AbstractExporter {

    public void export(List<User> listUser, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response,"application/pdf",".pdf", "user_");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());


        document.open();

        /* set font style */
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLACK);

        /*Add title */
        Paragraph paragraph = new Paragraph("LIST TO USERS", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        /* Add header table - with 6 column*/
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        //làm cho header và header table cách xa 1 tý
        table.setSpacingBefore(10);

        /*chỉnh độ rộng của các cột*/
        table.setWidths(new float[] {1.5f,3.5f,3.0f,3.0f,3.0f,1.5f});

        writeTableHeader(table);
        writeTableData(table,listUser);

        document.add(table);

        document.close();
    }

    private void writeTableData(PdfPTable table, List<User> listUser) {
        for(User user : listUser)
        {
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getEmail());
            table.addCell(user.getFirstName());
            table.addCell(user.getLastName());
            table.addCell(user.getRoles().toString());
            table.addCell(String.valueOf(user.isEnabled()));
        }
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.GRAY);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("User ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("E-mail", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("First Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Last Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Roles", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Enabled ", font));
        table.addCell(cell);
    }


}
