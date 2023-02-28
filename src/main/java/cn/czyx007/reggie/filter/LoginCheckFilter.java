package cn.czyx007.reggie.filter;

import cn.czyx007.reggie.common.BaseContext;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否完成登录
 * @author : 张宇轩
 * @createTime : 2022/12/20 - 17:38
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final String[] urls = {"/employee/login", "/employee/logout", "/backend/**", "/front/**",
            "/user/sendMsg", "/user/login", "/doc.html", "/webjars/**", "/swagger-resources", "/v2/api-docs"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        boolean check = false;
        for (String url : urls) {
            if(PATH_MATCHER.match(url, requestURI)){
                check = true;
                break;
            }
        }

        if (check) {
            chain.doFilter(request, response);
            return;
        }

        if (request.getSession().getAttribute("employee") != null) {
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            chain.doFilter(request, response);
            return;
        }
        if (request.getSession().getAttribute("user") != null) {
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            chain.doFilter(request, response);
            return;
        }

//        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("未登录！请先跳转以下页面登录<br>\n" +
                "<a href=\"http://reggie.czyx007.cn/backend/page/login/login.html\">后台页面跳转</a><br>\n" +
                "<a href=\"http://reggie.czyx007.cn/front/page/login.html\">用户页面跳转</a>");
    }
}
