plugins {
    id("java-platform")
}

val coroutinesVersion = "1.6.4"

val androidLifecycle = "2.6.0"
val androidxCore = "1.9.0"
val androidxFragment = "1.5.5"
val androidxAppcompat = "1.6.1"
val androidxAnnotation = "1.3.0"
val androidxLegacy = "1.0.0"
val androidxConstraint = "2.1.4"
val androidxRecyclerview = "1.3.0"
val androidxCardView = "1.0.0"

val googleMaterial = "1.8.0"

val koinVersion = "3.3.3"

val retrofit = "2.9.0"
val converters = "2.9.0"
val loggingInterceptor = "4.10.0"
val gson = "2.10.1"

val rxPermissions = "0.12"
val rxAndroid = "2.1.1"

val spinKit = "1.4.0"

val hawk = "2.0.1"
val splashScreen = "1.0.0"
val viewPager2 = "1.0.0"
val dotIndicator = "4.3"

dependencies {
    constraints {
        api("${Libs.KOTLIN_STD_LIB}:${Versions.KOTLIN}")
        api("${Libs.KTX_COROUTINES_CORE}:$coroutinesVersion")
        api("${Libs.KTX_COROUTINES_ANDROID}:$coroutinesVersion")

        api("${Libs.ANDROIDX_LIFECYCLE_LIVEDATA}:$androidLifecycle")
        api("${Libs.ANDROIDX_LIFECYCLE_VIEWMODEL_KTX}:$androidLifecycle")
        api("${Libs.ANDROIDX_NAVIGATION_FRAGMENT_KTX}:${Versions.ANDROID_NAVIGATION}")
        api("${Libs.ANDROIDX_NAVIGATION_UI_KTX}:${Versions.ANDROID_NAVIGATION}")
        api("${Libs.ANDROIDX_CORE_KTX}:$androidxCore")
        api("${Libs.ANDROIDX_FRAGMENT_KTX}:$androidxFragment")
        api("${Libs.ANDROIDX_APPCOMPAT}:$androidxAppcompat")
        api("${Libs.ANDROIDX_ANNOTATION}:$androidxAnnotation")
        api("${Libs.ANDROIDX_LEGACY}:$androidxLegacy")
        api("${Libs.ANDROIDX_CONSTRAINTLAYOUT}:$androidxConstraint")
        api("${Libs.ANDROIDX_RECYCLERVIEW}:$androidxRecyclerview")
        api("${Libs.ANDROIDX_CARDVIEW}:$androidxCardView")

        api("${Libs.GOOGLE_MATERIAL}:$googleMaterial")

        api("${Libs.GOOGLE_FIREBASE_BOM}:${Versions.FIREBASE_BOM}")
        api(Libs.GOOGLE_FIREBASE_ANALYTICS)
        api(Libs.GOOGLE_FIREBASE_CRASHLYTICS)
        api(Libs.GOOGLE_FIREBASE_MESSAGES)

        api("${Libs.KOIN_CORE}:$koinVersion")
        api("${Libs.KOIN_ANDROID}:$koinVersion")

        api("${Libs.RETROFIT}:$retrofit")
        api("${Libs.RETROFIT_GSON_CONVERTER}:$converters")
        api("${Libs.RETROFIT_LOGGING_INTERCEPTOR}:$loggingInterceptor")
        api("${Libs.GOOGLE_GSON}:$gson")

        api("${Libs.RX_PERMISSIONS}:$rxPermissions")
        api("${Libs.RX_JAVA2_ANDROID}:$rxAndroid")

        api("${Libs.SPINKIT}:$spinKit")
        api("${Libs.HAWK}:$hawk")

        api("${Libs.SPLASH_SCREEN}:$splashScreen")
        api("${Libs.VIEW_PAGER2}:$viewPager2")
        api("${Libs.DOT_INDICATOR}:$dotIndicator")
    }
}
