package market.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import market.book.dto.member.MemberLoginDto;
import market.book.dto.member.MemberSaveDto;
import market.book.global.exception.DuplicateEmailException;
import market.book.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/signup-view")
    public String signupView(@ModelAttribute MemberSaveDto memberSaveDto) {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute MemberSaveDto memberSaveDto,
                         BindingResult bindingResult,
                         Errors errors) {

        if ( bindingResult.hasErrors()) return "signup";

        try {
            memberService.create(memberSaveDto);
        } catch(DuplicateEmailException e) {
            errors.reject("signUpFail", "사용할 수 없는 이메일입니다.");
            return "signup";
        }

        return "redirect:/members/login-view";
    }

    @GetMapping("/login-view")
    public String loginView(@RequestParam(required = false, defaultValue = "false") Boolean fail,
                            @ModelAttribute MemberLoginDto memberLoginDto,
                            Errors errors) {
        if(fail) {
            // MemberCustomLoginFailHandler 에서 redirect
            errors.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return "login";
    }
}
