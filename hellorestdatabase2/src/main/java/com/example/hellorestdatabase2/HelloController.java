package com.example.hellorestdatabase2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import org.springframework.web.client.RestTemplate;



@RestController
public class HelloController {

    private static int id;
    private RestTemplate restTemplate = new RestTemplate();




    @RequestMapping("/greeting")
    public String greeting() {
        return "Hello World!";
    }




    @RequestMapping("/greetingObject")
    public Greeting greetingObject() {
        id++;
        return new Greeting(id,"Hello");
    }




    @RequestMapping("/greetingByName")
    public String greetingByName(@RequestParam(value="name", defaultValue="World") String name) {
        return "Hello " + name;
    }




    @RequestMapping(value="/newgreeting", method= RequestMethod.POST)
    public Greeting newGreeting(@RequestBody String name) {
        Greeting greeting = new Greeting(1,name);
        return greeting;
    }




    @RequestMapping(value="/samegreeting", method=RequestMethod.POST)
    public Greeting sameGreeting(@RequestBody Greeting greeting) {
        return greeting;
    }




    @RequestMapping(value="/getHighestGreeting", method=RequestMethod.POST)
    public Greeting getHighestGreeting(@RequestBody List<Greeting> list) {
        TreeSet<Greeting> set = new TreeSet<Greeting>();
        for(Greeting g: list) {
            set.add(g);
        }
        return set.last();
    }




    @RequestMapping(value="/getHighestGreeting2", method=RequestMethod.POST)
    public Greeting getHighestGreeting2(@RequestBody List<Greeting> list) {
        Greeting highest = list.get(0);
        for(Greeting g: list) {
            if(g.getId() > highest.getId()) {
                highest = g;
            }
        }
        return highest;
    }




    @RequestMapping(value="/updateGreeting", method=RequestMethod.PUT)
    public Greeting updateGreeting(@RequestBody String newMessage) throws IOException {
        //ObjectMapper provides functionality for reading and writing JSON
    	ObjectMapper mapper = new ObjectMapper();
        String message = FileUtils.readFileToString(new File("./message.txt"), "UTF-8");
        //deserialize JSON to greeting object
        Greeting greeting = mapper.readValue(message, Greeting.class);
        //update message
        greeting.setContent(newMessage);
        //serialize greeting object to JSON
        mapper.writeValue(new File("./message.txt"), greeting);
        return greeting;
    }




    @RequestMapping(value="/getHackerNews", method=RequestMethod.GET)
    public String getHackerNews() {
        System.setProperty("webdriver.chrome.driver","chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://news.ycombinator.com");
        List<WebElement> list = driver.findElements(By.className("storylink"));
        ArrayList<String> stories = new ArrayList<String>();
        for(WebElement e: list) {
            stories.add(e.getText());
        }
        driver.close();
        return stories.toString();
    }




    @RequestMapping(value="/deleteGreeting", method =RequestMethod.DELETE)
    public void deleteGreeting(@RequestBody int id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String message = FileUtils.readFileToString(new File("./message.txt"), "UTF-8");
        Greeting greeting = mapper.readValue(message, Greeting.class);
        if(greeting.getId() == id) {
            FileUtils.writeStringToFile(new File("./message.txt"),"", "UTF-8");
        }
    }




    @RequestMapping(value="/createGreeting", method=RequestMethod.POST)
    public Greeting createGreeting(@RequestBody String str) throws Exception {
        Random r = new Random();
        Greeting g = new Greeting(r.nextInt(),str);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("./message.txt"),g);
        return g;
    }




    @Scheduled (cron = "*/3 * * * * *")
	public void printAlternateGreetings()
    {
    	String updateURL = "http://localhost:8080/updateGreeting";
    	String getURL = "http://localhost:8080/getGreeting";
    	Greeting g = restTemplate.getForObject(getURL, Greeting.class);
    	System.out.println(g.getContent());
    	if (g.getContent().equals("Hello World"))
	    {
	    	g.setContent("Bye World");
	    }
    	else
	    {
	    	g.setContent("Hello World");
	    }
    	restTemplate.put(updateURL, g.getContent());
    }









}
