package com.minitrello.backend.controller;

import com.minitrello.backend.dto.request.TaskRequest;
import com.minitrello.backend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/column/{columnId}")
    public String create(@PathVariable Long columnId, @RequestBody @Valid TaskRequest request) {
        taskService.createTask(columnId, request);
        return "Tạo thẻ thành công";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestBody @Valid TaskRequest request) {
        taskService.updateTask(id, request);
        return "Cập nhật thẻ thành công";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "Xóa thẻ thành công";
    }

    @PutMapping("/column/{columnId}/reorder")
    public String reorder(@PathVariable Long columnId, @RequestBody List<Long> orderedTaskIds) {
        taskService.reorderTasks(columnId, orderedTaskIds);
        return "Sắp xếp thẻ thành công";
    }
}