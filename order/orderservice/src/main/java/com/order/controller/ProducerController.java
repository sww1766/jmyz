package com.order.controller;

import com.order.service.ProducerService;
import com.order.utils.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author admin
 */
@Api(value = "rocketMq接口", tags = "rocketMq接口，不需要token")
@Slf4j
@RestController
@RequestMapping("mq")
public class ProducerController {

	@Resource
	private ProducerService producerService;

	@ApiOperation(value = "mq发消息", notes = "mq发消息")
	@PostMapping(value = "producer")
	public ResponseData producer(String string) {
		return producerService.producer(string);
	}

}
