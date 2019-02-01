package org.molgenis.core.ui.controller;

import static java.util.Objects.requireNonNull;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.molgenis.settings.AppSettings;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/context")
public class UiContextController {

  private final AppSettings appSettings;

  public UiContextController(AppSettings appSettings) {
    this.appSettings = requireNonNull(appSettings);
  }

  @ApiOperation(value = "Returns the ui context object", response = ResponseEntity.class)
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Returns object containing settings relevant for user interface ",
        response = ResponseEntity.class)
  })
  @GetMapping("/**")
  @ResponseBody
  public UiContextResponse getContext() {
    return UiContextResponse.builder()
        .setLogoNavBarHref(appSettings.getLogoNavBarHref())
        .setLogoTopHref(appSettings.getLogoTopHref())
        .setLogoTopMaxHeight(appSettings.getLogoTopMaxHeight())
        .build();
  }
}
