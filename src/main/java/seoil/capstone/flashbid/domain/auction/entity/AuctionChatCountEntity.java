package seoil.capstone.flashbid.domain.auction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.Entity;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "AuctionChatCount")
@Table(name = "auction_chat_count")
public class AuctionChatCountEntity {
    @Id
    @Column(name = "auction_id")
    private Long id;

    @Column
    private Long chatCount;
}
