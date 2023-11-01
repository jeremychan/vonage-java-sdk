/*
 *   Copyright 2023 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client;

import com.vonage.client.account.AccountClient;
import com.vonage.client.application.ApplicationClient;
import com.vonage.client.auth.*;
import com.vonage.client.auth.hashutils.HashUtil;
import com.vonage.client.conversion.ConversionClient;
import com.vonage.client.insight.InsightClient;
import com.vonage.client.meetings.MeetingsClient;
import com.vonage.client.messages.MessagesClient;
import com.vonage.client.numbers.NumbersClient;
import com.vonage.client.proactiveconnect.ProactiveConnectClient;
import com.vonage.client.redact.RedactClient;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sns.SnsClient;
import com.vonage.client.subaccounts.SubaccountsClient;
import com.vonage.client.users.UsersClient;
import com.vonage.client.verify.VerifyClient;
import com.vonage.client.video.VideoClient;
import com.vonage.client.verify2.Verify2Client;
import com.vonage.client.voice.VoiceClient;
import org.apache.http.client.HttpClient;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Top-level Vonage API client object.
 * <p>
 * Construct an instance of this object with one or more {@link AuthMethod}s (providing all the authentication methods
 * for the APIs you wish to use), and then call {@link #getVoiceClient()} to obtain a client for the Vonage Voice API.
 * <p>.
 */
public class VonageClient {
    private final HttpWrapper httpWrapper;
    private final AccountClient account;
    private final ApplicationClient application;
    private final InsightClient insight;
    private final NumbersClient numbers;
    private final SmsClient sms;
    private final VoiceClient voice;
    private final VerifyClient verify;
    private final SnsClient sns;
    private final ConversionClient conversion;
    private final RedactClient redact;
    private final MessagesClient messages;
    private final Verify2Client verify2;
    private final SubaccountsClient subaccounts;
    private final ProactiveConnectClient proactiveConnect;
    private final MeetingsClient meetings;
    private final UsersClient users;
    private final VideoClient video;

    private VonageClient(Builder builder) {
        httpWrapper = new HttpWrapper(builder.httpConfig, builder.authCollection);
        httpWrapper.setHttpClient(builder.httpClient);

        account = new AccountClient(httpWrapper);
        application = new ApplicationClient(httpWrapper);
        insight = new InsightClient(httpWrapper);
        numbers = new NumbersClient(httpWrapper);
        verify = new VerifyClient(httpWrapper);
        voice = new VoiceClient(httpWrapper);
        sms = new SmsClient(httpWrapper);
        sns = new SnsClient(httpWrapper);
        conversion = new ConversionClient(httpWrapper);
        redact = new RedactClient(httpWrapper);
        messages = new MessagesClient(httpWrapper);
        verify2 = new Verify2Client(httpWrapper);
        subaccounts = new SubaccountsClient(httpWrapper);
        proactiveConnect = new ProactiveConnectClient(httpWrapper);
        meetings = new MeetingsClient(httpWrapper);
        users = new UsersClient(httpWrapper);
        video = new VideoClient(httpWrapper);
    }

    public AccountClient getAccountClient() {
        return account;
    }

    public ApplicationClient getApplicationClient() {
        return application;
    }

    public InsightClient getInsightClient() {
        return insight;
    }

    public NumbersClient getNumbersClient() {
        return numbers;
    }

    public SmsClient getSmsClient() {
        return sms;
    }

    public SnsClient getSnsClient() {
        return sns;
    }

    public VerifyClient getVerifyClient() {
        return verify;
    }

    public VoiceClient getVoiceClient() {
        return voice;
    }

    public ConversionClient getConversionClient() {
        return conversion;
    }

    /**
     *
     * @return The Redact API client.
     * @deprecated This API will be removed in the next major release.
     */
    @Deprecated
    public RedactClient getRedactClient() {
        return redact;
    }

    /**
     *
     * @return The Messages v1 client.
     * @since 6.5.0
     */
    public MessagesClient getMessagesClient() {
        return messages;
    }

