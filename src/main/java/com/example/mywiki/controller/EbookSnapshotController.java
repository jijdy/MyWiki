package com.example.mywiki.controller;

import com.example.mywiki.mapper.EbookSnapshotMapperCust;
import com.example.mywiki.resp.CommonResp;
import com.example.mywiki.resp.EbookSnapshotResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {

    @Resource
    EbookSnapshotMapperCust ebookSnapshotMapperCust;

    @GetMapping("/get-statistic")
    public CommonResp<List<EbookSnapshotResp>> StaticFind() {
        CommonResp<List<EbookSnapshotResp>> commonResp = new CommonResp<>();
        List<EbookSnapshotResp> list = ebookSnapshotMapperCust.dataFind();
        commonResp.setContent(list);
        return commonResp;
    }

    @GetMapping("/get-30-statistic")
    public CommonResp<List<EbookSnapshotResp>> Static30Find() {
        CommonResp<List<EbookSnapshotResp>> commonResp = new CommonResp<>();
        List<EbookSnapshotResp> list = ebookSnapshotMapperCust.data30dayFind();
        commonResp.setContent(list);
        return commonResp;
    }


}
