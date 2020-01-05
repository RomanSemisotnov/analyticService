package MyPackage.Controllers;

import MyPackage.Services.DeviceAnalyticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics/devices")
public class DeviceAnalyticsController {

    @Autowired
    private DeviceAnalyticService deviceAnalyticService;

    @GetMapping("/{record_id}/all")
    public Map<String, Long> all(@PathVariable int record_id,
                                 @RequestParam(name = "startDate", required = false) String from,
                                 @RequestParam(name = "endDate", required = false) String to) throws ParseException {

        List<Integer> record_ids = new ArrayList<>();
        record_ids.add(record_id);

        return deviceAnalyticService.all(record_ids, from, to);
    }

    @GetMapping("/{record_id}")
    public List<Map<String, Object>> get(@PathVariable int record_id,
                                         @RequestParam(name = "startDate", required = false) String from,
                                         @RequestParam(name = "endDate", required = false) String to) throws ParseException {

        List<Integer> record_ids = new ArrayList<>();
        record_ids.add(record_id);

        return deviceAnalyticService.get(record_ids, from, to);
    }

    @GetMapping("/{record_id}/rating")
    public Map<String, Long> getRating(@PathVariable int record_id,
                                       @RequestParam(name = "startDate", required = false) String startDate,
                                       @RequestParam(name = "endDate", required = false) String endDate) throws ParseException {

        List<Integer> record_ids = new ArrayList<>();
        record_ids.add(record_id);

        return deviceAnalyticService.getRating(record_ids, startDate, endDate);
    }

}
