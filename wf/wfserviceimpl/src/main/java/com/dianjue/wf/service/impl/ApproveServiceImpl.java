package com.dianjue.wf.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dianjue.sso.dto.UserInfoDto;
import com.dianjue.wf.api.ApiProcessManageController;
import com.dianjue.wf.api.AuthcenterClient;
import com.dianjue.wf.dao.ApproveDao;
import com.dianjue.wf.dao.ApproveFlowsDao;
import com.dianjue.wf.dao.ApproveModelDao;
import com.dianjue.wf.dao.ApproveNodeDao;
import com.dianjue.wf.entity.Approve;
import com.dianjue.wf.entity.ApproveFlows;
import com.dianjue.wf.entity.ApproveFlowsRequest;
import com.dianjue.wf.entity.ApproveModel;
import com.dianjue.wf.entity.ApproveModelRequest;
import com.dianjue.wf.entity.ApproveNext;
import com.dianjue.wf.entity.ApproveNode;
import com.dianjue.wf.service.ApproveService;
import com.dianjue.wf.utils.R;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class ApproveServiceImpl implements ApproveService {

  @Resource
  private ApproveModelDao approveModelDao;

  @Resource
  private ApproveFlowsDao approveFlowsDao;

  @Resource
  private ApproveNodeDao approveNodeDao;

  @Resource
  private AuthcenterClient authcenterClient;

  @Resource
  RepositoryService repositoryService;

  @Resource
  ApiProcessManageController apiProcessManageController;

  @Resource
  TaskService taskService;

  @Resource
  protected RuntimeService runtimeService;

  @Resource
  private HistoryService historyService;

  @Resource
  ApproveDao approveDao;

  @Override
  public List<ApproveModel> getModelListByTypeId(int approveTypeId, String token) {
    QueryWrapper<ApproveModel> queryWrapper = new QueryWrapper<>();
    if(approveTypeId != 0){
      queryWrapper.lambda().eq(ApproveModel::getApproveTypeId, approveTypeId);
    }
    UserInfoDto userInfoDto = authcenterClient.getToken(token).getData();
    queryWrapper.lambda().and(am -> am.eq(ApproveModel::getTenantId,userInfoDto.getTenantId()).or().isNull(ApproveModel::getTenantId));

    List<ApproveModel> models = approveModelDao.selectList(queryWrapper);
    List<ApproveModel> filterModels;
    if(approveTypeId == 0){
      filterModels = models.stream().sorted(Comparator.comparing(ApproveModel::getRecTime).reversed()).collect(Collectors.toList());
    }else{
      filterModels = models.stream().filter(m -> m.getTenantId()!=null && !m.getModelId().equals("1") &&
          !m.getModelId().equals("2") && !m.getModelId().equals("3")).
          sorted(Comparator.comparing(ApproveModel::getRecTime).reversed()).collect(Collectors.toList());
    }

    if(filterModels.size()>0){
      filterModels = filterModels.stream().filter(distinctByKey(ApproveModel::getModelCode)).collect(Collectors.toList());
      return filterModels;
    }else {
      return models;
    }
  }

  private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Map<Object,Boolean> seen = new ConcurrentHashMap<>();
    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void updateModel(ApproveModelRequest queryModel,String token) {
    ApproveModel approveModel = approveModelDao.selectById(queryModel.getModelId());
    UserInfoDto userInfoDto = authcenterClient.getToken(token).getData();
    String modelId = UUID.randomUUID().toString().replaceAll("-","");

    //封装model_json
    JSONObject modelJson = new JSONObject();
    modelJson.put("name",approveModel.getModelName());
    JSONArray dataArray = new JSONArray();
    JSONObject startJson = new JSONObject();
    startJson.put("name",approveModel.getModelName());
    startJson.put("type","start");
    dataArray.add(startJson);

    List<ApproveNode> approveNodeList = queryModel.getApproveNodeList();
    approveNodeList.forEach(node -> {
      JSONObject nodeJson = new JSONObject();
      nodeJson.put("name",node.getNodeName());
      nodeJson.put("index",node.getNodeIndex());
      nodeJson.put("type",node.getNodeType());

      node.setNodeId(UUID.randomUUID().toString().replaceAll("-",""));
      node.setModelId(!"123".contains(approveModel.getModelId())?approveModel.getModelId():modelId);
      node.setTenantId(userInfoDto.getTenantId());
      if(node.getNodeType().equals("node")){
        nodeJson.put("approveUser",node.getApproveUser());
        JSONArray itemArray = new JSONArray();
        JSONObject yItemJson = new JSONObject();
        yItemJson.put("label","同意");
        yItemJson.put("value","YES");
        itemArray.add(yItemJson);
        JSONObject nItemJson = new JSONObject();
        nItemJson.put("label","驳回");
        nItemJson.put("value","NO");
        itemArray.add(nItemJson);
        nodeJson.put("items",itemArray);
      }else if(node.getNodeType().equals("read")){
        nodeJson.put("ccUser",node.getCcUser());
      }
      dataArray.add(nodeJson);

      approveNodeDao.insert(node);
    });
    JSONObject endJson = new JSONObject();
    endJson.put("name","流程终点");
    endJson.put("type","end");
    dataArray.add(endJson);
    modelJson.put("data", dataArray);

    String processKey = dynamicCreate(modelJson,approveModel);
    approveModel.setProcessKey(processKey);
    approveModel.setTenantId(userInfoDto.getTenantId());
    approveModel.setRecTime(LocalDateTime.now());
    approveModel.setModelId(modelId);
//    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
//    LocalDateTime dateTime = LocalDateTime.now();
//    approveModel.setModelCode("SP"+dateTime.format(dtf)+(int) (Math.random() * 900 + 100));
    approveModel.setRecTime(LocalDateTime.now());
    approveModel.setTenantId(userInfoDto.getTenantId());
    approveModel.setStatus(queryModel.getStatus());
    approveModelDao.insert(approveModel);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public String processStart(ApproveFlowsRequest approveFlowsRequest, String token) {
    UserInfoDto userInfoDto = authcenterClient.getToken(token).getData();
    ApproveModel approveModel = approveModelDao.selectById(approveFlowsRequest.getModelId());

    if(approveModel == null){
      return "modelId错误：" + approveFlowsRequest.getModelId();
    }
    QueryWrapper<ApproveNode> approveNodeQueryWrapper = new QueryWrapper<>();
    approveNodeQueryWrapper.lambda().eq(ApproveNode::getModelId,approveModel.getModelId()).
        eq(ApproveNode::getTenantId,userInfoDto.getTenantId());
    List<ApproveNode> approveNodeList = approveNodeDao.selectList(approveNodeQueryWrapper);
    ApproveNode indexNode = approveNodeList.stream().filter(node->node.getNodeIndex().equals("1")).
        collect(Collectors.toList()).get(0);

    String processKey = approveModel.getProcessKey();
    JSONObject varJson = new JSONObject();
    varJson.put("userId",JSONObject.parseObject(JSONArray.parseArray(indexNode.getApproveUser()).get(0).toString()).getString("userId"));
    JSONObject pJson = new JSONObject();
    pJson.put("createUserId",approveFlowsRequest.getApplyUserId());
    pJson.put("description",approveModel.getModelName());
    R r = apiProcessManageController.startProcess(processKey,varJson.toJSONString(),pJson.toJSONString());
    String processInstanceId = (String) r.get("processInstanceId");
    taskService.setAssignee((String) r.get("taskId"),varJson.getString("userId"));

    ProcessInstance rpi = runtimeService.createProcessInstanceQuery()
        .processInstanceId(processInstanceId).singleResult();

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    LocalDateTime dateTime = LocalDateTime.now();
    Approve approve = new Approve();
    approve.setApproveCode("SP"+dateTime.format(dtf)+(int) (Math.random() * 900 + 100));
    approve.setApproveId(UUID.randomUUID().toString().replaceAll("-",""));
    approve.setApplyUserId(approveFlowsRequest.getApplyUserId());
    approve.setApplyUserName(approveFlowsRequest.getApplyUserName());
    approve.setApproveTitle(approveFlowsRequest.getApproveTitle());
    approve.setModelId(approveFlowsRequest.getModelId());
    approve.setProcessInstanceId(processInstanceId);
    approve.setRecTime(dateTime);
    approve.setTenantId(userInfoDto.getTenantId());
    approveDao.insert(approve);

    ApproveFlows approveFlows = new ApproveFlows();
    approveFlows.setApproveId(approve.getApproveId());
    //判断流程是否结束
    //审批状态，1：审批中，2：审批驳回，3：审批通过
    if (rpi == null) {
      //通过
      if(approveFlowsRequest.getOperaType()==1){
        approveFlows.setApproveStatus(3);
      }else{
        approveFlows.setApproveStatus(2);
      }
    }else {
      approveFlows.setApproveStatus(1);
    }
    approveFlows.setApproveSuggest(approveFlowsRequest.getApproveSuggest());
    approveFlows.setApproveTime(dateTime);
    approveFlows.setCurrentNodeIndex(indexNode.getNodeIndex());
    approveFlows.setOperaType(approveFlowsRequest.getOperaType());
    approveFlows.setRecTime(dateTime);
    approveFlows.setTenantId(userInfoDto.getTenantId());
    approveFlowsDao.insert(approveFlows);

    return approveFlows.getApproveId();
  }

  @Override
  public String processNext(ApproveNext approveNext, String token) {
    Approve approve = approveDao.selectById(approveNext.getApproveId());
    UserInfoDto userInfoDto = authcenterClient.getToken(token).getData();
    ApproveModel approveModel = approveModelDao.selectById(approve.getModelId());

    if(approveModel == null){
      return "modelId错误：" + approve.getModelId();
    }
    QueryWrapper<ApproveFlows> approveFlowsQueryWrapper = new QueryWrapper<>();
    approveFlowsQueryWrapper.lambda().eq(ApproveFlows::getApproveId,approve.getApproveId()).orderByDesc(ApproveFlows::getApproveTime);
    ApproveFlows approveFlows = approveFlowsDao.selectOne(approveFlowsQueryWrapper);
    if(approveFlows==null){
      return "请输入正确的approveId";
    }
    if("true".equals(approveFlows.getVerProcessIsEnd())){
      return "流程已结束";
    }

    QueryWrapper<ApproveNode> approveNodeQueryWrapper = new QueryWrapper<>();
    approveNodeQueryWrapper.lambda().eq(ApproveNode::getModelId,approveModel.getModelId());
    List<ApproveNode> approveNodeList = approveNodeDao.selectList(approveNodeQueryWrapper);
    ApproveNode indexNode = approveNodeList.stream().filter(node ->
        Integer.parseInt(node.getNodeIndex())>=Integer.parseInt(approveFlows.getCurrentNodeIndex())).
        collect(Collectors.toList()).get(0);

    JSONObject varJson = new JSONObject();
    varJson.put("userId",JSONObject.parseObject(JSONArray.parseArray(indexNode.getApproveUser()).get(0).toString()).getString("userId"));
    varJson.put("msg",approveFlows.getOperaType()==1?"YES":"NO");
    R r = apiProcessManageController.check(approve.getProcessInstanceId(),varJson.toJSONString());

    ApproveFlows saveFlows = new ApproveFlows();
    LocalDateTime dateTime = LocalDateTime.now();
    //判断流程是否结束
    //审批状态，1：审批中，2：审批驳回，3：审批通过
    if (r.get("verProcessIsEnd").equals("true")) {
      //通过
      if(approveNext.getOperaType()==1){
        saveFlows.setApproveStatus(3);
        saveFlows.setOperaType(1);
      }else{
        saveFlows.setApproveStatus(2);
        saveFlows.setOperaType(2);
      }
      saveFlows.setVerProcessIsEnd("true");
    }else {
      saveFlows.setApproveStatus(1);
      taskService.setAssignee((String) r.get("taskId"),varJson.getString("userId"));
    }
    saveFlows.setApproveId(approveNext.getApproveId());
    saveFlows.setFlowId(UUID.randomUUID().toString().replaceAll("-",""));
    saveFlows.setApproveSuggest(approveNext.getApproveSuggest());
    saveFlows.setApproveTime(dateTime);
    saveFlows.setCurrentNodeIndex(String.valueOf(Integer.parseInt(indexNode.getNodeIndex())+1));
    saveFlows.setRecTime(dateTime);
    saveFlows.setTenantId(userInfoDto.getTenantId());

    approveFlowsDao.insert(saveFlows);

    return approveNext.getApproveId();
  }

  @Override
  public List<ApproveFlows> processQuery(int status,int approveTypeId, LocalDateTime approveTime,String approveTitle,String token) {
    UserInfoDto userInfoDto = authcenterClient.getToken(token).getData();
    List<String> processInstanceIdList = new ArrayList<>();
    if (status == 1 || status == 3) { // 待我审的,我已审的
      R r = apiProcessManageController.queryTasks(userInfoDto.getAccountId());
      List<Object> objects = (List<Object>) r.get("data");
      objects.forEach(object -> {
        JSONObject jsonObject = JSONObject.parseObject(object.toString());
        String processInstanceId = jsonObject.getString("processInstanceId");
        processInstanceIdList.add(processInstanceId);
      });
      if(status == 3){//我已审的
        R r1 = apiProcessManageController.queryHistoricTasks(userInfoDto.getAccountId(),"1","1000");
        List<Object> objects1 = (List<Object>) r1.get("data");
        objects1.forEach(object -> {
          JSONObject jsonObject = JSONObject.parseObject(object.toString());
          JSONObject historicProcessInstanceJsonObject = JSONObject.parseObject(jsonObject.getString("historicProcessInstance"));
          String processInstanceId = historicProcessInstanceJsonObject.getString("processInstanceId");
          processInstanceIdList.add(processInstanceId);
        });
      }
    } else if(status == 2 ){//我发起的
      QueryWrapper<Approve> queryWrapper = new QueryWrapper<>();
      queryWrapper.lambda().eq(Approve::getTenantId,userInfoDto.getTenantId()).
          eq(Approve::getApproveId,userInfoDto.getAccountId());
      approveDao.selectList(queryWrapper).forEach(af -> processInstanceIdList.add(af.getProcessInstanceId()));
    } else if(status ==4){ //抄送我的
      QueryWrapper<Approve> queryWrapper = new QueryWrapper<>();
      queryWrapper.lambda().eq(Approve::getTenantId,userInfoDto.getTenantId());
      List<Approve> approveList = approveDao.selectList(queryWrapper);
      approveList.forEach(m -> {
        QueryWrapper<ApproveFlows> approveFlowsQueryWrapper = new QueryWrapper<>();
        approveFlowsQueryWrapper.lambda().eq(ApproveFlows::getApproveId,m.getApproveId()).orderByDesc(ApproveFlows::getCurrentNodeIndex);
        ApproveFlows approveFlows = approveFlowsDao.selectOne(approveFlowsQueryWrapper);

        QueryWrapper<ApproveNode> approveNodeQueryWrapper = new QueryWrapper<>();
        approveNodeQueryWrapper.lambda().eq(ApproveNode::getModelId, m.getModelId()).
            like(ApproveNode::getCcUser,userInfoDto.getAccountId()).le(ApproveNode::getNodeIndex,approveFlows.getCurrentNodeIndex());
        List<ApproveNode> approveNodeList = approveNodeDao.selectList(approveNodeQueryWrapper);
        if(approveNodeList.size()>0){
          processInstanceIdList.add(m.getProcessInstanceId());
        }
      });
    }

    List<ApproveFlows> approveFlowsList = approveFlowsDao.selectBatchIds(processInstanceIdList);
    Iterator<ApproveFlows> flowsIterator = approveFlowsList.iterator();
    while (flowsIterator.hasNext()) {
      ApproveFlows flows = flowsIterator.next();
      approveFlowsList.forEach(af -> {
        String approveId = af.getApproveId();
        Approve approve = approveDao.selectById(approveId);
        String modelId = approve.getModelId();
        ApproveModel approveModel = approveModelDao.selectById(modelId);
        af.setApproveCode(approve.getApproveCode());
        af.setApproveTitle(approve.getApproveTitle());
        af.setBeginTime(approve.getRecTime());
        af.setApproveTypeId(approveModel.getApproveTypeId());

        if(((approveTypeId==1||approveTypeId==2)&&approveModel.getApproveTypeId()!=approveTypeId) ||
            (approveTime!=null && approve.getRecTime().isBefore(approveTime)) ||
            (approveTitle!=null && !approve.getApproveTitle().contains(approveTitle))){
          flowsIterator.remove();
          approveFlowsList.remove(flows);
        }
      });
    }

    return approveFlowsList;
  }

  @Override
  public JSONArray processNode(String approveId, String token) {
    Approve approve = approveDao.selectById(approveId);
    List<HistoricActivityInstance> datas = historyService.createHistoricActivityInstanceQuery()
        .processInstanceId(approve.getProcessInstanceId()).list();
    JSONArray jsonArray = new JSONArray();
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    datas.forEach(ha->{
      JSONObject jsonObject = new JSONObject();
      String applyUser = approve.getApplyUserName() ;
      //开始节点
      if(ha.getActivityType().equals("startEvent")){
        Date startTime = ha.getStartTime();
        //endTime为空，则该节点未完成
        Date endTime = ha.getEndTime();
        jsonObject.put("startTime",sdf.format(startTime));
        jsonObject.put("endTime",sdf.format(endTime));
        jsonObject.put("applyUser",applyUser);
        jsonObject.put("approveUser","");
        jsonObject.put("isOver",endTime==null?"false":"true");
        jsonObject.put("nodeType","start");
        jsonArray.add(jsonObject);
      }
      //查找审批节点
      if(ha.getActivityType().equals("userTask")){
        Date startTime = ha.getStartTime();
        Date endTime = ha.getEndTime();
        String approveUser = ha.getAssignee();
        jsonObject.put("startTime",sdf.format(startTime));
        jsonObject.put("endTime",sdf.format(endTime));
        jsonObject.put("applyUser",applyUser);
        jsonObject.put("approveUser",approveUser);
        jsonObject.put("isOver",endTime==null?"false":"true");
        jsonObject.put("nodeType","userTask");
        jsonArray.add(jsonObject);
      }
      //查找审批节点
      if(ha.getActivityType().equals("endEvent")){
        Date startTime = ha.getStartTime();
        Date endTime = ha.getEndTime();
        String approveUser = ha.getAssignee();
        jsonObject.put("startTime",sdf.format(startTime));
        jsonObject.put("endTime",sdf.format(endTime));
        jsonObject.put("applyUser",applyUser);
        jsonObject.put("approveUser",approveUser);
        jsonObject.put("isOver","true");
        jsonObject.put("nodeType","end");
        jsonArray.add(jsonObject);
      }
    });
    return jsonArray;
  }

  @Override
  public boolean operaModelByModelId(String modelId, String token) {
    UserInfoDto userInfoDto = authcenterClient.getToken(token).getData();
    ApproveModel approveModel = approveModelDao.selectById(modelId);
    if(approveModel.getStatus()==1){
      approveModel.setStatus(2);
    }else{
      approveModel.setStatus(1);
    }
    int result = approveModelDao.updateById(approveModel);

    return result>=1;
  }

  @Override
  public ApproveFlows getProcessFlowByApproveId(String approveId, String token) {
    UserInfoDto userInfoDto = authcenterClient.getToken(token).getData();
    QueryWrapper<ApproveFlows> approveFlowsQueryWrapper = new QueryWrapper<>();
    approveFlowsQueryWrapper.lambda().eq(ApproveFlows::getApproveId,approveId).eq(ApproveFlows::getTenantId,userInfoDto.getTenantId());

    List<ApproveFlows> approveFlowsList = approveFlowsDao.selectList(approveFlowsQueryWrapper).stream().sorted(Comparator.comparing(ApproveFlows::getRecTime).reversed()).collect(Collectors.toList());
    if(approveFlowsList.size()>0){
      ApproveFlows approveFlows = approveFlowsList.get(0);
      Approve approve = approveDao.selectById(approveId);
      String modelId = approve.getModelId();
      ApproveModel approveModel = approveModelDao.selectById(modelId);
      approveFlows.setApproveCode(approve.getApproveCode());
      approveFlows.setApproveTitle(approve.getApproveTitle());
      approveFlows.setBeginTime(approve.getRecTime());
      approveFlows.setApproveTypeId(approveModel.getApproveTypeId());

      return approveFlows;
    }
    return null;
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

  private String dynamicCreate(JSONObject paramJson,ApproveModel approveModel) {
    log.info("流程节点参数: {}", paramJson.toJSONString());
    String pName = paramJson.getString("name");
    String data = paramJson.getString("data");
    JSONArray jsonArray = JSONArray.parseArray(data);
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
    String processKey = "process-"+UUID.randomUUID().toString().replaceAll("-","");
    process.setId(processKey);
    process.setName(pName);

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

        String userId = jsonObject.getString("userId");
        String userName = jsonObject.getString("userName");

        if(index==1){
          process.addFlowElement(createSequenceFlow("start", "task"+1,"flow"+1,"",""));
        }
        //创建互斥网关
        process.addFlowElement(createExclusiveGateway("exclusivegateway"+index));
        //创建连线
        process.addFlowElement(createSequenceFlow("task"+index, "exclusivegateway"+index,"flow"+(index+1),"",""));

        JSONArray itemArray = JSONArray.parseArray(jsonObject.getString("items"));
        for(int j=0;j<itemArray.size();j++){
          JSONObject itemObject = JSONObject.parseObject(itemArray.getString(j));
          String value = itemObject.getString("value");
          String label = itemObject.getString("label");
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
      }

    }
    // 2. 生成BPMN自动布局
    new BpmnAutoLayout(model).execute();

    // 3. 部署这个BPMN模型
    Deployment deployment = repositoryService.createDeployment()
        .addBpmnModel(approveModel.getModelCode()+".bpmn", model).name(approveModel.getModelName()).deploy();

    return processKey;
  }
}
