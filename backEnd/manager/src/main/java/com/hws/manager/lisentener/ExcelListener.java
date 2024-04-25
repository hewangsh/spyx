package com.hws.manager.lisentener;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.hws.manager.mapper.CategoryMapper;
import com.alibaba.excel.util.ListUtils;
import com.hws.model.vo.product.CategoryExcelVo;

import java.util.List;

public class ExcelListener<T> implements ReadListener<T> {

    /**
     每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    //构造器传递mapper，操作数据库（TODO 不能直接用spring注入，并发时不知道是读的哪一行--存在并发问题）
    private CategoryMapper categoryMapper;
    public ExcelListener(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }
    //从第二行开始读取，把每行读取的内容封装到t对象里面
    // 每解析一行数据就会调用一次该方法
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        //把每行数据对象t放到cachedDataList集合中
        cachedDataList.add(t);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            //调用方法一次性批量添加数据库里
            savaData();
            //清理集合
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    private void savaData() {
        categoryMapper.batchInsert((List<CategoryExcelVo>)cachedDataList);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //保存数据：对于数据没有满100行数据的情况，上面的方法不执行，就调用这个方法
        savaData();
    }
}