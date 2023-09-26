package com.lmfriends.cafe24app.filter;

import java.io.IOException;

import com.lmfriends.cafe24app.config.Cafe24Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
public class CustomFilter extends GenericFilter {

  protected Cafe24Config cafe24Config;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    log.info("CustomFilter::doFilter {}", cafe24Config.getClientId());
    chain.doFilter(request, response);
  }

}
