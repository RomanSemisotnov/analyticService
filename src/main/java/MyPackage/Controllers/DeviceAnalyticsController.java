package MyPackage.Controllers;

import MyPackage.Services.DeviceAnalyticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics/devices")
public class DeviceAnalyticsController {

    @Autowired
    private DeviceAnalyticService deviceAnalyticService;

    @GetMapping("/{record_id}/all")
    public Map<String, Long> all(@PathVariable int record_id,
                                 @RequestParam(name = "startDate", required = false) String startDate,
                                 @RequestParam(name = "endDate", required = false) String endDate) throws ParseException {

        return deviceAnalyticService.all(record_id, startDate, endDate);
    }

    @GetMapping("/{record_id}")
    public List<Map<String, Object>> get(@PathVariable int record_id,
                                         @RequestParam(name = "startDate", required = false) String startDate,
                                         @RequestParam(name = "endDate", required = false) String endDate) throws ParseException {

        return deviceAnalyticService.get(record_id, startDate, endDate);
    }

    @GetMapping("/{record_id}/rating")
    public Map<String, Long> getRating(@PathVariable int record_id,
                                       @RequestParam(name = "startDate", required = false) String startDate,
                                       @RequestParam(name = "endDate", required = false) String endDate) throws ParseException {

        return deviceAnalyticService.getRating(record_id, startDate, endDate);
    }

}
