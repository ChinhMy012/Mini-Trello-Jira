package com.minitrello.backend.service;

import com.minitrello.backend.common.ErrorCode;
import com.minitrello.backend.dto.request.TaskRequest;
import com.minitrello.backend.entity.ColumnEntity;
import com.minitrello.backend.entity.Task;
import com.minitrello.backend.exception.AppException;
import com.minitrello.backend.repository.ColumnRepository;
import com.minitrello.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ColumnRepository columnRepository;

    private void validateColumnOwnership(ColumnEntity column) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!column.getBoard().getOwner().getUsername().equals(currentUsername)) {
            throw new AppException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }

    @Transactional
    public void createTask(Long columnId, TaskRequest request) {
        ColumnEntity column = columnRepository.findById(columnId)
                .orElseThrow(() -> new AppException(ErrorCode.COLUMN_NOT_FOUND));
        validateColumnOwnership(column);

        int nextPosition = column.getTasks().stream()
                .mapToInt(Task::getPosition)
                .max()
                .orElse(-1) + 1;

        Task task = Task.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .position(nextPosition)
                .columnEntity(column)
                .build();

        taskRepository.save(task);
    }

    @Transactional
    public void updateTask(Long taskId, TaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));
        validateColumnOwnership(task.getColumnEntity());

        task.setTitle(request.getTitle());
        task.setContent(request.getContent());
        taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));
        validateColumnOwnership(task.getColumnEntity());

        taskRepository.delete(task);
    }

    // THUẬT TOÁN KÉO THẢ TASK HÀNG LOẠT (Bao gồm chuyển cột chéo)
    @Transactional
    public void reorderTasks(Long targetColumnId, List<Long> orderedTaskIds) {
        ColumnEntity targetColumn = columnRepository.findById(targetColumnId)
                .orElseThrow(() -> new AppException(ErrorCode.COLUMN_NOT_FOUND));
        validateColumnOwnership(targetColumn);

        for (int i = 0; i < orderedTaskIds.size(); i++) {
            Long id = orderedTaskIds.get(i);
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));

            // Bảo mật: Thẻ bị di chuyển cũng phải thuộc về bảng mà user sở hữu
            validateColumnOwnership(task.getColumnEntity());

            task.setColumnEntity(targetColumn); // Chuyển sang cột mới (nếu kéo chéo cột)
            task.setPosition(i); // Đặt lại chỉ số index mới
            taskRepository.save(task);
        }
    }
}