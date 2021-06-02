////package com.example.mywiki.filter;
////
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
////import org.springframework.stereotype.Component;
////
////import javax.servlet.*;
////import javax.servlet.http.HttpServletRequest;
////import java.io.IOException;
////import java.util.logging.Filter;
////import java.util.logging.LogRecord;
////
////@Component
////public class LogFilter implements Filter {
////    private static final Logger LOG = LoggerFactory.getLogger(LogFilter.class);
////
////
////    public void init(FilterConfig filterConfig) throws ServletException {
////
////    }
////
////    public void doFilter(ServletRequest servletRequest,ServletResponse servletResponse,FilterChain filterChain)throws IOException, ServletException {
////        //打印请求信息
////        HttpServletRequest request = (HttpServletRequest) servletRequest;
////        LOG.info("---------LogFilter开始--------");
////        LOG.info("请求地址:{} {} ",request.getRequestURI().toString(),request.getMethod());
////        LOG.info("远程地址：{}", request.getRemoteAddr());
////
////        long starTime = System.currentTimeMillis();
////        filterChain.doFilter(servletRequest,servletResponse);
////        LOG.info("---------LogFilter结束 耗时：{} ms ---------",System.currentTimeMillis() - starTime);
////    }
////
////    @Override
////    public boolean isLoggable(LogRecord record) {
////        return true;
////    }
////}
//
// package com.example.mywiki.filter;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.stereotype.Component;
//
// import javax.servlet.*;
// import javax.servlet.http.HttpServletRequest;
// import java.io.IOException;
//
// //通过springboot过滤器对所进行的程序通过日志进行监控及运行时间的检查
// @Component
// public class LogFilter implements Filter {
//
//     private static final Logger LOG = LoggerFactory.getLogger(LogFilter.class);
//
//     @Override
//     public void init(FilterConfig filterConfig) throws ServletException {
//
//     }
//
//     @Override
//     public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//         // 打印请求信息
//         HttpServletRequest request = (HttpServletRequest) servletRequest;
//         LOG.info("------------- LogFilter 开始 -------------");
//         LOG.info("请求地址: {} {}", request.getRequestURL().toString(), request.getMethod());
//         LOG.info("远程地址: {}", request.getRemoteAddr());
//
//         long startTime = System.currentTimeMillis();
//         filterChain.doFilter(servletRequest, servletResponse);
//         LOG.info("------------- LogFilter 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
//     }
// }