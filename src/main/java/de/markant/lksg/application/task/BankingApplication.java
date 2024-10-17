package de.markant.lksg.application.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "de.markant.lksg.application.task")
public class BankingApplication {

  public static void main(String[] args) {
    SpringApplication.run(BankingApplication.class, args);

  }

}