package seoil.capstone.flashbid.global.aop.aspect;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.service.AccountService;
import seoil.capstone.flashbid.global.common.error.ApiException;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthUserAspect {
    private final AccountService accountService;
    @Around("@annotation(seoil.capstone.flashbid.global.aop.annotation.AuthUser)")
    public Object injectUser(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth);
        if(auth==null || auth.getPrincipal()==null){
            throw new ApiException(HttpStatus.UNAUTHORIZED,"401E001","인증되지 않은 유저 ");
        }
        Account account = accountService.getUserByUuid(auth.getPrincipal().toString());
        Object[] args = joinPoint.getArgs();
        for(int i=0; i<args.length; i++){
            if(args[i] instanceof Account){
                args[i] = account;
            }
        }
        return joinPoint.proceed(args);

    }
}
