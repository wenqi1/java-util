import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.*;

public class ExcelUtil {

    /**
     * @param isXLS true为xls文件，false为xlsx文件
     * @param file excel文件
     * @param sheetName sheet表名，如果为null，默认第1张表
     * @return List
     * @throws Exception
     */
    public static List readExcel(boolean isXLS,File file,String sheetName) throws Exception{
        FileInputStream fis = null;
        Workbook workbook = null;

        try {
            //创建输入流
            fis = new FileInputStream(file);
            //封装一个sheet的数据
            ArrayList<List> s = new ArrayList<>();
            if(isXLS){
                workbook = new HSSFWorkbook(fis);
            }else{
                workbook = new XSSFWorkbook(fis);
            }

            //获取表对象
            Sheet sheet;
            if(sheetName == null){
                sheet = workbook.getSheetAt(0);
            }else{
                sheet = workbook.getSheet(sheetName);
            }

            //获取行数
            int rows = sheet.getLastRowNum();
            for(int j = 0; j <= rows; j++){
                Row row = sheet.getRow(j);
                //封装一行的数据
                List<String> r = new ArrayList<>();
                //迭代单元格
                row(sheet,r,row);
                s.add(r);
            }

            return s;
        }finally {
            if(workbook != null){
                workbook.close();
            }
            if(fis != null){
                fis.close();
            }
        }
    }

    /**
     * 导出excel
     * @param sheetName excel的表名
     * @param data excel的数据
     * @param file 导出的excel文件地址
     */
    public static void exportExcel(String sheetName, List<List<String>> data, File file) throws Exception{
        Workbook wb = null;
        FileOutputStream fileOutputStream = null;
        try {
            //创建一个HSSFWorkbook
            wb = new XSSFWorkbook();

            // 在workbook中添加一个sheet
            Sheet sheet = wb.createSheet(sheetName);

            //创建内容
            for(int i = 0; i < data.size(); i++){
                //向sheet添加行
                Row row = sheet.createRow(i);

                for(int j = 0; j < data.get(i).size(); j++){
                    //向row添加单元格
                    row.createCell(j).setCellValue(data.get(i).get(j));
                }
            }

            //创建输出流
            fileOutputStream = new FileOutputStream(file);
            wb.write(fileOutputStream);
        } finally {
            if(fileOutputStream != null){
                fileOutputStream.close();
            }

            if(wb != null){
                wb.close();
            }
        }

    }

    /**
     * 获取单元格数据
     * @param cell
     * @return
     */
    private static String cell(Cell cell){
        if(cell == null){
           return null;
        }

        switch (cell.getCellType()){
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue() ? "true" : "false";
            case NUMERIC:
                NumberFormat numberFormat = NumberFormat.getInstance();
                //禁用科学计数法
                numberFormat.setGroupingUsed(false);
                return numberFormat.format(cell.getNumericCellValue());
            default:
                return null;
        }
    }

    /**
     * 获取每行的数据
     * @param r 将一行的数据封装到list中
     * @param row
     */
    private static void row(Sheet sheet,List r,Row row){
        for (int k = 0; k < row.getLastCellNum(); k++) {
            //单元格数据
            String c;
            Cell cell = row.getCell(k);
            //判断是否为合并单元格
            CellRangeAddress mergedRegion = isMergedRegion(sheet, row.getRowNum(), k);
            //如果是合并单元格
            if(mergedRegion != null){
                //合并单元格的起始列
                int firstColumn = mergedRegion.getFirstColumn();
                //合并单元格的起始行
                int firstRow = mergedRegion.getFirstRow();
                c = cell(sheet.getRow(firstColumn).getCell(firstRow));
            }else{
                c = cell(cell);
            }

            r.add(c);
        }
    }

    /**
     * 判断单元格是否为合并单元格
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    private static CellRangeAddress isMergedRegion(Sheet sheet, int row , int column) {
        //获取合并单元格的数量
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            //获取合并单元格的范围地址
            CellRangeAddress range = sheet.getMergedRegion(i);
            //合并单元格的起始列
            int firstColumn = range.getFirstColumn();
            //合并单元格的结束列
            int lastColumn = range.getLastColumn();
            //合并单元格的起始行
            int firstRow = range.getFirstRow();
            //合并单元格的结束行
            int lastRow = range.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return range;
                }
            }
        }
        return null;
    }

}