    /**
     *
     * @return The Proactive Connect client.
     * @since 7.6.0
     */
    public ProactiveConnectClient getProactiveConnectClient() {
        return proactiveConnect;
    }

    /**
     *
     * @return The Meetings client.
     * @since 7.6.0
     */
    public MeetingsClient getMeetingsClient() {
        return meetings;
    }

    /**
     *
     * @return The Verify v2 client.
     * @since 7.4.0
     */
    public Verify2Client getVerify2Client() {
        return verify2;
    }

    /**
     *
     *
     * @return The Subaccounts client.
     * @since 7.5.0
     */
    public SubaccountsClient getSubaccountsClient() {
        return subaccounts;
    }

    /**
     *
     *
     * @return The Users client.
     * @since 7.7.0
     */
    public UsersClient getUsersClient() {
        return users;
    }

    /**
     * Returns the Video client.
     *
     * @return The Video API client.
     * @since 8.0.0-beta1
     */
    public VideoClient getVideoClient() {
        return video;
    }

    /**
     * Generate a JWT for the application the client has been configured with.
     *
     * @return A String containing the token data.
     *
     * @throws VonageUnacceptableAuthException if no {@link JWTAuthMethod} is available
     */
    public String generateJwt() throws VonageUnacceptableAuthException {
        JWTAuthMethod authMethod = httpWrapper.getAuthCollection().getAuth(JWTAuthMethod.class);
        return authMethod.generateToken();
    }

