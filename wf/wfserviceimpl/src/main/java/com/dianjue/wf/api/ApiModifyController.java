package com.dianjue.wf.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dianjue.wf.entity.UserEntity;
import com.dianjue.wf.utils.R;
import com.dianjue.wf.utils.annotation.IgnoreAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api("动态创建流程")
public class ApiModifyController {

  Logger logger = LoggerFactory.getLogger(ApiModifyController.class);

  @Resource
  RepositoryService repositoryService;

  @Resource
  RuntimeService runtimeService;

  @Resource
  TaskService taskService;

  @Resource
  ApiProcessManageController apiProcessManageController;

  @IgnoreAuth
  @ApiOperation(value = "动态创建流程", notes = "动态创建流程")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "param", value = "param的json格式请按预定json格式传递", required = true)})
  @RequestMapping(value = "/dynamicCreate", method = RequestMethod.POST)
  public R dynamicCreate(@RequestBody String param) {
    JSONObject paramJson = JSONObject.parseObject(param);
    logger.info("dynamicCreate: " + paramJson.toJSONString());
    String pName = paramJson.getString("name");
    String data = paramJson.getString("data");
    JSONArray jsonArray = JSONArray.parseArray(data);
    List<CheckDemo> checkDemoList = new ArrayList<>();
    List<Integer> indexList = new ArrayList<>();
    //找出最大index
    for(int i=0;i<jsonArray.size();i++) {
      JSONObject jsonObject = JSONObject.parseObject(jsonArray.getString(i));
      String type = jsonObject.getString("type");
      if(type.equals("node")){
        int index = Integer.parseInt(jsonObject.getString("index"));
        indexList.add(index);
      }
    }
    int maxIndex = Collections.max(indexList);
    // 1. 创建bpmn模型
    BpmnModel model = new BpmnModel();
    Process process = new Process();
    model.addProcess(process);
    String processKey = "process-"+UUID.randomUUID();
    process.setId(processKey);
    process.setName(pName);

    List<Bak> bakList = new ArrayList<>();
    List<CcDemo> ccDemoList = new ArrayList<>();
    for(int i=0;i<jsonArray.size();i++){
      JSONObject jsonObject = JSONObject.parseObject(jsonArray.getString(i));
      String type = jsonObject.getString("type");
      String name = jsonObject.getString("name");

      //画开启节点
      if(type.equals("start")){
        process.addFlowElement(createStartEvent());
      }
      //画流程节点
      if(type.equals("node")){
        int index = jsonObject.getInteger("index");
        //创建task
        process.addFlowElement(createUserTask("task"+index, name));

        CheckDemo checkDemo = new CheckDemo();
        checkDemo.setProcessKey(processKey);
        checkDemo.setTaskNodeId("task"+index);
        String director = jsonObject.getString("director");
        checkDemo.setUserId(director);
        checkDemo.setIndex(String.valueOf(index));

        if(index==1){
          process.addFlowElement(createSequenceFlow("start", "task"+1,"flow"+1,"",""));
        }
        //创建互斥网关
        process.addFlowElement(createExclusiveGateway("exclusivegateway"+index));
        //创建连线
        process.addFlowElement(createSequenceFlow("task"+index, "exclusivegateway"+index,"flow"+(index+1),"",""));

        JSONArray itemArray = JSONArray.parseArray(jsonObject.getString("items"));
        for(int j=0;j<itemArray.size();j++){
          Bak bak = new Bak();
          JSONObject itemObject = JSONObject.parseObject(itemArray.getString(j));
          String value = itemObject.getString("value");
          String label = itemObject.getString("label");
          bak.setLabel(label);
          bak.setValue(value);
          bak.setUserId(director);
          bakList.add(bak);
          if(value.equals("YES") && index!=maxIndex){//如果不是最后一个task
            //创建互斥网关连线
            process.addFlowElement(createSequenceFlow("exclusivegateway"+index, "task"+(index+1),"flow_0_"+index,label,"#{msg=='"+value+"'}"));
          }else if(value.equals("NO")){
            //创建互斥网关连线
            process.addFlowElement(createEndEvent("end_1_"+index,"流程中止"));
            process.addFlowElement(createSequenceFlow("exclusivegateway"+index, "end_1_"+index,"flow_1_"+index,label,"#{msg=='"+value+"'}"));
          }else{//最后一个task
            //创建互斥网关连线
            process.addFlowElement(createEndEvent("end","流程结束"));
            process.addFlowElement(createSequenceFlow("exclusivegateway"+index, "end","flow_2_"+index,label,"#{msg=='"+value+"'}"));
          }
        }
        checkDemoList.add(checkDemo);
      }

      //抄送
      if(type.equals("read")){
        int ccIndex = jsonObject.getInteger("index");
        String ccUserId = jsonObject.getString("director");
        for(CheckDemo checkDemo : checkDemoList){
          if(ccIndex == Integer.parseInt(checkDemo.getIndex())+1){
            CcDemo ccDemo = new CcDemo();
            ccDemo.setCcUserId(ccUserId);
            ccDemo.setNextUserId(checkDemo.getUserId());
            ccDemoList.add(ccDemo);
          }
        }
      }
    }
    // 2. 生成BPMN自动布局
    new BpmnAutoLayout(model).execute();

    // 3. 部署这个BPMN模型
    Deployment deployment = repositoryService.createDeployment()
        .addBpmnModel("dynamic-model.bpmn", model).name("Dynamic process deployment").deploy();

//    // 4. 启动流程实例
//    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processId);
//
//    // 5. 发起任务
//    List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

//    Assert.assertEquals(1, tasks.size());
//    Assert.assertEquals("First task", tasks.get(0).getName());
//    Assert.assertEquals("fred", tasks.get(0).getAssignee());
//
//    // 6. 保存bpmn流程图
//    InputStream processDiagram = repositoryService
//        .getProcessDiagram(processInstance.getProcessDefinitionId());
//    try {
//      FileUtils.copyInputStreamToFile(processDiagram, new File("target/diagram.png"));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

    //模板用户绑定关系，根据processKey查询
    Sinleton.getInstance().getMap().put("checkDemoList", checkDemoList);
    Sinleton.getInstance().getMap().put("bakList", bakList);
    Sinleton.getInstance().getMap().put("ccDemoList", ccDemoList);
    Map<String, Object> resultMap = new HashMap<>();
//    resultMap.put("processId",processInstance.getProcessInstanceId());
//    resultMap.put("processDefinitionKey",processInstance.getProcessDefinitionKey());
//    resultMap.put("activityId",processInstance.getActivityId());
    resultMap.put("processKey",processKey);
//    resultMap.put("imageUrl","/wf/process/resource/read?processDefinitionId="+tasks.get(0).getProcessDefinitionId()+"&resourceType=image");
    return R.ok(resultMap);

//    // 7. 保存为bpmn.xml的xml类型文件
//    InputStream processBpmn = repositoryService
//        .getResourceAsStream(deployment.getId(), "dynamic-model.bpmn");
//    try {
//      FileUtils.copyInputStreamToFile(processBpmn, new File("target/process.bpmn20.xml"));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
  }

  //创建task
  private UserTask createUserTask(String id, String name) {
    UserTask userTask = new UserTask();
    userTask.setName(name);
    userTask.setId(id);
    return userTask;
  }

  //创建箭头
  private SequenceFlow createSequenceFlow(String from, String to,String id,String name,String conditionExpression) {
    SequenceFlow flow = new SequenceFlow();
    flow.setSourceRef(from);
    flow.setTargetRef(to);
    flow.setId(id);
    flow.setName(name);
    flow.setConditionExpression(conditionExpression);
    return flow;
  }

  /**
   *  创建互斥网关
   */
  private ExclusiveGateway createExclusiveGateway(String id){
    ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
    exclusiveGateway.setId(id);
    return  exclusiveGateway;
  }

  //开始事件
  private StartEvent createStartEvent() {
    StartEvent startEvent = new StartEvent();
    startEvent.setId("start");
    return startEvent;
  }

  //结束事件
  private EndEvent createEndEvent(String id,String name) {
    EndEvent endEvent = new EndEvent();
    endEvent.setId(id);
    endEvent.setName(name);
    return endEvent;
  }


  @IgnoreAuth
  @ApiOperation(value = "获取用户列表", notes = "获取用户")
  @GetMapping(value = "/getUsers")
  public R getUsers(){
    if(Sinleton.getInstance().getMap().get("users")==null){
      List<UserEntity> userEntityList = new ArrayList<>();
      for(int i=0;i<5;i++){
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId((long)i);
        userEntity.setUsername("用户"+i+"号");
        userEntityList.add(userEntity);
      }
      Sinleton.getInstance().getMap().put("users",userEntityList);
    }

    return R.ok(Sinleton.getInstance().getMap());
  }

  @IgnoreAuth
  @ApiOperation(value = "开启流程", notes = "开启流程")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "param",
          value = "param的json格式为:{\"processKey\":1,\"applyUserId\":1,\"bak\":\"fds\"}", required = true)})
  @PostMapping(value = "/processStart")
  public R processStart(@RequestBody String param){
    JSONObject paramJson = JSONObject.parseObject(param);
    logger.info("processStart: "+paramJson.toJSONString());
    String processKey = paramJson.getString("processKey");
    String applyUserId = paramJson.getString("applyUserId");
    String nextUserId = "";
    List<CheckDemo> checkDemoList = (List<CheckDemo>) Sinleton.getInstance().getMap().get("checkDemoList");

    for (CheckDemo checkDemo : checkDemoList){
      if(checkDemo.getProcessKey().equals(processKey) && checkDemo.getIndex().equals("1")){
        nextUserId = checkDemo.getUserId();
        break;
      }
    }

    String description = paramJson.getString("bak");

    JSONObject varJson = new JSONObject();
    varJson.put("userId",nextUserId);
    JSONObject pJson = new JSONObject();
    pJson.put("createUserId",applyUserId);
    pJson.put("description",description);

    R r = apiProcessManageController.startProcess(processKey,varJson.toJSONString(),pJson.toJSONString());
    String processInstanceId = (String) r.get("processInstanceId");

    //next
    apiProcessManageController.check(processInstanceId,varJson.toJSONString());

    taskService.setOwner((String) r.get("taskId"),nextUserId);
    taskService.setAssignee((String) r.get("taskId"),nextUserId);

    List<CheckDemo> checkDemoList1 = (List<CheckDemo>) Sinleton.getInstance().getMap().get("checkDemoList");
    for (CheckDemo checkDemo : checkDemoList1){
      if(checkDemo.getProcessKey().equals(processKey) && checkDemo.getIndex().equals("1")){
        nextUserId = checkDemo.getUserId();
        checkDemo.setProcessInstanceId(processInstanceId);
        break;
      }
    }

    ProcessDemo processDemo = new ProcessDemo();
    processDemo.setProcessInstanceId(processInstanceId);
    processDemo.setApplyUserId(applyUserId);
    processDemo.setNextUserId(nextUserId);
    processDemo.setProcessKey(processKey);
    processDemo.setActivityId((String) r.get("id"));
    processDemo.setIsEndActivity("false");
    processDemo.setTaskId((String) r.get("taskId"));
    processDemo.setTaskName((String) r.get("name"));
    processDemo.setDescription((String) r.get("description"));

    List<CcDemo> ccDemoList = (List<CcDemo>) Sinleton.getInstance().getMap().get("ccDemoList");
    for(CcDemo ccDemo : ccDemoList){
      if(ccDemo.getNextUserId().equals(nextUserId)){
        processDemo.setDuplUserId(ccDemo.getCcUserId());//抄送人
        break;
      }
    }

    //记录每一步流程
    List<ProcessDemo> processDemoList = new ArrayList<>();
    processDemoList.add(processDemo);
    Sinleton.getInstance().getMap().put("processDemoList",processDemoList);

    return R.ok(processInstanceId);
  }

  @IgnoreAuth
  @ApiOperation(value = "下一步流程", notes = "下一步流程")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "param",
          value = "param的json格式为:{\"processInstanceId\":1,\"msg\":msg值要和创建自定义流程时的msg保持一致}", required = true)})
  @PostMapping(value = "/processNext")
  public R processNext(@RequestBody String param){
    JSONObject paramJson = JSONObject.parseObject(param);
    logger.info("processNext: " + paramJson.toJSONString());
    String processInstanceId = paramJson.getString("processInstanceId");
    if(null == processInstanceId){
      return R.ok("processInstanceId不能为空");
    }
    String msg = paramJson.getString("msg");

    String nextUserId = "";
    List<CheckDemo> checkDemoList = (List<CheckDemo>) Sinleton.getInstance().getMap().get("checkDemoList");
    for (CheckDemo checkDemo : checkDemoList){
        if(checkDemo.getProcessInstanceId()==null){
          nextUserId = checkDemo.getUserId();
          break;
        }
    }

    JSONObject varJson = new JSONObject();
    varJson.put("userId",nextUserId);
    varJson.put("msg",msg);

    R r = apiProcessManageController.check(processInstanceId,varJson.toJSONString());

    if(!r.get("verProcessIsEnd").equals("true")){
      taskService.setOwner((String) r.get("taskId"), nextUserId);
      taskService.setAssignee((String) r.get("taskId"), nextUserId);
    }else{
      List<ProcessDemo> processDemoList = (List<ProcessDemo>) Sinleton.getInstance().getMap().get("processDemoList");
      List<ProcessDemo> newDemoList = new ArrayList<>();
      for(ProcessDemo processDemo : processDemoList){
        if(processDemo.getNextUserId().equals(nextUserId)){
          processDemo.setNextUserId("");
        }
        newDemoList.add(processDemo);
      }
      Sinleton.getInstance().getMap().remove("processDemoList");
      Sinleton.getInstance().getMap().put("processDemoList",newDemoList);
    }

    //抄送人
    List<CcDemo> ccDemoList = (List<CcDemo>) Sinleton.getInstance().getMap().get("ccDemoList");
    List<ProcessDemo> processDemoList = (List<ProcessDemo>) Sinleton.getInstance().getMap().get("processDemoList");
    if(processDemoList!=null){
      List<ProcessDemo> newDemoList = new ArrayList<>();
      for(ProcessDemo processDemo : processDemoList){
        for(CcDemo ccDemo : ccDemoList){
          if(ccDemo.getNextUserId().equals(nextUserId)){
            processDemo.setDuplUserId(ccDemo.getCcUserId());//抄送人
            break;
          }
        }
        newDemoList.add(processDemo);
      }
      Sinleton.getInstance().getMap().remove("processDemoList");
      Sinleton.getInstance().getMap().put("processDemoList",newDemoList);
    }

//    List<ProcessDemo> processDemoList = (List<ProcessDemo>) Sinleton.getInstance().getMap().get("processDemoList");
//    for(ProcessDemo processDemo : processDemoList){
//      //更新流程节点信息
//      if(processDemo.getProcessInstanceId().equals(processInstanceId)){
//        processDemo.setNextUserId(nextUserId);
//        processDemo.setActivityId((String) r.get("id"));
//        processDemo.setIsEndActivity((String) r.get("verProcessIsEnd"));
//        processDemo.setTaskId((String) r.get("taskId"));
//        processDemo.setTaskName((String) r.get("name"));
//      }
//    }
    return R.ok(processInstanceId);
  }

  @IgnoreAuth
  @ApiOperation(value = "根据processInstanceId查询流程", notes = "根据processInstanceId查询流程")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "param", value = "param的json格式为:{\"processInstanceId\":1}", required = true)})
  @PostMapping(value = "/processQuery")
  public R processQuery(@RequestBody String param){
    JSONObject paramJson = JSONObject.parseObject(param);
    logger.info("processQuery: " + paramJson.toJSONString());
    String processInstanceId = paramJson.getString("processInstanceId");
    Map<String, Object> resultMap = new HashMap<>();

    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

    List<ProcessDemo> processDemoList = (List<ProcessDemo>) Sinleton.getInstance().getMap().get("processDemoList");
    for(ProcessDemo processDemo : processDemoList){
      //更新流程节点信息
      if(processDemo.getProcessInstanceId().equals(processInstanceId)){
        resultMap.put("data",processDemo);
        resultMap.put("processInstanceId",processInstanceId);
      }
    }
    return R.ok(resultMap);
  }

  @IgnoreAuth
  @ApiOperation(value = "根据userId查询我需要处理的任务列表", notes = "根据userId查询我需要处理的任务列表")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "param", value = "param的json格式为:{\"userId\":1}", required = true)})
  @PostMapping(value = "/taskQuery")
  public R taskQuery(@RequestBody String param){
    JSONObject paramJson = JSONObject.parseObject(param);
    logger.info("taskQuery: " + paramJson.toJSONString());
    String userId = paramJson.getString("userId");
    List<Bak> bakList =  (List<Bak>) Sinleton.getInstance().getMap().get("bakList");

    String tyLabel="";
    String jjLabel="";
    for(Bak bak : bakList){
      if(bak.getUserId().equals(userId)){
        if(bak.getValue().equals("YES")){
          tyLabel = bak.getLabel();
        }else if(bak.getValue().equals("NO")){
          jjLabel = bak.getLabel();
        }
      }
    }

    R r = apiProcessManageController.queryTasks(userId);
    if(((List)r.get("data")).size()==0){
      List<ProcessDemo> processDemoList = (List<ProcessDemo>) Sinleton.getInstance().getMap().get("processDemoList");
      Map<String,Object> result = new HashMap<>();

      Map<String,Object> json = new HashMap<>();
      List<Object> resultList = new ArrayList<>();
      List<ProcessDemo> nextList = processDemoList.stream().filter(pd -> pd.getNextUserId().equals(userId)).
          collect(Collectors.toList());
      if(nextList.size()==0){
        nextList = processDemoList.stream().filter(pd -> pd.getDuplUserId().equals(userId)).
          collect(Collectors.toList());
        for(ProcessDemo processDemo : nextList){
          JSONObject jsonObject = new JSONObject();
          jsonObject.put("createUserId",processDemo.getApplyUserId());
          jsonObject.put("description",processDemo.getDescription());
          json.put("param",jsonObject.toString());
          json.put("processInstanceId", processDemo.getProcessInstanceId());
          json.put("name", processDemo.getTaskName());
          json.put("businessKey", processDemo.getProcessKey());
          json.put("tyLabel",tyLabel);
          json.put("jjLabel",jjLabel);
          resultList.add(json);
        }
        result.put("data",resultList);
        return R.ok(result);
      }

      for(ProcessDemo processDemo : nextList){
        if(processDemo.getProcessKey()==null){
          JSONObject jsonObject = new JSONObject();
          jsonObject.put("createUserId",processDemo.getApplyUserId());
          jsonObject.put("description",processDemo.getDescription());
          json.put("param",jsonObject.toString());
          json.put("processInstanceId", processDemo.getProcessInstanceId());
          json.put("name", processDemo.getTaskName());
          json.put("businessKey", processDemo.getProcessKey());
          json.put("tyLabel",tyLabel);
          json.put("jjLabel",jjLabel);
          resultList.add(json);
        }
      }
      result.put("data",resultList);
      return R.ok(result);
    }
    List<Map<String,Object>> dataList = (List<Map<String, Object>>) r.get("data");
    for(Map<String,Object> map : dataList){
      map.put("tyLabel",tyLabel);
      map.put("jjLabel",jjLabel);
    }
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("data",dataList);

    return R.ok(resultMap);
  }

  @IgnoreAuth
  @ApiOperation(value = "根据userId查询我的历史任务列表", notes = "根据userId查询我的历史任务列表")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "param", value = "param的json格式为:{\"userId\":1}", required = true)})
  @PostMapping(value = "/historyTaskQuery")
  public R historyTaskQuery(@RequestBody String param){
    JSONObject paramJson = JSONObject.parseObject(param);
    logger.info("historyTaskQuery: "+paramJson.toJSONString());
    String assignee = paramJson.getString("userId");
    String firstResult = paramJson.getString("pageNo");
    String maxResults = paramJson.getString("pageSize");

    return apiProcessManageController.queryHistoricTasks(assignee,firstResult,maxResults);
  }

  @IgnoreAuth
  @ApiOperation(value = "查询所有流程", notes = "查询所有流程")
  @PostMapping(value = "/processDefinitionQuery")
  public R processDefinitionQuery(){
    List<ProcessDefinition> processDefinitionLast= repositoryService.createProcessDefinitionQuery().
        orderByProcessDefinitionVersion().desc().list();

    List<ProcessDemo> processDemoList = new ArrayList<>();
    for(ProcessDefinition processDefinition : processDefinitionLast){
      ProcessDemo processDemo = new ProcessDemo();
      processDemo.setProcessInstanceId(processDefinition.getDeploymentId());
      processDemo.setProcessKey(processDefinition.getKey());
      processDemo.setProcessName(processDefinition.getName());
      processDemoList.add(processDemo);
    }
    Map<String,Object> resultMap = new HashMap<>();
    resultMap.put("data",processDemoList);
    return R.ok(resultMap);
  }

  @IgnoreAuth
  @ApiOperation(value = "抄送", notes = "抄送")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "param", value = "param的json格式为:{\"duplUserId\":1,\"processInstanceId\":1}", required = true)})
  @PostMapping(value = "/processDupl")
  public R processDupl(@RequestBody String param){
    JSONObject paramJson = JSONObject.parseObject(param);
    logger.info("processDupl: " + paramJson.toJSONString());
    String processInstanceId = paramJson.getString("processInstanceId");
    String duplUserId = paramJson.getString("duplUserId");

    List<ProcessDemo> processDemoList = (List<ProcessDemo>) Sinleton.getInstance().getMap().get("processDemoList");
    for(ProcessDemo processDemo : processDemoList){
      //更新流程节点信息
      if(processDemo.getProcessInstanceId().equals(processInstanceId)){
        processDemo.setDuplUserId(duplUserId);
      }
    }

    return R.ok("抄送给 "+duplUserId+" 成功");
  }

  @IgnoreAuth
  @ApiOperation(value = "根据抄送人ID抄送查询", notes = "根据抄送人ID抄送查询")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "param", value = "param的json格式为:{\"duplUserId\":1}", required = true)})
  @PostMapping(value = "/processDuplQuery")
  public R processDuplQuery(@RequestBody String param){
    JSONObject paramJson = JSONObject.parseObject(param);
    logger.info("processDuplQuery: " + paramJson.toJSONString());
    String duplUserId = paramJson.getString("duplUserId");
    Map<String,Object> resultMap = new HashMap<>();
    List<ProcessDemo> processDemoList = (List<ProcessDemo>) Sinleton.getInstance().getMap().get("processDemoList");

    List<ProcessDemo> duplList = processDemoList.stream().filter(pd -> pd.getDuplUserId().equals(duplUserId)).collect(
        Collectors.toList());
    resultMap.put("data",duplList);
    return R.ok(resultMap);
  }

  @IgnoreAuth
  @ApiOperation(value = "根据processInstanceId查询下一节点", notes = "根据processInstanceId查询下一节点")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "param", value = "param的json格式为:{\"processInstanceId\":1}", required = true)})
  @PostMapping(value = "/processNextNodeQuery")
  public R processNextNodeQuery(@RequestBody String param){
    JSONObject paramJson = JSONObject.parseObject(param);
    logger.info("processNextNodeQuery: " + paramJson.toJSONString());
    Map<String,Object> resultMap = new HashMap<>();

    String processInstanceId = paramJson.getString("processInstanceId");
    Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();

    ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(task.getProcessDefinitionId());
    List<ActivityImpl> activitiList = def.getActivities();
    String excId = task.getExecutionId();
    ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(excId).singleResult();
    String activitiId = execution.getActivityId();
    for(ActivityImpl activityImpl:activitiList){
      String id = activityImpl.getId();
      if(activitiId.equals(id)){
        List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();//获取从某个节点出来的所有线路
        for(PvmTransition tr:outTransitions){
          PvmActivity ac = tr.getDestination(); //获取线路的终点节点
          resultMap.put("nextNodeId",ac.getId());
          resultMap.put("nextNodeName",ac.getProperty("name"));
        }
        break;
      }
    }

    return R.ok(resultMap);
  }

  @IgnoreAuth
  @ApiOperation(value = "根据processKey查询", notes = "根据processKey查询")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "param", value = "param的json格式为:{\"processKey\":1}", required = true)})
  @PostMapping(value = "/taskUserQuery")
  public R taskUserQuery(@RequestBody String param){
    JSONObject paramJson = JSONObject.parseObject(param);
    logger.info("taskUserQuery: " + paramJson.toJSONString());
    String processKey = paramJson.getString("processKey");
    Map<String,Object> resultMap = new HashMap<>();
    List<CheckDemo> checkDemoList = (List<CheckDemo>) Sinleton.getInstance().getMap().get("checkDemoList");

    List<CheckDemo> taskUserList = checkDemoList.stream().filter(cd -> cd.getProcessKey().equals(processKey)).collect(
        Collectors.toList());
    resultMap.put("data",taskUserList);
    return R.ok(resultMap);
  }
}

