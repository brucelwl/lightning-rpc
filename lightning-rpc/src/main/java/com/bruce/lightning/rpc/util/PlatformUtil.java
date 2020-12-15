package com.bruce.lightning.rpc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

/**
 * Created by bruce on 2018/10/29 17:07
 */
public class PlatformUtil {

    // NOTE: since this class can be initialized by application code in some
    // cases, we must encapsulate all calls to System.getProperty("...") in
    // a doPrivileged block except for standard JVM properties such as
    // os.name, os.version, os.arch, java.vm.name, etc.

    private static final String os = System.getProperty("os.name");
    private static final String version = System.getProperty("os.version");
    private static final boolean embedded;
    private static final String embeddedType;
    private static final boolean useEGL;
    private static final boolean doEGLCompositing;
    // a property used to denote a non-default impl for this host
    private static String javafxPlatform;

    static {
        javafxPlatform = AccessController.doPrivileged((PrivilegedAction<String>) () -> System.getProperty("javafx.platform"));
        embedded = AccessController.doPrivileged((PrivilegedAction<Boolean>) () -> Boolean.getBoolean("com.sun.javafx.isEmbedded"));
        embeddedType = AccessController.doPrivileged((PrivilegedAction<String>) () -> System.getProperty("embedded"));
        useEGL = AccessController.doPrivileged((PrivilegedAction<Boolean>) () -> Boolean.getBoolean("use.egl"));
        if (useEGL) {
            doEGLCompositing = AccessController.doPrivileged((PrivilegedAction<Boolean>) () -> Boolean.getBoolean("doNativeComposite"));
        } else
            doEGLCompositing = false;
    }

    private static final boolean ANDROID = "android".equals(javafxPlatform) || "Dalvik".equals(System.getProperty("java.vm.name"));
    private static final boolean WINDOWS = os.startsWith("Windows");
    private static final boolean WINDOWS_VISTA_OR_LATER = WINDOWS && versionNumberGreaterThanOrEqualTo(6.0f);
    private static final boolean WINDOWS_7_OR_LATER = WINDOWS && versionNumberGreaterThanOrEqualTo(6.1f);
    private static final boolean MAC = os.startsWith("Mac");
    private static final boolean LINUX = os.startsWith("Linux") && !ANDROID;
    private static final boolean SOLARIS = os.startsWith("SunOS");
    private static final boolean IOS = os.startsWith("iOS");

    /**
     * Utility method used to determine whether the version number as
     * reported by system properties is greater than or equal to a given
     * value.
     *
     * @param value The value to test against.
     * @return false if the version number cannot be parsed as a float,
     *         otherwise the comparison against value.
     */
    private static boolean versionNumberGreaterThanOrEqualTo(float value) {
        try {
            return Float.parseFloat(version) >= value;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the operating system is a form of Windows.
     */
    public static boolean isWindows(){
        return WINDOWS;
    }

    /**
     * Returns true if the operating system is at least Windows Vista(v6.0).
     */
    public static boolean isWinVistaOrLater(){
        return WINDOWS_VISTA_OR_LATER;
    }

    /**
     * Returns true if the operating system is at least Windows 7(v6.1).
     */
    public static boolean isWin7OrLater(){
        return WINDOWS_7_OR_LATER;
    }

    /**
     * Returns true if the operating system is a form of Mac OS.
     */
    public static boolean isMac(){
        return MAC;
    }

    /**
     * Returns true if the operating system is a form of Linux.
     */
    public static boolean isLinux(){
        return LINUX;
    }

    public static boolean useEGL() {
        return useEGL;
    }

    public static boolean useEGLWindowComposition() {
        return doEGLCompositing;
    }

    public static boolean useGLES2() {
        String useGles2 = "false";
        useGles2 =
                AccessController.doPrivileged((PrivilegedAction<String>) () -> System.getProperty("use.gles2"));
        if ("true".equals(useGles2))
            return true;
        else
            return false;
    }

    /**
     * Returns true if the operating system is a form of Unix, including Linux.
     */
    public static boolean isSolaris(){
        return SOLARIS;
    }

    /**
     * Returns true if the operating system is a form of Linux or Solaris
     */
    public static boolean isUnix(){
        return LINUX || SOLARIS;
    }

    /**
     * Returns true if the platform is embedded.
     */
    public static boolean isEmbedded() {
        return embedded;
    }

    /**
     * Returns a string with the embedded type - ie eglx11, eglfb, dfb or null.
     */
    public static String getEmbeddedType() {
        return embeddedType;
    }

    /**
     * Returns true if the operating system is iOS
     */
    public static boolean isIOS(){
        return IOS;
    }

    private static void loadPropertiesFromFile(final File file) {
        Properties p = new Properties();
        try {
            InputStream in = new FileInputStream(file);
            p.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (javafxPlatform == null) {
            javafxPlatform = p.getProperty("javafx.platform");
        }
        String prefix = javafxPlatform + ".";
        int prefixLength = prefix.length();
        boolean foundPlatform = false;
        for (Object o : p.keySet()) {
            String key = (String) o;
            if (key.startsWith(prefix)) {
                foundPlatform = true;
                String systemKey = key.substring(prefixLength);
                if (System.getProperty(systemKey) == null) {
                    String value = p.getProperty(key);
                    System.setProperty(systemKey, value);
                }
            }
        }
        if (!foundPlatform) {
            System.err.println(
                    "Warning: No settings found for javafx.platform='"
                            + javafxPlatform + "'");
        }
    }

    public static boolean isAndroid() {
        return ANDROID;
    }


}
