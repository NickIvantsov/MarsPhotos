import org.gradle.api.artifacts.dsl.DependencyHandler

object AppDependencies {

    //android ui
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    const val archLifecycleExtensions = "android.arch.lifecycle:extensions:${Versions.archLifecycleExtensions}"
    const val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleViewModelKtx}"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtx}"

    //Glide
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glideCompiler}"
    const val glideOkhttp3Integration = "com.github.bumptech.glide:okhttp3-integration:${Versions.glideOkhttp3Integration}"

    //Retrofit
    const val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.retrofit2}"

    // Moshi
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val retrofit2ConverterMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit2ConverterMoshi}"

    //Coroutine
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCore}"

    //Test Libs
    private val junit = "junit:junit:${Versions.junit}"
    private val extJUnit = "androidx.test.ext:junit:${Versions.extJunit}"
    private val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"

    val appLibraries = arrayListOf<String>().apply {
        add(coreKtx)
        add(appcompat)
        add(material)
        add(constraintLayout)
        add(fragmentKtx)
        add(recyclerview)
        add(archLifecycleExtensions)
        add(lifecycleViewModelKtx)
        add(lifecycleRuntimeKtx)
        add(glide)
        add(glideOkhttp3Integration)
        add(retrofit2)
        add(moshi)
        add(retrofit2ConverterMoshi)
        add(coroutines)
        add(coroutinesCore)
    }

    val annotationProcessorLibs = arrayListOf<String>().apply {
        add(glideCompiler)
    }
    val androidTestLibraries = arrayListOf<String>().apply {
        add(extJUnit)
        add(espressoCore)
    }

    val testLibraries = arrayListOf<String>().apply {
        add(junit)
    }
}

//util functions for adding the different type dependencies from build.gradle file
fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}
fun DependencyHandler.annotationProcessor(list: List<String>) {
    list.forEach { dependency ->
        add("annotationProcessor", dependency)
    }
}