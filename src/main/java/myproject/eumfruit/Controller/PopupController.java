package myproject.eumfruit.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PopupController {

    @GetMapping(value = "/popup")
    public String popup() {
        return "/popup";
    }
}
