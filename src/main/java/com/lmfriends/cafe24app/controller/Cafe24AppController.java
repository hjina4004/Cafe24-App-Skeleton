package com.lmfriends.cafe24app.controller;

import java.util.Base64;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmfriends.cafe24app.dto.Cafe24AppDto;
import com.lmfriends.cafe24app.dto.Cafe24AppRedirectDto;
import com.lmfriends.cafe24app.service.Cafe24Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("app")
public class Cafe24AppController {

  private final Cafe24Service cafe24Service;

  @GetMapping("")
  public String app(Cafe24AppDto dto, RedirectAttributes re, Model model) {
    log.info("Cafe24Controller::app {}", dto);

    String mallId = dto.getMall_id();
    if (mallId == null || mallId.equals("")) {
      model.addAttribute("errorReason", "(앱 설치 보류)");
      return "app-error";
    }

    // 접근 토큰을 발급 받으려면 면저 접근 코드를 요청해야 합니다.
    // 코드 요청은 cURL이 아닌 웹브라우저에서 진행하셔야 합니다.
    String uri = "https://" + mallId + ".cafe24api.com/api/v2/oauth/authorize";
    ObjectMapper objectMapper = new ObjectMapper();
    String state = "";
    try {
      state = objectMapper.writeValueAsString(dto);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      model.addAttribute("errorReason", "(앱 설치 보류)");
      return "app-error";
    }

    String authCodeReceiveUrl = cafe24Service.getCafe24Config().getAppUri() + "/manager";
    re.addAttribute("response_type", "code");
    re.addAttribute("client_id", cafe24Service.getCafe24Config().getClientId());
    re.addAttribute("state", Base64.getEncoder().encodeToString(state.getBytes()));
    re.addAttribute("redirect_uri", authCodeReceiveUrl);
    re.addAttribute("scope", cafe24Service.getCafe24Config().getAppScope());

    return "redirect:" + uri;
  }

  @GetMapping("manager")
  public String manager(Cafe24AppRedirectDto dto, Model model) {
    log.info("Cafe24Controller::manager {}", dto);

    String base64State = dto.getState();
    if (base64State == null || base64State.equals("")) {
      model.addAttribute("errorReason", "(앱 관리 보류)");
      return "app-error";
    }

    byte[] binary = Base64.getDecoder().decode(base64State);
    String state = new String(binary);

    ObjectMapper objectMapper = new ObjectMapper();
    Cafe24AppDto cafe24AppDto;
    try {
      cafe24AppDto = objectMapper.readValue(state, Cafe24AppDto.class);

      // 발급 받은 인증 코드를 사용하여 실제로 API를 호출할 수 있는 사용자 토큰 요청
      cafe24Service.authentication(dto.getCode(), cafe24AppDto);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      model.addAttribute("errorReason", "(앱 관리 보류)");
      return "app-error";
    }

    return "app-manager";
  }
}
