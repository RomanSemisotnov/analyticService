package MyPackage.Controllers;

import MyPackage.Services.ClickThroughRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/analytics/clickThroughRate")
public class ClickThroughRateController {

    @Autowired
    private ClickThroughRateService clickThroughRateService;

    @GetMapping("/{record_id}")
    public Object get(@PathVariable int record_id,
                      @RequestParam(name = "startDate", required = false) String startDate,
                      @RequestParam(name = "endDate", required = false) String endDate) throws ParseException {

        List<Integer> record_ids = new ArrayList<>();
        record_ids.add(record_id);

        if (startDate == null) {
            return clickThroughRateService.get(record_ids);
        }

        return clickThroughRateService.get(record_ids, startDate, endDate);
    }

}
