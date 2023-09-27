package com.lmfriends.cafe24app.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lmfriends.cafe24app.dto.UserDto;
import com.lmfriends.cafe24app.model.User;
import com.lmfriends.cafe24app.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("auth")
public class AuthController {

  private final UserService userService;

  @GetMapping("login")
  public String loginForm() {
    log.info("AuthController::loginForm");
    return "auth/login";
  }

  @GetMapping("signup")
  public String signupForm(Model model) {
    log.info("AuthController::signupForm");
    UserDto userDto = new UserDto();
    model.addAttribute("user", userDto);
    return "auth/signup";
  }

  @PostMapping("signup")
  public String signup(@Valid @ModelAttribute("user") UserDto dto, BindingResult result, Model model) {
    log.info("AuthController::signup dto={},{}", dto.getUsername(), dto.getEmail());
    if (dto.getUsername().length() < 4) result.rejectValue("username", null, "아이디는 4자 이상입니다.");
    if (dto.getEmail().length() < 4) result.rejectValue("email", null, "유효한 이메일을 사용하십시오.");
    if (dto.getPassword().length() < 4) result.rejectValue("password", null, "비밀번호는 4자 이상입니다.");

    Optional<User> optionalUser = userService.findByUsername(dto.getUsername());
    if (optionalUser.isPresent()) {
      result.rejectValue("username", null, "There is already an account registered.");
    }

    if (result.hasErrors()) {
      log.info("AuthController::signup hasErrors={}", result.toString());
      model.addAttribute("user", dto);
      return "auth/signup";
    }

    userService.saveUser(dto);
    return "redirect:/auth/signup?success";
  }
}
