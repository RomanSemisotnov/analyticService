package MyPackage.Views;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Component("ExcelAnalyticView")
public class ExcelAnalyticView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook,
                                      HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse) throws Exception {

        List<Map<String, Object>> pages = (List<Map<String, Object>>) map.get("pages");
        for (Map<String, Object> page : pages) {

            Sheet sheet = workbook.createSheet((String) page.get("name"));
            sheet.setFitToPage(true);

            Long androidAndIosCount = (Long) page.get("androidAndIosCount");
            Long onlyIosCount = (Long) page.get("onlyIosCount");
            Long onlyAndroidCount = (Long) page.get("onlyAndroidCount");

            Long commonCount = (Long) page.get("commonCount");
            Long androidCount = (Long) page.get("androidCount");
            Long iosCount = (Long) page.get("iosCount");
            Long unknownCount = (Long) page.get("unknownCount");

            int currentRow = 0;

            Row row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue("Всего переходов по меткам: ");
            row.createCell(4).setCellValue("Количество меток, по которым перешли: ");

            row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue("Всего: " + commonCount);
            row.createCell(4).setCellValue("Android и Ios: " + androidAndIosCount);

            row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue("Android: " + androidCount);
            row.createCell(4).setCellValue("Только Android: " + onlyAndroidCount);

            row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue("Ios: " + iosCount);
            row.createCell(4).setCellValue("Только ios: " + onlyIosCount);

            row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue("Неизвестно: " + unknownCount);

            Long openCount = (Long) page.get("openCount");
            Long notOpenCount = (Long) page.get("notOpenCount");

            Long withConversion = (Long) page.get("withConversion");
            Long withoutConversion = (Long) page.get("withoutConversion");
            String averagePricePerClick = (String) page.get("averagePricePerClick");

            currentRow++;
            row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue("Меток всего");
            row.createCell(4).setCellValue("Сlick-through rate");
            row.createCell(7).setCellValue("Средняя цена клика");
            row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue("Открыто: " + openCount);
            row.createCell(4).setCellValue(withConversion + "/" + (withConversion + withoutConversion) +
                    "( " + (100.00 * withConversion / (withConversion + withoutConversion)) + "% )");
            row.createCell(7).setCellValue(averagePricePerClick);
            row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue("Не открыто: " + notOpenCount);

            List<Map<String, Object>> uidsStatistics = (List<Map<String, Object>>) page.get("uidsStatistics");

            currentRow++;
            row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue("#");
            row.createCell(1).setCellValue("Тег");

            row.createCell(4).setCellValue("Всего");
            row.createCell(5).setCellValue("Android");
            row.createCell(6).setCellValue("Ios");
            row.createCell(7).setCellValue("Неизвестно");

            for (Map<String, Object> uid : uidsStatistics) {
                row = sheet.createRow(currentRow++);
                row.createCell(0).setCellValue((Integer) uid.get("uid_id"));
                row.createCell(1).setCellValue((String) uid.get("uid_value"));

                row.createCell(4).setCellValue((Long) uid.get("request_count"));
                row.createCell(5).setCellValue((Long) uid.get("android_count"));
                row.createCell(6).setCellValue((Long) uid.get("ios_count"));
                row.createCell(7).setCellValue((Long) uid.get("unknown_count"));
            }

        }

    }

}
