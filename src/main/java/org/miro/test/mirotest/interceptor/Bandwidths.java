package org.miro.test.mirotest.interceptor;

import java.time.Duration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import javafx.util.Pair;
import org.miro.test.mirotest.ApplicationProperties;

public class Bandwidths {
    
    public static Bandwidth getLimit(Pair<String, String> httpRequestParams) {
        int bucketCapacity = getBucketCapacity(httpRequestParams);
        return Bandwidth.classic(bucketCapacity, Refill.intervally(bucketCapacity, Duration.ofMinutes(1)));
    }

    public static int getBucketCapacity(Pair<String, String> httpRequestParams) {
        int bucketCapacity = Integer.parseInt(ApplicationProperties.getProperty("rate.limit.default"));

        if (httpRequestParams.getValue().equals("/test")) {
            bucketCapacity = Integer.parseInt(ApplicationProperties.getProperty("rate.limit.get.test"));
        } else if (httpRequestParams.getValue().startsWith("/widgets")) {
            switch (httpRequestParams.getKey()) {
                case "GET":
                    if (httpRequestParams.getValue().matches("/widgets/[0-9]+")) {
                        bucketCapacity = Integer.parseInt(ApplicationProperties.getProperty("rate.limit.get.widget"));
                    } else {
                        bucketCapacity = Integer.parseInt(ApplicationProperties.getProperty("rate.limit.get.all"));
                    }
                    break;
                case "POST":
                    bucketCapacity = Integer.parseInt(ApplicationProperties.getProperty("rate.limit.post.widget"));
                    break;
                case "PUT":
                    bucketCapacity = Integer.parseInt(ApplicationProperties.getProperty("rate.limit.put.widget"));
                    break;
                case "DELETE":
                    bucketCapacity = Integer.parseInt(ApplicationProperties.getProperty("rate.limit.delete.widget"));
                    break;
            }
        }
        return bucketCapacity;
    }
}