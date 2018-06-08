package com.antifake.utils;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class SendSmsUtil {
	private static final Logger logger = LoggerFactory.getLogger(SendSmsUtil.class);
    /**
     * 生成验证码
     * @return
     */
    public static String getCaptcha() {
        String str = "0,1,2,3,4,5,6,7,8,9";
        String str2[] = str.split(",");// 将字符串以,分割
        Random rand = new Random();// 创建Random类的对象rand
        int index = 0;
        String randStr = "";// 创建内容为空字符串对象randStr
        randStr = "";// 清空字符串对象randStr中的值
        for (int i = 0; i < 4; ++i) {
            index = rand.nextInt(str2.length - 1);// 在0到str2.length-1生成一个伪随机数赋值给index
            randStr += str2[index];// 将对应索引的数组与randStr的变量值相连接
        }
        return randStr;
    }

    /**
     * 阿里短信的通用配置
     * @throws ClientException 
     */
    public static IAcsClient aliSmsConfig() {
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //替换成你的AK
        final String accessKeyId = "LTAIJTcSo2vbgJcq";//你的accessKeyId,
        final String accessKeySecret = "2blkCF4I4CtYxsEXyxli6aa7aL9m0U";//你的accessKeySecret，
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        return acsClient;
    }

    /**
     * 
     * @param templateCode      短信模板编号
     * @param telephone         手机号，可多个，以','隔开，最多1000
     * @param templateParam     变量内容
     * @return
     * @throws ServerException
     * @throws ClientException
     */
    public static String sendCodeSms(String telephone, String templateParam){
        IAcsClient acsClient = aliSmsConfig();
         //组装请求对象
         SendSmsRequest request = new SendSmsRequest();
         //使用post提交
         request.setMethod(MethodType.POST);
         //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
         request.setPhoneNumbers(telephone);
         //必填:短信签名-可在短信控制台中找到
         request.setSignName("妙联数字科技");
         //必填:短信模板-可在短信控制台中找到
         request.setTemplateCode("SMS_130921868");
         //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
         //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
         if(!StringUtils.isEmpty(templateParam)){
             request.setTemplateParam(templateParam);
         }
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ServerException e) {
            e.printStackTrace();
            return "fail";
        } catch (ClientException e) {
            e.printStackTrace();
            return "fail";
        }
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
            logger.info("短息发送成功！手机号：" + telephone);
            return "success";
        } else {
            logger.error("短信发送失败！手机号：" + telephone + "|返回错误码：" + sendSmsResponse.getCode());
            return "fail";
        }
    }
}
