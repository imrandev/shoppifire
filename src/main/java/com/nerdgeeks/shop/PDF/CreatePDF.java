package com.nerdgeeks.shop.PDF;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.print.PrinterJob;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.io.*;
import java.util.Date;

public class CreatePDF {

    private static String FILE = "E:/FirstPdf.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);


    public static void Init() {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addMetaData(document);
            addContent(document);
            document.close();
            openPdf();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openPdf(){
        try {
            if ((new File(FILE)).exists()) {
                Process process = Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler "+FILE);
                process.waitFor();
            } else {
                System.out.println("File not found");
            }
            System.out.println("Done");
        } catch (Exception e) {
            System.out.println(":: -----Exception---- ::\n"+e);
        }
    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private static void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    private static void addContent(Document document)
            throws DocumentException {

        Chapter catPart = new Chapter( 1);

        Paragraph topPart = new Paragraph();
        topPart.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(topPart, 1);
        topPart.add(new Paragraph("Super Shop Supplier Report", catFont));
        addEmptyLine(topPart, 1);
        topPart.add(new Paragraph( "" + new Date(), smallBold));
        catPart.add(topPart);

        addEmptyLine(topPart, 4);
        createTable(catPart);

        Paragraph supplierDetails = new Paragraph();
        addEmptyLine(supplierDetails, 4);
        supplierDetails.add(new Paragraph("Purchased ID: ",subFont));
        addEmptyLine(supplierDetails, 1);
        supplierDetails.add(new Paragraph("Purchased Date: ",subFont));
        addEmptyLine(supplierDetails, 1);
        supplierDetails.add(new Paragraph("Supplier Name: ",subFont));
        addEmptyLine(supplierDetails, 1);
        supplierDetails.add(new Paragraph("Total Price: ",subFont));
        addEmptyLine(supplierDetails, 1);
        supplierDetails.add(new Paragraph("Paid: ",subFont));
        addEmptyLine(supplierDetails, 1);
        supplierDetails.add(new Paragraph("Due: ",subFont));
        addEmptyLine(supplierDetails, 1);
        catPart.add(supplierDetails);

        document.add(catPart);
    }

//    private static void addContent(Document document) throws DocumentException {
//
//        Anchor anchor = new Anchor("First Chapter", catFont);
//        anchor.setName("First Chapter");
//
//        // Second parameter is the number of the chapter
//        Chapter catPart = new Chapter(new Paragraph(anchor), 1);
//
//        Paragraph subPara = new Paragraph("Subcategory 1", subFont);
//        Section subCatPart = catPart.addSection(subPara);
//        subCatPart.add(new Paragraph("Hello"));
//
//        subPara = new Paragraph("Subcategory 2", subFont);
//        subCatPart = catPart.addSection(subPara);
//        subCatPart.add(new Paragraph("Paragraph 1"));
//        subCatPart.add(new Paragraph("Paragraph 2"));
//        subCatPart.add(new Paragraph("Paragraph 3"));
//
//        // add a list
//        createList(subCatPart);
//        Paragraph paragraph = new Paragraph();
//        addEmptyLine(paragraph, 5);
//        subCatPart.add(paragraph);
//
//        // add a table
//        createTable(subCatPart);
//
//        // now add all this to the document
//        document.add(catPart);
//
//        // Next section
//        anchor = new Anchor("Second Chapter", catFont);
//        anchor.setName("Second Chapter");
//
//        // Second parameter is the number of the chapter
//        catPart = new Chapter(new Paragraph(anchor), 1);
//
//        subPara = new Paragraph("Subcategory", subFont);
//        subCatPart = catPart.addSection(subPara);
//        subCatPart.add(new Paragraph("This is a very important message"));
//
//        // now add all this to the document
//        document.add(catPart);
//    }

    private static void createTable(Chapter chapter)
            throws BadElementException {
        PdfPTable table = new PdfPTable(2);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Product Name"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Quantity"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");

        chapter.add(table);

    }

    private static void createList(Chapter subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public static void printPDF() throws IOException {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
        FileInputStream fis = new FileInputStream(FILE);
        Doc pdfDoc = new SimpleDoc(fis, null, null);
        DocPrintJob printJob = printService.createPrintJob();
        try {
            printJob.print(pdfDoc, new HashPrintRequestAttributeSet());
        } catch (PrintException e) {
            e.printStackTrace();
        }
        fis.close();
    }
}