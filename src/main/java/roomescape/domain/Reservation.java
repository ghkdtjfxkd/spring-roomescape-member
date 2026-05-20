package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import roomescape.domain.exception.DomainException;

@RequiredArgsConstructor
public class Reservation {

    private final Long id;
    private final String username;
    private final LocalDate reservationDate;
    private final ReservationTime reservationTime;
    private final Theme reservationTheme;

    public static Reservation pending(String username, LocalDate date, ReservationTime time, Theme theme, LocalDateTime requestedAt) {
        Reservation reservation = Reservation.of(null, username, date, time, theme);
        reservation.validateNotPast(requestedAt);
        return reservation;
    }

    public static Reservation of(Long id, String username, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(id, username, date, time, theme);
    }

    public static Reservation reschedule(Long id, String username, LocalDate date, ReservationTime time, Theme theme, LocalDateTime requestedAt) {
        Reservation reservation = Reservation.of(id, username, date, time, theme);
        reservation.validateNotPast(requestedAt);
        return reservation;
    }

    public long id() {
        return id;
    }

    public String username() {
        return username;
    }

    public LocalDate reservationDate() {
        return reservationDate;
    }

    public ReservationTime reservationTime() {
        return reservationTime;
    }

    public Theme reservationTheme() {
        return reservationTheme;
    }

    private void validateNotPast(LocalDateTime now) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate, reservationTime.startAt());
        if (now.isAfter(reservationDateTime)) {
            throw new DomainException("지나간 날짜, 시간에 대한 예약 생성은 불가능합니다.");
        }
    }
}
