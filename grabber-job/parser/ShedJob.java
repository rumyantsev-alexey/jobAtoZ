package ru.job4j.parser;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Класс реализует функционал проекта при работе по расписанию
 */
public class ShedJob implements Job {

    private static final Logger LOG = LogManager.getLogger(ShedJob.class.getName());

    public void execute(JobExecutionContext context) throws JobExecutionException {
        String opt = context.getJobDetail().getJobDataMap().getString("conf");
        DataBase db = new DataBase(opt);
        ParserSQLRU prs = new ParserSQLRU();
        LOG.info("Last timestamp:" + db.getLastTime());
        LOG.info(db.writeDB(prs.loadVac(db.getLastTime())) + " records saved");

    }

}
