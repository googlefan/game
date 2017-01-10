package com.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 通过classloader加载指定的资源
 * 
 * @author xcy
 * 
 */
public abstract class ResourceUtils {

    /** Pseudo URL prefix for loading from the class path: "classpath:" */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    /** URL prefix for loading from the file system: "file:" */
    public static final String FILE_URL_PREFIX = "file:";

    /** URL protocol for a file in the file system: "file" */
    public static final String URL_PROTOCOL_FILE = "file";

    /** URL protocol for an entry from a jar file: "jar" */
    public static final String URL_PROTOCOL_JAR = "jar";

    /** URL protocol for an entry from a zip file: "zip" */
    public static final String URL_PROTOCOL_ZIP = "zip";

    /** URL protocol for an entry from a WebSphere jar file: "wsjar" */
    public static final String URL_PROTOCOL_WSJAR = "wsjar";

    /** URL protocol for an entry from a JBoss jar file: "vfszip" */
    public static final String URL_PROTOCOL_VFSZIP = "vfszip";

    /** URL protocol for a JBoss file system resource: "vfsfile" */
    public static final String URL_PROTOCOL_VFSFILE = "vfsfile";

    /** URL protocol for a general JBoss VFS resource: "vfs" */
    public static final String URL_PROTOCOL_VFS = "vfs";

    /** URL protocol for an entry from an OC4J jar file: "code-source" */
    public static final String URL_PROTOCOL_CODE_SOURCE = "code-source";

    /** Separator between JAR URL and file path within the JAR */
    public static final String JAR_URL_SEPARATOR = "!/";

    /**
     * Charset to use when calling getResourceAsReader.
     */
    private static Charset charset;

    private ResourceUtils() {
        charset = Charset.forName("utf-8");
    }

    /**
     * Returns the URL of the resource on the classpath
     *
     * @param resource
     *            The resource to find
     * @return The resource
     * @throws IOException
     *             If the resource cannot be found or read
     */
    public static URL getResourceURL(String resource) throws IOException {
        return getResourceURL(getClassLoader(), resource);
    }

    /**
     * Returns the URL of the resource on the classpath
     *
     * @param loader
     *            The classloader used to load the resource
     * @param resource
     *            The resource to find
     * @return The resource
     * @throws IOException
     *             If the resource cannot be found or read
     */
    public static URL getResourceURL(ClassLoader loader, String resource) throws IOException {
        URL url = null;
        if (loader != null)
            url = loader.getResource(resource);
        if (url == null)
            url = ClassLoader.getSystemResource(resource);
        if (url == null)
            throw new IOException("Could not find resource " + resource);
        return url;
    }

    /**
     * Returns a resource on the classpath as a Stream object
     *
     * @param resource
     *            The resource to find
     * @return The resource
     * @throws IOException
     *             If the resource cannot be found or read
     */
    public static InputStream getResourceAsStream(String resource) throws IOException {
        return getResourceAsStream(getClassLoader(), resource);
    }

    /**
     * Returns a resource on the classpath as a Stream object
     *
     * @param loader
     *            The classloader used to load the resource
     * @param resource
     *            The resource to find
     * @return The resource
     * @throws IOException
     *             If the resource cannot be found or read
     */
    public static InputStream getResourceAsStream(ClassLoader loader, String resource) throws IOException {
        InputStream in = null;
        if (loader != null)
            in = loader.getResourceAsStream(resource);
        if (in == null)
            in = ClassLoader.getSystemResourceAsStream(resource);
        if (in == null)
            throw new IOException("Could not find resource " + resource);
        return in;
    }

    /**
     * Returns a resource on the classpath as a Properties object
     *
     * @param resource
     *            The resource to find
     * @return The resource
     * @throws IOException
     *             If the resource cannot be found or read
     */
    public static Properties getResourceAsProperties(String resource) throws IOException {
        Properties props = new Properties();
        InputStream in = null;
        if (resource.startsWith("/")) {
            resource = resource.substring(1);
        }
        in = getResourceAsStream(resource);
        props.load(in);
        in.close();
        return props;
    }
    /**
     * 依据资源路径,返回资源 Properties
     *
     * @param resource
     * (e.g. "classpath:myLog4j.properties"), an absolute file URL
     * (e.g. "file:C:/log4j.properties), or a plain absolute path in the file system
     * (e.g. "C:/log4j.properties")
     * @return The resource
     * @throws IOException
     *             If the resource cannot be found or read
     */
    public static Properties getResourceAsPropertiesByURL(String resource) throws IOException {
        Properties props = new Properties();
        InputStream in = new BufferedInputStream(new FileInputStream(getFile(resource))) ;
        props.load(in);
        in.close();
        return props;
    }

