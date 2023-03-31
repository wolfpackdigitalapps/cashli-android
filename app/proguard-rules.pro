# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.wolfpackdigital.cashli.domain.entities.* {
    public protected private *;
}
-keep class com.wolfpackdigital.cashli.presentation.entities.* {
    public protected private *;
}
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes SourceFile, LineNumberTable, Signature, InnerClasses, EnclosingMethod, *Annotation*

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# GSON Pro-Guard rules
-keep class com.google.gson.** { *; }
-keep class com.orhanobut.hawk.** { *; }

-keep class **.R$*
-keep @kotlinx.parcelize.Parcelize public class *

