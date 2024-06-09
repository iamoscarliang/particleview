<div align="center">
<img src="https://github.com/iamoscarliang/particleview/blob/master/screenshots/feature_graph.gif" width="600">

[![Build](https://github.com/iamoscarliang/particleview/workflows/Build/badge.svg)](https://github.com/iamoscarliang/particleview/actions)
[![Version](https://jitpack.io/v/iamoscarliang/particleview.svg)](https://jitpack.io/#iamoscarliang/particleview)
![minSdk](https://img.shields.io/badge/minSdk-26-brightgreen)
[![license](https://img.shields.io/badge/license-MIT-brightgreen)](https://github.com/iamoscarliang/particleview/blob/master/LICENSE)

[Getting Started](#computer-getting-started) • [How To Use](#wrench-how-to-use) • [Samples](#tada-sample) • [License](#balance_scale-license)

**Lightweight particle effect animation for Android**
</div>

---

## :computer: Getting Started
Add the jitpack
```groovy
maven { url 'https://jitpack.io' }
```
Add the dependency
```groovy
dependencies {
    implementation 'com.github.iamoscarliang:particleview:<version>'
}
```

## :wrench: How To Use

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
            size = 40
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


## :tada: Samples
[XML](https://github.com/iamoscarliang/particleview/tree/master/sample/xml/src/main)

[Compose](https://github.com/iamoscarliang/particleview/tree/master/sample/compose/src/main)

## :balance_scale: License
 ParticleView is licensed under the [MIT license](https://github.com/iamoscarliang/particleview/blob/master/LICENSE)
