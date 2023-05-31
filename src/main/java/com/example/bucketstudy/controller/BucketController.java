package com.example.bucketstudy.controller;


import com.example.bucketstudy.domain.BucketTest;
import com.example.bucketstudy.domain.User;
import com.example.bucketstudy.domain.dto.SignResponse;
import com.example.bucketstudy.service.PricingPlanService;
import com.example.bucketstudy.service.SignService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Duration;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/khj93")
public class BucketController {

    private final PricingPlanService pricing;

    private final SignService signService;

    @PostMapping("/bucketTest")
    public ResponseEntity<BucketTest.Response> bucketTest(Principal principal) throws Exception {

        SignResponse user = signService.getUser(principal.getName());

        log.info("REQUEST : {}", user);

        String getApi = "FREE";

        if(user.getPay().equals("BASIC")) {
            getApi = "BA-";
        } else if(user.getPay().equals("PROFESSIONAL")) {
            getApi = "PX-";
        } else {
            getApi = "FREE";
        }

        Bucket bucket = pricing.resolveBucket(getApi);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        long saveToken = probe.getRemainingTokens();

        if(probe.isConsumed()) {
            log.info("SUCCESS");
            log.info("Token Name : {}", user.getPay());
            log.info("Available Token : {} ", saveToken + 1);

            return ResponseEntity.status(HttpStatus.OK).build();
        }

        long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;

        log.info("TOO MANY REQUEST");
        log.info("Available Token : {} ", saveToken);
        log.info("Wait Time {} Second ", waitForRefill);

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();





    }



}
