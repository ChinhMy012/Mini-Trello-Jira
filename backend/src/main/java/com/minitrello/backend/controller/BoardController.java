package com.minitrello.backend.controller;

import com.minitrello.backend.dto.request.BoardRequest;
import com.minitrello.backend.dto.response.BoardResponse;
import com.minitrello.backend.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public BoardResponse create(@RequestBody @Valid BoardRequest request) {
        return boardService.createBoard(request);
    }

    @GetMapping("/my-boards")
    public List<BoardResponse> getMyBoards() {
        return boardService.getMyBoards();
    }
}
