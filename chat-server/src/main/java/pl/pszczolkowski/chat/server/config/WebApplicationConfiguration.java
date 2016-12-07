package pl.pszczolkowski.chat.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import pl.pszczolkowski.chat.server.config.security.HessianBurlapAuthenticationInterceptor;

@Configuration
@EnableWebMvc
public class WebApplicationConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    HessianBurlapAuthenticationInterceptor hssianBurlapAuthenticationInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(hssianBurlapAuthenticationInterceptor);
    }

}

