package com.example.PieceOfPeace.memory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // @RequestPart로 JSON과 파일을 함께 받을 때, Spring이 내부적으로 객체를 생성하고 필드를 설정하기 위해 Setter가 필요할 수 있습니다.
@Schema(description = "기억 생성 요청 DTO")
public class MemoryCreateRequest {

    @Schema(description = "기억에 대한 텍스트 내용")
    private String content;

}
