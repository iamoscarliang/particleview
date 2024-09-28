<div align="center">
<img src="https://github.com/iamoscarliang/particleview/blob/master/images/showcase.gif" width="600">

[![Build](https://github.com/iamoscarliang/particleview/workflows/Build/badge.svg)](https://github.com/iamoscarliang/particleview/actions)
[![Version](https://jitpack.io/v/iamoscarliang/particleview.svg)](https://jitpack.io/#iamoscarliang/particleview)
![minSdk](https://img.shields.io/badge/minSdk-26-brightgreen)
[![license](https://img.shields.io/badge/license-MIT-brightgreen)](https://github.com/iamoscarliang/particleview/blob/master/LICENSE)

[Getting Started](#wrench-getting-started) • [How To Use](#computer-how-to-use) • [Features](#pushpin-features) • [Samples](#tada-samples) • [License](#balance_scale-license)

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
    duration = 5000
)
```

Compose
```kotlin
ParticleView(
    modifier = Modifier.fillMaxSize(),
    images = listOf(
        Image(
            imageId = R.drawable.particle,
            size = 30
        ),
    ),
    startX = FloatOffset(0.0f, 1.0f),
    startY = FloatOffset(0.0f, 1.0f),
    angle = IntOffset(-30, 30),
    speed = FloatOffset(300.0f, 600.0f),
    particlePerSecond = 20,
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
- `isRunning` - controls the current state of the animation
- `onParticleClick` - callback being executed when any particle is clicked
- `onAnimationEnd` - callback being executed when end of the animation

## :pushpin: Features

### Recycling
Recycling mechanism has been added since [v1.2](https://github.com/iamoscarliang/particleview/releases/tag/1%2C2) to improve performance.
ParticleView use a pre-created object pool to store unused particles, when a particle finishing emitting, it will be added back to the pool and being reused next time.
The mechanism can decrease the creation of particles, improving rendering efficiency, and preventing memory leak.

<img src="https://github.com/iamoscarliang/particleview/blob/master/images/recycle_mechanism" width="600">

## :tada: Samples
[XML](https://github.com/iamoscarliang/particleview/tree/master/sample/xml/src/main)

[Compose](https://github.com/iamoscarliang/particleview/tree/master/sample/compose/src/main)

## :balance_scale: License
ParticleView is licensed under the [MIT license](https://github.com/iamoscarliang/particleview/blob/master/LICENSE)
