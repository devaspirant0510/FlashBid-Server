package seoil.capstone.flashbid.domain.auction.entity;


import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated
    private DeliveryType deliveryType;

}
