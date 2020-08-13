package io.openapitools.swagger.config;

import java.util.Map;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;

public class SwaggerFlows {

    /**
     * Configuration for the OAuth Implicit flow
     */
    @Parameter
    private Entry implicit;

    /**
     * Configuration for the OAuth Resource Owner Password flow
     */
    @Parameter
    private Entry password;

    /**
     * Configuration for the OAuth Client Credentials flow. Previously called
     * application in OpenAPI 2.0.
     */
    @Parameter
    private Entry clientCredentials;

    /**
     * Configuration for the OAuth Authorization Code flow. Previously called
     * accessCode in OpenAPI 2.0.
     */
    @Parameter
    private Entry authorizationCode;

    @Parameter
    private Map<String, Object> extensions;

    public OAuthFlows createOAuthFlowsModel() {
        OAuthFlows flows = new OAuthFlows();
        flows.setImplicit(implicit.toOAuthFlowModel());
        flows.setPassword(password.toOAuthFlowModel());
        flows.setClientCredentials(clientCredentials.toOAuthFlowModel());
        flows.setAuthorizationCode(authorizationCode.toOAuthFlowModel());

        if (extensions != null && !extensions.isEmpty()) {
            flows.setExtensions(extensions);
        }

        return flows;
    }

    public static class Entry {
        /**
         * For implicit/authorizationCode flows: REQUIRED. The authorization URL to be used for this flow. This MUST be in the form of a URL.
         */
        @Parameter
        private String authorizationUrl;

        /**
         * For password/clientCredentials/AuthorizationCode flows: REQUIRED. The token URL to be used for this flow. This MUST be in the form of a URL.
         */
        @Parameter
        private String tokenUrl;

        /**
         * The URL to be used for obtaining refresh tokens. This MUST be in the form of a URL.
         */
        @Parameter
        private String refreshUrl;

        /**
         * REQUIRED. The available scopes for the OAuth2 security scheme. A map between the scope name and a short description for it.
         */
        @Parameter(required = true)
        private Map<String, String> scopes;

        @Parameter
        private Map<String, Object> extensions;

        public OAuthFlow toOAuthFlowModel() {
            OAuthFlow flow = new OAuthFlow();

            flow.setAuthorizationUrl(authorizationUrl);
            flow.setTokenUrl(tokenUrl);
            flow.setRefreshUrl(refreshUrl);
            if (scopes != null){
                Scopes ss = new Scopes();
                scopes.entrySet().forEach(s -> ss.addString(s.getKey(), s.getValue()));
                flow.setScopes(ss);
            }

            if (extensions != null && !extensions.isEmpty()) {
                flow.setExtensions(extensions);
            }
            return flow;
        }
    }
}
