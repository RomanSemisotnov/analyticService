package MyPackage.Controllers;

import MyPackage.Services.AveragePricePerClickService;
import MyPackage.Services.ClickThroughRateService;
import MyPackage.Services.DeviceAnalyticService;
import MyPackage.Services.UidAnalyticService;
import MyPackage.Views.ExcelAnalyticView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/analytics/excel")
public class ExcelAnalyticsController {

    @Autowired
    private ExcelAnalyticView excelAnalyticView;

    @Autowired
    private DeviceAnalyticService deviceAnalyticService;

    @Autowired
    private UidAnalyticService uidAnalyticService;

    @Autowired
    private ClickThroughRateService clickThroughRateService;

    @Autowired
    private AveragePricePerClickService averagePricePerClickService;

    @GetMapping("/{record_ids}")
    public ExcelAnalyticView get(Model model,
                                 @PathVariable List<Integer> record_ids,
                                 HttpServletResponse httpServletResponse,
                                 @RequestParam(name = "startDate", required = false) String startDate,
                                 @RequestParam(name = "endDate", required = false) String endDate) throws Exception {

        List<Integer> ids;
        Map<String, Object> pageValues;
        List<Map<String, Object>> pages = new ArrayList<>();
        for (Integer id : record_ids) {
            pageValues = new HashMap<>();
            ids = new ArrayList<>();
            ids.add(id);

            pageValues.put("name", "Партия " + id);

            Map<String, Long> deviceStatistics = deviceAnalyticService.all(ids, startDate, endDate);
            pageValues.put("commonCount", deviceStatistics.get("commonCount"));
            pageValues.put("androidCount", deviceStatistics.get("androidCount"));
            pageValues.put("iosCount", deviceStatistics.get("iosCount"));
            pageValues.put("unknownCount", deviceStatistics.get("unknownCount"));

            Map<String, Long> deviceRating = deviceAnalyticService.getRating(ids, startDate, endDate);
            pageValues.put("androidAndIosCount", deviceRating.get("androidAndIosCount"));
            pageValues.put("onlyIosCount", deviceRating.get("onlyIosCount"));
            pageValues.put("onlyAndroidCount", deviceRating.get("onlyAndroidCount"));

            Long openCount = uidAnalyticService.getOpenCount(ids, startDate, endDate);
            Long notOpenCount = uidAnalyticService.getNotOpenCount(ids, startDate, endDate);
            pageValues.put("openCount", openCount);
            pageValues.put("notOpenCount", notOpenCount);

            Map<String, Long> clickThroughRate;
            String averagePricePerClick;
            if (startDate == null) {
                clickThroughRate = clickThroughRateService.get(ids);
                averagePricePerClick = averagePricePerClickService.get(ids);
            } else {
                clickThroughRate = clickThroughRateService.get(ids, startDate, endDate);
                averagePricePerClick = averagePricePerClickService.get(ids, startDate, endDate);
            }
            pageValues.put("withConversion", clickThroughRate.get("withConversion"));
            pageValues.put("withoutConversion", clickThroughRate.get("withoutConversion"));
            pageValues.put("averagePricePerClick", averagePricePerClick);

            List<Map<String, Object>> uidsStatistics = deviceAnalyticService.get(ids, startDate, endDate);
            pageValues.put("uidsStatistics", uidsStatistics);

            pages.add(pageValues);
        }

        model.addAttribute("pages", pages);

        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=analytics.xlsx");
        return excelAnalyticView;
    }

}
