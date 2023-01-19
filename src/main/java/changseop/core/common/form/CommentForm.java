package changseop.core.common.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentForm {
    private String title;
    private String description;

    public CommentForm(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
