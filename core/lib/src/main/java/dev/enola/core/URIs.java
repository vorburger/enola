package dev.enola.core;

import dev.enola.core.proto.URI;

public final class URIs {
    private URIs() { }

    public static URI from(String s) {
        java.net.URI uri = java.net.URI.create(s);
        URI.Parts.Builder builder = URI.Parts.newBuilder();

        if (uri.getScheme() == null) {
            throw new IllegalArgumentException(s + " URI has no scheme: " + uri);
        }
        builder.setScheme(uri.getScheme());

        // TODO uri.getAuthority() == null && ?
        if (uri.getSchemeSpecificPart() == null) {
            throw new IllegalArgumentException(s + " URI has no authority: " + uri);
        }
        builder.setEntity(uri.getSchemeSpecificPart());

        return URI.newBuilder().setParts(builder).build();
    }

    public static URI struct(URI uri) {
        if (uri.hasParts()) {
            return uri;
        }
        if (!uri.hasText()) {
            throw new IllegalArgumentException("URI proto is empty and has neither text nor struct");
        }
        return from(uri.getText());
    }
}