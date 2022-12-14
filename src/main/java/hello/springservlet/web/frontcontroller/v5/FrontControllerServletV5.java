package hello.springservlet.web.frontcontroller.v5;

import hello.springservlet.web.frontcontroller.ModelView;
import hello.springservlet.web.frontcontroller.MyView;
import hello.springservlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.springservlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.springservlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.springservlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.springservlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.springservlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.springservlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import hello.springservlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdaptor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdaptors = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerAdapters() {

        handlerAdaptors.add(new ControllerV3HandlerAdapter());
        handlerAdaptors.add(new ControllerV4HandlerAdaptor());
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object handler = getHandler(request);
        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyHandlerAdapter adaptor = getHandlerAdaptor(handler);

        ModelView mv = adaptor.handle(request, response, handler);

        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);

        view.render(mv.getModel(), request, response);
    }

    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private MyHandlerAdapter getHandlerAdaptor(Object handler) {
        for (MyHandlerAdapter adaptor : handlerAdaptors) {
            if (adaptor.supports(handler)) {
                return adaptor;
            }
        }
        throw new IllegalArgumentException("handler adaptor??? ?????? ??? ????????????. handler = " + handler);
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }
}
