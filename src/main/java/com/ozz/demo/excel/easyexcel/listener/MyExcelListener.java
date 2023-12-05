package com.ozz.demo.excel.easyexcel.listener;

import cn.hutool.log.StaticLog;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MyExcelListener<T> extends AnalysisEventListener<T> {
    // 每隔1000条消费数据，然后清理list
    private int batchCount = 1000;
    // 缓存的数据
    private List<T> cachedDataList = new ArrayList<>(batchCount);
    // 数据消费者
    private Consumer<List<T>> consumer;

    public MyExcelListener(Consumer<List<T>> consumer) {
        this.consumer = consumer;
    }
    public MyExcelListener(Consumer<List<T>> consumer, int batchCount) {
        this.consumer = consumer;
        this.batchCount = batchCount;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        StaticLog.debug("解析行{}: {}", context.readRowHolder().getRowIndex()+1, JSON.toJSONString(data));
        cachedDataList.add(data);
        // 达到批处理次数，释放缓存
        if (cachedDataList.size() >= batchCount) {
            consumerData();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        consumerData();
        StaticLog.debug("所有数据解析完成！");
    }

    /**
     * 消费数据
     */
    private void consumerData() {
        StaticLog.debug("{}条数据，开始消费！", cachedDataList.size());
        consumer.accept(cachedDataList);
        cachedDataList = new ArrayList<>(batchCount);// 消费完成清理 list
        StaticLog.debug("消费成功！");
    }
}