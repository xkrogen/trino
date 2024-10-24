/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.server.security.oauth2;

import io.airlift.configuration.Config;
import io.airlift.configuration.ConfigDescription;
import io.airlift.configuration.ConfigSecuritySensitive;
import io.airlift.units.Duration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotEmpty;

import java.util.Base64;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.concurrent.TimeUnit.HOURS;

public class RefreshTokensConfig
{
    private Duration tokenExpiration = Duration.succinctDuration(1, HOURS);
    private String issuer = "Trino_coordinator";
    private String audience = "Trino_coordinator";
    private SecretKey secretKey;

    public Duration getTokenExpiration()
    {
        return tokenExpiration;
    }

    @Config("http-server.authentication.oauth2.refresh-tokens.issued-token.timeout")
    @ConfigDescription("Expiration time for issued token. It needs to be equal or lower than duration of refresh token issued by IdP")
    public RefreshTokensConfig setTokenExpiration(Duration tokenExpiration)
    {
        this.tokenExpiration = tokenExpiration;
        return this;
    }

    @NotEmpty
    public String getIssuer()
    {
        return issuer;
    }

    @Config("http-server.authentication.oauth2.refresh-tokens.issued-token.issuer")
    @ConfigDescription("Issuer representing this coordinator instance, that will be used in issued token. In addition current Version will be added to it")
    public RefreshTokensConfig setIssuer(String issuer)
    {
        this.issuer = issuer;
        return this;
    }

    @NotEmpty
    public String getAudience()
    {
        return audience;
    }

    @Config("http-server.authentication.oauth2.refresh-tokens.issued-token.audience")
    @ConfigDescription("Audience representing this coordinator instance, that will be used in issued token")
    public RefreshTokensConfig setAudience(String audience)
    {
        this.audience = audience;
        return this;
    }

    @Config("http-server.authentication.oauth2.refresh-tokens.secret-key")
    @ConfigDescription("Base64 encoded secret key used to encrypt generated token")
    @ConfigSecuritySensitive
    public RefreshTokensConfig setSecretKey(String key)
    {
        if (isNullOrEmpty(key)) {
            return this;
        }
        secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
        return this;
    }

    public SecretKey getSecretKey()
    {
        return secretKey;
    }
}
