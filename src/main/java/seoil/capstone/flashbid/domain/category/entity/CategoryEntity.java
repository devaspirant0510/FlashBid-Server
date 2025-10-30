package seoil.capstone.flashbid.domain.category.entity;

import jakarta.persistence.*;
import lombok.*;
import seoil.capstone.flashbid.global.core.BaseTimeEntity;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "category")
public class CategoryEntity  extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToOne
    private CategoryEntity root;
}
