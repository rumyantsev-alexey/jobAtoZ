package ru.job4j.parser;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Класс предназначен для начального запуска проекта и конфигурации планировщика
 */
public class StartParser {

    private static final Logger LOG = LogManager.getLogger(StartParser.class.getName());

    public static void main(final String[ ] args) {
        String opt;
        LOG.info("Begin job..");
        opt = args.length != 0 ? args[0] : "app.properties";

        Properties prt = new Properties();
            try (InputStream inputStream = StartParser.class.getClassLoader().getResourceAsStream(opt)) {
                prt.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JobDetail job = newJob(ShedJob.class)
                    .withIdentity("job1", "group1")
                    .build();
            job.getJobDataMap().put("conf", opt);

            CronTrigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .withSchedule(cronSchedule(prt.getProperty("cron.time")))
                    .build();

            Scheduler sched = null;
            try {
                sched = new StdSchedulerFactory().getScheduler();
                sched.scheduleJob(job, trigger);
                sched.start();
            } catch (SchedulerException e) {
                LOG.warn(e.getMessage(), e);
            }
        }
    }
