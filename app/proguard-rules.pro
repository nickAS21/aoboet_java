# Зберегти CoreComponentFactory
-keep class androidx.core.app.CoreComponentFactory { *; }

# Зберегти всі класи з androidx.core
-keep class androidx.core.** { *; }

# Якщо є MultiDex
-keep class androidx.multidex.** { *; }

# Аннотації та мета-дані
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes Exceptions

# Зберегти всі Android компоненти
-keep public class * extends android.app.Application
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
