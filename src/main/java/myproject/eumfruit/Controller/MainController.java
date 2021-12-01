package myproject.eumfruit.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    @GetMapping(value="/")
    public String main() {
        return "/main/main";
    }
}
