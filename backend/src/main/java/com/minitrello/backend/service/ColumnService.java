package com.minitrello.backend.service;


import com.minitrello.backend.common.ErrorCode;
import com.minitrello.backend.dto.request.ColumnRequest;
import com.minitrello.backend.entity.Board;
import com.minitrello.backend.entity.ColumnEntity;
import com.minitrello.backend.exception.AppException;
import com.minitrello.backend.repository.BoardRepository;
import com.minitrello.backend.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColumnService {
    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;

    private void validateBoardOwnership(Board board) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!board.getOwner().getUsername().equals(currentUsername)) {
            throw new AppException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }

    @Transactional
    public void createColumn(Long boardId, ColumnRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));
        validateBoardOwnership(board);

        // Tính vị trí kế tiếp (bằng max position hiện tại + 1)
        int nextPosition = board.getColumns().stream()
                .mapToInt(ColumnEntity::getPosition)
                .max()
                .orElse(-1) + 1;

        ColumnEntity column = ColumnEntity.builder()
                .title(request.getTitle())
                .position(nextPosition)
                .board(board)
                .build();

        columnRepository.save(column);
    }

    @Transactional
    public void updateColumnTitle(Long columnId, ColumnRequest request) {
        ColumnEntity column = columnRepository.findById(columnId)
                .orElseThrow(() -> new AppException(ErrorCode.COLUMN_NOT_FOUND));
        validateBoardOwnership(column.getBoard());

        column.setTitle(request.getTitle());
        columnRepository.save(column);
    }

    @Transactional
    public void deleteColumn(Long columnId) {
        ColumnEntity column = columnRepository.findById(columnId)
                .orElseThrow(() -> new AppException(ErrorCode.COLUMN_NOT_FOUND));
        validateBoardOwnership(column.getBoard());

        columnRepository.delete(column);
    }

    // THUẬT TOÁN KÉO THẢ CỘT: Nhận vào danh sách ID theo thứ tự mới từ Frontend gửi lên
    @Transactional
    public void reorderColumns(Long boardId, List<Long> orderedColumnIds) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));
        validateBoardOwnership(board);

        for (int i = 0; i < orderedColumnIds.size(); i++) {
            Long id = orderedColumnIds.get(i);
            ColumnEntity column = columnRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.COLUMN_NOT_FOUND));

            column.setPosition(i); // Cập nhật lại vị trí index (0, 1, 2...)
            columnRepository.save(column);
        }
    }
}