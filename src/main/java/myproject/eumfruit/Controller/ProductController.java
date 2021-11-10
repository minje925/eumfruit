package myproject.eumfruit.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProductController {
    @GetMapping("/prolist")
    public String prolist(Model model) {
        return "/prolist";
    }
}
