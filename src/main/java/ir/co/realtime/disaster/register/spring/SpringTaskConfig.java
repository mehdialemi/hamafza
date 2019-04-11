package ir.co.realtime.disaster.register.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan({ "ir.co.realtime.disaster.register.task" })
public class SpringTaskConfig {

}
