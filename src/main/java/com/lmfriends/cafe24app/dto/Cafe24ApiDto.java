package com.lmfriends.cafe24app.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cafe24ApiDto {

  LocalDate start_date;
  LocalDate end_date;
  String embed;
}
