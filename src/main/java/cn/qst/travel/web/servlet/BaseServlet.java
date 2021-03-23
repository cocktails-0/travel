package cn.qst.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;


public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //System.out.println("baseServlet的service方法被执行了...");

        //完成方法分发
        //1.获取请求路径
        String uri = req.getRequestURI(); //   /travel/user/add
        //System.out.println("请求uri:"+uri);//  /travel/user/add
        //2.获取方法名称
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
        //System.out.println("方法名称："+methodName);
        //3.获取方法对象Method
        //谁调用我？我代表谁
        //System.out.println(this);//UserServlet的对象cn.qst.travel.web.servlet.UserServlet@4903d97e
        try {
            //获取方法
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //4.执行方法
            //暴力反射
            //method.setAccessible(true);
            method.invoke(this,req,resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    /**
     * 直接将传入的对象序列化为json，并且写回客户端
     * @param obj
     */
    public void writeValue(Object obj,HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),obj);
    }

    /**
     * 将传入的对象序列化为json，返回
     * @param obj
     * @return
     */
    public String writeValueAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }


    /**
     * 字符串数字类型转换方法
     *      将数字字符串转换为数字，例如："5" -> 5
     * @param numStr 数字字符串
     * @return 整型数字
     * @throws ClassCastException
     *      类型转换异常，避免传入的字符串内容不是数字
     */
    protected int parseInt(String numStr, int defaultValue) throws ClassCastException {
        return (numStr != null && numStr.length() > 0 && !"null".equalsIgnoreCase(numStr))
                ? Integer.parseInt(numStr) : defaultValue;      // 注意：浏览器提交的空值到后台会被识别为<字符串"null">
    }

    //10=======================>
    /**
     * 非数字字符串参数处理方法
     * @param str 字符串对象
     * @return 处理后的字符串对象
     */
    protected String parseStr(String str) {
        /*tomcat7存在中文乱码问题*/
        if (str != null && str.length() > 0 && !"null".equalsIgnoreCase(str)) {
            str = new String(str.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            return str;
        }
        return null;
    }

}
