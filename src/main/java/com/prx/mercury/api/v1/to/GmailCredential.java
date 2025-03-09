package com.prx.mercury.api.v1.to;

import javax.annotation.Nullable;

/// GmailCredential.
public record GmailCredential(
        @Nullable String client_id,
        @Nullable String client_secret,
        @Nullable String refresh_token,
        @Nullable String grant_type,
        @Nullable String access_token,
        @Nullable String user_email
) {

    @Override
    public String toString() {
        return "GmailCredential{" +
                "client_id='" + client_id + '\'' +
                ", client_secret='" + client_secret + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", grant_type='" + grant_type + '\'' +
                ", access_token='" + access_token + '\'' +
                ", user_email='" + user_email + '\'' +
                '}';
    }
}
