package com.minitrello.backend.controller;

import com.minitrello.backend.dto.request.ColumnRequest;
import com.minitrello.backend.service.ColumnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/columns")
@RequiredArgsConstructor
public class ColumnController {
    private final ColumnService columnService;

    @PostMapping("/board/{boardId}")
    public String create(@PathVariable Long boardId, @RequestBody @Valid ColumnRequest request) {
        columnService.createColumn(boardId, request);
        return "Tạo cột thành công";
    }

    @PutMapping("/{id}")
    public String updateTitle(@PathVariable Long id, @RequestBody @Valid ColumnRequest request) {
        columnService.updateColumnTitle(id, request);
        return "Cập nhật tên cột thành công";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        columnService.deleteColumn(id);
        return "Xóa cột thành công";
    }

    @PutMapping("/board/{boardId}/reorder")
    public String reorder(@PathVariable Long boardId, @RequestBody List<Long> orderedColumnIds) {
        columnService.reorderColumns(boardId, orderedColumnIds);
        return "Sắp xếp cột thành công";
    }
}