package com.lmfriends.cafe24app.filter;

import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.lmfriends.cafe24app.config.Cafe24Config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Component
public class AuthorizationFilter implements Filter {

  private final Cafe24Config cafe24Config;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String strMethod = httpServletRequest.getMethod();
    String strOrigin = httpServletRequest.getHeader("Origin");
    String strURI = httpServletRequest.getRequestURI();
    String strApiAuthority = httpServletRequest.getHeader("X-Api-Authority");
    log.info("AuthorizationFilter::doFilter [{}]{} {} X-Api-Authority={}", strMethod, strURI, strOrigin, strApiAuthority);

    if (strURI.startsWith("/api") && !strMethod.equals("OPTIONS") && !cafe24Config.getClientId().equals(strApiAuthority)) {
      HttpServletResponse httpServletResponse = (HttpServletResponse) response;
      httpServletResponse.setStatus(401);

      httpServletResponse.setHeader("Access-Control-Allow-Origin", strOrigin);
      httpServletResponse.setContentType("application/json");
      httpServletResponse.setCharacterEncoding("utf-8");

      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("code", 401);
      map.put("message", "Unauthorized");
      JSONObject jsonObject = new JSONObject(map);

      httpServletResponse.getWriter().write(jsonObject.toString());
    } else {
      chain.doFilter(request, response);
    }
  }

}
