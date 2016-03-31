package aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

/**
 * 该类用于拦截api层日志的，使用请谨慎，会影响性能
 * 使用方法：表达式里配置你需要拦截的类和方法
 * =========================================================
 *    <bean id="apiServiceLogAop" class="aop.ApiServiceLogAop"></bean>
 *
 *   <aop:config>
 *   <aop:aspect ref="apiServiceLogAop">
 *   <aop:around method="doMonitor" pointcut="execution(* com.trade.order.api.*.*ServiceImpl*.*(..))"/>
 *   </aop:aspect>
 *   </aop:config>
 * =========================================================
 */
@Slf4j
public class ApiServiceLogAop {
    public Object doMonitor(ProceedingJoinPoint pjp) {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();

        Object[] params = pjp.getArgs();
        int length = 0;
        if (params != null) {
            length = params.length;
        }

        StringBuffer paramStr = new StringBuffer(" ");
        for (int i = 0; i < length; i++) { // 组装打印参数
            paramStr.append(JSON.toJSONString(params[i])).append("|");
        }

        String resultStr = null;
        Long start = System.currentTimeMillis();
        long end = 0L;
        try {
            Object result = pjp.proceed();
            end = System.currentTimeMillis();
            resultStr = JSON.toJSONString(result);
            return result;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (end == 0) {
                end = System.currentTimeMillis();
            }
            log.info("invoke service=" + className + "." + methodName
                    + ",params=[" + paramStr.substring(0, paramStr.length() - 1) + "],result=[" + resultStr + "]"
                    + ",use time=" + (end - start) + "ms");
        }
    }
}
