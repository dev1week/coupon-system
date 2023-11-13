package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.*;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;

    private final CouponCountRepository couponCountRepository;


    private final CouponCreateProducer couponCreateProducer;


    private final AppliedUserRepository appliedUserRepository;


    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer, AppliedUserRepository appliedUserRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
        this.appliedUserRepository = appliedUserRepository;
    }

    public void apply(Long userId){
        //자바의
        Long apply = appliedUserRepository.add(userId);

        //지원 개수가 1보다 클 경우 이미 발급 받았던 이력이 있는 유저임
        if(apply!=1){
            return;
        }


        //아래 로직 전체에 락을 걸게된다면 너무 느려진다.

        //쿠폰 개수에 대한 정합성만 관리하면되므로 해당 부분만 락을 걸어준다.
        //레디스는 싱글 스레드 기반으로 동작하므로 레이스 컨디션을 해결할 수 있다.

//        long count = couponRepository.count();
        Long count = couponCountRepository.increment();

        if(count>100){
            return;
        }


//        couponRepository.save(new Coupon((userId)));
        //db에 바로 입력하는 것이 아닌 큐에 등록하도록한다.
        couponCreateProducer.create(userId);
    }
}
