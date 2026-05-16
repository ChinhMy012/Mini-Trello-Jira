package com.minitrello.backend.service;
import com.minitrello.backend.common.ErrorCode;
import com.minitrello.backend.dto.request.BoardRequest;
import com.minitrello.backend.dto.response.BoardResponse;
import com.minitrello.backend.entity.Board;
import com.minitrello.backend.entity.ColumnEntity;
import com.minitrello.backend.entity.User;
import com.minitrello.backend.exception.AppException;
import com.minitrello.backend.repository.BoardRepository;
import com.minitrello.backend.repository.ColumnRepository; // Thêm import này
import com.minitrello.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Thêm import này

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ColumnRepository columnRepository; // 1. Inject thêm repository của Column

    @Transactional // 2. Đảm bảo tính nguyên tử: Tạo Board và Column phải đi cùng nhau
    public BoardResponse createBoard(BoardRequest request) {
        // 1. Lấy username từ SecurityContext
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Tìm User trong DB
        User owner = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 3. Tạo và lưu Board
        Board board = Board.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .owner(owner)
                .build();

        board = boardRepository.save(board);

        // 4. TỰ ĐỘNG TẠO 3 CỘT MẶC ĐỊNH CHO BOARD MỚI
        List<String> defaultTitles = List.of("To Do", "Doing", "Done");
        for (int i = 0; i < defaultTitles.size(); i++) {
            ColumnEntity column = ColumnEntity.builder()
                    .title(defaultTitles.get(i))
                    .position(i) // Vị trí 0, 1, 2
                    .board(board) // Gắn vào board vừa tạo
                    .build();
            columnRepository.save(column);
        }

        // 5. Trả về Response DTO
        return mapToBoardResponse(board);
    }

    public List<BoardResponse> getMyBoards() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return boardRepository.findAllByOwnerUsername(currentUsername).stream()
                .map(this::mapToBoardResponse)
                .toList();
    }

    private BoardResponse mapToBoardResponse(Board board) {
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .description(board.getDescription())
                .ownerUsername(board.getOwner().getUsername())
                .build();
    }
}