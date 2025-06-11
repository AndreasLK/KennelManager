// File: buildSrc/src/main/kotlin/Dependencies.kt

object Versions {
    const val CORE_KTX                  = "1.13.1"
    const val LIFECYCLE_RUNTIME_KTX     = "2.9.0"
    const val ACTIVITY_COMPOSE          = "1.10.1"
    const val COMPOSE_BOM               = "2025.05.01"
    const val CONSTRAINTLAYOUT          = "2.1.4"
    const val CONSTRAINTLAYOUT_COMPOSE  = "1.1.1"
    const val APPCOMPAT                 = "1.7.0"
    const val RECYCLERVIEW              = "1.3.2"
    const val JUNIT                     = "4.13.2"
    const val JUNIT_EXT                 = "1.1.5"
    const val ESPRESSO_CORE             = "3.5.1"
    const val BCPROV                    = "1.70"
    const val COLOR_PICKER_DIALOG       = "2.1.0"
}

object Deps {
    // Core KTX
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"

    // Lifecycle KTX
    const val LIFECYCLE_RUNTIME_KTX = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE_RUNTIME_KTX}"

    // Activity-Compose
    const val ACTIVITY_COMPOSE = "androidx.activity:activity-compose:${Versions.ACTIVITY_COMPOSE}"

    // Compose BOM
    const val COMPOSE_BOM = "androidx.compose:compose-bom:${Versions.COMPOSE_BOM}"

    // Compose UI modules (BOM manages their versions)
    const val COMPOSE_UI               = "androidx.compose.ui:ui"
    const val COMPOSE_UI_GRAPHICS      = "androidx.compose.ui:ui-graphics"
    const val COMPOSE_UI_TOOLING_PREV  = "androidx.compose.ui:ui-tooling-preview"
    const val MATERIAL3                = "androidx.compose.material3:material3"
    const val COMPOSE_UI_TOOLING       = "androidx.compose.ui:ui-tooling"
    const val COMPOSE_UI_TEST_JUNIT4   = "androidx.compose.ui:ui-test-junit4"
    const val COMPOSE_UI_TEST_MANIFEST = "androidx.compose.ui:ui-test-manifest"

    // ConstraintLayout (classic & Compose)
    const val CONSTRAINTLAYOUT           = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINTLAYOUT}"
    const val CONSTRAINTLAYOUT_COMPOSE   = "androidx.constraintlayout:constraintlayout-compose:${Versions.CONSTRAINTLAYOUT_COMPOSE}"

    // AppCompat & RecyclerView
    const val APPCOMPAT     = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val RECYCLERVIEW  = "androidx.recyclerview:recyclerview:${Versions.RECYCLERVIEW}"

    // Testing
    const val JUNIT            = "junit:junit:${Versions.JUNIT}"
    const val ANDROIDX_JUNIT   = "androidx.test.ext:junit:${Versions.JUNIT_EXT}"
    const val ESPRESSO_CORE    = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"

    // Extras
    const val BCPROV        = "org.bouncycastle:bcprov-jdk15on:${Versions.BCPROV}"
    const val BCPKIX        = "org.bouncycastle:bcpkix-jdk15on:${Versions.BCPROV}"
    const val COLOR_PICKER  = "com.github.github:ColorPickerDialog:${Versions.COLOR_PICKER_DIALOG}"
}
