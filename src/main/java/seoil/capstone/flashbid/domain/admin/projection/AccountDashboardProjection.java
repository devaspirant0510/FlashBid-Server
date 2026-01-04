package seoil.capstone.flashbid.domain.admin.projection;


public interface AccountDashboardProjection {
    Long getTotalUserCount();

    Long getTodayUserCount();

    Long getYesterdayUserCount();
}
