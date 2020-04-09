package com.alibaba.test.steed.service;


import com.alibaba.test.steed.model.ResultWithBLOBs;
import com.alibaba.test.steed.model.Task;

import java.util.List;

/**
 * Created by liyang on 2019/8/28.
 */

public interface ITaskService {

    List<Task> selectAll();

    Task selectByPrimaryKey(Integer id);

    int runTest(String host, String data, String comment);

    List<ResultWithBLOBs> selectResult(Integer id, String caseIds);

    int deleteTask(Integer ruleId);

}
