/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2023-2024 The Enola <https://enola.dev> Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.enola.common.io.iri;

import static java.util.Collections.emptyMap;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.net.MediaType;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class URIs {
    // see also class dev.enola.common.io.iri.IRIs

    // TODO Review if all this String instead of URI-based processing could be removed and replaced
    // with first encoding invalid special characters in URIs; see the related TBD in
    // FileGlobResourceProvider.

    // URI Query Parameter Names
    private static final String MEDIA_TYPE = "mediaType";
    private static final String CHARSET = "charset"; // as in MediaType#CHARSET_ATTRIBUTE

    private static final Splitter AMPERSAND_SPLITTER =
            Splitter.on('&').omitEmptyStrings().trimResults();

    public static record MediaTypeAndOrCharset(String mediaType, String charset) {}

    public static boolean hasQueryParameter(URI uri, String key) {
        var queryMap = getQueryMap(uri);
        return queryMap.containsKey(key);
    }

    public static MediaTypeAndOrCharset getMediaTypeAndCharset(URI uri) {
        var queryMap = getQueryMap(uri);
        var charsetParameter = queryMap.get(CHARSET);
        var mediaTypeParameter = queryMap.get(MEDIA_TYPE.toLowerCase());
        return new MediaTypeAndOrCharset(mediaTypeParameter, charsetParameter);
    }

    public static URI addMediaType(URI uri, MediaType mediaType) {
        return addQueryParameter(uri, MEDIA_TYPE, mediaType.toString().replace(" ", ""));
    }

    public static URI addQuery(URI uri, Map<String, String> parameters) {
        for (var parameter : parameters.entrySet()) {
            uri = addQueryParameter(uri, parameter.getKey(), parameter.getValue());
        }
        return uri;
    }

    /**
     * Returns an URI with everything except the query parameters of the uri (1st) parameter, but
     * the query parameters of originalUriWithQuery (2nd) parameter - IFF the uri (1st) parameter
     * has no query; otherwise just returns the uri (1st) parameter as-is.
     *
     * <p>See {@link URIsTest#testAddQueryGivenOriginalUriWithQuery()} for example.
     */
    public static URI addQuery(URI uri, URI originalUriWithQuery) {
        if (Strings.isNullOrEmpty(originalUriWithQuery.getQuery())) return uri;
        if (!Strings.isNullOrEmpty(uri.getQuery())) return uri;

        return URI.create(uri.toString() + "?" + originalUriWithQuery.getQuery());
    }

    public static String addQuery(String string, Map<String, String> parameters) {
        return addQuery(URI.create(string), parameters).toString();
    }

    private static URI addQueryParameter(URI uri, String key, String value) {
        String connector;
        if (uri.getQuery() != null && uri.getQuery().contains(key)) {
            return uri;
        } else if (uri.getQuery() == null) {
            if (!uri.getSchemeSpecificPart().contains("?")) {
                connector = "?";
            } else {
                // Special case of "scheme:?"-like URIs with "empty" query
                connector = "";
            }
        } else {
            connector = "&";
        }
        return URI.create(uri + connector + key + "=" + encodeQueryParameterValue(value));
    }

    private static String encodeQueryParameterValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    // package-local not public (for now)
    static String getQueryString(String uri) {
        var qp = uri.indexOf('?');
        if (qp > -1) {
            // Handle Glob URIs, like e.g. "file:/tmp//?.txt"
            if (uri.indexOf('=', qp) == -1) return "";
            var fp = uri.indexOf('#');
            if (fp == -1) {
                return uri.substring(qp + 1);
            } else {
                return uri.substring(qp + 1, fp);
            }
        }
        return "";
    }

    public static Map<String, String> getQueryMap(String uri) {
        return getQueryMapGivenQueryString(getQueryString(uri));
    }

    static Map<String, String> getQueryMapGivenQueryString(String query) {
        if (Strings.isNullOrEmpty(query)) {
            return emptyMap();
        }
        Map<String, String> map = new HashMap<>();
        AMPERSAND_SPLITTER.split(query).forEach(queryParameter -> put(queryParameter, map));
        return map;
    }

    // package-local not public (for now)
    static Map<String, String> getQueryMap(URI uri) {
        if (uri == null) return emptyMap();

        String query = uri.getQuery();
        if (Strings.isNullOrEmpty(query)) {
            var part = uri.getSchemeSpecificPart();
            query = getQueryString(part);
        }
        return getQueryMapGivenQueryString(query);
    }

    private static void put(String queryParameter, Map<String, String> map) {
        var p = queryParameter.indexOf('=');
        if (p == -1) {
            map.put(queryParameter, null);
        } else {
            var key = queryParameter.substring(0, p);
            if (map.containsKey(key))
                throw new IllegalArgumentException(
                        "URI Query Parameter has duplicate key: " + queryParameter);
            var value = queryParameter.substring(p + 1);
            map.put(key.toLowerCase(), value);
        }
    }

    /**
     * Get a {@link Path} from an {@link URI}. This method is used internally by {@link
     * dev.enola.common.io.resource.Resource} framework implementations, and typically shouldn't be
     * called directly by users. Please see the {@link dev.enola.common.io.resource.FileResource}
     * for more related background.
     */
    public static Path getFilePath(URI uri) {
        // TODO Replace this with return Path.of(uri); but it needs more work...
        // Both for relative file URIs and query parameters and ZIP files.
        // Nota bene: https://stackoverflow.com/q/25032716/421602
        // https://docs.oracle.com/javase/7/docs/technotes/guides/io/fsp/zipfilesystemprovider.html
        // https://docs.oracle.com/en/java/javase/21/docs/api/jdk.zipfs/module-summary.html

        var scheme = uri.getScheme();
        var authority = uri.getAuthority();
        var path = getPath(uri);
        return getFilePath(scheme, authority, path);
    }

    public static Path getFilePath(String uri) {
        var scheme = getScheme(uri);
        var authority = ""; // TODO Implement getAuthority(String uri)
        var path = getPath(uri);
        return getFilePath(scheme, "", path);
    }

    private static Path getFilePath(String scheme, String authority, String path) {
        // TODO Don't hard-code this to file: but use MoreFileSystems.URI_SCHEMAS, somehow...
        if ("file".equals(scheme)) {
            return FileSystems.getDefault().getPath(path);
        } else
            try {
                // TODO Better null or "" for path?
                URI fsURI = new URI(scheme, authority, "", null, null);
                var fs = FileSystems.getFileSystem(fsURI);
                return fs.getPath(path);
            } catch (URISyntaxException e) {
                // This is rather unexpected...
                throw new IllegalStateException(
                        "Failed to create FileSystem Authority URI: " + scheme + ":" + path, e);
            }
    }

    static String getScheme(String iri) {
        if (iri == null) return "";
        var p = iri.indexOf(':');
        if (p == -1) return "";
        return iri.substring(0, p);
    }

    static String getSchemeSpecificPart(String iri) {
        if (iri == null) return "";
        var p = iri.indexOf(':');
        if (p == -1) return "";
        return iri.substring(p + 1);
    }

    /**
     * Get the "path"-like component of any URI. Similar to {@link URI#getPath()}, but also works
     * e.g. for "non-standard" relative "file:hello.txt" URIs, and correctly chops off query
     * arguments and fragments.
     *
     * <p>TODO This does not yet decode correctly! :=((
     *
     * <p>TODO Is this really required?! Re-review which tests URI#getPath() fails, and why...
     */
    public static String getPath(URI uri) {
        return chopFragmentAndQuery(uri.getSchemeSpecificPart());
    }

    public static String getPath(String uri) {
        return chopFragmentAndQuery(getSchemeSpecificPart(uri));
    }

    private static String chopFragmentAndQuery(String ssp) {
        var chop = ssp.indexOf('?', 0);

        // Handle Glob URIs, like e.g. "file:/tmp//?.txt"
        if (ssp.indexOf('=', chop) == -1) chop = -1;

        if (chop == -1) chop = ssp.indexOf('#', 0);
        if (chop == -1) chop = ssp.length();

        return ssp.substring(0, chop);
    }

    private static String chopLastSlash(String path) {
        if (!path.endsWith("/")) return path;
        else return path.substring(0, path.length() - 1);
    }

    /**
     * Extracts the "file name" from an URI, or the empty string if there is none. The filename is
     * simply the last part of the path of the URI. It COULD be a directory! Works for file: http:
     * and other even for "weird" URIs, such as those from Classpath URLs.
     *
     * <p>See also {@link com.google.common.io.Files#getFileExtension(String)} and @{@link
     * com.google.common.io.Files#getNameWithoutExtension(String)}.
     */
    public static String getFilename(URI uri) {
        return getLastPathSegmentOrHost(uri, false);
    }

    public static String getFilenameOrLastPathSegmentOrHost(URI uri) {
        return getLastPathSegmentOrHost(uri, true);
    }

    private static String getLastPathSegmentOrHost(URI uri, boolean evenIfSlashed) {
        String path;
        var scheme = uri.getScheme();
        if ("file".equals(scheme)) {
            path = uri.getPath();
            if (path != null) {
                if (path.endsWith("/")) {
                    return "";
                } else {
                    return chopFragmentAndQuery(new java.io.File(path).getName());
                }
            }
        } else if ("jar".equals(scheme)) {
            return chopFragmentAndQuery(getFilename(URI.create(uri.getSchemeSpecificPart())));
        } else if ("http".equals(scheme) || "https".equals(scheme)) {
            path = chopFragmentAndQuery(uri.getPath());
        } else {
            path = getPath(uri);
        }

        if (!evenIfSlashed && (Strings.isNullOrEmpty(path) || path.endsWith("/"))) {
            return "";
        }
        if (evenIfSlashed && (Strings.isNullOrEmpty(path) || "/".equals(path))) {
            var host = uri.getHost();
            return host;
        }

        int p;
        if (!path.endsWith("/")) p = path.lastIndexOf('/');
        else p = path.substring(0, path.length() - 1).lastIndexOf('/');

        return p > -1 ? chopLastSlash(path.substring(p + 1)) : chopLastSlash(path);
    }

    /**
     * This converts e.g. "file:relative.txt" to "file:///tmp/.../relative.txt" (depending on the
     * current working directory, obviously). It looses any query parameters and fragment.
     */
    public static URI rel2abs(URI uri) {
        if (uri == null) return uri;
        if (!"file".equals(uri.getScheme())) return uri;
        if (!uri.isOpaque()) return uri;

        String relativePath = uri.getSchemeSpecificPart();
        return Path.of(relativePath).toAbsolutePath().toUri();
    }

    private URIs() {}
}
