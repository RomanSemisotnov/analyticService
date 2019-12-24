package MyPackage.Controllers;

import MyPackage.Services.DeviceAnalyticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/analytics/devices")
public class DeviceAnalyticsController {

    @Autowired
    private DeviceAnalyticService deviceAnalyticService;

    @GetMapping("/{record_id}/all")
    public Object all(@PathVariable int record_id,
                      @RequestParam(name = "startDate", required = false) String from,
                      @RequestParam(name = "endDate", required = false) String to) {


        return deviceAnalyticService.all(record_id, from, to);
    }

    @GetMapping("/{record_id}")
    public List get(@PathVariable int record_id,
                    @RequestParam(name = "startDate", required = false) String from,
                    @RequestParam(name = "endDate", required = false) String to) {

        return deviceAnalyticService.get(record_id, from, to);
    }

    @GetMapping("/{record_id}/rating")
    public Object getRating(@PathVariable int record_id,
                            @RequestParam(name = "startDate", required = false) String startDate,
                            @RequestParam(name = "endDate", required = false) String endDate) throws ParseException {

        return deviceAnalyticService.getRating(record_id, startDate, endDate);
    }

}
