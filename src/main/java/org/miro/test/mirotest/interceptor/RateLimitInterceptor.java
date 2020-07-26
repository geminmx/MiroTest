package org.miro.test.mirotest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final String HEADER_LIMIT = "Rate-Limit-Capacity";
    private static final String HEADER_LIMIT_REMAINING = "Rate-Limit-Remaining-Tokens";
    private static final String HEADER_RETRY_AFTER = "Rate-Limit-Retry-After-Seconds";

    @Autowired
    private MethodLimitService methodLimitService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (response.getHeaderNames().size() > 0) {
            return true;
        }
        String httpMethod = request.getMethod();
        String requestURI = request.getRequestURI();
        Pair<String, String> httpRequestParams = new Pair<>(httpMethod, requestURI);

        Bucket tokenBucket = methodLimitService.resolveBucket(httpRequestParams);

        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.addHeader(HEADER_LIMIT, String.valueOf(Bandwidths.getBucketCapacity(httpRequestParams)));
            response.addHeader(HEADER_LIMIT_REMAINING, String.valueOf(probe.getRemainingTokens()));
            return true;

        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.addHeader(HEADER_LIMIT, String.valueOf(Bandwidths.getBucketCapacity(httpRequestParams)));
            response.addHeader(HEADER_RETRY_AFTER, String.valueOf(waitForRefill));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value()); // 429

            return false;
        }
    }
}