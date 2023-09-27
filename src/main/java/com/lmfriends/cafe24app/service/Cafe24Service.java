package com.lmfriends.cafe24app.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.lmfriends.cafe24app.config.Cafe24Config;
import com.lmfriends.cafe24app.dto.Cafe24ApiDto;
import com.lmfriends.cafe24app.dto.Cafe24AppDto;
import com.lmfriends.cafe24app.dto.ResponseDto;
import com.lmfriends.cafe24app.model.Cafe24Token;
import com.lmfriends.cafe24app.repository.Cafe24TokenRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Log4j2
@Getter
@Service
@RequiredArgsConstructor
public class Cafe24Service {

  private final Cafe24Config cafe24Config;
  private final Cafe24TokenRepository cafe24TokenRepository;

  public String authentication(String authorizeCode, Cafe24AppDto cafe24AppDto) {
    log.info("Cafe24Service::authentication code={}, cafe24AppDto={}", authorizeCode, cafe24AppDto);

    RequestBody formBody = new FormBody.Builder()
        .add("grant_type", "authorization_code")
        .add("code", authorizeCode)
        .add("redirect_uri", cafe24Config.getAppUri() + "/manager")
        .build();

    ResponseDto<JSONObject> res = requestWithAuthorization(cafe24AppDto.getMall_id(), formBody);
    if (res.getCode() == 200)
      saveToken(res.getData());
    else
      return "fail";

    return "success";
  }

  // refresh token은 2주간 유효
  public String getAccessTokenByRefreshToken(String mallId, Integer shopNo) {
    Optional<Cafe24Token> optionalCafe24Token = cafe24TokenRepository
        .findByMallIdAndShopNoAndClientId(mallId, shopNo, cafe24Config.getClientId());
    if (optionalCafe24Token.isPresent()) {
      String strRefreshToken = optionalCafe24Token.get().getRefreshToken();
      LocalDateTime expiresAt = optionalCafe24Token.get().getRefreshTokenExpiresAt();
      return isExpired(expiresAt) ? "" : getAccessTokenByRefreshToken(mallId, shopNo, strRefreshToken);
    }
    return "";
  }

  public ResponseDto<JSONObject> customer(String mallId, Integer shopNo, String memberId) {
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("https")
        .host(mallId + ".cafe24api.com")
        .path("/api/v2/admin/customers")
        .queryParam("member_id", memberId)
        .build();

    return requestWithAaccessToken("GET", mallId, shopNo, uriComponents.toString());
  }

  public ResponseDto<JSONObject> orders(String mallId, Integer shopNo, Cafe24ApiDto dto) {
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("https")
        .host(mallId + ".cafe24api.com")
        .path("/api/v2/admin/orders")
        .queryParam("shop_no", shopNo)
        .queryParam("start_date", dto.getStart_date())
        .queryParam("end_date", dto.getEnd_date())
        .queryParam("embed", dto.getEmbed())
        .build();

    return requestWithAaccessToken("GET", mallId, shopNo, uriComponents.toString());
  }

  public ResponseDto<JSONObject> order(String mallId, Integer shopNo, String orderId, Cafe24ApiDto dto) {
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("https")
        .host(mallId + ".cafe24api.com")
        .path("/api/v2/admin/orders/" + orderId)
        .queryParam("shop_no", shopNo)
        .queryParam("embed", dto.getEmbed())
        .build();

    return requestWithAaccessToken("GET", mallId, shopNo, uriComponents.toString());
  }

  protected String getAccessTokenByRefreshToken(String mallId, Integer shopNo, String refreshToken) {
    RequestBody formBody = new FormBody.Builder()
        .add("grant_type", "refresh_token")
        .add("refresh_token", refreshToken)
        .build();

    ResponseDto<JSONObject> res = requestWithAuthorization(mallId, formBody);
    if (res.getCode() == 200)
      return saveToken(res.getData());
    else
      return "";
  }

  public Boolean setupScripttags() {
    return true;
  }

  public String getAccessToken(String mallId, Integer shopNo) {
    Optional<Cafe24Token> optionalCafe24Token = cafe24TokenRepository
        .findByMallIdAndShopNoAndClientId(mallId, shopNo, cafe24Config.getClientId());
    if (optionalCafe24Token.isPresent()) {
      String strAccessToken = optionalCafe24Token.get().getAccessToken();
      LocalDateTime expiresAt = optionalCafe24Token.get().getExpiresAt();
      return isExpired(expiresAt) ? getAccessTokenByRefreshToken(mallId, shopNo) : strAccessToken;
    }
    return "";
  }

