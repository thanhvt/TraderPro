## Add project specific ProGuard rules here.
## You can control the set of applied configuration files using the
## proguardFiles setting in build.gradle.
##
## For more details, see
##   http://developer.android.com/guide/developing/tools/proguard.html
#
## If your project uses WebView with JS, uncomment the following
## and specify the fully qualified class name to the JavaScript interface
## class:
##-keepclassmembers class fqcn.of.javascript.interface.for.webview {
##   public *;
##}
#
## Uncomment this to preserve the line number information for
## debugging stack traces.
##-keepattributes SourceFile,LineNumberTable
#
## If you keep the line number information, uncomment this to
## hide the original source file name.
##-renamesourcefileattribute SourceFile
#
#-verbose
#-dontshrink
#-dontoptimize
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-dontskipnonpubliclibraryclassmembers
#-dontpreverify
#-dontwarn com.google.android.gms.**
#-dontwarn java.nio.file.Files
#-dontwarn java.nio.file.Path
#-dontwarn java.nio.file.OpenOption
#-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#-dontwarn com.squareup.okhttp.**
#-dontwarn android.support.**
#-dontwarn com.github.**
#-dontwarn com.baoyz.swipemenulistview.**
#-dontwarn com.google.firebase.**
#-dontwarn com.github.binance-exchange.**
#-dontwarn com.squareup.retrofit2.**
#-dontwarn com.jakewharton.picasso.**
#-dontwarn android.support.v7.**
#-dontwarn org.apache.poi.**
#-dontwarn com.squareup.picasso.**
#-dontwarn javax.annotation.Nullable
#-dontwarn javax.annotation.ParametersAreNonnullByDefault
#-dontwarn com.squareup.okhttp.**
##-renamesourcefileattribute SourceFile
#-ignorewarnings
#-keep class * {
#    public private *;
#}
#-keepparameternames
#-keepattributes *Annotation*
#-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumber
#-keep class android.support.v7.** { *; }
#-keep interface android.support.v7.** { *; }
#-keep public class com.google.android.gms.** { *; }
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgent
#-keep public class * extends android.preference.Preference
#-keep public class * extends android.support.v4.app.Fragment
#-keep public class * extends android.support.v4.app.DialogFragment
#-keep public class * extends android.app.Fragment
#
#-keepclasseswithmembernames class * {
# native <methods>;
#}
#
#-keep public class * extends android.view.View {
# public <init>(android.content.Context);
# public <init>(android.content.Context, android.util.AttributeSet);
# public <init>(android.content.Context, android.util.AttributeSet, int);
# public void set*(...);
#}
#
#-keepclasseswithmembers class * {
# public <init>(android.content.Context, android.util.AttributeSet);
#}
#
#-keepclasseswithmembers class * {
# public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
#-keepclassmembers class * extends android.app.Activity {
# public void *(android.view.View);
#}
#
## For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
#-keepclassmembers enum * {
# public static **[] values();
# public static ** valueOf(java.lang.String);
#}
#
#-keep class * implements android.os.Parcelable {
# public static final android.os.Parcelable$Creator *;
#}
#
#-keepclassmembers class **.R$* {
# public static <fields>;
#}
#######################
## Keep Android classes
#-keep class ** extends android.** {
#    <fields>;
#    <methods>;
#}
#
## Keep serializable classes & fields
#-keep class ** extends java.io.Serializable {
#    <fields>;
#}
#
#-assumenosideeffects class android.util.Log {
#    public static *** d(...);
#    public static *** e(...);
#    public static *** w(...);
#}
#
# #    implementation 'com.baoyz.swipemenulistview:library:1.3.0'
# #    implementation group: 'org.apache.poi', name: 'poi', version: '3.17'
# #    implementation 'com.github.medyo:android-about-page:1.2.4'
# #    implementation 'com.google.firebase:firebase-messaging:10.2.1'
# #    implementation 'com.github.binance-exchange:binance-java-api:-SNAPSHOT'
# #    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
# #    implementation 'com.squareup.retrofit2:converter-gson:2.1.0'
# #    implementation 'com.jakewharton.picasso:picasso2-okhttp3-downloader:1.1.0'
# #    implementation 'com.squareup.picasso:picasso:2.5.2'
