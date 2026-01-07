package seoil.capstone.flashbid.global.common.error;


import seoil.capstone.flashbid.global.common.response.ErrorDetails;

public class NotFoundAuctionException extends ApiException {
    public NotFoundAuctionException() {
        super(new ErrorDetails(
                "https://chlorinated-peripheral-27a.notion.site/E401_AUTH_INCORRECT_ID_AND_PASSWORD-2832f64f80ba80209546f9b5bde5dbe1",
                "존재하지 않는 경매입니다.",
                404,
                "해당 경매는 존재하지 않습니다.",
                null,
                "E404_AUCTION_NOTFOUND"
        ));
    }
}
