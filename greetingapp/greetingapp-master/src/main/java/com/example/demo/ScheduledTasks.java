package com.example.demo;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {

    RestTemplate restTemplate = new RestTemplate();

    //@Scheduled(cron = "*/10 * * * * *")
    public Greeting createGreeting() {
        Greeting g = new Greeting(0, RandomStringUtils.randomAlphabetic(10));
        String postUrl = "http://localhost:8080/createGreeting";
        restTemplate.postForObject(postUrl, g, Greeting.class);
        return g;
    }

    //@Scheduled(cron = "*/30 * * * * *")
    public Greeting getGreeting() {
        int id = RandomUtils.nextInt(0, 100);
        String getUrl = "http://localhost:8080/getGreeting/" + id;
        Greeting g = restTemplate.getForObject(getUrl, Greeting.class);
        System.out.println(g.getContent());
        return g;
    }


    @Scheduled (fixedRate = 5000)
    public void periodicTask1() {
        System.out.println("The time now is " + new Date());
    }



    //@Scheduled(cron = "*/45 * * * * *")
    public void updateGreeting() {
        String putURL = "http://localhost:8080/updateGreeting";
        if (getGreeting().getContent().equals("bye")) {
            restTemplate.put(putURL, "hello");
        } else {
            restTemplate.put(putURL, "bye");
        }
        Greeting g = restTemplate.getForObject("http://localhost:8080/greeting", Greeting.class);
        System.out.println(g.getContent());
    }


}
