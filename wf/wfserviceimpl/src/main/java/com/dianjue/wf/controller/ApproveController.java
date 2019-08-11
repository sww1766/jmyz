package com.dianjue.wf.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dianjue.commonutils.data.TokenUtils;
import com.dianjue.commonutils.enumutils.SysResponseEnum;
import com.dianjue.commonutils.exception.BizException;
import com.dianjue.commonutils.responseinfo.ResponseData;
import com.dianjue.wf.entity.ApproveFlowsRequest;
import com.dianjue.wf.entity.ApproveModelRequest;
import com.dianjue.wf.entity.ApproveNext;
import com.dianjue.wf.service.ApproveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import java.util.logging.Level;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "审批", tags = "审批")
@Log4j2
@RestController
@RequestMapping("/approve")
public class ApproveController {

  @Resource
  ApproveService approveService;

  @ApiOperation(value = "查询审批类型列表", notes = "查询审批类型列表")
  @GetMapping("/getTypeList")
  ResponseData getTypeList() {
    JSONObject jo1 = new JSONObject();
    jo1.put("id",0);
    jo1.put("name","请选择");
    JSONObject jo2 = new JSONObject();
    jo2.put("id",1);
    jo2.put("name","特殊费");
    JSONObject jo3 = new JSONObject();
    jo3.put("id",2);
    jo3.put("name","赔付");
    JSONArray jsonArray = new JSONArray();
    jsonArray.add(jo1);
    jsonArray.add(jo2);
    jsonArray.add(jo3);
    return new ResponseData<>(JSONObject.toJSON(jsonArray));
  }

  @ApiOperation(value = "根据类型ID查询最新审批模型列表", notes = "根据类型ID查询审批模型列表")
  @GetMapping("/getModelListByTypeId")
  ResponseData getModelListByTypeId(@RequestParam(value = "approveTypeId") int approveTypeId, HttpServletRequest request) {
    String token = TokenUtils.getRequestToken(request);
    if (StringUtils.isBlank(token)) {
      throw new BizException(SysResponseEnum.FAILED.getCode(), "获取用户token失败", null, Level.WARNING);
    }
    log.info("获取当前操作人信息开始: authorization:{}", token);
    return new ResponseData<>(approveService.getModelListByTypeId(approveTypeId, token));
  }

  @ApiOperation(value = "更新审批模型", notes = "更新审批模型")
  @PostMapping("/updateModel")
  ResponseData updateModel(@RequestBody ApproveModelRequest approveModel, HttpServletRequest request) {
    String token = TokenUtils.getRequestToken(request);
    if (StringUtils.isBlank(token)) {
      throw new BizException(SysResponseEnum.FAILED.getCode(), "获取用户token失败", null, Level.WARNING);
    }
    log.info("获取当前操作人信息开始: authorization:{}", token);
    approveService.updateModel(approveModel,token);
    return new ResponseData<>();
  }

  @ApiOperation(value = "开启流程审批", notes = "开启流程审批")
  @PostMapping("/processStart")
  ResponseData processStart(@RequestBody ApproveFlowsRequest approveFlowsRequest, HttpServletRequest request) {
    String token = TokenUtils.getRequestToken(request);
    if (StringUtils.isBlank(token)) {
      throw new BizException(SysResponseEnum.FAILED.getCode(), "获取用户token失败", null, Level.WARNING);
    }
    log.info("获取当前操作人信息开始: authorization:{}", token);
    return new ResponseData<>(approveService.processStart(approveFlowsRequest,token));
  }

  @ApiOperation(value = "下一步", notes = "下一步")
  @PostMapping("/processNext")
  ResponseData processNext(@RequestBody ApproveNext approveNext, HttpServletRequest request) {
    String token = TokenUtils.getRequestToken(request);
    if (StringUtils.isBlank(token)) {
      throw new BizException(SysResponseEnum.FAILED.getCode(), "获取用户token失败", null, Level.WARNING);
    }
    log.info("获取当前操作人信息开始: authorization:{}", token);
    return new ResponseData<>(approveService.processNext(approveNext,token));
  }

  @ApiOperation(value = "查询", notes = "查询")
  @ApiImplicitParams({
      @ApiImplicitParam(
          name = "status",
          value = "查询状态，1：待我审的，2：我发起的，3：我已审的，4：抄送我的",
          example = "1",
          dataType = "int"),
      @ApiImplicitParam(
          name = "approveTypeId",
          value = "类型，0：全部，1：特殊费，2：赔付",
          example = "1",
          dataType = "int"),
  })
  @PostMapping("/processQuery")
  ResponseData processQuery(
      @RequestParam(value = "status",defaultValue = "1") int status,
      @RequestParam(value = "approveTypeId",defaultValue = "0") int approveTypeId,
      @RequestParam(value = "approveTime",required = false) LocalDateTime approveTime,
      @RequestParam(value = "approveTitle",required = false) String approveTitle, HttpServletRequest request) {
    String token = TokenUtils.getRequestToken(request);
    if (StringUtils.isBlank(token)) {
      throw new BizException(SysResponseEnum.FAILED.getCode(), "获取用户token失败", null, Level.WARNING);
    }
    log.info("获取当前操作人信息开始: authorization:{}", token);
    return new ResponseData<>(approveService.processQuery(status,approveTypeId,approveTime,approveTitle,token));
  }

  @ApiOperation(value = "流程节点", notes = "流程节点")
  @PostMapping("/processNode")
  ResponseData processNode(@RequestParam(value = "approveId") String approveId, HttpServletRequest request) {
    String token = TokenUtils.getRequestToken(request);
    if (StringUtils.isBlank(token)) {
      throw new BizException(SysResponseEnum.FAILED.getCode(), "获取用户token失败", null, Level.WARNING);
    }
    log.info("获取当前操作人信息开始: authorization:{}", token);
    return new ResponseData<>(JSON.toJSON(approveService.processNode(approveId,token)));
  }

  @ApiOperation(value = "禁用或启用模板", notes = "禁用或启用模板")
  @GetMapping("/operaModelByModelId")
  ResponseData operaModelByModelId(@RequestParam("modelId") String modelId, HttpServletRequest request) {
    String token = TokenUtils.getRequestToken(request);
    if (StringUtils.isBlank(token)) {
      throw new BizException(SysResponseEnum.FAILED.getCode(), "获取用户token失败", null, Level.WARNING);
    }
    log.info("获取当前操作人信息开始: authorization:{}", token);
    return new ResponseData<>(approveService.operaModelByModelId(modelId,token));
  }

  @ApiOperation(value = "根据审批id查询最新1条流水", notes = "根据审批id查询最新1条流水")
  @GetMapping("/getProcessFlowByApproveId")
  ResponseData getProcessFlowByApproveId(@RequestParam("approveId") String approveId, HttpServletRequest request) {
    String token = TokenUtils.getRequestToken(request);
    if (StringUtils.isBlank(token)) {
      throw new BizException(SysResponseEnum.FAILED.getCode(), "获取用户token失败", null, Level.WARNING);
    }
    log.info("获取当前操作人信息开始: authorization:{}", token);
    return new ResponseData<>(approveService.getProcessFlowByApproveId(approveId,token));
  }


}
