package com.example.PieceOfPeace.memory.controller;

import com.example.PieceOfPeace.memory.dto.request.MemoryCreateRequest;
import com.example.PieceOfPeace.memory.dto.request.MemoryUpdateRequest;
import com.example.PieceOfPeace.memory.dto.response.MemoryResponse;
import com.example.PieceOfPeace.memory.service.MemoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/memories")
@RequiredArgsConstructor
public class MemoryController {

    private final MemoryService memoryService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> createMemory(
            @RequestPart("request") MemoryCreateRequest request,
            @RequestPart(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles,
            Principal principal
    ) throws IOException {
        String writerEmail = principal.getName();
        memoryService.createMemory(request, mediaFiles, writerEmail);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{memoryId}")
    public ResponseEntity<MemoryResponse> findMemoryById(@PathVariable Long memoryId) {
        MemoryResponse response = memoryService.findMemoryById(memoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MemoryResponse>> findMemories(Principal principal) {
        String writerEmail = principal.getName();
        List<MemoryResponse> response = memoryService.findMemoriesByWriter(writerEmail);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{memoryId}")
    public ResponseEntity<Void> updateMemory(
            @PathVariable Long memoryId,
            @Valid @RequestBody MemoryUpdateRequest request,
            Principal principal
    ) {
        String userEmail = principal.getName();
        memoryService.updateMemory(memoryId, request, userEmail);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{memoryId}")
    public ResponseEntity<Void> deleteMemory(
            @PathVariable Long memoryId,
            Principal principal
    ) {
        String userEmail = principal.getName();
        memoryService.deleteMemory(memoryId, userEmail);
        return ResponseEntity.ok().build();
    }
}
