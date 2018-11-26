package com.futao.springmvcdemo.controller.business;

import com.alibaba.fastjson.JSONObject;
import com.futao.springmvcdemo.annotation.interceptor.impl.RequestLogInterceptor;
import com.futao.springmvcdemo.annotation.listener.OnlineHttpSessionListener;
import com.futao.springmvcdemo.model.entity.SingleValueResult;
import com.futao.springmvcdemo.model.system.ErrorMessageFields;
import com.futao.springmvcdemo.service.StatisticService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author futao
 * Created on 2018/10/11.
 * 统计
 */
@RestController
@RequestMapping(path = "statistic", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StatisticController {

    @Resource
    private OnlineHttpSessionListener onlineHttpSessionListener;

    @Resource
    private RequestLogInterceptor requestLogInterceptor;

    @Resource
    private StatisticService statisticService;

    /**
     * 获取当前在线人数
     *
     * @return
     */
    @GetMapping("onlinePeopleQuantity")
    public SingleValueResult onlinePeopleQuantity() {
        return new SingleValueResult(onlineHttpSessionListener.getOnlinePeopleQuantity().get());
    }
    /**
     * 获取接口的请求次数统计
     *
     * @return
     */
    @GetMapping("apiRequest")
    public ConcurrentHashMap<String, AtomicInteger> apiRequest() {
        return requestLogInterceptor.getApiRequestStatistic();
    }

    /**
     * 查看所有的错误码
     *
     * @return
     */
    @GetMapping("errorMessages")
    public List<ErrorMessageFields> errorMessages() {
        ArrayList<ErrorMessageFields> errorMessages = statisticService.getErrorMessages();
        System.out.println(JSONObject.toJSONString(errorMessages));
        return errorMessages;
    }

}
