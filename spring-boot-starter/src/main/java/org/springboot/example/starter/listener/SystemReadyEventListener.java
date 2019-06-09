package org.springboot.example.example.starter.listener;

import java.net.InetAddress;
import java.net.UnknownHostException;


import lombok.extern.slf4j.Slf4j;
import org.springboot.example.example.starter.context.EnvironmentHolder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

@Slf4j
public class SystemReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    Environment env = EnvironmentHolder.getEnvironment();
    String protocol = "http";
    if (env.getProperty("server.ssl.key-store") != null) {
      protocol = "https";
    }
    try {
      log.info("\n----------------------------------------------------------\n\t" +
              "Application '{}' started success! \n\tWith Access URLs:\n\t" +
              "Local: \t\t{}://localhost:{}/swagger-ui.html\n\t" +
              "External: \t{}://{}:{}/swagger-ui.html\n\t" +
              "Profile(s): \t{}\n----------------------------------------------------------",
          env.getProperty("spring.application.name"),
          protocol,
          env.getProperty("server.port"),
          protocol,
          InetAddress.getLocalHost().getHostAddress(),
          env.getProperty("server.port"),
          env.getActiveProfiles());
    } catch (UnknownHostException e) {
    }
  }
}
