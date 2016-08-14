package main.java.com.hotelSystem.controller.filter;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.manager.managerImpl.daoManagerImpl.ConnectionAllocator;

import javax.servlet.*;
import java.io.IOException;

/**
 * This filter requests {@link java.sql.Connection Connection} allocation to {@link ConnectionAllocator}
 * before request processing and request it releasing after request processed.
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 * @see ConnectionAllocator
 */
public class ConnectionAllocationFilter implements Filter {

    /**
     * allocator, which will allocate and release connection for current
     * execution thread.
     */
    private static ConnectionAllocator connectionAllocator;

    /**
     * Gets allocator from {@link GlobalContext} and inits {@link #connectionAllocator} field.
     *
     * @param filterConfig {@inheritDoc}
     * @throws ServletException {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        connectionAllocator = (ConnectionAllocator) GlobalContext.getValue(GlobalContextConstant.CONNECTION_ALLOCATOR);
    }

    /**
     * Requests connection allocation to {@link ConnectionAllocator} instance before request processing.
     * Also after request processing connection requests connection releasing to allocator.
     *
     * @param request  {@inheritDoc}
     * @param response {@inheritDoc}
     * @param chain    {@inheritDoc}
     * @throws IOException      {@inheritDoc}
     * @throws ServletException {@inheritDoc}
     */
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
