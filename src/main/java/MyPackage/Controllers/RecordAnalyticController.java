package MyPackage.Controllers;

import MyPackage.Services.RecordAnalyticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics/devices")
public class RecordAnalyticController {

    @Autowired
    private RecordAnalyticService recordAnalyticService;

    @GetMapping("/{record_id}/all")
    public List<Map<String, Object>> all(@PathVariable int record_id,
                                         @RequestParam(name = "from", required = false) String from,
                                         @RequestParam(name = "to", required = false) String to) {

        return recordAnalyticService.all(record_id, from, to);
    }

    @GetMapping("/{record_id}")
    public List<Map<String, Object>> get(@PathVariable int record_id,
                                         @RequestParam(name = "from", required = false) String from,
                                         @RequestParam(name = "to", required = false) String to) {

        return recordAnalyticService.get(record_id, from, to);
    }

    @GetMapping("/{record_id}/rating")
    public List getRating(@PathVariable int record_id) {
        return recordAnalyticService.getRating(record_id);
    }

}
