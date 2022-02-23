package com.phisa.Schedular;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.Date;

@Configuration
@EnableScheduling
public class ScheduledConfiguration {
    @Scheduled(fixedRate = 5000)
    public void job() {
        Schedular schedular = new Schedular();
        File[] file = schedular.findFilesByPattern("[tT]est[1-2].txt", "./src/main/resources");
        schedular.displayFiles(file);
       // System.out.println(Thread.currentThread().getName()+" The Task1 executed at "+ new Date());
        try {
            //delete file from file array
            //schedular.deleteFiles(file);
            //Thread.sleep(10000);
            //display remaining files
            schedular.displayFiles(file);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
