package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationHistory;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.ErrorMessage;
import roomescape.exception.custom.BadRequestException;
import roomescape.exception.custom.ConflictException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationHistoryDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

@Service
@RequiredArgsConstructor
public class ReservationCommandService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationHistoryDao reservationHistoryDao;
    private final ThemeDao themeDao;

    @Transactional
    public ReservationResponse create(String name, LocalDate date, long timeId, long themeId, LocalDateTime requestedAt) {
        ReservationTime time = getReservationTime(timeId);
        Theme theme = getTheme(themeId);

        Reservation reservation = Reservation.pending(name, date, time, theme, requestedAt);
        validateNoDuplicateReservation(date, timeId, themeId);
        Reservation savedReservation = reservationDao.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    @Transactional
    public ReservationResponse update(Long reservationId, LocalDate date, Long timeId, LocalDateTime requestedAt) {
        ReservationTime time = getReservationTime(timeId);
        Reservation existing = reservationDao.findById(reservationId);

        validateNoDuplicateReservation(date, timeId, existing.reservationTheme().id());
        Reservation rescheduled = Reservation.reschedule(reservationId, existing.username(), date, time, existing.reservationTheme(), requestedAt);

        return ReservationResponse.from(reservationDao.update(rescheduled));
    }

    @Transactional
    public void delete(long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId);
        reservationHistoryDao.save(ReservationHistory.from(reservation));
        reservationDao.delete(reservationId);
    }

    private ReservationTime getReservationTime(long timeId) {
        return reservationTimeDao.findAvailableByTimeId(timeId)
                .orElseThrow(() -> new BadRequestException(ErrorMessage.TIME_NOT_FOUND.format(timeId)));
    }

    private Theme getTheme(long themeId) {
        return themeDao.findAvailableByThemeId(themeId)
                .orElseThrow(() -> new BadRequestException(ErrorMessage.THEME_NOT_FOUND.format(themeId)));
    }

    private void validateNoDuplicateReservation(LocalDate date, long timeId, long themeId) {
        if (reservationDao.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new ConflictException(ErrorMessage.DUPLICATE_RESERVATION);
        }
    }
}
