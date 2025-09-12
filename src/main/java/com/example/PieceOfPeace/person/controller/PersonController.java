package com.example.PieceOfPeace.person.controller;

import com.example.PieceOfPeace.person.dto.request.PersonRegisterRequest;
import com.example.PieceOfPeace.person.dto.request.PersonUpdateRequest;
import com.example.PieceOfPeace.person.dto.response.PersonResponse;
import com.example.PieceOfPeace.person.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "인물 찾기 API", description = "인물(Person) 관련 CRUD 및 조회 API")
@RestController
@RequestMapping("/api/people")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "새로운 인물 등록 👤", description = "새로운 인물의 Vector ID, 이름, 관계, 프로필 사진을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "인물 등록 성공"),
            @ApiResponse(responseCode = "400", description = "이미 등록된 인물이거나, 요청 데이터 유효성 검증 실패"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> registerPerson(
            @Parameter(description = "Vector ID, 이름, 관계를 담은 JSON 데이터")
            @RequestPart("request") @Schema(implementation = PersonRegisterRequest.class) String requestJson,
            @Parameter(description = "업로드할 프로필 사진")
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            Principal principal
    ) throws IOException {
        PersonRegisterRequest request = objectMapper.readValue(requestJson, PersonRegisterRequest.class);
        String writerEmail = principal.getName();
        personService.registerPerson(request, profileImage, writerEmail);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Vector ID로 인물 조회 👨‍👩‍👧‍👦", description = "Gemini API가 식별한 Vector ID를 통해 등록된 인물 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인물 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 Vector ID의 인물을 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping("/vector/{vectorId}")
    public ResponseEntity<PersonResponse> findPersonByVectorId(@Parameter(description = "조회할 인물의 Vector ID") @PathVariable String vectorId) {
        PersonResponse response = personService.findPersonByVectorId(vectorId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내가 등록한 모든 인물 조회 📖", description = "현재 로그인한 사용자가 등록한 모든 인물 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인물 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping
    public ResponseEntity<List<PersonResponse>> findMyPeople(Principal principal) {
        String writerEmail = principal.getName();
        List<PersonResponse> response = personService.findMyPeople(writerEmail);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "인물 정보 수정 ✏️", description = "특정 ID를 가진 인물의 이름과 관계를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인물 정보 수정 성공"),
            @ApiResponse(responseCode = "403", description = "수정할 권한이 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 인물을 찾을 수 없음")
    })
    @PatchMapping("/{personId}")
    public ResponseEntity<Void> updatePerson(
            @Parameter(description = "수정할 인물의 ID") @PathVariable Long personId,
            @Valid @RequestBody PersonUpdateRequest request,
            Principal principal
    ) {
        String userEmail = principal.getName();
        personService.updatePerson(personId, request, userEmail);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "인물 정보 삭제 🗑️", description = "특정 ID를 가진 인물 정보를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인물 정보 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "삭제할 권한이 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 인물을 찾을 수 없음")
    })
    @DeleteMapping("/{personId}")
    public ResponseEntity<Void> deletePerson(
            @Parameter(description = "삭제할 인물의 ID") @PathVariable Long personId,
            Principal principal
    ) {
        String userEmail = principal.getName();
        personService.deletePerson(personId, userEmail);
        return ResponseEntity.ok().build();
    }
}
