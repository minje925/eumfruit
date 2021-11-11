package myproject.eumfruit.Controller;

import lombok.RequiredArgsConstructor;
import myproject.eumfruit.dto.MemberForm;
import myproject.eumfruit.entity.Member;
import myproject.eumfruit.service.MemberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "new")
    public String memberForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "/member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberForm memberForm, BindingResult bindingResult, Model model) {
        // 검증하려는 객체 앞에 @Valid 선언, 파라미터로 BindingResult 객체를 추가하여 검사 후 결과를 bindingResult에 담아준다.
        if(bindingResult.hasErrors()) {
            // 에러가 있다면 회원가입페이지로 이동한다.
            return "/member/memberForm";
        }
        try {
            Member member = Member.createMember(memberForm, passwordEncoder);
            memberService.saveMember(member);
        } catch(IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage()); // 회원가입시 중복 예외 발생하면 에러 메시지 전달
            return "/member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember() {
        return "/member/memberLoginForm";
    }
    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "/member/memberLoginForm";
    }


}
