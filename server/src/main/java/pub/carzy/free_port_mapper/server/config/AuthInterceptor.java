package pub.carzy.free_port_mapper.server.config;

import lombok.Data;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pub.carzy.free_port_mapper.server.exces.ApplicationException;
import pub.carzy.free_port_mapper.server.util.HttpThreadLocal;
import pub.carzy.free_port_mapper.server.util.JwtUtils;
import pub.carzy.free_port_mapper.common.util.ResponseCode;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 作用： 验证 用户是否登录
 *
 * @author admin
 */
@Data
public class AuthInterceptor implements HandlerInterceptor, WebMvcConfigurer {
    /**
     * 忽略的uri
     */
    private List<String> ignoreUri = new ArrayList<>();
    /**
     * 权限校验头
     */
    private String tokenHeader = "Authorization";
    /**
     * 权限校验前缀
     */
    private String tokenPrefix = "Bearer ";

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private Set<String> blacklist;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI().endsWith("/") ?
                request.getRequestURI().substring(0, request.getRequestURI().length() - 1)
                : request.getRequestURI();
        // Ant方式路径匹配 /**  ？  _
        PathMatcher matcher = new AntPathMatcher();
        for (String ignoredUrl : ignoreUri) {
            if (matcher.match(ignoredUrl, uri)) {
                return true;
            }
        }
        String token = captureToken(request);
        //2、未登录用户或黑名单token，直接拒绝访问
        if (token == null || !jwtUtils.verify(token) || !jwtUtils.isTokenExpired(token) || blacklist.contains(token)) {
            //拒绝未登录
            rejectNotLoggedRequest();
            return false;
        }
        //将token绑定在线程变量
        HttpThreadLocal.TOKEN.set(token);
        return true;
    }

    private void rejectNotLoggedRequest() {
        throw new ApplicationException(ResponseCode.NOT_LOGGED_IN);
    }

    /**
     * 截取token
     *
     * @param request 请求
     * @return token
     */
    protected String captureToken(HttpServletRequest request) {
        String header = request.getHeader(tokenHeader);
        if (header == null) {
            return null;
        }
        if (!header.startsWith(tokenPrefix)) {
            return null;
        }
        return header.replaceFirst(tokenPrefix, "").trim();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清除线程变量
        HttpThreadLocal.TOKEN.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //清除线程变量
        HttpThreadLocal.TOKEN.remove();
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).addPathPatterns("/**");
    }
}
