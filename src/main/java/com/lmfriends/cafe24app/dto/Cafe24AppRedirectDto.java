package com.lmfriends.cafe24app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cafe24AppRedirectDto {

  private String code;
  private String state;
}
