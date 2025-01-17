package thuong.todolist.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import thuong.todolist.utils.JwtHelper;

import java.io.IOException;
import java.util.Collections;

@Component
public class CustomSecurityFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;

    // Constructor injection để inject JwtHelper
    public CustomSecurityFilter(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Bỏ "Bearer " để lấy token thực

            if (jwtHelper.decryptToken(token)) {
                // Token hợp lệ
                String email = jwtHelper.decryptEmail(token); // Giải mã email từ token

                // Thiết lập SecurityContext
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email, null, Collections.emptyList()
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // Token không hợp lệ hoặc hết hạn
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Invalid or expired token");
                return;
            }
        }

        // Tiếp tục xử lý request
        filterChain.doFilter(request, response);
    }
}