package com.example.mywiki.service;

import com.example.mywiki.mapper.EbookSnapshotMapperCust;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EbookSnapshotService {

    @Resource
    public EbookSnapshotMapperCust ebookSnapshotMapperCust;


    public void generator() {
        ebookSnapshotMapperCust.generatorSnapshot();
    }
}
