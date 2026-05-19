package roomescape.domain;

import java.net.URL;
import lombok.RequiredArgsConstructor;
import roomescape.domain.exception.DomainException;

@RequiredArgsConstructor
public class Theme {

    private final Long id;
    private final String name;
    private final String thumbnailUrl;
    private final String description;
    private final ThemeStatus status;

    public static Theme pending(String name, String thumbnailUrl, String description) {
        validateUrl(thumbnailUrl);
        return new Theme(null, name, thumbnailUrl, description, ThemeStatus.DRAFT);
    }

    private static void validateUrl(String url) {
        try {
            new URL(url).toURI();
        } catch (Exception e) {
            throw new DomainException("유효한 URL 형식이 아닙니다.");
        }
    }

    public static Theme of(long id, String name, String thumbnailUrl, String description, ThemeStatus status) {
        return new Theme(id, name, thumbnailUrl, description, status);
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String thumbnailUrl() {
        return thumbnailUrl;
    }

    public String description() {
        return description;
    }

    public boolean isDeleted() {
        return this.status == ThemeStatus.DELETED;
    }

    public ThemeStatus status() {
        return status;
    }
}
