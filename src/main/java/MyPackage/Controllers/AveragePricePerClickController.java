package MyPackage.Controllers;

import MyPackage.Services.AveragePricePerClickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/analytics/averagePricePerClick")
public class AveragePricePerClickController {

    @Autowired
    private AveragePricePerClickService averagePricePerClickService;

    @GetMapping("/{record_id}")
    public Object get(@PathVariable int record_id,
                      @RequestParam(name = "startDate", required = false) String startDate,
                      @RequestParam(name = "endDate", required = false) String endDate) throws ParseException {

        if (startDate == null) {
            return averagePricePerClickService.get(record_id);
        }

        return averagePricePerClickService.get(record_id, startDate, endDate);
    }

}
