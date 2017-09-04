package io.monitorjbl.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/", consumes = ALL_VALUE)
public class ApiInfoController {
  private static final List<Pattern> IGNORED_ENDPOINTS = asList(
      Pattern.compile("/swagger.*"),
      Pattern.compile("/error"));

  private final RequestMappingHandlerMapping handlerMapping;
  private final Map<String, Object> info = new HashMap<String, Object>() {{
    put("version", "1.0");
  }};

  @Autowired
  public ApiInfoController(RequestMappingHandlerMapping handlerMapping) {
    this.handlerMapping = handlerMapping;
  }

  @PostConstruct
  public void loadEndpoints() {
    info.put("links", handlerMapping.getHandlerMethods().keySet().stream()
        .flatMap(r -> r.getPatternsCondition().getPatterns().stream())
        .filter(p -> IGNORED_ENDPOINTS.stream().noneMatch(regex -> regex.matcher(p).matches()))
        .collect(toSet()).stream()
        .sorted()
        .collect(toList()));
  }

  @RequestMapping(method = GET)
  public Map<String, Object> info() {
    return info;
  }
}