    /**
     * Returns a resource on the classpath as a Properties object
     *
     * @param loader
     *            The classloader used to load the resource
     * @param resource
     *            The resource to find
     * @return The resource
     * @throws IOException
     *             If the resource cannot be found or read
     */
    public static Properties getResourceAsProperties(ClassLoader loader, String resource) throws IOException {
        Properties props = new Properties();
        InputStream in = null;
        String propfile = resource;
        in = getResourceAsStream(loader, propfile);
        props.load(in);
        in.close();
        return props;
    }

    /**
     * Returns a resource on the classpath as a Reader object
     *
     * @param resource
     *            The resource to find
     * @return The resource
     * @throws IOException
     *             If the resource cannot be found or read
     */
    public static Reader getResourceAsReader(String resource) throws IOException {
        Reader reader;
        if (charset == null) {
            reader = new InputStreamReader(getResourceAsStream(resource));
        } else {
            reader = new InputStreamReader(getResourceAsStream(resource), charset);
        }

        return reader;
    }

    /**
     * Returns a resource on the classpath as a Reader object
     *
     * @param loader
     *            The classloader used to load the resource
     * @param resource
     *            The resource to find
     * @return The resource
     * @throws IOException
     *             If the resource cannot be found or read
     */
    public static Reader getResourceAsReader(ClassLoader loader, String resource) throws IOException {
        Reader reader;
        if (charset == null) {
            reader = new InputStreamReader(getResourceAsStream(loader, resource));
        } else {
            reader = new InputStreamReader(getResourceAsStream(loader, resource), charset);
        }

        return reader;
    }

    /**
     * Returns a resource on the classpath as a File object
     *
     * @param resource
     *            The resource to find
     * @return The resource
     * @throws IOException
     *             If the resource cannot be found or read
     */
    public static File getResourceAsFile(String resource) throws IOException {
        return new File(getResourceURL(resource).getFile());
    }

    /**
     * Returns a resource on the classpath as a File object
     *
     * @param loader
     *            - the classloader used to load the resource
     * @param resource
     *            - the resource to find
     * @return The resource
     * @throws IOException
     *             If the resource cannot be found or read
     */
    public static File getResourceAsFile(ClassLoader loader, String resource) throws IOException {
        return new File(getResourceURL(loader, resource).getFile());
    }

    /**
     * Gets a URL as an input stream
     *
     * @param urlString
     *            - the URL to get
     * @return An input stream with the data from the URL
     * @throws IOException
     *             If the resource cannot be found or read
     */
    public static InputStream getUrlAsStream(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        return conn.getInputStream();
    }

    /**
     * Gets a URL as a Reader
     *
     * @param urlString
     *            - the URL to get
     * @return A Reader with the data from the URL
     * @throws IOException
     *             If the resource cannot be found or read
     */
    public static Reader getUrlAsReader(String urlString) throws IOException {
        return new InputStreamReader(getUrlAsStream(urlString));
    }

    /**
     * Gets a URL as a Properties object
     *
     * @param urlString
     *            - the URL to get
     * @return A Properties object with the data from the URL
     * @throws IOException
     *             If the resource cannot be found or read
     */
    public static Properties getUrlAsProperties(String urlString) throws IOException {
        Properties props = new Properties();
        InputStream in = null;
        String propfile = urlString;
        in = getUrlAsStream(propfile);
        props.load(in);
        in.close();
        return props;
    }

    /**
     * Loads a class
     *
     * @param className
     *            - the class to load
     * @return The loaded class
     * @throws ClassNotFoundException
     *             If the class cannot be found (duh!)
     */
    public static Class classForName(String className) throws ClassNotFoundException {
        Class clazz = null;
        try {
            clazz = getClassLoader().loadClass(className);
        } catch (Exception e) {
            // Ignore. Failsafe below.
        }
        if (clazz == null) {
            clazz = Class.forName(className);
        }
        return clazz;
    }

