package fit.fitspring.controller.dto.communal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "커서 기반 페이지네이션을 위한 dto")
public class SliceResDto<D> {

    @Schema(description = "현재 넘겨준 페이지에 넘어간 개수(마지막 페이지일시, size 보다 작을 수 있음)", example = "20")
    private int numberOfElements;

    @Schema(description = "다음페이지가 있는지 알려주는 값(마지막 페이지라면 false)", example = "true")
    private boolean hasNext;

    @Schema(description = "데이터 리스트")
    private List<D> dto;
}
