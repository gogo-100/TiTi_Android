plugins {
    id("titi.android.data.local")
}

android {
    namespace = "com.titi.app.data.sleep.impl"
}

dependencies {
    implementation(project(":data:sleep:api"))
}
