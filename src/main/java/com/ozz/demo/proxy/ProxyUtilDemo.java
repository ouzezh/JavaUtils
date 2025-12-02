package com.ozz.demo.proxy;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.aop.aspects.Aspect;
import com.ozz.demo.proxy.model.MyCar;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class ProxyUtilDemo {
    public static void main(String[] args) {
        // Java
        Runnable runnable = ProxyUtil.newProxyInstance(new MyInvocationHandler<>(new MyCar()), Runnable.class);
        runnable.run();
        
        // Cglib
        MyCar myCar = ProxyUtil.proxy(new MyCar(), MyAspect.class);
        myCar.run();
    }

    public static class MyInvocationHandler<T> implements InvocationHandler {
        private T target;
        public MyInvocationHandler(T target) {
            super();
            this.target = target;
        }
        @SneakyThrows
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            log.info(String.format("invoke method %s...", method.getName()));
            return method.invoke(target, args);
        }
    }

    public static class MyAspect implements Aspect {
        @Override
        public boolean before(Object o, Method method, Object[] objects) {
            log.info("before");
            return true;
        }

        @Override
        public boolean after(Object o, Method method, Object[] objects, Object o1) {
            log.info("after");
            return true;
        }

        /**
         * 异常后执行（有BUG无效）
         */
        @Override
        public boolean afterException(Object o, Method method, Object[] objects, Throwable throwable) {
            log.info("afterException");
            return true;
        }
    }

}
