package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationUpdateRequest(

        @NotNull(message = "예약 날짜는 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate date,

        @NotNull(message = "시간은 필수입니다.")
        Long timeId
) {
}