    private static ClassLoader getClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ResourceUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap
                // ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the
                    // caller can live with null...
                }
            }
        }
        return cl;
    }

    public static Charset getCharset() {
        return charset;
    }

    /**
     * 
     * @param charset
     */
    public static void setCharset(Charset charset) {
        ResourceUtils.charset = charset;
    }

    /**
     * Return whether the given resource location is a URL: either a special
     * "classpath" pseudo URL or a standard URL.
     * 
     * @param resourceLocation
     *            the location String to check
     * @return whether the location qualifies as a URL
     * @see #CLASSPATH_URL_PREFIX
     * @see java.net.URL
     */
    public static boolean isUrl(String resourceLocation) {
        if (resourceLocation == null) {
            return false;
        }
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            return true;
        }
        try {
            new URL(resourceLocation);
            return true;
        } catch (MalformedURLException ex) {
            return false;
        }
    }

    /**
     * Resolve the given resource location to a {@code java.net.URL}.
     * <p>
     * Does not check whether the URL actually exists; simply returns the URL
     * that the given location would correspond to.
     * 
     * @param resourceLocation
     *            the resource location to resolve: either a "classpath:" pseudo
     *            URL, a "file:" URL, or a plain file path
     * @return a corresponding URL object
     * @throws FileNotFoundException
     *             if the resource cannot be resolved to a URL
     */
    public static URL getURL(String resourceLocation) throws FileNotFoundException {
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            ClassLoader cl = getClassLoader();
            URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
            if (url == null) {
                String description = "class path resource [" + path + "]";
                throw new FileNotFoundException(description + " cannot be resolved to URL because it does not exist");
            }
            return url;
        }
        try {
            // try URL
            return new URL(resourceLocation);
        } catch (MalformedURLException ex) {
            // no URL -> treat as file path
            try {
                return new File(resourceLocation).toURI().toURL();
            } catch (MalformedURLException ex2) {
                throw new FileNotFoundException("Resource location [" + resourceLocation
                        + "] is neither a URL not a well-formed file path");
            }
        }
    }

    /**
     * Resolve the given resource location to a {@code java.io.File}, i.e. to a
     * file in the file system.
     * <p>
     * Does not check whether the file actually exists; simply returns the File
     * that the given location would correspond to.
     * 
     * @param resourceLocation
     *            the resource location to resolve: either a "classpath:" pseudo
     *            URL, a "file:" URL, or a plain file path
     * @return a corresponding File object
     * @throws FileNotFoundException
     *             if the resource cannot be resolved to a file in the file
     *             system
     */
    public static File getFile(String resourceLocation) throws FileNotFoundException {
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            String description = "class path resource [" + path + "]";
            ClassLoader cl = getClassLoader();
            URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
            if (url == null) {
                throw new FileNotFoundException(description + " cannot be resolved to absolute file path "
                        + "because it does not reside in the file system");
            }
            return getFile(url, description);
        }
        try {
            // try URL
            return getFile(new URL(resourceLocation));
        } catch (MalformedURLException ex) {
            // no URL -> treat as file path
            return new File(resourceLocation);
        }
    }

    /**
     * Resolve the given resource URL to a {@code java.io.File}, i.e. to a file
     * in the file system.
     * 
     * @param resourceUrl
     *            the resource URL to resolve
     * @return a corresponding File object
     * @throws FileNotFoundException
     *             if the URL cannot be resolved to a file in the file system
     */
    public static File getFile(URL resourceUrl) throws FileNotFoundException {
        return getFile(resourceUrl, "URL");
    }

    /**
     * Resolve the given resource URL to a {@code java.io.File}, i.e. to a file
     * in the file system.
     * 
     * @param resourceUrl
     *            the resource URL to resolve
     * @param description
     *            a description of the original resource that the URL was
     *            created for (for example, a class path location)
     * @return a corresponding File object
     * @throws FileNotFoundException
     *             if the URL cannot be resolved to a file in the file system
     */
    public static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
        if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
            throw new FileNotFoundException(description + " cannot be resolved to absolute file path "
                    + "because it does not reside in the file system: " + resourceUrl);
        }
        try {
            return new File(toURI(resourceUrl).getSchemeSpecificPart());
        } catch (URISyntaxException ex) {
            // Fallback for URLs that are not valid URIs (should hardly ever
            // happen).
            return new File(resourceUrl.getFile());
        }
    }

    /**
     * Resolve the given resource URI to a {@code java.io.File}, i.e. to a file
     * in the file system.
     * 
     * @param resourceUri
     *            the resource URI to resolve
     * @return a corresponding File object
     * @throws FileNotFoundException
     *             if the URL cannot be resolved to a file in the file system
     */
    public static File getFile(URI resourceUri) throws FileNotFoundException {
        return getFile(resourceUri, "URI");
    }

    /**
     * Resolve the given resource URI to a {@code java.io.File}, i.e. to a file
     * in the file system.
     * 
     * @param resourceUri
     *            the resource URI to resolve
     * @param description
     *            a description of the original resource that the URI was
     *            created for (for example, a class path location)
     * @return a corresponding File object
     * @throws FileNotFoundException
     *             if the URL cannot be resolved to a file in the file system
     */
    public static File getFile(URI resourceUri, String description) throws FileNotFoundException {
        if (!URL_PROTOCOL_FILE.equals(resourceUri.getScheme())) {
            throw new FileNotFoundException(description + " cannot be resolved to absolute file path "
                    + "because it does not reside in the file system: " + resourceUri);
        }
        return new File(resourceUri.getSchemeSpecificPart());
    }

    /**
     * Determine whether the given URL points to a resource in the file system,
     * that is, has protocol "file", "vfsfile" or "vfs".
     * 
     * @param url
     *            the URL to check
     * @return whether the URL has been identified as a file system URL
     */
    public static boolean isFileURL(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_FILE.equals(protocol) || URL_PROTOCOL_VFSFILE.equals(protocol) || URL_PROTOCOL_VFS
                .equals(protocol));
    }

    /**
     * Determine whether the given URL points to a resource in a jar file, that
     * is, has protocol "jar", "zip", "vfszip", "wsjar" or "code-source".
     * <p>
     * "zip" and "wsjar" are used by WebLogic Server and WebSphere,
     * respectively, but can be treated like jar files. The same applies to
     * "code-source" URLs on OC4J, provided that the path contains a jar
     * separator.
     * 
     * @param url
     *            the URL to check
     * @return whether the URL has been identified as a JAR URL
     */
    public static boolean isJarURL(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_JAR.equals(protocol) || URL_PROTOCOL_ZIP.equals(protocol)
                || URL_PROTOCOL_VFSZIP.equals(protocol) || URL_PROTOCOL_WSJAR.equals(protocol) || (URL_PROTOCOL_CODE_SOURCE
                .equals(protocol) && url.getPath().contains(JAR_URL_SEPARATOR)));
    }

    /**
     * Extract the URL for the actual jar file from the given URL (which may
     * point to a resource in a jar file or to a jar file itself).
     * 
     * @param jarUrl
     *            the original URL
     * @return the URL for the actual jar file
     * @throws MalformedURLException
     *             if no valid jar file URL could be extracted
     */
    public static URL extractJarFileURL(URL jarUrl) throws MalformedURLException {
        String urlFile = jarUrl.getFile();
        int separatorIndex = urlFile.indexOf(JAR_URL_SEPARATOR);
        if (separatorIndex != -1) {
            String jarFile = urlFile.substring(0, separatorIndex);
            try {
                return new URL(jarFile);
            } catch (MalformedURLException ex) {
                // Probably no protocol in original jar URL, like
                // "jar:C:/mypath/myjar.jar".
                // This usually indicates that the jar file resides in the file
                // system.
                if (!jarFile.startsWith("/")) {
                    jarFile = "/" + jarFile;
                }
                return new URL(FILE_URL_PREFIX + jarFile);
            }
        } else {
            return jarUrl;
        }
    }

    /**
     * Create a URI instance for the given URL, replacing spaces with "%20" URI
     * encoding first.
     * <p>
     * Furthermore, this method works on JDK 1.4 as well, in contrast to the
     * {@code URL.toURI()} method.
     * 
     * @param url
     *            the URL to convert into a URI instance
     * @return the URI instance
     * @throws URISyntaxException
     *             if the URL wasn't a valid URI
     * @see java.net.URL#toURI()
     */
    public static URI toURI(URL url) throws URISyntaxException {
        return toURI(url.toString());
    }

    /**
     * Create a URI instance for the given location String, replacing spaces
     * with "%20" URI encoding first.
     * 
     * @param location
     *            the location String to convert into a URI instance
     * @return the URI instance
     * @throws URISyntaxException
     *             if the location wasn't a valid URI
     */
    public static URI toURI(String location) throws URISyntaxException {
        return new URI(StringUtils.replace(location, " ", "%20"));
    }

    /**
     * Set the {@link URLConnection#setUseCaches "useCaches"} flag on the given
     * connection, preferring {@code false} but leaving the flag at {@code true}
     * for JNLP based resources.
     * 
     * @param con
     *            the URLConnection to set the flag on
     */
    public static void useCachesIfNecessary(URLConnection con) {
        con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
    }

    public static void main(String[] args) {
        System.setProperty("game.home", "D:\\workspace_zf\\game-server\\config\\gameconfig.properties");
        System.out.println("系统属性, game.home:" + System.getProperty("game.home"));
        
        try {
            Properties p = getResourceAsPropertiesByURL("D:\\workspace_zf\\game-server\\config\\gameconfig.properties");
            System.out.println(p.getProperty("server.sid"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
