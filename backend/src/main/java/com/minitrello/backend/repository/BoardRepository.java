package com.minitrello.backend.repository;

import com.minitrello.backend.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // Lấy danh sách bảng mà User đó sở hữu
    List<Board> findAllByOwnerUsername(String username);
}
