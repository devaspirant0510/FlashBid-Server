package seoil.capstone.flashbid.domain.auction.dto.response;


import lombok.Getter;

@Getter
public class ViewCountResultDto {
    private long viewCount;

    public static ViewCountResultDto create(long viewCount) {
        ViewCountResultDto result = new ViewCountResultDto();
        result.viewCount = viewCount;
        return result;
    }
}
