package com.choing.flashbid.domain.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import com.choing.flashbid.domain.user.entity.Account;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_fcm")
@Entity(name = "UserFcm")
public class UserFcmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    private String token;

    private Boolean enabled;
}
