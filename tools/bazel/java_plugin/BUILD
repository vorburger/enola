load("@rules_java//java:defs.bzl", "java_plugin")

java_plugin(
    name = "autovalue",
    processor_class = "com.google.auto.value.processor.AutoValueProcessor",
    visibility = ["//:__subpackages__"],
    deps = ["@maven//:com_google_auto_value_auto_value"],
)

java_plugin(
    name = "autoservice",
    processor_class = "com.google.auto.service.processor.AutoServiceProcessor",
    visibility = ["//:__subpackages__"],
    deps = ["@maven//:com_google_auto_service_auto_service"],
)
