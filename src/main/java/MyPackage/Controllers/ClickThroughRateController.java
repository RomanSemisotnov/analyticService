package MyPackage.Controllers;

import MyPackage.Services.ClickThroughRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/analytics/clickThroughRate")
public class ClickThroughRateController {

    @Autowired
    private ClickThroughRateService clickThroughRateService;

    @GetMapping("/{record_id}")
    public List get(@PathVariable int record_id,
                    @RequestParam(name = "startDate", required = false) String startDate,
                    @RequestParam(name = "endDate", required = false) String endDate) throws ParseException {

        if (startDate == null) {
            return clickThroughRateService.get(record_id);
        }

        return clickThroughRateService.get(record_id, startDate, endDate);
    }

}
