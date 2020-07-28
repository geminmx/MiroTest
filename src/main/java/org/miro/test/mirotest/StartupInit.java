package org.miro.test.mirotest;

import org.miro.test.mirotest.widget.WidgetStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupInit {

    @Bean
    public WidgetStorage widgetStorage() {
        return new WidgetStorage();
    }
}
