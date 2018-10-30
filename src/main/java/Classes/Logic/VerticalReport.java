package Classes.Logic;

import Classes.Entities.FolderReport;
import Classes.FolderManager;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by SM David on 22/10/2018.
 */
public class VerticalReport {

    private String dir;
    private String export;
    private List<FolderReport> reports = null;
    private Logic logic = Logic.getInstance();

    public VerticalReport(String dir) {
        this.dir = dir;
        this.export = dir;
    }

    public VerticalReport(String dir,String export) {
        this.dir = dir;
        if(export == null)
            export = dir;
        this.export = export;
    }

    private List<FolderReport> generate() throws IOException {

        if(reports != null)
            return reports;

        List<Path> directories = logic.getFolders(dir,false,null);
        List<Path> files;

        List<FolderReport> report = new ArrayList<>();
        boolean hasCover =false;
        for (Path d:directories){

            hasCover = false;
            files = logic.getFiles(d.toString(),false);

            for(Path f:files){
                if(f.getFileName().toString().toLowerCase().contains("front")) {
                    hasCover = true;
                    break;
                }
            }

            report.add(new FolderReport(d.getFileName().toString(),hasCover,files.size(),getCreationDate(d.toString())));
        }

        return reports = report;
    }

    private Date getCreationDate(String dir) {

        try {
            URI folderRoute = new File(dir).toURI();
            Path p = Paths.get(folderRoute);
            BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class);
            FileTime a = attr.creationTime();
            return new Date(a.toMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void toExcel() throws IOException {
        List<FolderReport> reports = generate();

        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

          /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Report");

        /*
        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
         */

        // Create a Row
        int rowNum = 0;
        int colNum = 0;
        Row headerRow = sheet.createRow(rowNum++);


        String [] header = FolderReport.getHeader();
        for(String h:header){
            Cell cell = headerRow.createCell(colNum);
            cell.setCellValue(header[colNum++]);
        }


        // Create Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));


        for(FolderReport r:reports){
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(r.getName());
            row.createCell(1).setCellValue(r.getCover()?"YES":"NO");
            row.createCell(2).setCellValue(r.getTotal());

            Cell dateOfBirthCell = row.createCell(3);
            dateOfBirthCell.setCellValue(r.getModified());
            dateOfBirthCell.setCellStyle(dateCellStyle);

        }

        // Resize all columns to fit the content size
        for(int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(export+File.separator+"report.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }

    public void toJson() throws Exception{
        List<FolderReport> reports = generate();
        StringBuffer ans = new StringBuffer("{");
        boolean first = true;
        for(FolderReport f:reports){
            ans.append(first ? "" : ",").append(f.toJson());
            first = false;
        }
        PrintWriter writer = new PrintWriter(export+File.separator+"report.json", "UTF-8");
        writer.print(ans);
        writer.close();
    }
}
