package myproject.eumfruit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogDto {
    private String postdate;

    private String title;

    private String description;

    private String bloggerNm;

    private String display;

    private String link;

    private String bloggerlink;

    public BlogDto(String postdate, String title, String description, String bloggerNm, String display, String link, String bloggerlink) {
        this.postdate = postdate;
        this.title = title;
        this.description = description;
        this.bloggerNm = bloggerNm;
        this.display = display;
        this.link = link;
        this.bloggerlink = bloggerlink;
    }
}
