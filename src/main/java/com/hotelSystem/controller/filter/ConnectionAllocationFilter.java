package main.java.com.hotelSystem.controller.filter;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.manager.managerImpl.daoManagerImpl.ConnectionAllocator;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class ConnectionAllocationFilter implements Filter {

    private static ConnectionAllocator connectionAllocator;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        connectionAllocator = (ConnectionAllocator) GlobalContext.getValue(GlobalContextConstant.CONNECTION_ALLOCATOR);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        connectionAllocator.allocateConnection();
        chain.doFilter(request, response);
        connectionAllocator.closeConnection();
    }

    @Override
    public void destroy() {

    }
}
