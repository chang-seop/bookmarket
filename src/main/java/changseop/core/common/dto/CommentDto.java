package changseop.core.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CommentDto {
    private Long id;
    private String title;
    private String description;


    @Builder
    public CommentDto(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
