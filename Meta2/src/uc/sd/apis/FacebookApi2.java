package uc.sd.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.extractors.AccessTokenExtractor;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class FacebookApi2 extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://www.facebook.com/v2.2/dialog/oauth?client_id=%s&redirect_uri=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://graph.facebook.com/v2.2/oauth/access_token";
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new AccessTokenExtractor() {

            @Override
            public Token extract(String response) {

                Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");
                try {

                    JSONObject json = (JSONObject) JSONValue.parse(response);
                    String token = (String) json.get("access_token");

                    return new Token(token, "", response);
                } catch (Exception e) {
                    throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
                }
            }
        };
    }

    @Override
    public String getAuthorizationUrl(final OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Must provide a valid url as callback. Facebook does not support OOB");
        final StringBuilder sb = new StringBuilder(String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(
                config.getCallback())));
        if (config.hasScope()) {
            sb.append('&').append(OAuthConstants.SCOPE).append('=').append(OAuthEncoder.encode(config.getScope()));
        }

        final String state = config.getState();
        if (state != null) {
            sb.append('&').append(OAuthConstants.STATE).append('=').append(OAuthEncoder.encode(state));
        }
        return sb.toString();
    }

    public String getShareUrl(final OAuthConfig config, String url, String text) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Must provide a valid url as callback. Facebook does not support OOB");
        StringBuilder sb;
        if(text==null) {
            sb = new StringBuilder(String.format("https://www.facebook.com/dialog/share?app_id=%s&display=%s&href=%s&redirect_uri=%s", config.getApiKey(), "page", OAuthEncoder.encode(url), OAuthEncoder.encode(config.getCallback())));
        } else {
            sb = new StringBuilder(String.format("https://www.facebook.com/dialog/share?app_id=%s&display=%s&href=%s&redirect_uri=%s&quote=%s", config.getApiKey(), "page", OAuthEncoder.encode(url), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(text)));
        }
        if (config.hasScope()) {
            sb.append('&').append(OAuthConstants.SCOPE).append('=').append(OAuthEncoder.encode(config.getScope()));
        }

        final String state = config.getState();
        if (state != null) {
            sb.append('&').append(OAuthConstants.STATE).append('=').append(OAuthEncoder.encode(state));
        }
        return sb.toString();
    }
}
