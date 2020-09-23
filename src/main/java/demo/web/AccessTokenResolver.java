package demo.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AccessTokenResolver {

    private final OAuth2AuthorizedClientService clientService;

    public String getToken(OAuth2AuthenticationToken oauth2AuthToken) {

        String authorizedClientRegistrationId = oauth2AuthToken.getAuthorizedClientRegistrationId();
        String name = oauth2AuthToken.getName();
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(authorizedClientRegistrationId, name);

        if (client == null) {
            return null;
        }

        return client.getAccessToken().getTokenValue();
    }
}
