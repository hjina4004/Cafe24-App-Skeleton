package com.lmfriends.cafe24app.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cafe24_tokens")
public class Cafe24Token {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String mallId;
  private Integer shopNo;
  private String clientId;
  private String userId;

  private String accessToken;
  private LocalDateTime expiresAt;
  private String refreshToken;
  private LocalDateTime refreshTokenExpiresAt;

  @Column(columnDefinition = "TEXT")
  private String scopes;
  
  private LocalDateTime issuedAt;

  @CreatedDate
  private LocalDateTime createdAt;
  @LastModifiedDate
  private LocalDateTime updatedAt;
}
