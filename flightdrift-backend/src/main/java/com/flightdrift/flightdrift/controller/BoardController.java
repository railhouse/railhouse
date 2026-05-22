package com.flightdrift.flightdrift.controller;

import com.flightdrift.flightdrift.dto.ApiResponse;
import com.flightdrift.flightdrift.dto.board.BoardResponse;
import com.flightdrift.flightdrift.dto.board.CreateBoardRequest;
import com.flightdrift.flightdrift.dto.board.EditBoardRequest;
import com.flightdrift.flightdrift.service.BoardService;
import com.flightdrift.flightdrift.dto.board.BoardListResponse;
import com.flightdrift.flightdrift.dto.board.BoardWrapperResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Boards")
@SecurityRequirement(name = "bearerAuth")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/teams/{teamId}/boards")
    @Operation(summary = "Create a board")
    public ResponseEntity<ApiResponse<?>> createBoard(
            Authentication authentication,
            @PathVariable Long teamId,
            @Valid @RequestBody CreateBoardRequest request) {

        String username = authentication.getName();
        BoardResponse board = boardService.createBoard(username, teamId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Board created successfully", new BoardWrapperResponse(board)));
    }

    @GetMapping("/teams/{teamId}/boards")
    @Operation(summary = "Get all boards for a team")
    public ResponseEntity<ApiResponse<?>> getBoards(
            Authentication authentication,
            @PathVariable Long teamId) {

        String username = authentication.getName();
        List<BoardResponse> boards = boardService.getBoards(username, teamId);

        return ResponseEntity.ok(new ApiResponse<>(true, "Boards retrieved successfully", new BoardListResponse(boards)));
    }

    @PutMapping("/boards/{id}")
    @Operation(summary = "Edit a board")
    public ResponseEntity<ApiResponse<?>> editBoard(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody EditBoardRequest request) {

        String username = authentication.getName();
        BoardResponse board = boardService.editBoard(username, id, request);

        return ResponseEntity.ok(new ApiResponse<>(true, "Board updated successfully", new BoardWrapperResponse(board)));
    }

    @DeleteMapping("/boards/{id}")
    @Operation(summary = "Delete a board")
    public ResponseEntity<ApiResponse<?>> deleteBoard(
            Authentication authentication,
            @PathVariable Long id) {

        String username = authentication.getName();
        boardService.deleteBoard(username, id);

        return ResponseEntity.ok(new ApiResponse<>(true, "Board deleted successfully", null));
    }
}
