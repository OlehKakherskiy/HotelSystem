package main.java.com.epam.project4.app.util;

import main.java.com.epam.project4.app.constants.MessageCode;
import main.java.com.epam.project4.exception.LocalizedRuntimeException;
import main.java.com.epam.project4.exception.SystemException;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class DebugLevelLoggerProxy implements InvocationHandler {

    private static final Logger debugLogger = Logger.getLogger(DebugLevelLoggerProxy.class);

    private Object proxiedObject;

    public static Object newInstance(Object objectUnderProxy) {
        List<Class> interfaceList = getAllInterfaces(objectUnderProxy.getClass());
        Class<?>[] interfaces = new Class[interfaceList.size()];
        interfaceList.toArray(interfaces);
        System.out.println("interfaces = " + Arrays.toString(interfaces));
        return Proxy.newProxyInstance(objectUnderProxy.getClass().getClassLoader(),
                interfaces, new DebugLevelLoggerProxy(objectUnderProxy));
    }

    private DebugLevelLoggerProxy(Object proxiedObject) {
        this.proxiedObject = proxiedObject;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        debugLogger.debug(MessageFormat.format("Called method {0} of class {1} with params {2}",
                method.getName(), proxiedObject.getClass().getName(), Arrays.toString(args)));
        Object result = null;
        try {
            result = method.invoke(proxiedObject, args);
            if (result != null) {
                debugLogger.debug(MessageFormat.format("Method {0} result {1}", method.getName(), result));
            }
            return result;
        } catch (IllegalAccessException e) {
//            debugLogger.error(MessageFormat.format("Error caused during invoking method {0} of class {1}",
//                    method.getName(), proxiedObject.getClass().getName()), e);
            throw new SystemException(MessageCode.GENERAL_SYSTEM_EXCEPTION, e);
        } catch (InvocationTargetException e1) {
//            debugLogger.error(MessageFormat.format("Error caused during invoking method {0} of class {1}",
//                    method.getName(), proxiedObject.getClass().getName()), e1);
            throw (LocalizedRuntimeException) e1.getTargetException();
        }
    }

    private static List<Class> getAllInterfaces(Class clazz) {
        if (clazz == Object.class) {
            return new ArrayList<>();
        }
        List<Class> list = getAllInterfaces(clazz.getSuperclass());
        list.addAll(Arrays.asList(clazz.getInterfaces()));
        System.out.println("implementedInterfaces = " + Arrays.toString(list.toArray()));
        return list;
    }
}
