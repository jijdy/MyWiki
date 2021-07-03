package com.example.mywiki.mapper;

import com.example.mywiki.resp.EbookSnapshotResp;

import java.util.List;

public interface EbookSnapshotMapperCust {

    void generatorSnapshot();

    List<EbookSnapshotResp> dataFind();

    List<EbookSnapshotResp> data30dayFind();
}
