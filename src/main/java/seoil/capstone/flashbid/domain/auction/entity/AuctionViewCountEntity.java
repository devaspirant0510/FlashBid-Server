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

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "AuctionViewCount")
@Table(name = "auction_view_count")
public class AuctionViewCountEntity {
    @Id
    @Column(name = "auction_id")
    private Long id;

    @Column
    private Long viewCount;

    @Column
    private LocalDateTime backupTime;
}
