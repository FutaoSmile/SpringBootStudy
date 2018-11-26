package com.futao.springmvcdemo.controller

import com.futao.springmvcdemo.model.entity.SingleValueResult
import com.futao.springmvcdemo.model.entity.User
import com.futao.springmvcdemo.model.system.MailM
import com.futao.springmvcdemo.model.system.Constant
import com.futao.springmvcdemo.service.KotlinTestService
import com.futao.springmvcdemo.service.MailService
import com.futao.springmvcdemo.service.impl.AccessLimitServiceImpl
import com.futao.springmvcdemo.service.impl.KotlinTestServiceImpl
import org.apache.rocketmq.client.producer.DefaultMQProducer
import org.apache.rocketmq.common.message.Message
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Conditional
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.thymeleaf.context.Context
import java.nio.charset.Charset
import javax.annotation.Resource

/**
 * @author futao
 * Created on 2018/10/17.
 */
@Conditional(KotlinTestServiceImpl::class)
@RestController
@RequestMapping(path = ["kotlinTest"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
open class KotlinTestController {

    @Resource
    private lateinit var redisTemplate: RedisTemplate<Any, Any>

    @Resource
    private lateinit var mailService: MailService

    /**
     * 存入缓存
     */
    @GetMapping(path = ["setCache"])
//    @RocketMQMessageListener
    open fun cache(
            @RequestParam("name") name: String,
            @RequestParam("age") age: Int
    ): User {
        val user = User().apply {
            username = name
            setAge(age.toString())
        }
        redisTemplate.opsForValue().set(name, user)
        return user
    }


    /**
     * 获取缓存
     */
    @GetMapping(path = ["getCache"])
    open fun getCache(
            @RequestParam("name") name: String
    ): User? {
        return if (redisTemplate.opsForValue().get(name) != null) {
            redisTemplate.opsForValue().get(name) as User
        } else null
    }

    @GetMapping(path = ["mailSend"])
    open fun mailSend() {
        mailService.sendSimpleEmail(arrayOf("taof@wicrenet.com", "1185172056@qq.com"), arrayOf("taof@wicrenet.com", "1185172056@qq.com"), "SpringbootMail", "from Springboot SimpleMail")
    }

    @GetMapping(path = ["htmlMailSend"])
    open fun htmlMailSend(
            @RequestParam("isHtml") isHtml: Boolean,
            @RequestParam("content") content: String
    ) {
        mailService.sendHtmlEmail(arrayOf("taof@wicrenet.com", "1185172056@qq.com"), arrayOf("taof@wicrenet.com", "1185172056@qq.com"), "SpringbootMail", content, isHtml)
    }

    @GetMapping(path = ["htmlTemplateHtml"])
    open fun htmlTemplateHtml(
            @RequestParam("templatePath") templatePath: String
    ) {
        mailService.sendHtmlEmailWithTemplate(
                arrayOf("taof@wicrenet.com", "1185172056@qq.com"),
                arrayOf("taof@wicrenet.com", "1185172056@qq.com"),
                "SpringbootMail",
                templatePath,
                Context().apply {
                    setVariable("username", "futao")
                }
        )
    }


    private val logger = LoggerFactory.getLogger(this::class.java)

    @Resource
    private lateinit var rocketMqService: DefaultMQProducer

    /**
     * mq
     */
    @GetMapping("producer")
    open fun producer(
            @RequestParam("message") message: String,
            @RequestParam("repeat") repeat: Int
    ) {
        for (i in (1..repeat)) {
            val message1 = Message(Constant.ROCKET_MQ_TOPIC_MAIL, Constant.ROCKET_MQ_TAG_MAIL_REGISTER, (message + i).toByteArray(Charset.forName(Constant.UTF8_ENCODE)))
            logger.info("开始发送消息：${message + i},::,$message1")
            val sendResult = rocketMqService.send(message1)
            logger.info("发送消息结果：$sendResult")
        }
    }


    @GetMapping("sendMailMq")
    open fun sendMailMq() {
        val mailM = MailM().apply {
            to = arrayOf("1185172056@qq.com")
            cc = arrayOf("taof@wicrenet.com")
            subject = "消息队列"
            content = "<h1>您好，RocketMq</h1>"
        }
        mailService.sendMq(mailM)
    }

    @Resource
    private lateinit var accessLimitServiceImpl: AccessLimitServiceImpl

    /**
     * 接口限流测试-令牌桶
     */
    @RequestMapping("rate")
    open fun rateLimit(): SingleValueResult {
        return if (!accessLimitServiceImpl.getPermit()) {
            SingleValueResult("限流")
        } else {
            SingleValueResult("正常业务逻辑")
        }
    }


    @Resource
    private lateinit var ktService: KotlinTestService

    @GetMapping(path = ["os"])
    open fun os(): SingleValueResult {
        return SingleValueResult(ktService.t())
    }

}