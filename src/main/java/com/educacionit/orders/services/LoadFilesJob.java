
package com.educacionit.orders.services;


import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoadFilesJob implements Job {


    private static final Logger logger = Logger.getLogger(LoadFilesJob.class);


    public void execute(JobExecutionContext context) throws JobExecutionException {

        logger.debug ("Finding CVS files...");
        logger.debug ("Getting DataMap...");
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        String path = dataMap.getString("path");
        String extension = dataMap.getString("extension");

        logger.debug (String.format("Loading files %s from %s", extension, path));
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {

            List<String> result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(extension)).collect(Collectors.toList());

            logger.debug (String.format("Files %d found !!!", result.size()));

        } catch (IOException e) {

            logger.error (String.format("Problems loading files %s from %s", extension, path), e);
        }
    }
}