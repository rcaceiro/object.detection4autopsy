package pt.ipleiria.object_detection_autopsy.report;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.TskCoreException;
import pt.ipleiria.object_detection_autopsy.model.Detection;
import pt.ipleiria.object_detection_autopsy.model.ImageDetection;
import pt.ipleiria.object_detection_autopsy.model.VideoDetection;

public class ExcelExporter implements Closeable
{
 private final Sheet videoSheet;
 private final Sheet imageSheet;
 private final Workbook workbook;
 private final File file;

 public ExcelExporter(String path) throws IOException, InvalidFormatException
 {
  this.file = new File(path);
  this.workbook = new XSSFWorkbook();
  this.imageSheet = this.createOrGetSheet(SHEET_NAMES.IMAGE);
  this.videoSheet = this.createOrGetSheet(SHEET_NAMES.VIDEO);
 }

 public void write(BlackboardAttribute attribute) throws TskCoreException
 {
  List<AbstractFile> files = Case.getCurrentCase().getSleuthkitCase().findAllFilesWhere("obj_id = " + String.valueOf(attribute.getParentArtifact().getObjectID()));
  
  System.out.println("");
 }
 
 public void write(ImageDetection imageDetection, String filename)
 {
  if (imageDetection == null)
  {
   return;
  }
  Sheet sheet = this.workbook.getSheet(SHEET_NAMES.IMAGE);
  if (sheet == null)
  {
   sheet = this.workbook.createSheet(SHEET_NAMES.IMAGE);
   sheet.createFreezePane(0, 0);
  }
  short colIndex = (short) (sheet.getLeftCol() + 0x1);
  sheet.setActiveCell(new CellAddress(0, colIndex));
  Cell headerCell = sheet.getRow(0).getCell(colIndex);
  if (headerCell == null)
  {
   return;
  }
  headerCell.setCellValue(filename);
  Iterator<Row> rows = sheet.rowIterator();
  while (rows.hasNext())
  {
   Row row = rows.next();
   Cell firstCell = row.getCell(0);
   String cellValue = firstCell.getStringCellValue();
   Cell newCell = row.createCell(colIndex);
   newCell.setCellValue("");
   for (Detection detection : imageDetection.getDetections())
   {
    boolean foundRow = false;
    Iterator<Cell> cells = row.cellIterator();
    while (cells.hasNext())
    {
     Cell cell = cells.next();
     if (detection.getClassName().compareTo(cellValue) == 0)
     {
      cell.setCellValue("x");
      foundRow = true;
      break;
     }
     cell.setCellValue("");
    }
    if (!foundRow)
    {
     Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
     newRow.forEach((cell) ->
     {
      if (detection.getClassName().compareTo(cellValue) == 0)
      {
       cell.setCellValue("x");
      }
      else
      {
       cell.setCellValue("");
      }
     });
    }
   }
  }
 }

 public void writeOut() throws FileNotFoundException, IOException
 {
  this.workbook.write(new FileOutputStream(this.file));
 }

 @Override
 public void close() throws IOException
 {
  this.workbook.close();
 }

 private Sheet createOrGetSheet(String sheetName)
 {
  Sheet sheet = this.workbook.getSheet(sheetName);
  if (sheet == null)
  {
   sheet = this.workbook.createSheet(sheetName);
   sheet.createFreezePane(0, 0);
  }
  return sheet;
 }
 
 private class SHEET_NAMES
 {
  public final static String IMAGE = "image";
  public final static String VIDEO = "video";
 }
}
