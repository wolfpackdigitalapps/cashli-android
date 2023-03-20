plugins {
    id("java-platform")
}

val coroutinesVersion = "1.5.2"

val androidLifecycle = "2.4.1"
val androidxCore = "1.8.0"
val androidxFragment = "1.4.1"
val androidxAppcompat = "1.4.2"
val androidxAnnotation = "1.3.0"
val androidxLegacy = "1.0.0"
val androidxConstraint = "2.1.4"
val androidxRecyclerview = "1.2.1"
val androidxCardView = "1.0.0"

val googleMaterial = "1.6.1"

val koinVersion = "2.0.1"

val retrofit = "2.9.0"
val converters = "2.9.0"
val loggingInterceptor = "4.9.1"
val gson = "2.9.0"

val rxPermissions = "0.12"
val rxAndroid = "2.1.1"

val spinKit = "1.4.0"

val hawk = "2.0.1"

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
        api("${Libs.KOIN_ANDROIDX_SCOPE}:$koinVersion")
        api("${Libs.KOIN_ANDROIDX_VIEWMODEL}:$koinVersion")

        api("${Libs.RETROFIT}:$retrofit")
        api("${Libs.RETROFIT_GSON_CONVERTER}:$converters")
        api("${Libs.RETROFIT_LOGGING_INTERCEPTOR}:$loggingInterceptor")
        api("${Libs.GOOGLE_GSON}:$gson")

        api("${Libs.RX_PERMISSIONS}:$rxPermissions")
        api("${Libs.RX_JAVA2_ANDROID}:$rxAndroid")

        api("${Libs.SPINKIT}:$spinKit")
        api("${Libs.HAWK}:$hawk")
    }
}
