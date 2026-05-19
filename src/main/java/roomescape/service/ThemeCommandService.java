package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.dto.response.ThemeResponse;
import roomescape.exception.ErrorMessage;
import roomescape.exception.custom.BadRequestException;
import roomescape.exception.custom.ConflictException;
import roomescape.exception.custom.NotFoundException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;

@Service
@RequiredArgsConstructor
public class ThemeCommandService {

    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;

    @Transactional
    public ThemeResponse create(String name, String thumbnailUrl, String description) {
        validateNoDuplicateTheme(name);
        Theme saved = themeDao.save(Theme.pending(name, thumbnailUrl, description));
        return ThemeResponse.from(saved);
    }

    public void delete(long id) {
        Theme theme = themeDao.findByThemeId(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.THEME_NOT_FOUND));

        if (theme.isDeleted()) {
            throw new BadRequestException(ErrorMessage.THEME_ALREADY_DELETED);
        }

        if (reservationDao.existsByThemeId(id)) {
            throw new ConflictException(ErrorMessage.THEME_IN_USE);
        }

        themeDao.delete(id);
    }

    private void validateNoDuplicateTheme(String name) {
        if (themeDao.existsByName(name)) {
            throw new ConflictException(ErrorMessage.DUPLICATE_THEME);
        }
    }
}
