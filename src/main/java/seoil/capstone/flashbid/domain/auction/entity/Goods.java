package seoil.capstone.flashbid.domain.auction.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import seoil.capstone.flashbid.global.common.enums.DeliveryType;

@Entity
public class Goods {
    @Id
    private Long id ;

    @Column
    private String title;

    @Column
    private String description;

    @Enumerated
    private DeliveryType deliveryType;

}
