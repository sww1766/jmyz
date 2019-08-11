package com.dianjue.wf.service;

import com.alibaba.fastjson.JSONArray;
import com.dianjue.wf.entity.ApproveFlows;
import com.dianjue.wf.entity.ApproveFlowsRequest;
import com.dianjue.wf.entity.ApproveModel;
import com.dianjue.wf.entity.ApproveModelRequest;
import com.dianjue.wf.entity.ApproveNext;
import java.time.LocalDateTime;
import java.util.List;

public interface ApproveService {

  List<ApproveModel> getModelListByTypeId(int typeId,String token);

  void updateModel(ApproveModelRequest approveModel,String token);

  /**
   * 开启流程
   * @param approveFlowsRequest
   * @param token
   * @return
   */
  String processStart(ApproveFlowsRequest approveFlowsRequest, String token);

  /**
   * 下一步流程
   * @param approveNext 审批流水表ID
   * @param token
   * @return
   */
  String processNext(ApproveNext approveNext, String token);

  /**
   * 查询流程
   * @param status 查询状态，1：待我审的，2：我发起的，3：我已审的，4：抄送我的
   * @param token
   * @return
   */
  List<ApproveFlows> processQuery(int status,int approveTypeId, LocalDateTime approveTime,String approveTitle, String token);


  /**
   * 查询流程
   * @param approveFlowId 审批流id
   * @param token
   * @return
   */
  JSONArray processNode(String approveFlowId, String token);

  /**
   * 根据模板id开启或禁用模板
   * @param modelId 模板ID
   * @param token
   * @return
   */
  boolean operaModelByModelId(String modelId, String token);

  ApproveFlows getProcessFlowByApproveId(String approveId, String token);
}
