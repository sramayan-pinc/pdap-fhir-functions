package com.premierinc.pdap.fhir.functions;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;

import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Azure Functions with HTTP Trigger.
 */
public class GetJwtToken {
    //Production Client ID : c20f3140-ebf8-4783-8d34-9bca2477c826
    //Non-Production Client ID : 9b8edf2f-b80f-4f51-825d-5672bf041233
    private static final String CLIENT_ID_PROD = "c20f3140-ebf8-4783-8d34-9bca2477c826";
    private static final String CLIENT_ID_DEV = "9b8edf2f-b80f-4f51-825d-5672bf041233";
    private static final String SUBJECT_KEY = "9b8edf2f-b80f-4f51-825d-5672bf041233";
    private static final String AUDIENCE_URL = "https://fhir.epic.com/interconnect-fhir-oauth/oauth2";
    private static final String SIGNING_PUBLIC_KEY = "976B4FDF579CE88A26D8659E1F2FF02DD51539BC";

    /**
     * This function listens at endpoint "/api/GetJwtToken". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/GetJwtToken
     * 2. curl {your host}/api/GetJwtToken
     */
    @FunctionName("GetJwtToken")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP JWT token setup.");

        String token = createJWT(CLIENT_ID_DEV, CLIENT_ID_DEV, AUDIENCE_URL, SUBJECT_KEY);
        return request.createResponseBuilder(HttpStatus.OK).body(token).build();
    }

    public static String createJWT(String id, String issuer, String audience, String subject) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 4000;
        Date exp = new Date(expMillis);
        String uuid = UUID.randomUUID().toString();

        String jws = Jwts.builder()
                .setIssuer(issuer)
                .setSubject(subject)
                .setAudience(audience)
                .setIssuedAt(now)
                .setExpiration(exp)
                .setId(uuid)
                .compact();

        return jws;
    }
}
