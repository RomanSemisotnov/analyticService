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
    public List all(@PathVariable int record_id,
                    @RequestParam(name = "startDate", required = false) String from,
                    @RequestParam(name = "endDate", required = false) String to) {


        return deviceAnalyticService.all(record_id, from, to);
    }

    @GetMapping("/{record_id}")
    public List<Map<String, Object>> get(@PathVariable int record_id,
                                         @RequestParam(name = "startDate", required = false) String from,
                                         @RequestParam(name = "endDate", required = false) String to) {

        return deviceAnalyticService.get(record_id, from, to);
    }

    @GetMapping("/{record_id}/rating")
    public List getRating(@PathVariable int record_id,
                          @RequestParam(name = "startDate", required = false) String startDate,
                          @RequestParam(name = "endDate", required = false) String endDate) throws ParseException {

        return deviceAnalyticService.getRating(record_id, startDate, endDate);
    }

}
