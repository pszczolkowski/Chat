package pl.pszczolkowski.chat.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import pl.pszczolkowski.chat.server.annotation.Api;

@Service
public class ApisContainerImpl implements ApisContainer {

    private ApplicationContext applicationContext;

    private List<Object> apis;

    @Autowired
    public ApisContainerImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public List<Object> getAll() {
        if (apis == null) {
            apis = new ArrayList<>(applicationContext.getBeansWithAnnotation(Api.class).values());
        }

        return Collections.unmodifiableList(apis);
    }

}
