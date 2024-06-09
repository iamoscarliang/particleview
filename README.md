<div align="center">
<img src="https://github.com/iamoscarliang/particleview/blob/master/screenshots/feature_graph.gif" width="600">

[![Build](https://github.com/iamoscarliang/particleview/workflows/Build/badge.svg)](https://github.com/iamoscarliang/particleview/actions)
[![Version](https://jitpack.io/v/iamoscarliang/particleview.svg)](https://jitpack.io/#iamoscarliang/particleview)
![minSdk](https://img.shields.io/badge/minSdk-26-brightgreen)
[![license](https://img.shields.io/badge/license-MIT-brightgreen)](https://github.com/iamoscarliang/particleview/blob/master/LICENSE)

[Getting Started](#wrench-getting-started) • [How To Use](#computer-how-to-use) • [Samples](#tada-samples) • [License](#balance_scale-license)

**A lightweight particle effect animation for Android**
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
    implementation 'com.github.iamoscarliang:particleview:xml:<version>'
    // Compose
    implementation 'com.github.iamoscarliang:particleview:compose:<version>'
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
    countPerSecond = 20,
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
    countPerSecond = 20,
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
- `countPerSecond` - the count of particle emit per second
- `duration` - the duration of the animation
- `fadeOutDuration` - the fade out duration at the end of animation
- `fadeOutEnable` - controls the fade out effect at the end of animation. When false, the particle will disappear immediately and the fadeOutDuration has no effect
- `isRunning` - controls the current state of the animation. When false, the animation will jump to the end and play the fade out effect dependent on the fadeOutEnable is true or not
- `onParticleClickListener` - callback being executed when any particle is clicked
- `onAnimationEndListener` - callback being executed when end of the animation

## :tada: Samples
[XML](https://github.com/iamoscarliang/particleview/tree/master/sample/xml/src/main)

[Compose](https://github.com/iamoscarliang/particleview/tree/master/sample/compose/src/main)

## :balance_scale: License
 ParticleView is licensed under the [MIT license](https://github.com/iamoscarliang/particleview/blob/master/LICENSE)
