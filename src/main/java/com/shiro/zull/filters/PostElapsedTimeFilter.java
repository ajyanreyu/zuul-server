package com.shiro.zull.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PostElapsedTimeFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(PostElapsedTimeFilter.class);

    /**
     * Define filter type. Must return a text string according to the filter pre, post, route
     *
     * @return String
     * @author Albano Yanes <ajyanreyu@tgmail.com>
     */
    @Override
    public String filterType() {
        return "post";
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
     * @throws ZuulException
     * @author Albano Yanes <ajyanreyu@tgmail.com>
     */
    @Override
    public Object run() throws ZuulException {

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        log.info("post login");
        Long initTime = (Long) request.getAttribute("initTime");
        Long endTime = System.currentTimeMillis();
        Long elapsedTime = endTime - initTime ;
        log.info(String.format("Elapsed time in seconds %s ", elapsedTime.doubleValue() / 1000));
        return null;
    }
}
