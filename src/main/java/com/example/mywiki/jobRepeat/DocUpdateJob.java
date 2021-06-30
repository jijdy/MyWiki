package com.example.mywiki.jobRepeat;

import com.example.mywiki.mapper.DocMapperCust;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DocUpdateJob {
    public final Logger LOG = LoggerFactory.getLogger(DocUpdateJob.class);

    @Resource
    private DocMapperCust docMapperCust;

    @Scheduled(cron = "5/30 * * * * ?")
    public void cron()  {
        docMapperCust.updateEbookInfo();
        LOG.info("每隔30秒更新一次电子书文档数据");
    }

}
