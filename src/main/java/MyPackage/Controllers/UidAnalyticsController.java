package MyPackage.Controllers;

import MyPackage.Services.UidAnalyticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/analytics/uids")
public class UidAnalyticsController {

    @Autowired
    private UidAnalyticService uidAnalyticService;

    @GetMapping("/{record_id}/openCount")
    public Object getOpenCount(@PathVariable int record_id,
                               @RequestParam(name = "startDate", required = false) String startDate,
                               @RequestParam(name = "endDate", required = false) String endDate) throws ParseException {

        List<Integer> record_ids = new ArrayList<>();
        record_ids.add(record_id);

        return uidAnalyticService.getOpenCount(record_ids, startDate, endDate);
    }

    @GetMapping("/{record_id}/notOpenCount")
    public Object getNotOpenCount(@PathVariable int record_id,
                                  @RequestParam(name = "startDate", required = false) String startDate,
                                  @RequestParam(name = "endDate", required = false) String endDate) throws ParseException {

        List<Integer> record_ids = new ArrayList<>();
        record_ids.add(record_id);

        return uidAnalyticService.getNotOpenCount(record_ids, startDate, endDate);
    }

}
