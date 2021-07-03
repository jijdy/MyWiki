package com.example.mywiki.jobRepeat;

import com.example.mywiki.mapper.EbookSnapshotMapperCust;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class EbookSnopstopJob {
    public final Logger LOG = LoggerFactory.getLogger(EbookSnopstopJob.class);

    @Resource
    private EbookSnapshotMapperCust ebookSnapshotMapperCust;

    /*
    * 电子书快照定时执行，时间间隔设定
    * */
    @Scheduled(cron = "5/30 * * * * ?")
    public void cron()  {
        LOG.info("生成电子书快照开始：");
        long start = System.currentTimeMillis();
        ebookSnapshotMapperCust.generatorSnapshot();
        LOG.info("电子书快照生成，耗时：{}", System.currentTimeMillis() - start);
    }

}
