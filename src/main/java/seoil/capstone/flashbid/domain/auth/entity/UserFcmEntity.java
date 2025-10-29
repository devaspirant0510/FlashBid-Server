package seoil.capstone.flashbid.domain.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import seoil.capstone.flashbid.domain.user.entity.Account;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserFcmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    private String token;

    private Boolean enabled;
}
