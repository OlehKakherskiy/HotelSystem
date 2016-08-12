package main.java.com.epam.project4.app.util;

import main.java.com.epam.project4.app.constants.MessageCode;
import main.java.com.epam.project4.exception.LocalizedRuntimeException;
import main.java.com.epam.project4.exception.SystemException;
import main.java.com.epam.project4.model.service.IService;
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
 * Proxy object, that wrapps original one and logs all method calls and
 * returned params
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class DebugLevelLoggerProxy implements InvocationHandler {

    private static final Logger debugLogger = Logger.getLogger(DebugLevelLoggerProxy.class);

    /**
     * original object, that is wrapped by logger
     */
    private Object proxiedObject;

    /**
     * instantiates new wrapper (proxy) for target object. Wrapper is implemented all
     * interfaces, that implements target object.
     *
     * @param objectUnderProxy object, that will be wrapped
     * @return target object's wrapper
     */
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


    /**
     * Log with {@link org.apache.log4j.Level#DEBUG} level all method calls
     * and returned parameters
     *
     * @param proxy  {@inheritDoc}
     * @param method {@inheritDoc}
     * @param args   {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        debugLogger.debug(MessageFormat.format("Called method {0} of class {1} with params {2}",
                method.getName(), proxiedObject.getClass().getName(), Arrays.toString(args)));
        Object result = null;
        try {
            result = method.invoke(proxiedObject, args);
            if (result != null) {
                debugLogger.debug(MessageFormat.format("Method {0} result {1}", method.getName(), result));
            } else {
                debugLogger.debug(MessageFormat.format("Method {0} finished executing", method.getName()));
            }
            return result;
        } catch (IllegalAccessException e) {
            throw new SystemException(MessageCode.GENERAL_SYSTEM_EXCEPTION, e);
        } catch (InvocationTargetException e1) {
            if (IService.class.isAssignableFrom(proxiedObject.getClass())
                    && LocalizedRuntimeException.class.isAssignableFrom(e1.getTargetException().getClass())) {
                throw new SystemException(MessageCode.GENERAL_SYSTEM_EXCEPTION, e1);
            } else {
                throw e1.getTargetException();
            }
        }
    }

    /**
     * gets recursively all interfaces of target class.
     *
     * @param clazz class, which interfaces and parents interfaces will be returned
     * @return list of interfaces of target class and all it's parent classes till parent isn't {@link Object}
     */
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
