<div align="center">
<img src="https://github.com/iamoscarliang/particleview/blob/master/images/showcase.gif" width="600">

[![Build](https://github.com/iamoscarliang/particleview/workflows/Build/badge.svg)](https://github.com/iamoscarliang/particleview/actions)
[![Version](https://jitpack.io/v/iamoscarliang/particleview.svg)](https://jitpack.io/#iamoscarliang/particleview)
![minSdk](https://img.shields.io/badge/minSdk-26-brightgreen)
[![license](https://img.shields.io/badge/license-MIT-brightgreen)](https://github.com/iamoscarliang/particleview/blob/master/LICENSE)

[Getting Started](#wrench-getting-started) • [How To Use](#computer-how-to-use) • [Features](#pushpin-features) • [Samples](#tada-samples) • [Dependencies](#balance_scale-dependencies)

**A lightweight particle effect for Android**
</div>

---

## :wrench: Getting Started
Add the jitpack
```groovy
maven { url 'https://jitpack.io' }
```
Add the dependency

```groovy
dependencies {
    // XML
    implementation 'com.github.iamoscarliang.particleview:xml:<version>'
    // Compose
    implementation 'com.github.iamoscarliang.particleview:compose:<version>'
}
```

## :computer: How To Use

### ParticleView

XML
```xml
<com.oscarliang.particleview.xml.ParticleView
    android:id="@+id/particle_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

```kotlin
particleView.start(
    configs = listOf(
        ParticleConfig(
            images = listOf(
                Image(
                    imageId = R.drawable.particle,
                    size = 30
                )
            ),
            startX = FloatOffset(0.0f, 1.0f),
            startY = FloatOffset(0.0f, 1.0f),
            angle = IntOffset(-30, 30),
            speed = FloatOffset(300.0f, 600.0f),
            particlePerSecond = 20,
        )
    ),
    duration = 5000
)
```

Compose
```kotlin
ParticleView(
    modifier = Modifier.fillMaxSize(),
    configs = listOf(
        ParticleConfig(
            images = listOf(
                Image(
                    imageId = R.drawable.particle,
                    size = 30
                )
            ),
            startX = FloatOffset(0.0f, 1.0f),
            startY = FloatOffset(0.0f, 1.0f),
            angle = IntOffset(-30, 30),
            speed = FloatOffset(300.0f, 600.0f),
            particlePerSecond = 20,
        )
    ),
    duration = 5000
)
```

### Configuration
- `images` - the image of the particle
- `startX` - the start x position of the particle. Range from [0.0f, 1.0f] relative to the view's width
- `startY` - the start y position of the particle. Range from [0.0f, 1.0f] relative to the view's height
- `speed` - the speed of the particle moving in (px / per second)
- `accelX` - the x acceleration added to the speed per second
- `accelY` - the y acceleration added to the speed per second
- `angle` - the direction of the particle's speed. Range from [0, 360]. Top - 180, Right - 90, Bottom - 0, Left - 270
- `rotation` - the start rotation of the particle. Range from [0, 360]
- `rotationSpeed` - the rotation speed of the particle rotating in (degree / per second)
- `particleDuration` - the duration of the individual particle
- `particleFadeOutDuration` - the duration of the fade out effect of particle
- `particlePerSecond` - the amount of particle being emitted per second
- `duration` - the duration of the animation
- `isPause` - pause or resume the animation
- `isCancel` - cancel the animation
- `onParticlesEnd` - callback being executed when end of animation or cancellation

## :pushpin: Features

### Recycling
Recycling mechanism has been added since [v1.2](https://github.com/iamoscarliang/particleview/releases/tag/1%2C2) to improve performance.
ParticleView use a pre-created object pool to store unused particles, when a particle finishing emitting, it will be added back to the pool and being reused next time.
The mechanism can decrease the creation of particles, improving rendering efficiency, and preventing memory leak.

<img src="https://github.com/iamoscarliang/particleview/blob/master/images/recycle_mechanism.png" width="600">

### Playback Control
Playback control (since [v1.4](https://github.com/iamoscarliang/particleview/releases/tag/1.4)) can be easily integrated with Android's [lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle).

XML
```kotlin
override fun onPause() {
    super.onPause()     
    particleView.pause()
}

override fun onResume() {     
    super.onResume()
    particleView.resume()
}
```

Compose
```kotlin
var isPause by rememberSaveable { mutableStateOf(false) }

LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
    isPause = true
}
LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
    isPause = false
}

ParticleView(
    isPause = isPause
    //...
)
```

### Config Change Handling
Config change handling has been introduced at [v1.4](https://github.com/iamoscarliang/particleview/releases/tag/1.4) (Currently Compose support only) to prevent state loss when UI recreation.
ParticleView use [kotlin-parcelize](https://developer.android.com/kotlin/parcelize#kts) to serialize data for state saving and restoring.
For large complex objects like bitmap, ParticleView use [Glide](https://github.com/bumptech/glide) to cache image resource.

<img src="https://github.com/iamoscarliang/particleview/blob/master/images/config_change_mechanism.gif" width="600">

## :tada: Samples
- [XML](https://github.com/iamoscarliang/particleview/tree/master/sample/xml/src/main)
- [Compose](https://github.com/iamoscarliang/particleview/tree/master/sample/compose/src/main)

## :balance_scale: Dependencies
- [org.jetbrains.kotlin.android](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.android)
- [org.jetbrains.kotlin.plugin.parcelize](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.parcelize)
- [androidx.compose.ui:ui](https://developer.android.com/jetpack/androidx/releases/compose-ui)
- [androidx.compose.foundation:foundation](https://developer.android.com/jetpack/androidx/releases/compose-foundation)
- [com.github.bumptech.glide:glide](https://github.com/bumptech/glide)
- [junit:junit](https://github.com/junit-team/junit4)
- [io.mockk:mockk](https://github.com/mockk/mockk)
