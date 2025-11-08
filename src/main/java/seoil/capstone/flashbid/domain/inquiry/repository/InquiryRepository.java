package seoil.capstone.flashbid.domain.inquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findAllByUserId(Long userId);

    @Query("select i from Inquiry i left join fetch i.reply where i.id = :id")
    Optional<Inquiry> findByIdWithReply(@Param("id") Long id);

    @Query("select i from Inquiry i left join fetch i.reply where i.user.id = :userId order by i.id desc")
    List<Inquiry> findAllByUserIdWithReply(@Param("userId") Long userId);

    @Query("select i from Inquiry i left join fetch i.reply order by i.id desc")
    List<Inquiry> findAllWithReply();
}