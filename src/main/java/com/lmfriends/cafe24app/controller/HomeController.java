package com.lmfriends.cafe24app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.lmfriends.cafe24app.config.Cafe24Config;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class HomeController {

  final Cafe24Config cafe24Config;

  @GetMapping("")
  public String home(Model model) {
    model.addAttribute("appName", cafe24Config.getAppName());
    return "index";
  }
}
