package com.jpmorgan.log.aggregator.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
@Configuration
public class LogScrapingSchedule {
    Logger logger = LoggerFactory.getLogger(LogScrapingSchedule.class);

    @Value("${Loggerator.host}")
    String loggeratorHost;

    @Value("${loggerator.port}")
    int loggeratorPort;

    @Value("${loggerator.connection.timeout}")
    int loggeratorConnectionTimeout;

    @Value("${loggerator.read.timeout}")
    int loggeratorReadTimeout;

    @Value("${api.host.baseurl}")
    private String apiHost;

    @Scheduled(fixedDelayString = "${scheduler.fixed.time}")
    public void fetchLogs() {
        logger.info("Starting scheduler");
        List<String> parsedLineList = new ArrayList<>();
        BufferedReader reader = null;
        try {
            Socket loggeratorSocketConnect = loggeratorSocketConnect();
            reader = new BufferedReader(
                    new InputStreamReader(loggeratorSocketConnect.getInputStream()));
            String output;
            while((output = reader.readLine()) != null){
                //logger.info(output);
                parsedLineList.add(output);

            }
        }catch(IOException e){
            logger.error(e.getMessage());
        }finally{
            try {
                reader.close();
            }catch(IOException e){
            }
        }
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<List<String>> request = new HttpEntity<List<String>>(parsedLineList, headers);
            String result = restTemplate.postForObject(apiHost, request, String.class);

            logger.info("Parsed Lines received : " + parsedLineList.size());
            //next step insert them into data store
        }catch(Exception e){
            logger.error("Error storing the data : " + e.getMessage());
        }
    }

    private Socket loggeratorSocketConnect(){
        Socket s = new Socket();
        try {
            s.connect(new InetSocketAddress(loggeratorHost, loggeratorPort), loggeratorConnectionTimeout); // Connection timeout set to 5 seconds with socket.
            s.setSoTimeout(loggeratorReadTimeout); // Read timeout set to 2 seconds. This means socket should send response in maximum of 2 second for every request.
            logger.info("socket connection created");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return s;
    };

}
