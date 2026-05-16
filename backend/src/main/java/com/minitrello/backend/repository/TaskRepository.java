package com.minitrello.backend.repository;

import com.minitrello.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByColumnEntityIdOrderByPositionAsc(Long columnId);
}