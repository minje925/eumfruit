package myproject.eumfruit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CafeDto {

    private String title;

    private String description;

    private String cafeNm;

    private String display;

    private String link;

    private String cafelink;

    public CafeDto(String title, String description, String cafeNm, String display, String link, String cafelink) {
        this.title = title;
        this.description = description;
        this.cafeNm = cafeNm;
        this.display = display;
        this.link = link;
        this.cafelink = cafelink;
    }
}
