package com.lmfriends.cafe24app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmfriends.cafe24app.model.Cafe24Token;


public interface Cafe24TokenRepository extends JpaRepository<Cafe24Token, Long> {
  Optional<Cafe24Token> findByMallIdAndShopNoAndClientId(String mallId, Integer shopNo, String clientId);
}
