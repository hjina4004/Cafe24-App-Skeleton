package com.lmfriends.cafe24app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cafe24AppDto {

  private String lang;
  private String mall_id;
  private String nation;
  private Integer shop_no;
  private String timestamp;
  private String user_id;
  private String user_name;
  private String user_type;
  private String hmac;
}
