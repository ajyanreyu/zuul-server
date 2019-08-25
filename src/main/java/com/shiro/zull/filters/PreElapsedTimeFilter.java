package com.shiro.zull.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PreElapsedTimeFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(PreElapsedTimeFilter.class);

    /**
     * Define filter type. Must return a text string according to the filter pre, post, route
     *
     * @return String
     * @author Albano Yanes <ajyanreyu@tgmail.com>
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * Determines the order of execution of the filter
     *
     * @return int
     * @author Albano Yanes <ajyanreyu@tgmail.com>
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * Determines whether the filter is executed
     *
     * @return boolean
     * @author Albano Yanes <ajyanreyu@tgmail.com>
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * Calculate the response time
     *
     * @return
     * @throws ZuulException
     * @author Albano Yanes <ajyanreyu@tgmail.com>
     */
    @Override
    public Object run() throws ZuulException {

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        log.info(String.format("%s routed request to %s", request.getMethod(), request.getRequestURL().toString()));
        Long initTime = System.currentTimeMillis();
        request.setAttribute("initTime", initTime);
        return null;
    }
}
