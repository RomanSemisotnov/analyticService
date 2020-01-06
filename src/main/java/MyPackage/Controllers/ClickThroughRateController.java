package MyPackage.Controllers;

import MyPackage.Services.ClickThroughRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/analytics/clickThroughRate")
public class ClickThroughRateController {

    @Autowired
    private ClickThroughRateService clickThroughRateService;

    @GetMapping("/{record_id}")
    public Map<String, Long> get(@PathVariable int record_id,
                                 @RequestParam(name = "startDate", required = false) String startDate,
                                 @RequestParam(name = "endDate", required = false) String endDate) throws ParseException {

        return clickThroughRateService.get(record_id, startDate, endDate);
    }

}
