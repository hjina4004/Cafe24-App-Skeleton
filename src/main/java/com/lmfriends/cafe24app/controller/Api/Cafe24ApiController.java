package com.lmfriends.cafe24app.controller.Api;

import java.time.LocalDate;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lmfriends.cafe24app.dto.Cafe24ApiDto;
import com.lmfriends.cafe24app.service.ApiResponse;
import com.lmfriends.cafe24app.service.Cafe24Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class Cafe24ApiController {

  protected final Cafe24Service cafe24Service;

  @GetMapping("refresh-token/{mallId}/{shopNo}")
  public ApiResponse<JSONObject> refreshToken(
      @PathVariable(value = "mallId") String mallId,
      @PathVariable(value = "shopNo") Integer shopNo) {
    log.info("Cafe24ApiController::refreshToken {}, {}", mallId, shopNo);

    String strToken = cafe24Service.getAccessTokenByRefreshToken(mallId, shopNo);
    log.info("Cafe24ApiController::refreshToken token={}", strToken);

    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("token", strToken);
    JSONObject jsonObject = new JSONObject(map);

    return strToken.equals("")
        ? ApiResponse.res(0, "fail", jsonObject)
        : ApiResponse.res(1, "success", jsonObject);
  }

  @GetMapping("customers/{mallId}/{shopNo}/{memberId}")
  public ApiResponse<JSONObject> customer(
      @PathVariable(value = "mallId") String mallId,
      @PathVariable(value = "shopNo") Integer shopNo,
      @PathVariable(value = "memberId") String memberId) {
    return cafe24Service.customer(mallId, shopNo, memberId);
  }

  @GetMapping("orders/{mallId}/{shopNo}")
  public ApiResponse<JSONObject> orders(
      @PathVariable(value = "mallId") String mallId,
      @PathVariable(value = "shopNo") Integer shopNo,
      Cafe24ApiDto dto) {
    log.info("Cafe24ApiController::orders {} {} {}", mallId, shopNo, dto);
    if (dto.getEnd_date() == null)
      dto.setEnd_date(LocalDate.now());
    if (dto.getStart_date() == null)
      dto.setStart_date(LocalDate.now().withDayOfMonth(1));
    return cafe24Service.orders(mallId, shopNo, dto);
  }

  @GetMapping("orders/{mallId}/{shopNo}/{orderId}")
  public ApiResponse<JSONObject> order(
      @PathVariable(value = "mallId") String mallId,
      @PathVariable(value = "shopNo") Integer shopNo,
      @PathVariable(value = "orderId") String orderId,
      Cafe24ApiDto dto) {
    log.info("Cafe24ApiController::order {} {} {} {}", mallId, shopNo, dto);
    return cafe24Service.order(mallId, shopNo, orderId, dto);
  }
}
