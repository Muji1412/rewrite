package com.example.rewrite.controller;

import com.example.rewrite.command.UserVO;
import com.example.rewrite.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class UserController {

    @Autowired
    @Qualifier("UserService")
    private UserService userService;

    @GetMapping("/login")
    public String login(HttpSession session, RedirectAttributes redirectAttributes) {

        Object user = session.getAttribute("user");
        if (user != null) { // 로그인 돼있는 상태에서 로그인창 들어가면 홈으로 리턴
            redirectAttributes.addFlashAttribute("loginMsg", "이미 로그인 상태입니다.");
            return "redirect:/";
        } else {
            return "user/login";
        }


    }

    @PostMapping("/loginForm") // 로그인 진행
    public String loginForm(UserVO userVO, HttpSession session, RedirectAttributes redirectAttributes) {

        UserVO vo = userService.loginForm(userVO);

        if (vo != null) {
            log.info("로그인 성공");
            session.setAttribute("user", vo);
            return "redirect:/";
        } else {
            log.info("로그인 실패");
            return "redirect:/login";
        }
    }
}
