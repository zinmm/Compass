# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/zhujinming/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
#指定代码的压缩级别
-optimizationpasses 5
 #混淆时不会产生形形色色的类名  是否使用大小写混合
-dontusemixedcaseclassnames
 #指定不去忽略非公共的类库 是否混淆第三方jar
-dontskipnonpubliclibraryclasses
#不预校验  混淆时是否做预校验
-dontpreverify
#混淆时是否记录日志
-verbose
-ignorewarnings
#优化 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*