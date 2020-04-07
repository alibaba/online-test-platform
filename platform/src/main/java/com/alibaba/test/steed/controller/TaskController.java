package com.alibaba.test.steed.controller;

import com.alibaba.test.steed.domain.AjaxResult;
import com.alibaba.test.steed.model.ResultWithBLOBs;
import com.alibaba.test.steed.model.Task;
import com.alibaba.test.steed.service.ITaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * Created by liyang on 2019/8/27.
 */
@RestController
@RequestMapping("/api/task")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    ITaskService taskService;

    @RequestMapping("/selectResult")
    public PageInfo<List<ResultWithBLOBs>> selectResult(@RequestParam(value = "taskId") Integer taskid,
                                                            @RequestParam(value = "caseIds") String caseIds) {
        List<ResultWithBLOBs> resultWithBLOBsList = taskService.selectResult( taskid , caseIds );
        PageInfo page = new PageInfo(resultWithBLOBsList);
        return page;
    }

    @RequestMapping("/selectAllByPage")
    public PageInfo<List<Task>> selectAllByPage(HttpServletRequest request,
                                                  @RequestParam(value = "pageNum") Integer pageNum,
                                                    @RequestParam(value = "pageSize") Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Task> persons = taskService.selectAll();
        PageInfo page = new PageInfo(persons);
        return page;
    }

    @RequestMapping("/getTask")
    public Task getTask(HttpServletRequest request,
                        @RequestParam(value = "id") Integer id){
        Task t = taskService.selectByPrimaryKey( id );
        return t;
    }

    @RequestMapping(value = "/runTest")
    public AjaxResult runtest(HttpServletRequest request,
                              @RequestParam(value = "host") String host,
                                @RequestParam(value = "data") String data,
                                    @RequestParam(value = "comment") String comment,
                                        HttpServletResponse resp) {
        int taskid = taskService.runTest( host, data, comment );
        AjaxResult result = new AjaxResult();
        result.setSuccess( taskid );
        logger.info( "Task {} submitted.", taskid);
        return result;
    }

    @RequestMapping("/deleteTask")
    public AjaxResult deleteTask(@RequestParam(value = "id") Integer taskId){
        int ret = taskService.deleteTask( taskId );
        AjaxResult result = new AjaxResult();
        result.setSuccess( ret );
        return result;
    }

}
