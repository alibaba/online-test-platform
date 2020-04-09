package com.alibaba.test.steed.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.test.steed.service.IRuleService;
import com.alibaba.test.steed.service.ITaskService;
import com.alibaba.test.steed.constant.Configuration;
import com.alibaba.test.steed.constant.RuleLevel;
import com.alibaba.test.steed.constant.RuleResult;
import com.alibaba.test.steed.constant.TaskResult;
import com.alibaba.test.steed.dao.ResultMapper;
import com.alibaba.test.steed.dao.TaskMapper;
import com.alibaba.test.steed.domain.*;
import com.alibaba.test.steed.model.ResultExample;
import com.alibaba.test.steed.model.ResultWithBLOBs;
import com.alibaba.test.steed.model.Task;
import com.alibaba.test.steed.model.TaskExample;
import com.alibaba.test.steed.utils.Common;
import com.alibaba.test.steed.utils.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by liyang on 2019/8/28.
 */
@Component
@Service
public class TaskServiceImpl implements ITaskService {

    private Map<String, TaskRules> rulesMap = new Hashtable<>();

    @Autowired
    public TaskMapper taskMapper;

    @Autowired
    public ResultMapper resultMapper;

    @Autowired
    public IRuleService ruleService;

    @Autowired
    public Configuration config;

    ScheduledThreadPoolExecutor queryExecutor;

    ExecutorService executorService;

    private Logger logger = LoggerFactory.getLogger( TaskServiceImpl.class );

    @Override
    public List<Task> selectAll() {
        TaskExample taskExample = new TaskExample();
        taskExample.setOrderByClause( "`id` desc" );
        List<Task> task = taskMapper.selectByExampleWithBLOBs( taskExample );
        return task;
    }

    @Override
    public Task selectByPrimaryKey(Integer id) {
        return taskMapper.selectByPrimaryKey( id );
    }

    @Override
    public int runTest(String host, String data, String comment) {

        File file = new File( config.queryPath + data );
        Task task = new Task();
        task.setHost( host );
        task.setTestData( file.getName() );
        task.setTaskInfo( comment );
        task.setStartTime( new Date() );
        task.setTaskResult( TaskResult.RUNNING );
        taskMapper.insert( task );
        int taskid = task.getId();
        try {
            initRuleSets( String.valueOf( taskid ), file.getName() );
            int line = Common.getFileLineNum( file );
            final CountDownLatch latch = new CountDownLatch( line );

            InputStream inputStream = new FileInputStream( file );
            InputStreamReader is = new InputStreamReader( inputStream );
            BufferedReader br = new BufferedReader( is );
            String req;
            int current = 0;
            while ((req = br.readLine()) != null) {
                current++;
                executeQuery( host, req, latch, taskid, current, file.getName() );
            }
        } catch (Exception ex) {
            logger.error( ex.getMessage() );
        }

        return task.getId();
    }

    @Override
    public List<ResultWithBLOBs> selectResult(Integer id, String ids) {
        ResultExample resultExample = new ResultExample();
        ResultExample.Criteria c = resultExample.createCriteria();
        c.andTaskidEqualTo( id );
        c.andCaseidIn( Arrays.asList(ids.split(",")).stream().map(s -> Integer.valueOf( s )).collect(Collectors.toList()) );
        List<ResultWithBLOBs> resultWithBLOBsList = resultMapper.selectByExampleWithBLOBs( resultExample );
        return resultWithBLOBsList;
    }

    @Override
    public int deleteTask(Integer ruleId) {
        return taskMapper.deleteByPrimaryKey( ruleId );
    }

    private void executeQuery(final String host, final String req, final CountDownLatch latch, final Integer taskid, final Integer line, final String data) {

        if (queryExecutor == null) {
            queryExecutor = new ScheduledThreadPoolExecutor( config.queryPoolSize );
        }
        queryExecutor.schedule( (new Runnable() {

            @Override
            public void run() {

                try {
                    final String request = host + req;
                    long start = System.currentTimeMillis();
                    final String resp = HttpClient.get( request );
                    logger.debug( "Request is : {}" , request );
                    logger.debug( "Response is : {}" , resp );
                    long end = System.currentTimeMillis();
                    logger.info( "Query line : " + line + " run millisecond:" + (end - start) );
                    executeRule( host, request, resp, latch, taskid, line, data );
                } catch (Exception e) {
                    logger.error( e.getMessage() );
                }
            }
        }), line *  config.queryInterval , TimeUnit.MILLISECONDS );

    }

    private void executeRule(final String host, final String request, final String resp, final CountDownLatch latch, final Integer taskid, final Integer line, final String data) {

        if (executorService == null) {
            executorService = Executors.newFixedThreadPool( config.rulePoolSize );
        }

        executorService.submit( new Runnable() {
            @Override
            public void run() {
                RuleRequestDto rd = new RuleRequestDto();
                rd.type = data;
                rd.data.response = resp;
                rd.data.request = request;
                rd.data.taskid = String.valueOf( taskid );
                final String rq = JSON.toJSONString( rd );
                final String rs = ruleService.queryRuleEngine( rq );
                logger.debug( "Rule request is {}" , rs );
                logger.debug( "Rule response is {}" , rs );
                compute( String.valueOf( taskid ), host, request, resp, rs, String.valueOf( line ), false );
                latch.countDown();
                finalTask( taskid, latch );
            }
        } );

    }