    /**
     * @return The {@link HttpWrapper}
     */
    HttpWrapper getHttpWrapper() {
        return httpWrapper;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder with default initial configuration.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private AuthCollection authCollection;
        private HttpConfig httpConfig = HttpConfig.defaultConfig();
        private HttpClient httpClient;
        private String applicationId, apiKey, apiSecret, signatureSecret;
        private byte[] privateKeyContents;
        private HashUtil.HashType hashType = HashUtil.HashType.MD5;

        /**
         * @param httpConfig Configuration options for the {@link HttpWrapper}.
         *
         * @return This builder.
         */
        public Builder httpConfig(HttpConfig httpConfig) {
            this.httpConfig = httpConfig;
            return this;
        }

        /**
         * @param httpClient Custom implementation of {@link HttpClient}.
         *
         * @return This builder.
         *
         * @deprecated This method will be removed in the next major release.
         */
        @Deprecated
        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        /**
         * Set the application ID for this client. This will be used alongside your private key
         * (se via {@link #privateKeyContents}) for authenticating requests.
         *
         * @param applicationId The application UUID.
         *
         * @return This builder.
         *
         * @since 7.11.0
         */
        public Builder applicationId(UUID applicationId) {
            return applicationId(applicationId.toString());
        }

        /**
         * When setting an applicationId, it is also expected that the {@link #privateKeyContents} will also be set.
         *
         * @param applicationId Used to identify each application.
         *
         * @return This builder.
         */
        public Builder applicationId(String applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        /**
         * When setting an apiKey, it is also expected that {@link #apiSecret(String)} and/or {@link
         * #signatureSecret(String)} will also be set.
         *
         * @param apiKey The API Key found in the dashboard for your account.
         *
         * @return This builder.
         */
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * When setting an apiSecret, it is also expected that {@link #apiKey(String)} will also be set.
         *
         * @param apiSecret The API Secret found in the dashboard for your account.
         *
         * @return This builder.
         */
        public Builder apiSecret(String apiSecret) {
            this.apiSecret = apiSecret;
            return this;
        }

        /**
         * When setting a signatureSecret, it is also expected that {@link #apiKey(String)} will also be set.
         *
         * @param signatureSecret The Signature Secret found in the dashboard for your account.
         *
         * @return This builder.
         */
        public Builder signatureSecret(String signatureSecret) {
            this.signatureSecret = signatureSecret;
            return this;
        }

        /**
         *
         * @param hashType The hashing strategy for signature keys.
         *
         * @return This builder.
         */
        public Builder hashType(HashUtil.HashType hashType) {
            this.hashType = hashType;
            return this;
        }

        /**
         * When setting the contents of your private key, it is also expected that {@link #applicationId(String)} will
         * also be set.
         *
         * @param privateKeyContents The contents of your private key used for JWT generation.
         *
         * @return This builder.
         */
        public Builder privateKeyContents(byte[] privateKeyContents) {
            this.privateKeyContents = privateKeyContents;
            return this;
        }

        /**
         * When setting the contents of your private key, it is also expected that {@link #applicationId(String)} will
         * also be set.
         *
         * @param privateKeyContents The contents of your private key used for JWT generation.
         *
         * @return This builder.
         */
        public Builder privateKeyContents(String privateKeyContents) {
            return privateKeyContents(privateKeyContents.getBytes());
        }

        /**
         * When setting the path of your private key, it is also expected that {@link #applicationId(String)} will also
         * be set.
         *
         * @param privateKeyPath The path to your private key used for JWT generation.
         *
         * @return This builder.
         *
         * @throws VonageUnableToReadPrivateKeyException if the private key could not be read from the file system.
         */
        public Builder privateKeyPath(Path privateKeyPath) throws VonageUnableToReadPrivateKeyException {
            try {
                return privateKeyContents(Files.readAllBytes(privateKeyPath));
            } catch (IOException e) {
                throw new VonageUnableToReadPrivateKeyException("Unable to read private key at " + privateKeyPath, e);
            }
        }

        /**
         * When setting the path of your private key, it is also expected that {@link #applicationId(String)} will also
         * be set.
         *
         * @param privateKeyPath The path to your private key used for JWT generation.
         *
         * @return This builder.
         *
         * @throws VonageUnableToReadPrivateKeyException if the private key could not be read from the file system.
         */
        public Builder privateKeyPath(String privateKeyPath) throws VonageUnableToReadPrivateKeyException {
            return privateKeyPath(Paths.get(privateKeyPath));
        }

        /**
         * @return a new {@link VonageClient} from the stored builder options.
         *
         * @throws VonageClientCreationException if credentials aren't provided in a valid pairing or there were issues
         *                                      generating an {@link JWTAuthMethod} with the provided credentials.
         */
        public VonageClient build() {
            authCollection = generateAuthCollection(applicationId,
                    apiKey,
                    apiSecret,
                    signatureSecret,
                    privateKeyContents,
                    hashType);
          
            return new VonageClient(this);
        }

        private AuthCollection generateAuthCollection(String applicationId,
                                                      String key,
                                                      String secret,
                                                      String signature,
                                                      byte[] privateKeyContents,
                                                      HashUtil.HashType hashType) {
            AuthCollection authMethods = new AuthCollection();

            try {
                validateAuthParameters(applicationId, key, secret, signature, privateKeyContents);
            } catch (IllegalStateException e) {
                throw new VonageClientCreationException("Failed to generate authentication methods.", e);
            }

            if (key != null && secret != null) {
                authMethods.add(new TokenAuthMethod(key, secret));
            }

            if (key != null && signature != null) {
                authMethods.add(new SignatureAuthMethod(key, signature, hashType));
            }

            if (applicationId != null && privateKeyContents != null) {
                authMethods.add(new JWTAuthMethod(applicationId, privateKeyContents));
            }

            return authMethods;
        }

        private void validateAuthParameters(String applicationId, String key, String secret, String signature, byte[] privateKeyContents) {
            if (key != null && secret == null && signature == null) {
                throw new IllegalStateException(
                        "You must provide an API secret or signature secret in addition to your API key.");
            }

            if (secret != null && key == null) {
                throw new IllegalStateException("You must provide an API key in addition to your API secret.");
            }

            if (signature != null && key == null) {
                throw new IllegalStateException("You must provide an API key in addition to your signature secret.");
            }

            if (applicationId == null && privateKeyContents != null) {
                throw new IllegalStateException("You must provide an application ID in addition to your private key.");
            }

            if (applicationId != null && privateKeyContents == null) {
                throw new IllegalStateException("You must provide a private key in addition to your application id.");
            }
        }
    }
}
