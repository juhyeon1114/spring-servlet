package hello.springservlet.web.frontcontroller.v3;

import hello.springservlet.web.frontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {

    ModelView process(Map<String, String> paramMap);

}
