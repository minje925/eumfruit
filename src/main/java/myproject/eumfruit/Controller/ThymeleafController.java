package myproject.eumfruit.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ThymeleafController {

    @GetMapping(value = "/test")
    public String thymeleafExample01(Model model) {
        model.addAttribute("data", "타임리프 예제입니다.");
        return "test/test";
    }

}
