package changseop.core.common.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CommentDto {
    private String title;
    private String description;


    public CommentDto(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
