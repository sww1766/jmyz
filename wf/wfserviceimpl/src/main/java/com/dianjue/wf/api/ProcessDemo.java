package com.dianjue.wf.api;

public class ProcessDemo {

  /**
   * 申请人ID
   */
  private String applyUserId;
  /**
   * 下一步审批人
   */
  private String nextUserId;
  /**
   * 流程实例ID
   */
  private String processInstanceId;
  /**
   * 当前节点
   */
  private String activityId;

  /**
   * 流程标识key
   */
  private String processKey;

  /**
   * 是否是最后一个节点
   */
  private String isEndActivity;

  /**
   * 任务ID
   */
  private String taskId;

  /**
   * 任务名称
   */
  private String taskName;

  /**
   * 流程名称
   */
  private String processName;

  /**
   * 备注信息
   */
  private String description;

  /**
   * 抄送人
   */
  private String duplUserId;

  public String getApplyUserId() {
    return applyUserId;
  }

  public void setApplyUserId(String applyUserId) {
    this.applyUserId = applyUserId;
  }

  public String getNextUserId() {
    return nextUserId;
  }

  public void setNextUserId(String nextUserId) {
    this.nextUserId = nextUserId;
  }

  public String getProcessInstanceId() {
    return processInstanceId;
  }

  public void setProcessInstanceId(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }

  public String getActivityId() {
    return activityId;
  }

  public void setActivityId(String activityId) {
    this.activityId = activityId;
  }

  public String getIsEndActivity() {
    return isEndActivity;
  }

  public void setIsEndActivity(String isEndActivity) {
    this.isEndActivity = isEndActivity;
  }

  public String getProcessKey() {
    return processKey;
  }

  public void setProcessKey(String processKey) {
    this.processKey = processKey;
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public String getTaskName() {
    return taskName;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  public String getProcessName() {
    return processName;
  }

  public void setProcessName(String processName) {
    this.processName = processName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDuplUserId() {
    return duplUserId;
  }

  public void setDuplUserId(String duplUserId) {
    this.duplUserId = duplUserId;
  }
}
