package MyPackage.Controllers;

import MyPackage.Services.DeviceAnalyticService;
import MyPackage.Views.ExcelAnalyticView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics/excel")
public class ExcelAnalyticsController {

    @Autowired
    private ExcelAnalyticView excelAnalyticView;

    @Autowired
    private DeviceAnalyticService deviceAnalyticService;

    @GetMapping("/{record_ids}")
    public Map<String, Object> get(Model model,
                                   @PathVariable List<Integer> record_ids,
                                   HttpServletResponse httpServletResponse,
                                   @RequestParam(name = "startDate", required = false) String startDate,
                                   @RequestParam(name = "endDate", required = false) String endDate) throws Exception {

        Map<String, Object> object = deviceAnalyticService.getRating(record_ids, startDate, endDate);
        

        return object;

        //  httpServletResponse.setHeader("Content-Disposition", "attachment; filename=file.xls");
        // return excelAnalyticView;
    }

}
