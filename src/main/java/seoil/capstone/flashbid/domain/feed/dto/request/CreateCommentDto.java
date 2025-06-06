package seoil.capstone.flashbid.domain.feed.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentDto {
    private String contents;
    private Long commentId;
    private Long feedId;
}
