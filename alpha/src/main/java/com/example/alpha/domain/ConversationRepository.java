package com.example.alpha.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    // 🔹 대화 목록 최신순 조회
    List<Conversation> findAllByOrderByCreatedAtDesc();

}
