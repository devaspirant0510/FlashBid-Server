package seoil.capstone.flashbid.domain.auction.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
import seoil.capstone.flashbid.global.common.enums.DeliveryType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Goods {
    @Id
    private Long id ;

    @Column(length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated()
    @Column(name = "delivery_type", nullable = false, length = 20)

    private DeliveryType deliveryType;

}
