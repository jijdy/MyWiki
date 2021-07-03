package com.example.mywiki.service;

import com.example.mywiki.mapper.EbookSnapshotMapperCust;
import com.example.mywiki.resp.EbookSnapshotResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookSnapshotService {

    @Resource
    public EbookSnapshotMapperCust ebookSnapshotMapperCust;


    public void generator() {
        ebookSnapshotMapperCust.generatorSnapshot();
    }

    public List<EbookSnapshotResp> findDate() {
        return ebookSnapshotMapperCust.dataFind();
    }

    public List<EbookSnapshotResp> find30Day() {
        return ebookSnapshotMapperCust.data30dayFind();
    }
}
