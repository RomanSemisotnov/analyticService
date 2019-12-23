package MyPackage.Controllers;

import MyPackage.Services.UidAnalyticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/analytics/uids")
public class UidAnalyticsController {

    @Autowired
    private UidAnalyticService uidAnalyticService;

    @GetMapping("/{record_id}/openCount")
    public List getOpenCount(@PathVariable int record_id,
                             @RequestParam(name = "startDate", required = false) String startDate,
                             @RequestParam(name = "endDate", required = false) String endDate) throws ParseException {

        return uidAnalyticService.getOpenCount(record_id, startDate, endDate);
    }

    @GetMapping("/{record_id}/notOpenCount")
    public List getNotOpenCount(@PathVariable int record_id,
                                @RequestParam(name = "startDate", required = false) String startDate,
                                @RequestParam(name = "endDate", required = false) String endDate) throws ParseException {

        return uidAnalyticService.getNotOpenCount(record_id, startDate, endDate);
    }

}
