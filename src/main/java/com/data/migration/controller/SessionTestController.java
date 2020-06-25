package com.data.migration.controller;

import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin/v1")
public class SessionTestController {
//    @RequestMapping(value = "/first", method = RequestMethod.GET)
//    public Map<String, Object> firstResp (HttpServletRequest request){
//        Map<String, Object> map = new HashMap<>();
//        request.getSession().setAttribute("url", request.getRequestURL());
//        map.put("request Url", request.getRequestURL());
//        return map;
//    }
//    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
//    public Map<String, Object> sessions (HttpServletRequest request){
//        Map<String, Object> map = new HashMap<>();
//        map.put("sessionId", request.getSession().getId());
//        map.put("message", request.getSession().getAttribute("url"));
//        return map;
//    }

}
