package com.example.consumer.consumer;


import com.example.consumer.domain.Coupon;
import com.example.consumer.domain.FailedEvent;
import com.example.consumer.repository.CouponRepository;
import com.example.consumer.repository.FailedEventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CouponCreateConsumer {

    private final CouponRepository couponRepository;

    private final FailedEventRepository failedEventRepository;

    public CouponCreateConsumer(CouponRepository couponRepository, FailedEventRepository failedEventRepository) {
        this.couponRepository = couponRepository;
        this.failedEventRepository = failedEventRepository;
    }

    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    public void listener(Long userId){
        System.out.println(userId);

        try{
            couponRepository.save(new Coupon(userId));
        }catch(Exception e){
            failedEventRepository.save(new FailedEvent((userId)));
        }

    }
}
