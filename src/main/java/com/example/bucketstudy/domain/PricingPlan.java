package com.example.bucketstudy.domain;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.sql.Ref;
import java.time.Duration;

public enum PricingPlan {

    FREE {
        public Bandwidth getLimit() {
            return Bandwidth.classic(3, Refill.intervally(3, Duration.ofHours(1)));
        }
    },

    BASIC {
        public Bandwidth getLimit() {
            return Bandwidth.classic(5, Refill.intervally(5, Duration.ofHours(1)));
        }
    },

    PROFESSIONAL {
        public Bandwidth getLimit() {
            return Bandwidth.classic(10, Refill.intervally(10, Duration.ofHours(1)));
        }
    };

    public abstract Bandwidth getLimit();

    public static PricingPlan resolvePlanFromApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return FREE;
        } else if(apiKey.startsWith("BA-")) {
            return BASIC;
        } else if(apiKey.startsWith("PX-")) {
            return PROFESSIONAL;
        }

        return FREE;
    }

}
