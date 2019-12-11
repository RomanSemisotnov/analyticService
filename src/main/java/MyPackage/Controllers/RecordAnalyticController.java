package MyPackage.Controllers;

import MyPackage.Services.RecordAnalyticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recordAnalytics")
public class RecordAnalyticController {

    @Autowired
    private RecordAnalyticService recordAnalyticService;

    @GetMapping("/{record_id}")
    public Integer all(@PathVariable int record_id,
                    @RequestParam(name = "from", required = false) String from,
                    @RequestParam(name = "to", required = false) String to) {

        return 123;
    }

    @GetMapping("/{record_id}/withUid")
    public Integer get(@PathVariable int record_id,
                    @RequestParam(name = "from", required = false) String from,
                    @RequestParam(name = "to", required = false) String to) {

        return 228;
    }

}
