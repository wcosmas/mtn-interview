package com.mtn.uganda.interview.interview;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);
    Page<Post> findByUserId(Long userId, Pageable pageable);
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<Post> findByTitleContainingIgnoreCase(String title);
}
