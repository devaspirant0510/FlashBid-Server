package seoil.capstone.flashbid.domain.auction.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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

    @Enumerated()
    @Column(name = "delivery_type", nullable = false, length = 20)

    private DeliveryType deliveryType;

}
