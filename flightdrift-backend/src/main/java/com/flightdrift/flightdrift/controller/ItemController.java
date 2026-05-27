package com.flightdrift.flightdrift.controller;

import com.flightdrift.flightdrift.dto.ApiResponse;
import com.flightdrift.flightdrift.dto.item.CreateItemRequest;
import com.flightdrift.flightdrift.dto.item.EditItemRequest;
import com.flightdrift.flightdrift.dto.item.ItemListResponse;
import com.flightdrift.flightdrift.dto.item.ItemResponse;
import com.flightdrift.flightdrift.dto.item.ItemWrapperResponse;
import com.flightdrift.flightdrift.service.ItemService;
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
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

/*
 * Author: Jamius Siam
 * Since: 27/05/2026
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Items")
@SecurityRequirement(name = "bearerAuth")
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/projects/{projectId}/items")
    @Operation(summary = "Create an item")
    public ResponseEntity<ApiResponse<?>> createItem(
            Authentication authentication,
            @PathVariable UUID projectId,
            @Valid @RequestBody CreateItemRequest request) {

        String username = authentication.getName();
        ItemResponse item = itemService.createItem(username, projectId, request);

        return ResponseEntity.status(CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Item created successfully",
                                new ItemWrapperResponse(item)
                        )
                );
    }

    @GetMapping("/projects/{projectId}/items")
    @Operation(summary = "Get all items for a project")
    public ResponseEntity<ApiResponse<?>> getItems(
            Authentication authentication,
            @PathVariable UUID projectId) {

        String username = authentication.getName();
        List<ItemResponse> items = itemService.getItems(username, projectId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Items retrieved successfully", new ItemListResponse(items))
        );
    }

    @PutMapping("/items/{id}")
    @Operation(summary = "Edit an item")
    public ResponseEntity<ApiResponse<?>> editItem(
            Authentication authentication,
            @PathVariable UUID id,
            @Valid @RequestBody EditItemRequest request) {

        String username = authentication.getName();
        ItemResponse item = itemService.editItem(username, id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Item updated successfully",
                        new ItemWrapperResponse(item)
                )
        );
    }

    @DeleteMapping("/items/{id}")
    @Operation(summary = "Delete an item")
    public ResponseEntity<ApiResponse<?>> deleteItem(
            Authentication authentication,
            @PathVariable UUID id) {

        String username = authentication.getName();
        itemService.deleteItem(username, id);

        return ResponseEntity.ok(new ApiResponse<>(true, "Item deleted successfully", null));
    }
}
