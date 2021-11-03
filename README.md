MissingPermission inconsistency bug reproduction project
========================================================

This project demonstrates an issue with inconsistent behavior with lint reporting the `MissingPermission` issue.

Summary
-------
A submodule calls a function which requires a dangerous permission, without checking first if the permission is granted.

`./gradlew :app:lintDebug` produces inconsistent results: sometimes it reports `MissingPermission`, sometimes it doesn't, depending on if/where a `<uses-permission>` is declared in a manifest file.

`./gradlew lintDebug` always reports `MissingPermission`.

Details
-------
The project has a submodule, `mymodule`, which calls code which requires a dangerous permission (`RECORD_AUDIO`), but doesn't check for the permission:

```kotlin
object MyClass {
    fun foo(
    ) {
        fooImpl()
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun fooImpl() {
    }
}
```

The implementation of `foo()` should check if the app has the `RECORD_AUDIO` permission, before calling `fooImpl()`, which requires this permission.

The `app` module has configured lint to check dependencies:
```gradle
lintOptions {
    checkDependencies true
}
```

When running `./gradlew lintDebug`, lint reports a `MissingPermission` check, as expected.

When running `./gradlew :app:lintDebug`, lint only sometimes reports the `MissingPermission` check.  It depends on if and where the permission is declared in an `AndroidManifest.xml` file as follows:
```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```

If this permission is declared **only** in the **app** manifest, then `./gradlew :app:lintDebug` fails to report the `MissingPermission` issue.

In all the other combinations of this permission being declared, or not, in the **app**  versus **submodule**, `./gradlew :app:lintDebug` reports the issue.

`./gradlew lintDebug` always reports the issue.

Another way to represent this is in table format:

**Expected behavior**:
|permission in app manifest|permission in module manifest|`./gradlew lintDebug`|`./gradlew :app:lintDebug`|
|---|---|---|---|
|❌|❌|Finds issue|Finds issue|
|✅|❌|Finds issue|Finds issue|
|❌|✅|Finds issue|Finds issue|
|✅|✅|Finds issue|Finds issue|

**Actual behavior**:
|permission in app manifest|permission in module manifest|`./gradlew lintDebug`|`./gradlew :app:lintDebug`|
|---|---|---|---|
|❌|❌|Finds issue|Finds issue|
|✅|❌|Finds issue|**No issue** 👈|
|❌|✅|Finds issue|Finds issue|
|✅|✅|Finds issue|Finds issue|
