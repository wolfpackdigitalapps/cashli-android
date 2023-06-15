@file:Suppress("UnstableApiUsage")
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("com.google.firebase.appdistribution")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = Versions.COMPILE_SDK
    defaultConfig {
        applicationId = "com.wolfpackdigital.cashli"
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = Versions.VERSION_CODE
        versionName = Versions.VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "BASE_URL",
            "\"https://api-dev.cashli.io/api/\""
        )
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    val dimension = "env"
    flavorDimensions += listOf(dimension)
    productFlavors {
        create("develop") {
            this.dimension = dimension
            resValue("string", "app_name", "Cashli Dev")
            versionCode = getBuildVersion()
            applicationIdSuffix = ".develop"
            versionNameSuffix = "-develop"
        }

        create("staging") {
            this.dimension = dimension
            resValue("string", "app_name", "Cashli Staging")
            versionCode = getBuildVersion()
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
        }

        create("production") {
            this.dimension = dimension
            resValue("string", "app_name", "Cashli")
            versionCode = 1
            versionName = "1.0"
        }
    }

    buildFeatures.dataBinding = true
    buildFeatures.compose = true

    composeOptions.kotlinCompilerExtensionVersion = Versions.COMPOSE

    lint {
        abortOnError = true
        warningsAsErrors = true
        lintConfig = file("lint.xml")
        baseline = file("lint-baseline.xml")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions.jvmTarget = "17"
    namespace = "com.wolfpackdigital.cashli"
    kotlin {
        jvmToolchain(17)
    }
}

fun getBuildVersion() =
    System.getenv("BITRISE_BUILD_NUMBER")?.toIntOrNull() ?: Versions.VERSION_CODE

dependencies {
    api(platform(project(":depconstraints")))
    kapt(platform(project(":depconstraints")))

    // Kotlin & Coroutines
    implementation(Libs.KOTLIN_STD_LIB)
    implementation(Libs.KTX_COROUTINES_CORE)
    implementation(Libs.KTX_COROUTINES_ANDROID)

    // Lifecycle, LiveData, ViewModel
    implementation(Libs.ANDROIDX_LIFECYCLE_LIVEDATA)
    implementation(Libs.ANDROIDX_LIFECYCLE_VIEWMODEL_KTX)

    // Navigation
    implementation(Libs.ANDROIDX_NAVIGATION_FRAGMENT_KTX)
    implementation(Libs.ANDROIDX_NAVIGATION_UI_KTX)

    // Android defaults
    implementation(Libs.ANDROIDX_CORE_KTX)
    implementation(Libs.ANDROIDX_FRAGMENT_KTX)
    implementation(Libs.ANDROIDX_APPCOMPAT)
    implementation(Libs.ANDROIDX_ANNOTATION)
    implementation(Libs.ANDROIDX_LEGACY)
    implementation(Libs.ANDROIDX_CONSTRAINTLAYOUT)
    implementation(Libs.ANDROIDX_RECYCLERVIEW)
    implementation(Libs.ANDROIDX_CARDVIEW)
    implementation(Libs.GOOGLE_MATERIAL)

    // Firebase
    implementation(platform("${Libs.GOOGLE_FIREBASE_BOM}:${Versions.FIREBASE_BOM}"))
    implementation(Libs.GOOGLE_FIREBASE_ANALYTICS)
    implementation(Libs.GOOGLE_FIREBASE_CRASHLYTICS)
    implementation(Libs.GOOGLE_FIREBASE_MESSAGES)

    // Koin
    implementation(Libs.KOIN_CORE)
    implementation(Libs.KOIN_ANDROID)

    // Networking
    implementation(Libs.RETROFIT)
    implementation(Libs.RETROFIT_GSON_CONVERTER)
    implementation(Libs.GOOGLE_GSON)

    implementation(platform("${Libs.OKHTTP_BOM}:${Versions.OKHTTP_BOM}"))
    implementation(Libs.OKHTTP)
    implementation(Libs.OKHTTP_LOGGING_INTERCEPTOR)

    // Permissions
    implementation(Libs.RX_PERMISSIONS)
    implementation(Libs.RX_JAVA2_ANDROID)
    implementation(Libs.SPINKIT)

    // Hawk
    implementation(Libs.HAWK)

    // SplashScreen
    implementation(Libs.SPLASH_SCREEN)

    // ViewPager2
    implementation(Libs.VIEW_PAGER2)

    // DotIndicator
    implementation(Libs.DOT_INDICATOR)

    // CustomTabs
    implementation(Libs.ANDROIDX_BROWSER)

    // PlaidLink
    implementation(Libs.PLAID_LINK)

    // Tooltips
    implementation(Libs.TOOLTIP_BALLOON)

    // Pagination
    implementation(Libs.PAGINATION)

    // Locale
    implementation(Libs.LOCALE)

    // Compose
    implementation(Libs.ANDROIDX_COMPOSE_UI)
    implementation(Libs.ANDROIDX_COMPOSE_MATERIAL)
    implementation(Libs.ANDROIDX_COMPOSE_UI_TOOLING)
    implementation(Libs.ANDROIDX_COMPOSE_UI_TOOLING_PREVIEW)
    implementation(Libs.ANDROIDX_COMPOSE_RUNTIME)
    implementation(Libs.ANDROIDX_COMPOSE_RUNTIME_LIVEDATA)
    implementation(Libs.COLORFUL_SLIDERS)
}