# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
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

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class * implements com.bumptech.glide.load.model.ModelLoaderFactory
-keep class * implements com.bumptech.glide.load.ResourceDecoder
-keep class * extends com.bumptech.glide.load.engine.cache.MemoryCache
-keep class * extends com.bumptech.glide.load.engine.cache.DiskCache.Factory
-keep class * extends com.bumptech.glide.load.Key
-keep class * implements com.bumptech.glide.load.Encoder
-keep public class com.bumptech.glide.load.resource.bitmap.** {
  public protected *;
}
-keep class com.bumptech.glide.samples.giphy.** { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Jackson
-dontwarn com.fasterxml.jackson.databind.**
-keep class com.fasterxml.jackson.databind.ObjectMapper {
    public <methods>;
    protected <methods>;
}
-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames interface com.fasterxml.jackson.** { *; }
-keep class org.codehaus.** { *; }
-keep interface org.codehaus.** { *; }
-dontwarn org.codehaus.**

# Firebase Messaging
-keep class com.google.firebase.messaging.** { *; }
-keepattributes Signature

# EasyUpiPayment
-keep class dev.shreyaspatil.easyupipayment.** { *; }
-keepattributes Signature

# Gson
-keep class com.google.gson.stream.** { *; }

# AndroidX
-keep class androidx.lifecycle.Lifecycle { *; }
-keep class androidx.lifecycle.LifecycleRegistry { *; }
-keep class androidx.lifecycle.LifecycleOwner { *; }
-keep class androidx.lifecycle.ViewModel { *; }
-keep class androidx.lifecycle.ViewModelProvider.Factory { *; }
-keep class androidx.savedstate.** { *; }
-keep class androidx.navigation.** { *; }

# AndroidX Annotations
-keep class androidx.annotation.** { *; }

# AndroidX Core KTX
-keep class androidx.core.content.ContextCompat { *; }

# AndroidX AppCompat
-keep class androidx.appcompat.** { *; }

# AndroidX ConstraintLayout
-keep class androidx.constraintlayout.** { *; }

# Google Material
-keep class com.google.android.material.** { *; }
