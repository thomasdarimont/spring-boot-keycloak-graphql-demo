package demo.web;

import io.leangen.graphql.spqr.spring.autoconfigure.SpqrProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * Replaces {@link io.leangen.graphql.spqr.spring.web.GuiController}
 */
@Controller
class GuiController {

    private final String guiPath;

    private final SpqrProperties spqrProperties;

    private final AccessTokenResolver accessTokenResolver;

    public GuiController(@Value("${graphql.spqr.gui.endpoint:/gui}") String guiPath, SpqrProperties spqrProperties, AccessTokenResolver tokenResolver) {
        this.guiPath = guiPath;
        this.spqrProperties = spqrProperties;
        this.accessTokenResolver = tokenResolver;
    }

    @RequestMapping(
            value = {"${graphql.spqr.gui.endpoint:/gui}"},
            produces = {"text/html; charset=utf-8"}
    )
    String gui(@AuthenticationPrincipal Authentication authentication, Model model) throws IOException {

        SpqrProperties.Gui gui = this.spqrProperties.getGui();

        String accessToken = accessTokenResolver.getToken((OAuth2AuthenticationToken) authentication);

        model.addAttribute("pageTitle", gui.getPageTitle());
        model.addAttribute("graphQLEndpoint", gui.getTargetEndpoint());
        model.addAttribute("webSocketEndpoint", gui.getTargetWsEndpoint());
        model.addAttribute("accessToken", accessToken);

        return "playground-jwt.html";
    }

    @GetMapping("/")
    String showGui() {
        return "redirect:" + guiPath;
    }
}
