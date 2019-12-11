package MyPackage.Controllers;

import MyPackage.Services.RecordAnalyticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recordAnalytics")
public class RecordAnalyticController {

    @Autowired
    private RecordAnalyticService recordAnalyticService;

    @GetMapping("/{record_id}")
    public String all(@PathVariable int record_id,
                    @RequestParam(name = "from", required = false) String from,
                    @RequestParam(name = "to", required = false) String to) {

        return recordAnalyticService.all(record_id);
    }

    @GetMapping("/{record_id}/withUid")
    public String get(@PathVariable int record_id,
                    @RequestParam(name = "from", required = false) String from,
                    @RequestParam(name = "to", required = false) String to) {

        return recordAnalyticService.get(record_id);
    }

}
