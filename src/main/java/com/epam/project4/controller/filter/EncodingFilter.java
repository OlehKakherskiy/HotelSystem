package main.java.com.epam.project4.controller.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filter changes request character's encoding to encoding, specified in web.xml or UTF-8
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class EncodingFilter implements Filter {

    /**
     * encoding, which default is UTF-8, if other is not defined in web.xml
     */
    private String encoding;


    /**
     * inits {@link #encoding} via filter's config. If there's no param
     * with name "encoding", sets encoding to UTF-8
     *
     * @param filterConfig {@inheritDoc}
     * @throws ServletException {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String initEncoding = filterConfig.getInitParameter("encoding");
        encoding = (initEncoding == null) ? "UTF-8" : initEncoding;
    }

    /**
     * sets request's character encoding to {@link #encoding}
     *
     * @param request  {@inheritDoc}
     * @param response {@inheritDoc}
     * @param chain    {@inheritDoc}
     * @throws IOException      {@inheritDoc}
     * @throws ServletException {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
