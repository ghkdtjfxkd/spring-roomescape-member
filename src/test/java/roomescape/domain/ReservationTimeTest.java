package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import roomescape.domain.TimeStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    @DisplayName("DELETED 상태의 시간은 isDeleted()가 true를 반환한다.")
    void isDeletedReturnsTrueWhenDeleted() {
        ReservationTime reservationTime = ReservationTime.of(1L, LocalTime.of(10, 0), TimeStatus.DELETED);

        assertThat(reservationTime.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("AVAILABLE 상태의 시간은 isDeleted()가 false를 반환한다.")
    void isDeletedReturnsFalseWhenAvailable() {
        ReservationTime reservationTime = ReservationTime.of(1L, LocalTime.of(10, 0), TimeStatus.AVAILABLE);

        assertThat(reservationTime.isDeleted()).isFalse();
    }

}