  protected Boolean isExpired(LocalDateTime expiresAt) {
    LocalDateTime dayNow = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    LocalDateTime dayeExpiresAt = expiresAt.truncatedTo(ChronoUnit.MINUTES);
    return !dayNow.isBefore(dayeExpiresAt);
  }

  protected ResponseDto<JSONObject> requestWithAuthorization(String mallId, RequestBody formBody) {
    String preparedAuthorization = cafe24Config.getClientId() + ":" + cafe24Config.getClientSecret();
    String strAuthorization = "Basic " + Base64.getEncoder().encodeToString(preparedAuthorization.getBytes());
    Request request = new Request.Builder()
        .url("https://" + mallId + ".cafe24api.com/api/v2/oauth/token")
        .addHeader("Authorization", strAuthorization)
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .post(formBody)
        .build();

    Integer resCode = -1;
    String resMessage = "error";
    JSONObject jsonObject = null;
    OkHttpClient client = new OkHttpClient();
    Response response;
    try {
      response = client.newCall(request).execute();
      resCode = response.code();
      resMessage = resCode == 200 ? "success" : "error";
      JSONParser jsonParser = new JSONParser();
      jsonObject = (JSONObject) jsonParser.parse(response.body().string());
      log.info("Cafe24Service::requestWithAuthorization resCode={}, response.json={}", resCode,
          jsonObject.toString());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return ResponseDto.res(resCode, resMessage, jsonObject);
  }

  protected ResponseDto<JSONObject> requestWithAaccessToken(String method, String mallId, Integer shopNo, String url) {
    log.info("Cafe24Service::requestWithAaccessToken shopNo={}, url={}", shopNo, url);
    String accessToken = getAccessToken(mallId, shopNo);
    if ("".equals(accessToken))
      return ResponseDto.res(-404, "NOT accessToken");

    Request request = new Request.Builder()
        .url(url)
        .addHeader("Authorization", "Bearer " + accessToken)
        .addHeader("Content-Type", "application/json")
        .get()
        .build();

    Integer resCode = -1;
    String resMessage = "error";
    JSONObject jsonObject = null;
    OkHttpClient client = new OkHttpClient();
    Response response;
    try {
      response = client.newCall(request).execute();
      resCode = response.code();
      resMessage = resCode == 200 ? "success" : "error";
      JSONParser jsonParser = new JSONParser();
      jsonObject = (JSONObject) jsonParser.parse(response.body().string());
      log.info("Cafe24Service::requestWithAuthorization resCode={}, response.json={}", resCode,
          jsonObject.toString());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return ResponseDto.res(resCode, resMessage, jsonObject);
  }

  protected String saveToken(JSONObject jsonObject) {
    String strMallId = (String) jsonObject.get("mall_id");
    Integer shopNo = Integer.parseInt((String) jsonObject.get("shop_no"));
    String strClientId = (String) jsonObject.get("client_id");
    String userId = (String) jsonObject.get("user_id");
    String accessToken = (String) jsonObject.get("access_token");
    LocalDateTime expiresAt = LocalDateTime.parse((String) jsonObject.get("expires_at"));
    String refreshToken = (String) jsonObject.get("refresh_token");
    LocalDateTime refreshTokenExpiresAt = LocalDateTime.parse((String) jsonObject.get("refresh_token_expires_at"));
    JSONArray scopeJsonArray = (JSONArray) jsonObject.get("scopes");
    LocalDateTime issuedAt = LocalDateTime.parse((String) jsonObject.get("issued_at"));

    Optional<Cafe24Token> optionalCafe24Token = cafe24TokenRepository
        .findByMallIdAndShopNoAndClientId(strMallId, shopNo, strClientId);
    if (optionalCafe24Token.isPresent()) {
      Cafe24Token cafe24Token = optionalCafe24Token.get();
      cafe24Token.setUserId(userId);
      cafe24Token.setAccessToken(accessToken);
      cafe24Token.setExpiresAt(expiresAt);
      cafe24Token.setRefreshToken(refreshToken);
      cafe24Token.setRefreshTokenExpiresAt(refreshTokenExpiresAt);
      cafe24Token.setIssuedAt(issuedAt);
      cafe24TokenRepository.save(cafe24Token);
    } else {
      Cafe24Token cafe24Token = Cafe24Token
          .builder()
          .mallId(strMallId)
          .shopNo(shopNo)
          .clientId(strClientId)
          .userId(userId)
          .accessToken(accessToken)
          .expiresAt(expiresAt)
          .refreshToken(refreshToken)
          .refreshTokenExpiresAt(refreshTokenExpiresAt)
          .scopes(scopeJsonArray.toString())
          .issuedAt(issuedAt)
          .build();
      cafe24TokenRepository.save(cafe24Token);
    }

    return accessToken;
  }
}
