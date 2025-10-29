package seoil.capstone.flashbid.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seoil.capstone.flashbid.domain.payment.entity.PaymentEntity;
import seoil.capstone.flashbid.domain.payment.projection.UserPaymentProjection;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    @Query("SELECT p.userId.id AS userId, SUM(p.chargedPoint) AS totalPoints " +
           "FROM Payment p " +
           "WHERE p.userId.id IN :userIds " +
           "GROUP BY p.userId.id " +
           "ORDER BY SUM(p.chargedPoint) DESC")
    List<UserPaymentProjection> findTotalPointsByUserIdsOrderByTotalPointsDesc(@Param("userIds") List<Long> userIds);
}