    private void finalTask(Integer taskid, CountDownLatch latch) {

        if (latch.getCount() == 0) {
            logger.info( "Task {} finished.", taskid );
            Task task = new Task();
            task.setId( taskid );
            task.setEndTime( new Date() );
            task.setRuleResult( getTaskRuleStatistics( String.valueOf( taskid ) ) );
            task.setTaskResult( updateTaskResult( String.valueOf( taskid ) ) ? TaskResult.PASS : TaskResult.FAIL );
            logger.info( "Task result is {}" , JSON.toJSONString( task ) );
            taskMapper.updateByPrimaryKeySelective( task );
        }
    }

    public synchronized void compute(String taskid, String host, String req, String res, String rule, String queryid, boolean retry) {

        if (!rulesMap.containsKey( taskid )) {
            TaskRules tr = new TaskRules();
            rulesMap.put( taskid, tr );
        }
        try {
            TaskRules tr = rulesMap.get( taskid );
            RuleResponseDto rd = JSON.parseObject( rule, RuleResponseDto.class );
            if (rd.data.executedRuleSet == null) {
                return;
            }
            for (RuleVo rr : rd.data.executedRuleSet) {
                String ruleid = String.valueOf( rr.getId() );
                TaskRuleStatistics trs;
                if (tr.result.containsKey( ruleid )) {
                    trs = tr.result.get( ruleid );
                } else {
                    trs = new TaskRuleStatistics();
                    trs.ruleId = rr.getId().toString();
                    trs.ruleName = rr.getName().toString();
                    trs.ruleCategoryName = rr.getCategory();
                    trs.ruleLevel = rr.getLevel().toString();
                    tr.result.put( rr.getId().toString(), trs );
                    if (rr.getLevel().equals( RuleLevel.HIGH ) || rr.getLevel().equals( RuleLevel.LOW )) {
                        trs.finalStatus = RuleResult.PASS;
                    } else {
                        trs.finalStatus = RuleResult.FAIL;
                    }
                }

                trs.total += 1;
                if (rr.getState().equals( RuleResult.PASS )) {
                    trs.passNum += 1;
                    if (trs.passed.size() < config.querySampleCount) {
                        trs.passed.add( queryid );
                    }
                    if (rr.getLevel().equals( RuleLevel.MEDIUM )) {
                        trs.finalStatus = RuleResult.PASS;
                    }
                    if (trs.finalStatus.equals( RuleResult.MISS )) {
                        trs.finalStatus = RuleResult.PASS;
                    }
                } else {
                    if (trs.error.size() < config.querySampleCount && !trs.error.contains( rr.getMessage() )) {
                        trs.error.add( rr.getMessage() );
                    }
                    if (trs.failed.size() < config.querySampleCount) {
                        trs.failed.add( queryid );
                        if (!tr.detailList.contains( queryid )) {
                            tr.detailList.add( queryid );
                            insertResult( taskid, req, res, queryid ,rule );
                        }
                    }
                    if (rr.getLevel().equals( RuleLevel.HIGH ) || rr.getLevel().equals( RuleLevel.LOW )) {
                        trs.finalStatus = RuleResult.FAIL;
                    }
                }
            }
        } catch (Exception ex) {
            logger.error( ex.getMessage() );
        }

    }

    public boolean updateTaskResult(String taskid) {
        TaskRules tr = rulesMap.get( taskid );
        boolean reportStatus = true;
        for (TaskRuleStatistics ts : tr.result.values()) {
            if (ts.finalStatus == null) {
                logger.error( "Task failed ts.finalStatus is null " + ts.ruleId );
                reportStatus = false;
                break;
            }
            if (ts.finalStatus.equals( RuleResult.FAIL )) {
                logger.info( "Rule id {} result is FAIL. " , ts.ruleId );
                reportStatus = false;
                break;
            }
            if (ts.finalStatus.equals( RuleResult.MISS ) && ts.ruleLevel.equals( RuleLevel.HIGH )) {
                logger.error( "High level rule missed, rule id is {}." , ts.ruleId );
                reportStatus = false;
                break;
            }
        }

        if (tr.result.values().size() < config.minimumRule) {
            reportStatus = false;
            logger.error( "Task rule less then " + config.minimumRule );
        }

        if (reportStatus == false) {
            // Email Alert
        }

        return reportStatus;
    }

    private void insertResult(String taskid, String request, String response,String line , String rule) {
        ResultWithBLOBs result = new ResultWithBLOBs();
        result.setTaskid( Integer.valueOf( taskid ) );
        result.setResponse( response );
        result.setRequest( request );
        result.setCaseid( Integer.valueOf(line) );
        result.setRuleResult( rule );
        resultMapper.insert( result );
    }

    private String getTaskRuleStatistics(String taskid) {
        TaskRules tr = rulesMap.get( taskid );
        String info = JSON.toJSONString( tr );
        return info;
    }

    public void initRuleSets(String taskid, String taskType) {

        try {
            TaskRules tr = new TaskRules();
            String sets = ruleService.getRuleSetByType( taskType );
            ArrayList<RuleVo> res = JSON.parseObject( sets, RuleResponseDto.class ).data.ruleSet;
            for (RuleVo rr : res) {
                TaskRuleStatistics ts = new TaskRuleStatistics();
                ts.finalStatus = RuleResult.MISS;
                ts.ruleId = rr.id;
                ts.ruleName = rr.name;
                ts.ruleCategoryId = rr.categoryId;
                ts.ruleCategoryName = rr.category;
                ts.ruleLevel = rr.level;
                tr.result.put( rr.id, ts );
            }
            rulesMap.put( taskid, tr );
        } catch (Exception ex) {
            logger.error( ex.getMessage() );
        }
    }

}
