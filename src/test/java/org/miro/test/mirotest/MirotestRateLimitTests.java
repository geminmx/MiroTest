package org.miro.test.mirotest;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javafx.util.Pair;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.miro.test.mirotest.interceptor.Bandwidths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Mirotest.class)
@AutoConfigureMockMvc
public class MirotestRateLimitTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getAllWidgets_whenRequestsWithinRateLimit_thenAccepted_nextRejected() throws Exception {
		String uri = "/widgets";
		RequestBuilder request = get(uri).contentType(MediaType.APPLICATION_JSON_VALUE);

		for (int i = 1; i <= Bandwidths.getBucketCapacity(new Pair<>("GET", "/widgets")); i++) {
			mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(header().exists("Rate-Limit-Remaining-Tokens"))
					.andExpect(header().exists("Rate-Limit-Capacity"));
		}

		mockMvc.perform(request)
				.andExpect(status().isTooManyRequests())
				.andExpect(header().exists("Rate-Limit-Retry-After-Seconds"))
				.andExpect(header().exists("Rate-Limit-Capacity"));
	}
}