package seoil.capstone.flashbid.domain.dm.entity;

import jakarta.persistence.*;
import lombok.*;
import seoil.capstone.flashbid.domain.user.entity.Account;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dm_participate")
public class DMParticipate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 채팅방
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private DMRoom room;

    // 참가자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participate")
    private Account participant;
}
