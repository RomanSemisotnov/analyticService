package MyPackage.Controllers;


import MyPackage.Models.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class Test1Controller {


    @PostMapping
    public Device exec(@RequestBody Device device){
        return device;
    }

}
