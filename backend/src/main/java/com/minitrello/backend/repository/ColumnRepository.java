package com.minitrello.backend.repository;

import com.minitrello.backend.entity.ColumnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnRepository extends JpaRepository<ColumnEntity, Long> {
    List<ColumnEntity> findAllByBoardIdOrderByPositionAsc(Long boardId);
}