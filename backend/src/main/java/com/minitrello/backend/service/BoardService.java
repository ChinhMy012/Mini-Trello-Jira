package com.minitrello.backend.service;

import com.minitrello.backend.common.ErrorCode;
import com.minitrello.backend.dto.request.BoardRequest;
import com.minitrello.backend.dto.response.BoardResponse;
import com.minitrello.backend.entity.Board;
import com.minitrello.backend.entity.User;
import com.minitrello.backend.exception.AppException;
import com.minitrello.backend.repository.BoardRepository;
import com.minitrello.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardResponse createBoard(BoardRequest request) {
        // 1. Lấy username từ SecurityContext (Token người dùng đang đăng nhập)
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Tìm User trong DB
        User owner = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 3. Map sang Entity và lưu [cite: 25, 26]
        Board board = Board.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .owner(owner)
                .build();

        board = boardRepository.save(board);

        // 4. Trả về Response DTO
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
