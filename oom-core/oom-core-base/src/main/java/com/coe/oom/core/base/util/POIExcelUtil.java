package com.coe.oom.core.base.util;

import com.coe.oom.core.base.exception.entity.CheckException;
import com.coe.oom.core.base.util.excel.ExcelHead;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class POIExcelUtil {


    public final static String yyyy_MM_ddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat format = new SimpleDateFormat(yyyy_MM_ddHHmmss);

    Logger logger = LoggerFactory.getLogger(POIExcelUtil.class);

    /**
     * 总行数
     */
    public int totalRows = 0;
    /**
     * 总列数
     */
    public int totalCells = 0;

    /**
     * 根据文件名读取excel文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public List<ArrayList<String>> readFile(String fileName) throws Exception {
        List<ArrayList<String>> dataLst = new ArrayList<ArrayList<String>>();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        // 检查文件是否存在
        File file = new File(fileName);
        if (file == null || !file.exists()) {
            return dataLst;
        }
        // 读取excel
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            // 根据版本选择创建Workbook的方式
            Workbook wb = null;
            if (fileType.equalsIgnoreCase("xls")) {
                wb = new HSSFWorkbook(inputStream);
            } else if (fileType.equalsIgnoreCase("xlsx")) {
                wb = new XSSFWorkbook(inputStream);
            }

            if (wb == null) {
                return dataLst;
            }
            dataLst = readWorkbook(wb);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.error("关闭流异常:" + e, e);
            }
        }
        return dataLst;
    }

    /**
     * 读取数据
     *
     * @param wb
     * @return
     */
    private List<ArrayList<String>> readWorkbook(Workbook wb) {
        List<ArrayList<String>> dataLst = new ArrayList<ArrayList<String>>();
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        if (this.totalRows >= 1 && sheet.getRow(0) != null) {
            // 列数
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        // 循环Excel的行
        for (int r = 0; r < this.totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            ArrayList<String> rowLst = new ArrayList<String>();
            // 循环Excel的列
            for (int cellIndex = 0; cellIndex < this.totalCells; cellIndex++) {
                Cell cell = row.getCell(cellIndex);
                String cellValue = "";
                if (cell == null) {
                    rowLst.add(cellValue);
                    continue;
                }
                // 处理数字型的,自动去零
                if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                    // 在excel里,日期也是数字,在此要进行判断
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        cellValue = DateUtil.dateConvertString(cell.getDateCellValue(), DateUtil.yyyy_MM_ddHHmmss);
                    } else {
                        if ("0.0".equals(cell.getNumericCellValue() + "")) {
                            cellValue = "0";
                        } else {
                            cellValue = StringUtil.getRightStr(cell.getNumericCellValue() + "");
                        }
                    }
                } else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {// 处理字符串型
                    cellValue = cell.getStringCellValue();
                } else if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {// 处理布尔型
                    cellValue = cell.getBooleanCellValue() + "";
                } else {// 其它数据类型
                    cellValue = cell.toString() + "";
                }
                rowLst.add(cellValue.trim());
            }
            dataLst.add(rowLst);
        }
        return dataLst;
    }

    public static CellStyle getStyleForHeader(SXSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);// 设置这些样式--表头
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        Font font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }


    public static CellStyle getStyleForBody(SXSSFWorkbook wb) {
        CellStyle style2 = wb.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        Font font2 = wb.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
        return style2;
    }


    private static ThreadLocal<SXSSFWorkbook> sxssfWorkbookThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<Sheet> sheetThreadLocal = new ThreadLocal<>();


    public static SXSSFWorkbook generateHeader(String sheetTitle, String[] headers) {
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(5000);
        Sheet sheet = sxssfWorkbook.createSheet();
        CellStyle style = getStyleForHeader(sxssfWorkbook);
        // 生成表头内容
        Row row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers[i]);
        }
        sxssfWorkbookThreadLocal.set(sxssfWorkbook);
        sheetThreadLocal.set(sheet);
        return sxssfWorkbook;
    }

    /**
     * @param rows
     * @param currPage 当前页码
     * @param pageSize 总页数
     */
    public static void fillBody(List<String[]> rows, int staartIndex) {
        int index = staartIndex;
        SXSSFWorkbook sxssfWorkbook = sxssfWorkbookThreadLocal.get();
        Sheet sheet = sheetThreadLocal.get();
        CellStyle styleForBody = getStyleForBody(sxssfWorkbook);
        for (int i = 0; i < rows.size(); i++) {
            String[] rowSingle = rows.get(i);
            Row row = sheet.createRow(index++);
            for (int j = 0; j < rowSingle.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(styleForBody);
                cell.setCellValue(rowSingle[j]);
            }
        }
    }

    public static void writeToFile(String filePath) throws FileNotFoundException {
        writeToStream(new FileOutputStream(filePath));
    }

    public static void writeToStream(OutputStream outputStream) {
        try {
            SXSSFWorkbook sxssfWorkbook = sxssfWorkbookThreadLocal.get();
            sxssfWorkbook.write(outputStream);
        } catch (Exception e) {
        } finally {
            try {
                outputStream.close();
            } catch (Exception e2) {
            }
            sheetThreadLocal.remove();
            sxssfWorkbookThreadLocal.remove();
        }

    }

    /**
     * 创建excel
     *
     * @param sheetTitle
     * @param headers
     * @param rows
     * @param filePathAndName
     * @return
     * @throws IOException
     */
    public static String createExcel(String sheetTitle, String[] headers, List<String[]> rows, String filePathAndName,
                                     Map<Integer, Integer>... maps) throws IOException {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(sheetTitle);
        // 设置表格的宽
        for (Map<Integer, Integer> map : maps) {
            for (Integer row : map.keySet()) {
                sheet.setColumnWidth(row, map.get(row));
            }
        }
        // 设置表格默认列宽15个字节
        sheet.setDefaultColumnWidth(15);

        // 生成表头样式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);// 设置这些样式--表头
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        HSSFCellStyle style3 = workbook.createCellStyle();
        style3.setFillForegroundColor(HSSFColor.WHITE.index);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font3 = workbook.createFont();
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style3.setFont(font3);
        HSSFDataFormat format = workbook.createDataFormat();
        style3.setDataFormat(format.getFormat("@"));

        // 生成表头内容
        HSSFRow hssfRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = hssfRow.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers[i]);
        }

        // 生成表头以外的内容
        int rowIndex = 1;
        for (String[] row : rows) {
            hssfRow = sheet.createRow(rowIndex);
            for (int i = 0; i < row.length; i++) {
                HSSFCell cell = hssfRow.createCell(i);
                if (NumberUtil.isDecimal(row[i]) && row[i].indexOf(".") > 0) {
                    cell.setCellStyle(style2);
                    cell.setCellValue(Double.valueOf(row[i]));
                } else if (NumberUtil.isPInteger(row[i]) && row[i].length() < 10) {// 32位机器,整型最大4294967296
                    cell.setCellStyle(style2);
                    cell.setCellValue(Integer.valueOf(row[i]));
                } else {
                    cell.setCellStyle(style3);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(row[i]);
                }
            }
            rowIndex++;
        }
        return writeHSSFWorkbook(workbook, filePathAndName);
    }

    public static Sheet createSheet(String sheetTitle, String[] headers, Map<Integer, Integer>... maps)
            throws IOException {
        // 声明一个工作薄
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        CellStyle headerStyle = StylesFactory.createHeaderStyle(workbook);

        // 生成一个表格
        Sheet sheet = workbook.createSheet(sheetTitle);

        // 设置表格的宽
        for (Map<Integer, Integer> map : maps) {
            for (Integer row : map.keySet()) {
                sheet.setColumnWidth(row, map.get(row));
            }
        }
        // 设置表格默认列宽15个字节
        sheet.setDefaultColumnWidth(15);
        // 为什么设置setDefaultColumnStyle会让列宽变成0？
        // sheet.setDefaultColumnStyle(6, cellStyleMap.get("string"));
        // 生成表头内容
        Row hssfRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = hssfRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(headers[i]);
        }

        return sheet;
    }

    public static Sheet addHeaders(Sheet sheet, int rowIndex, String[] headers) {
        CellStyle style = StylesFactory.createHeaderStyle(sheet.getWorkbook());
        Row hssfRow = sheet.createRow(rowIndex);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = hssfRow.createCell(i);
            cell.setCellStyle(style);
            if (null != headers[i])
                cell.setCellValue(headers[i]);
            int _i = i;
            while (null == headers[i + 1]) {
                i++;
            }
            // 首行、最后一行、首列、最后一列
            if (i > _i)
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, _i, i));
        }
        return sheet;
    }

    public static Sheet addRows(Sheet sheet, int rowIndex, List<Object[]> rows) {
        CellStyle stringStyle = StylesFactory.createStringStyle(sheet.getWorkbook());
        // 生成表头以外的内容
        for (Object[] row : rows) {
            Row hssfRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < row.length; i++) {
                /*
                 * if (row[i] instanceof Number) {
                 * cell.setCellStyle(cellStyleMap.get("number"));
                 * cell.setCellValue(row[i]+"");
                 * cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC); }else {
                 */
                Cell cell = hssfRow.createCell(i);
                cell.setCellStyle(stringStyle);
                // cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                if (null != row[i]){
                    //cell.setCellValue(row[i] + "");
                    Object val=row[i];
                    if (val instanceof Double) {
                        cell.setCellValue((Double) val);
                    } else if (val instanceof Integer) {
                        cell.setCellValue((Integer) val);
                    } else if (val instanceof Date) {
                        cell.setCellValue(format.format((Date) val));
                    } else if (val instanceof Boolean) {
                        cell.setCellValue((Boolean) val);
                    }else if(val instanceof  BigDecimal){
                        if (NumberUtil.isDecimal(val.toString()) && val.toString().indexOf(".") > 0) {
                            cell.setCellValue(Double.valueOf(val.toString()));
                        } else if (NumberUtil.isNumberic(val.toString()) && val.toString().length() < 10) {// 32位机器,整型最大4294967296
                            cell.setCellValue(Integer.valueOf(val.toString()));
                        }
                    } else if (val instanceof String) {
                        cell.setCellValue((String) val);
                    } else {
                        cell.setCellValue((String) val);
                    }
                }
                /* } */
            }
        }
        return sheet;
    }

    public static Sheet addRows(Sheet sheet, List<Object[]> rows) {
        int rowIndex = sheet.getLastRowNum() + 1;
        return addRows(sheet, rowIndex, rows);
    }

    /*
     * public static Cell setData(Sheet sheet, Object data, int rownum, int
     * cellnum) { Row row = null==sheet.getRow(rownum) ? sheet.createRow(rownum)
     * : sheet.getRow(rownum); Cell cell = null==row.getCell(cellnum) ?
     * row.createCell(cellnum) : row.getCell(cellnum);
     * cell.setCellStyle(cellStyleTl.get().get("string")); if(null!=data)
     * cell.setCellValue(data+""); return cell; }
     */

    public static String saveToFile(Sheet sheet, String filePathAndName) throws IOException {
        Workbook workbook = sheet.getWorkbook();
        return writeWorkbook(workbook, filePathAndName);
    }

    /**
     * 生成特定样式:随货清单
     *
     * @param sheetTitle
     * @param headers
     * @param rows
     * @param filePathAndName
     * @param maps
     * @return
     * @throws IOException
     */
    public static String createExcelGoodsList(String sheetTitle, String[] headers, List<String[]> rows,
                                              String filePathAndName, Map<Integer, Integer>... maps) throws IOException {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(sheetTitle);
        // 设置表格的宽
        for (Map<Integer, Integer> map : maps) {
            for (Integer row : map.keySet()) {
                sheet.setColumnWidth(row, map.get(row));
            }
        }
        // 设置表格默认列宽15个字节
        sheet.setDefaultColumnWidth(15);

        // 生成表头样式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        HSSFCellStyle style3 = workbook.createCellStyle();
        style3.setFillForegroundColor(HSSFColor.WHITE.index);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font3 = workbook.createFont();
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style3.setFont(font3);
        HSSFDataFormat format = workbook.createDataFormat();
        style3.setDataFormat(format.getFormat("@"));

        HSSFCellStyle style4 = workbook.createCellStyle();
        style4.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);// 设置这些样式--表头
        style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style4.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style4.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font4 = workbook.createFont();
        font4.setColor(HSSFColor.BLACK.index);
        font4.setFontHeightInPoints((short) 11);
        font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style4.setFont(font4);
        // 生成表头内容
        HSSFRow hssfRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = hssfRow.createCell(i);
            cell.setCellStyle(style2);
            cell.setCellValue(headers[i]);
        }

        // 生成表头以外的内容
        int rowIndex = 1;
        for (String[] row : rows) {
            hssfRow = sheet.createRow(rowIndex);
            for (int i = 0; i < row.length; i++) {
                HSSFCell cell = hssfRow.createCell(i);
                if (rowIndex == 2) {
                    cell.setCellStyle(style4);
                    cell.setCellValue(row[i]);
                    continue;
                }
                if (NumberUtil.isDecimal(row[i]) && row[i].indexOf(".") > 0) {
                    cell.setCellStyle(style2);
                    cell.setCellValue(Double.valueOf(row[i]));
                } else if (NumberUtil.isPInteger(row[i]) && row[i].length() < 10) {// 32位机器,整型最大4294967296
                    cell.setCellStyle(style2);
                    cell.setCellValue(Integer.valueOf(row[i]));
                } else {
                    cell.setCellStyle(style3);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(row[i]);
                }
            }
            rowIndex++;
        }
        return writeHSSFWorkbook(workbook, filePathAndName);

    }

    /**
     * 生成特定样式:发票箱单
     *
     * @param sheetTitle
     * @param headers
     * @param rows
     * @param filePathAndName
     * @param maps
     * @return
     * @throws IOException
     */
    public static String createExcelTwoPackageNo(String sheetTitle, String[] headers, List<String[]> rows,
                                                 String sheetTitle2, String[] headers2, List<String[]> rows2, String filePathAndName) throws IOException {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(sheetTitle);
        // 设置表格默认列宽15个字节
        sheet.setDefaultColumnWidth(15);
        // 生成一个表格2
        HSSFSheet sheet2 = workbook.createSheet(sheetTitle2);
        // 设置表格默认列宽15个字节
        sheet2.setDefaultColumnWidth(15);

        // 生成表头样式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        HSSFCellStyle style3 = workbook.createCellStyle();
        style3.setFillForegroundColor(HSSFColor.WHITE.index);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font3 = workbook.createFont();
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style3.setFont(font3);
        HSSFDataFormat format = workbook.createDataFormat();
        style3.setDataFormat(format.getFormat("@"));

        HSSFCellStyle style4 = workbook.createCellStyle();
        style4.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);// 设置这些样式--表头
        style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style4.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style4.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font4 = workbook.createFont();
        font4.setColor(HSSFColor.BLACK.index);
        font4.setFontHeightInPoints((short) 11);
        font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style4.setFont(font4);

        // 生成表头内容
        HSSFRow hssfRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = hssfRow.createCell(i);
            cell.setCellStyle(style2);
            cell.setCellValue(headers[i]);
        }

        // 生成表头以外的内容
        int rowIndex = 1;
        for (String[] row : rows) {
            hssfRow = sheet.createRow(rowIndex);
            for (int i = 0; i < row.length; i++) {
                HSSFCell cell = hssfRow.createCell(i);
                if (rowIndex == 11) {
                    cell.setCellStyle(style4);
                    cell.setCellValue(row[i]);
                    continue;
                }
                if (NumberUtil.isDecimal(row[i]) && row[i].indexOf(".") > 0) {
                    cell.setCellStyle(style2);
                    cell.setCellValue(Double.valueOf(row[i]));
                } else if (NumberUtil.isPInteger(row[i]) && row[i].length() < 10) {// 32位机器,整型最大4294967296
                    cell.setCellStyle(style2);
                    cell.setCellValue(Integer.valueOf(row[i]));
                } else {
                    cell.setCellStyle(style3);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(row[i]);
                }
            }
            rowIndex++;
        }
        // 生成表头内容
        HSSFRow hssfRow2 = sheet2.createRow(0);
        for (int i = 0; i < headers2.length; i++) {
            HSSFCell cell = hssfRow2.createCell(i);
            cell.setCellStyle(style2);
            cell.setCellValue(headers2[i]);
        }

        // 生成表头以外的内容
        int rowIndex2 = 1;
        for (String[] row : rows2) {
            hssfRow2 = sheet2.createRow(rowIndex2);
            for (int i = 0; i < row.length; i++) {
                HSSFCell cell = hssfRow2.createCell(i);
                if (rowIndex2 == 12) {
                    cell.setCellStyle(style4);
                    cell.setCellValue(row[i]);
                    continue;
                }
                if (NumberUtil.isDecimal(row[i]) && row[i].indexOf(".") > 0) {
                    cell.setCellStyle(style2);
                    cell.setCellValue(Double.valueOf(row[i]));
                } else if (NumberUtil.isPInteger(row[i]) && row[i].length() < 10) {// 32位机器,整型最大4294967296
                    cell.setCellStyle(style2);
                    cell.setCellValue(Integer.valueOf(row[i]));
                } else {
                    cell.setCellStyle(style3);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(row[i]);
                }
            }
            rowIndex2++;
        }
        return writeHSSFWorkbook(workbook, filePathAndName);
    }

    /**
     * 创建excel两个sheet
     *
     * @param sheetTitle
     * @param headers
     * @param rows
     * @param sheetTitle2
     * @param headers2
     * @param rows2
     * @param filePathAndName
     * @return
     * @throws IOException
     */
    public static String createExcelTwo(String sheetTitle, String[] headers, List<String[]> rows, String sheetTitle2,
                                        String[] headers2, List<String[]> rows2, String filePathAndName) throws IOException {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(sheetTitle);
        // 设置表格默认列宽15个字节
        sheet.setDefaultColumnWidth(15);
        // 生成一个表格2
        HSSFSheet sheet2 = workbook.createSheet(sheetTitle2);
        // 设置表格默认列宽15个字节
        sheet2.setDefaultColumnWidth(15);

        // 生成表头样式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);// 设置这些样式--表头
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        HSSFCellStyle style3 = workbook.createCellStyle();
        style3.setFillForegroundColor(HSSFColor.WHITE.index);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font3 = workbook.createFont();
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style3.setFont(font3);
        HSSFDataFormat format = workbook.createDataFormat();
        style3.setDataFormat(format.getFormat("@"));

        // 生成表头内容
        HSSFRow hssfRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = hssfRow.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers[i]);
        }

        // 生成表头以外的内容
        int rowIndex = 1;
        for (String[] row : rows) {
            hssfRow = sheet.createRow(rowIndex);
            for (int i = 0; i < row.length; i++) {
                HSSFCell cell = hssfRow.createCell(i);
                if (NumberUtil.isDecimal(row[i]) && row[i].indexOf(".") > 0) {
                    cell.setCellStyle(style2);
                    cell.setCellValue(Double.valueOf(row[i]));
                } else if (NumberUtil.isPInteger(row[i]) && row[i].length() < 10) {// 32位机器,整型最大4294967296
                    cell.setCellStyle(style2);
                    cell.setCellValue(Integer.valueOf(row[i]));
                } else {
                    cell.setCellStyle(style3);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(row[i]);
                }
            }
            rowIndex++;
        }
        // 生成表头内容
        HSSFRow hssfRow2 = sheet2.createRow(0);
        for (int i = 0; i < headers2.length; i++) {
            HSSFCell cell = hssfRow2.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers2[i]);
        }

        // 生成表头以外的内容
        int rowIndex2 = 1;
        for (String[] row : rows2) {
            hssfRow2 = sheet2.createRow(rowIndex2);
            for (int i = 0; i < row.length; i++) {
                HSSFCell cell = hssfRow2.createCell(i);
                if (NumberUtil.isDecimal(row[i]) && row[i].indexOf(".") > 0) {
                    cell.setCellStyle(style2);
                    cell.setCellValue(Double.valueOf(row[i]));
                } else if (NumberUtil.isPInteger(row[i]) && row[i].length() < 10) {// 32位机器,整型最大4294967296
                    cell.setCellStyle(style2);
                    cell.setCellValue(Integer.valueOf(row[i]));
                } else {
                    cell.setCellStyle(style3);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(row[i]);
                }
            }
            rowIndex2++;
        }
        return writeHSSFWorkbook(workbook, filePathAndName);
    }

    /**
     * 创建excel 样式2
     *
     * @param sheetTitle
     * @param headers
     * @param rows
     * @param filePathAndName
     * @return
     * @throws IOException
     */
    public static String createExcel2(String sheetTitle, String[] headers, List<String[]> rows, String filePathAndName)
            throws IOException {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(sheetTitle);
        // 设置表格默认列宽15个字节
        sheet.setDefaultColumnWidth(28);
        // 生成表头样式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);// 设置这些样式--表头
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
        style.setWrapText(true);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 13);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        HSSFCellStyle style3 = workbook.createCellStyle();
        style3.setFillForegroundColor(HSSFColor.WHITE.index);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font3 = workbook.createFont();
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style3.setFont(font3);
        HSSFDataFormat format = workbook.createDataFormat();
        style3.setDataFormat(format.getFormat("@"));

        // 生成表头内容
        HSSFRow hssfRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = hssfRow.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers[i]);
        }

        // 生成表头以外的内容
        int rowIndex = 1;
        for (String[] row : rows) {
            hssfRow = sheet.createRow(rowIndex);
            for (int i = 0; i < row.length; i++) {
                HSSFCell cell = hssfRow.createCell(i);
                if (NumberUtil.isDecimal(row[i]) && row[i].indexOf(".") > 0) {
                    cell.setCellStyle(style2);
                    cell.setCellValue(Double.valueOf(row[i]));
                } else if (NumberUtil.isNumberic(row[i]) && row[i].length() < 10) {// 32位机器,整型最大4294967296
                    cell.setCellStyle(style2);
                    cell.setCellValue(Integer.valueOf(row[i]));
                } else {
                    cell.setCellStyle(style3);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(row[i]);
                }
            }
            rowIndex++;
        }
        return writeHSSFWorkbook(workbook, filePathAndName);
    }

    /**
     * 设置单元格字体加粗ay 2017年5月15日
     *
     * @param workbook
     * @param cell
     */
    private static void setTextBoldweight(HSSFWorkbook workbook, boolean isCenter, HSSFCell... cell) {
        // 为Hsg添加样式
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        if (isCenter) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        }
        for (HSSFCell hssfCell : cell) {
            hssfCell.setCellStyle(style);
        }
    }

    /**
     * 设置多个单元格格式 2017年5月15日
     *
     * @param workbook
     * @param cell
     */
    private static void setCellsStyle(HSSFRow row, int start, int end, CellStyle style) {
        for (int i = start; i <= end; i++) {
            row.getCell(i).setCellStyle(style);
        }
    }

    public static void createBlanlkCell(int rowIndex, HSSFSheet sheet) {
        HSSFRow hssfRow4 = sheet.createRow(rowIndex);
        HSSFCell hssfRow4_cell0 = hssfRow4.createCell(0);
        hssfRow4_cell0.setCellValue("");
        HSSFCell hssfRow4_cell1 = hssfRow4.createCell(1);
        hssfRow4_cell1.setCellValue("");
        HSSFCell hssfRow4_cell2 = hssfRow4.createCell(2);
        hssfRow4_cell2.setCellValue("");
        HSSFCell hssfRow4_cell3 = hssfRow4.createCell(3);
        hssfRow4_cell3.setCellValue("");
        HSSFCell hssfRow4_cell4 = hssfRow4.createCell(4);
        hssfRow4_cell4.setCellValue("");
        HSSFCell hssfRow4_cell5 = hssfRow4.createCell(5);
        hssfRow4_cell5.setCellValue("");
        HSSFCell hssfRow4_cell6 = hssfRow4.createCell(6);
        hssfRow4_cell6.setCellValue("");
        // HSSFCell hssfRow4_cell7 = hssfRow4.createCell(7);
        // hssfRow4_cell7.setCellValue("");
    }




    /**
     * @param workbook
     * @param bold             是否加粗
     * @param horizontalCenter 是否水平居中
     * @param verticalCenter   是否垂直居中
     * @return
     */
    public static CellStyle getStyle(HSSFWorkbook workbook, boolean bold, Short horizontalCenter,
                                     boolean verticalCenter, boolean needborder, HSSFFont useFont) {
        HSSFFont font = workbook.createFont();
        if (useFont != null) {
            font = useFont;
        } else {
            font.setFontHeightInPoints((short) 10);
            font.setFontName("Times New Roman");
        }
        CellStyle style = workbook.createCellStyle();
        if (bold) {
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
        }
        if (horizontalCenter == null) {
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 设置居中
        } else {
            style.setAlignment(horizontalCenter); // 设置居中
        }
        if (verticalCenter) {
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中
        }
        if (needborder) {
            style.setBorderTop(HSSFCellStyle.BORDER_THIN); // 设置上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        }
        style.setFont(font);
        style.setWrapText(true);
        return style;
    }

    public static void createBlanlkCell2(int rowIndex, HSSFSheet sheet) {
        HSSFRow hssfRow4 = sheet.createRow(rowIndex);
        for (int i = 0; i < 8; i++) {
            HSSFCell hssfRow4_cell0 = hssfRow4.createCell(i);
            hssfRow4_cell0.setCellValue("");
        }
    }

    public static void createBlanlkCell3(int rowIndex, int cellCount, HSSFSheet sheet) {
        HSSFRow hssfRow4 = sheet.createRow(rowIndex);
        for (int i = 0; i < cellCount; i++) {
            HSSFCell hssfRow4_cell0 = hssfRow4.createCell(i);
            hssfRow4_cell0.setCellValue("");
        }
    }

    public static void export(String fileName, List<ModelExport> modelExport, OutputStream outputStream) {

        fileName = fileName + "-" + System.currentTimeMillis() + ".xls";
        try {
            HSSFWorkbook hSSFWorkbook = createWorkbook(fileName, modelExport);
            hSSFWorkbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HSSFWorkbook createWorkbook(String fileName, List<ModelExport> bathExport) {

        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        for (ModelExport modelExport : bathExport) {

            String[] headers = modelExport.getHeads();
            List<String[]> rows = modelExport.getRows();

            String sheetTitle = modelExport.getTitle();
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet(sheetTitle);
            // 设置表格默认列宽15个字节
            sheet.setDefaultColumnWidth(32);

            // 生成表头样式
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(HSSFColor.GREEN.index);// 设置这些样式--表头
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            // 生成一个字体
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFColor.BLACK.index);
            font.setFontHeightInPoints((short) 11);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            // 把字体应用到当前的样式
            style.setFont(font);

            // 生成并设置另一个样式
            HSSFCellStyle style2 = workbook.createCellStyle();
            style2.setFillForegroundColor(HSSFColor.WHITE.index);
            style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            // 生成另一个字体
            HSSFFont font2 = workbook.createFont();
            font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
            // 把字体应用到当前的样式
            style2.setFont(font2);

            HSSFCellStyle style3 = workbook.createCellStyle();
            style3.setFillForegroundColor(HSSFColor.WHITE.index);
            style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            // 生成另一个字体
            HSSFFont font3 = workbook.createFont();
            font3.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
            // 把字体应用到当前的样式
            style3.setFont(font3);
            HSSFDataFormat format = workbook.createDataFormat();
            style3.setDataFormat(format.getFormat("@"));

            HSSFRow hssfRow = null;

            if (modelExport.getHeadIndex() == null) {
                modelExport.setHeadIndex(0);
            }
            // 生成 内容
            int rowIndex = 0;
            for (String[] row : rows) {
                hssfRow = sheet.createRow(rowIndex);

                // 生成表头内容
                if (modelExport.getHeadIndex().intValue() == rowIndex) {
                    for (int i = 0; i < headers.length; i++) {
                        HSSFCell cell = hssfRow.createCell(i);
                        cell.setCellStyle(style);
                        cell.setCellValue(headers[i]);
                    }
                    hssfRow = sheet.createRow(++rowIndex);
                }
                for (int i = 0; i < row.length; i++) {
                    HSSFCell cell = hssfRow.createCell(i);
                    cell.setCellStyle(style3);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(row[i]);
                }

                rowIndex++;
            }

        }

        return workbook;

    }

    public static void main(String[] args) throws IOException {
//        Sheet s = createSheet("sheet title", new String[]{"1"});
//        s.createRow(4);
//        System.out.println(s.getLastRowNum());

        Object a = "123";
        //OutOrderImport

    }

    static class StylesFactory {
        public static CellStyle createHeaderStyle(Workbook workbook) {
            CellStyle header = workbook.createCellStyle();
            header.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);// 设置这些样式--表头
            header.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            header.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            header.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            header.setBorderRight(HSSFCellStyle.BORDER_THIN);
            header.setBorderTop(HSSFCellStyle.BORDER_THIN);
            header.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            // 生成一个字体
            Font font = workbook.createFont();
            font.setColor(HSSFColor.BLACK.index);
            font.setFontHeightInPoints((short) 11);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            // 把字体应用到当前的样式
            header.setFont(font);
            return header;
        }

        public static CellStyle createNumberStyle(Workbook workbook) {
            // 生成并设置另一个样式
            CellStyle number = workbook.createCellStyle();
            number.setFillForegroundColor(HSSFColor.WHITE.index);
            number.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            number.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            number.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            number.setBorderRight(HSSFCellStyle.BORDER_THIN);
            number.setBorderTop(HSSFCellStyle.BORDER_THIN);
            number.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            number.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            // 生成另一个字体
            Font font2 = workbook.createFont();
            font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
            // 把字体应用到当前的样式
            number.setFont(font2);
            return number;
        }

        public static CellStyle createStringStyle(Workbook workbook) {
            CellStyle string = workbook.createCellStyle();
            string.setFillForegroundColor(HSSFColor.WHITE.index);
            string.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            string.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            string.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            string.setBorderRight(HSSFCellStyle.BORDER_THIN);
            string.setBorderTop(HSSFCellStyle.BORDER_THIN);
            string.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            string.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            // 生成另一个字体
            Font font3 = workbook.createFont();
            font3.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
            // 把字体应用到当前的样式
            string.setFont(font3);
            DataFormat format = workbook.createDataFormat();
            string.setDataFormat(format.getFormat("@"));
            return string;
        }
    }


    private static String writeHSSFWorkbook(HSSFWorkbook workbook, String filePathAndName) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(filePathAndName);
            workbook.write(out);
        } catch (IOException e) {
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePathAndName;
    }

    private static String writeWorkbook(Workbook workbook, String filePathAndName) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(filePathAndName);
            workbook.write(out);
        } catch (IOException e) {
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePathAndName;
    }

    /***
     *
     * @param filePath  文件路径
     * @param c   实体类
     * @param fieldRow  对应实体类字段的 所在行 该行下面一定是值
     *                  如  第一行 订单信息
     *                      第二行 序号                  客户订单号       结算方账号
     *                      第三行 （对应实体类字段的） No                   CustomerOrderNo  BillingAccount
     *                     始终从  对应实体类字段的 的下一行开始读取 value
     *
     * @param <T>
     * @return
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws ParseException
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> List<T> readExcel(String filePath, T c, int fieldRow)
            throws SecurityException, IllegalArgumentException, ParseException, IOException, InstantiationException {
        List<T> list = new ArrayList<T>();

        if (filePath != null && !"".equals(filePath)) {
            File file = new File(filePath);
            if (!file.exists()) {
                return list;
            }
            String fileName = file.getName();
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            InputStream in = new FileInputStream(file);

            Workbook wb = null;
            if (fileType.equalsIgnoreCase("xls")) {
                wb = new HSSFWorkbook(in);
            }
            if (fileType.equalsIgnoreCase("xlsx")) {
                wb = new XSSFWorkbook(in);
            }
            if (wb == null) {
                return list;
            }
            Sheet sheet = wb.getSheetAt(0);// 只读取一个sheet
            if (sheet == null) {
                return list;
            }
            int rwoCount = sheet.getPhysicalNumberOfRows();
            if (rwoCount <= 0) {
                return list;
            }
            Map<Integer, ExcelHead> mapping = new HashMap<Integer, ExcelHead>();
            if (fieldRow == 0) {
                throw new CheckException("请检查excel模板是否正确");
            }
            Row fieldCnRow = sheet.getRow(fieldRow - 1);// 对应类字段中文含义所在行
            for (int i = 0; i < fieldCnRow.getLastCellNum(); i++) {
                Cell cell = fieldCnRow.getCell(i);
                if (cell != null) {
                    String fieldName = cell.getStringCellValue();
                    ExcelHead excelHead = new ExcelHead();
                    excelHead.setFiledCn(fieldName);
                    mapping.put(i, excelHead);
                }
            }

            Row fieldEnRow = sheet.getRow(fieldRow);// 对应类字段所在行
            for (int i = 0; i < fieldEnRow.getLastCellNum(); i++) {
                Cell cell = fieldEnRow.getCell(i);
                if (cell != null) {
                    ExcelHead excelHead = mapping.get(i);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String fieldName = cell.getStringCellValue();
                    excelHead.setFiledCnOld(fieldName);
                    fieldName = StringUtil.toOntRowName(fieldName.toLowerCase().trim());//转成驼峰命名
                    excelHead.setFiledEn(fieldName);
                    mapping.put(i, excelHead);
                }
            }

            fieldRow = fieldRow + 1;//读取下一行

            for (int i = fieldRow; i < rwoCount; i++) {
                Row rows = sheet.getRow(i);
                Class cl = c.getClass();
                T obj = null;
                try {
                    obj = (T) cl.newInstance();
                } catch (IllegalAccessException e) {
                    throw new CheckException("程序异常");
                }
                if (rows == null) {
                    continue;
                }
                for (int j = 0; j < rows.getLastCellNum(); j++) {
                    Cell cell = rows.getCell(j);
                    if (cell == null) {
                        continue;
                    }
                    ExcelHead excelHead = mapping.get(j);
                    if(excelHead==null){
                        continue;
                    }
                    Field field = null;
                    try {
                        field = cl.getDeclaredField(excelHead.getFiledEn());
                    } catch (NoSuchFieldException e) {
                        throw new CheckException("excel中该列" + mapping.get(j).getFiledCnOld() + "不存在请检查excel模板是否正确");
                    }
                    if (field != null) {
                        field.setAccessible(true);
                        Object objectVa = null;

                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                objectVa = rows.getCell(j).getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:

                                objectVa = rows.getCell(j).getNumericCellValue();
                                // 在excel里,日期也是数字,在此要进行判断
                                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    Date d = cell.getDateCellValue();
                                    objectVa = format.format(d);
                                } else {
                                    if ("0.0".equals(cell.getNumericCellValue() + "")) {
                                        objectVa = "0";
                                    } else {
                                        objectVa = getRightStr(cell.getNumericCellValue() + "");
                                    }
                                }
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                objectVa = rows.getCell(j).getBooleanCellValue();
                                break;
                            case Cell.CELL_TYPE_BLANK:
                                objectVa = rows.getCell(j).getStringCellValue();
                                break;
                            default:
                                objectVa = rows.getCell(j).toString() + "";
                                break;
                        }
                        
                        
                        
                        
                        if(null!=objectVa&&!StringUtil.isEqual(objectVa.toString(), "")) {
	                        try {
	                            if (field.getType() == Date.class) {
	                                try {
	                                		field.set(obj, DateUtil.stringConvertDate(objectVa.toString(), yyyy_MM_ddHHmmss));
	                                    //objectVa = format.parse(objectVa.toString());
	                                } catch (Exception e) {
	                                    throw new CheckException("excel中第" + (i+1) + "行，第" + (j+1) + "列日期格式错误");
	                                }
	                            } else if (field.getType() == BigDecimal.class) {
	                                field.set(obj, new BigDecimal(objectVa.toString()));
	                            } else if (field.getType() == Double.class) {
	                                field.set(obj, new Double(objectVa.toString()));
	                            } else if (field.getType() == Integer.class) {
	                                field.set(obj, new Integer(objectVa.toString()));
	                            } else if (field.getType() == int.class) {
	                                if (objectVa != null && !"".equals(objectVa)) {
	                                    int temp = Integer.parseInt(objectVa + "");
	                                    field.set(obj, temp);
	                                } else {
	                                    field.set(obj, 0);
	                                }
	                            } else {
	                                field.set(obj, objectVa.toString());
	                            }
	                        } catch (Exception e) {
	                            throw new CheckException("excel中第" + (i+1) + "行，第" + (j+1) + "数据格式错误");
	                        }
                        }
                    }
                }
                list.add(obj);
            }
        }
        return list;
    }


    /**
     * 创建excel 自动根据类型来填充值
     *
     * @param sheetTitle
     * @param headers
     * @param rows
     * @param filePathAndName
     * @return
     * @throws IOException
     */
    public static String createExceFromObject(String sheetTitle, String[] headers, List<Object[]> rows, String filePathAndName)
            throws IOException {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(sheetTitle);
        // 设置表格默认列宽15个字节
        sheet.setDefaultColumnWidth(28);
        // 生成表头样式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);// 设置这些样式--表头
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
        style.setWrapText(true);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 13);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        HSSFCellStyle style3 = workbook.createCellStyle();
        style3.setFillForegroundColor(HSSFColor.WHITE.index);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font3 = workbook.createFont();
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style3.setFont(font3);
//        HSSFDataFormat format = workbook.createDataFormat();
//        style3.setDataFormat(format.getFormat("@"));

        // 生成表头内容
        HSSFRow hssfRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = hssfRow.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers[i]);
        }

        // 生成表头以外的内容
        int rowIndex = 1;


        for (Object[] objs : rows) {
            Row row = sheet.createRow((short) rowIndex);
            for (int j = 0; j < objs.length; j++) {
                Cell cell = row.createCell(j);
                Object val = objs[j];

                if(val==null){
                    cell.setCellValue("");
                    continue;
                }

                if (val instanceof Double) {
                    cell.setCellValue((Double) val);
                } else if (val instanceof Integer) {
                    cell.setCellValue((Integer) val);
                } else if (val instanceof Date) {
                    cell.setCellStyle(style3);
                    cell.setCellValue(format.format((Date) val));
                } else if (val instanceof Boolean) {
                    cell.setCellValue((Boolean) val);
                }else if(val instanceof  BigDecimal){
                    if (NumberUtil.isDecimal(objs[j].toString()) && objs[j].toString().indexOf(".") > 0) {
                        cell.setCellStyle(style2);
                        cell.setCellValue(Double.valueOf(objs[j].toString()));
                    } else if (NumberUtil.isNumberic(objs[j].toString()) && objs[j].toString().length() < 10) {// 32位机器,整型最大4294967296
                        cell.setCellStyle(style2);
                        cell.setCellValue(Integer.valueOf(objs[j].toString()));
                    }
                } else if (val instanceof String) {
                    cell.setCellValue((String) val);
                } else {
                    cell.setCellValue((String) val);
                }

                cell.setCellStyle(style);
            }
            rowIndex++;
        }
        return writeHSSFWorkbook(workbook, filePathAndName);
    }

    /**
     * 去除整数后的000. 非0 不能去掉
     *
     * @param sNum
     * @return
     */
    public static String getRightStr(String sNum) {
        DecimalFormat decimalFormat = new DecimalFormat("#.000000");
        String resultStr = decimalFormat.format(new Double(sNum));
        if (resultStr.matches("^[-+]?\\d+\\.[0]+$")) {
            resultStr = resultStr.substring(0, resultStr.indexOf("."));
        }
        return resultStr;
    }




    
    
    
    /**
     * 根据文件名读取excel文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public  Map<String, List<ArrayList<String>>>  readFiles(String fileName) throws Exception {
    	Map<String, List<ArrayList<String>>> dataLst = new HashMap<String, List<ArrayList<String>>>();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        // 检查文件是否存在
        File file = new File(fileName);
        if (file == null || !file.exists()) {
            return dataLst;
        }
        // 读取excel
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            // 根据版本选择创建Workbook的方式
            Workbook wb = null;
            if (fileType.equalsIgnoreCase("xls")) {
                wb = new HSSFWorkbook(inputStream);
            } else if (fileType.equalsIgnoreCase("xlsx")) {
                wb = new XSSFWorkbook(inputStream);
            }

            if (wb == null) {
                return dataLst;
            }
            dataLst = readWorkbooks(wb);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.error("关闭流异常:" + e, e);
            }
        }
        return dataLst;
    }

    /**
     * 读取数据
     *
     * @param wb
     * @return
     */
    private Map<String, List<ArrayList<String>>> readWorkbooks(Workbook wb) {
    
    	Map<String, List<ArrayList<String>>> map = new HashMap<String, List<ArrayList<String>>>();
    	int count=wb.getNumberOfSheets();
    	
     	for(int i=0;i<count;i++){
     		List<ArrayList<String>> dataLst = new ArrayList<ArrayList<String>>();
		
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(i);
        // 行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        if (this.totalRows >= 1 && sheet.getRow(0) != null) {
            // 列数
        	this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        	
        // 循环Excel的行
        for (int r = 0; r < this.totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            ArrayList<String> rowLst = new ArrayList<String>();
            // 循环Excel的列
            for (int cellIndex = 0; cellIndex < this.totalCells; cellIndex++) {
                Cell cell = row.getCell(cellIndex);
                String cellValue = "";
                if (cell == null) {
                    rowLst.add(cellValue);
                    continue;
                }
                
                // 处理数字型的,自动去零
                if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                    // 在excel里,日期也是数字,在此要进行判断
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        cellValue = DateUtil.dateConvertString(cell.getDateCellValue(), DateUtil.yyyy_MM_ddHHmmss);
                    } else {
                        if ("0.0".equals(cell.getNumericCellValue() + "")) {
                            cellValue = "0";
                        } else {
                            cellValue = StringUtil.getRightStr(cell.getNumericCellValue() + "");
                        }
                    }
                } else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {// 处理字符串型
                    cellValue = cell.getStringCellValue();
                } else if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {// 处理布尔型
                    cellValue = cell.getBooleanCellValue() + "";
                } else {// 其它数据类型
                    cellValue = cell.toString() + "";
                }
                rowLst.add(cellValue.trim());
            }
            dataLst.add(rowLst);
        }
        map.put(sheet.getSheetName(), dataLst);
     	}
        return map;
        
        
    }


}
