package org.miro.test.mirotest.interceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.bucket4j.BucketConfiguration;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;

@Service
public class MethodLimitService {

    private final Map<Pair<String, String>, Pair<Bucket, Integer>> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(Pair<String, String> httpRequestParams) {
        Pair<Bucket, Integer> pair = cache.get(httpRequestParams);
        if (pair == null) {
            cache.put(httpRequestParams, new Pair<>(newBucket(httpRequestParams), Bandwidths.getBucketCapacity(httpRequestParams)));
            return cache.get(httpRequestParams).getKey();
        } else {
            if (pair.getValue() != Bandwidths.getBucketCapacity(httpRequestParams)) {
                Bandwidth newLimit = Bandwidths.getLimit(httpRequestParams);
                BucketConfiguration newConfiguration = Bucket4j.configurationBuilder()
                        .addLimit(newLimit)
                        .build();
                pair.getKey().replaceConfiguration(newConfiguration);
            }
            return pair.getKey();
        }
    }

    private Bucket newBucket(Pair<String, String> httpRequestParams) {
        return bucket(Bandwidths.getLimit(httpRequestParams));
    }

    private Bucket bucket(Bandwidth limit) {
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }
}