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

        Map<String, Object> pageValues;
        List<Map<String, Object>> pages = new ArrayList<>();
        for (Integer id : record_ids) {
            pageValues = new HashMap<>();

            pageValues.put("name", "Партия " + id);

            Map<String, Long> deviceStatistics = deviceAnalyticService.all(id, startDate, endDate);
            pageValues.put("commonCount", deviceStatistics.get("commonCount"));
            pageValues.put("androidCount", deviceStatistics.get("androidCount"));
            pageValues.put("iosCount", deviceStatistics.get("iosCount"));
            pageValues.put("unknownCount", deviceStatistics.get("unknownCount"));

            Map<String, Long> deviceRating = deviceAnalyticService.getRating(id, startDate, endDate);
            pageValues.put("androidAndIosCount", deviceRating.get("androidAndIosCount"));
            pageValues.put("onlyIosCount", deviceRating.get("onlyIosCount"));
            pageValues.put("onlyAndroidCount", deviceRating.get("onlyAndroidCount"));

            Long openCount = uidAnalyticService.getOpenCount(id, startDate, endDate);
            Long notOpenCount = uidAnalyticService.getNotOpenCount(id, startDate, endDate);
            pageValues.put("openCount", openCount);
            pageValues.put("notOpenCount", notOpenCount);

            Map<String, Long> clickThroughRate;
            String averagePricePerClick;

            clickThroughRate = clickThroughRateService.get(id, startDate, endDate);
            averagePricePerClick = averagePricePerClickService.get(id, startDate, endDate);

            pageValues.put("withConversion", clickThroughRate.get("withConversion"));
            pageValues.put("withoutConversion", clickThroughRate.get("withoutConversion"));
            pageValues.put("averagePricePerClick", averagePricePerClick);

            List<Map<String, Object>> uidsStatistics = deviceAnalyticService.get(id, startDate, endDate);
            pageValues.put("uidsStatistics", uidsStatistics);

            pages.add(pageValues);
        }

        Comparator<Map<String, Object>> pageSort = Comparator.comparing(map -> ((String) map.get("name")));
        pages.sort(pageSort);

        model.addAttribute("pages", pages);

        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=analytics.xlsx");
        return excelAnalyticView;
    }

}
