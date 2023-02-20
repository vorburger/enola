# https://rules-proto-grpc.com/en/latest/#installation
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "rules_proto_grpc",
    sha256 = "fb7fc7a3c19a92b2f15ed7c4ffb2983e956625c1436f57a3430b897ba9864059",
    strip_prefix = "rules_proto_grpc-4.3.0",
    urls = ["https://github.com/rules-proto-grpc/rules_proto_grpc/archive/4.3.0.tar.gz"],
)

load("@rules_proto_grpc//:repositories.bzl", "rules_proto_grpc_repos", "rules_proto_grpc_toolchains")

rules_proto_grpc_toolchains()

rules_proto_grpc_repos()

load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")

rules_proto_dependencies()

rules_proto_toolchains()

# https://rules-proto-grpc.com/en/latest/lang/doc.html#doc-markdown-compile
load("@rules_proto_grpc//doc:repositories.bzl", rules_proto_grpc_doc_repos = "doc_repos")

rules_proto_grpc_doc_repos()

# https://rules-proto-grpc.com/en/latest/lang/buf.html#buf-proto-lint-test
load("@rules_proto_grpc//buf:repositories.bzl", rules_proto_grpc_buf_repos = "buf_repos")

rules_proto_grpc_buf_repos()

# https://rules-proto-grpc.com/en/latest/lang/java.html#java-grpc-library
load("@rules_proto_grpc//java:repositories.bzl", rules_proto_grpc_java_repos = "java_repos")

rules_proto_grpc_java_repos()

load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@io_grpc_grpc_java//:repositories.bzl", "IO_GRPC_GRPC_JAVA_ARTIFACTS", "IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS", "grpc_java_repositories")

maven_install(
    artifacts = IO_GRPC_GRPC_JAVA_ARTIFACTS,
    generate_compat_repositories = True,
    override_targets = IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS,
    repositories = [
        "https://repo.maven.apache.org/maven2/",
    ],
)

load("@maven//:compat.bzl", "compat_repositories")

compat_repositories()

grpc_java_repositories()

# https://github.com/tweag/rules_nixpkgs
http_archive(
    name = "io_tweag_rules_nixpkgs",
    strip_prefix = "rules_nixpkgs-691582fd6712502b38e7ef6a488203edc79143b8",
    urls = ["https://github.com/tweag/rules_nixpkgs/archive/691582fd6712502b38e7ef6a488203edc79143b8.tar.gz"],
)

load("@io_tweag_rules_nixpkgs//nixpkgs:repositories.bzl", "rules_nixpkgs_dependencies")

rules_nixpkgs_dependencies()

# TODO "nixpkgs_cc_configure" ?
load("@io_tweag_rules_nixpkgs//nixpkgs:nixpkgs.bzl", "nixpkgs_git_repository", "nixpkgs_package")

nixpkgs_git_repository(
    name = "nixpkgs",
    remote = "https://github.com/NixOS/nixpkgs",
    revision = "22.11",  # Any tag or commit hash
    sha256 = "TODO",
)

# TODO How to use Nix hello in a https://bazel.build/reference/be/shell ?
nixpkgs_package(
    name = "hello",
    # repository = "@nixpkgs",
    repositories = {"nixpkgs": "@nixpkgs//:default.nix"},
)

nixpkgs_package(
    name = "shellcheck",
    # repository = "@nixpkgs",
    repositories = {"nixpkgs": "@nixpkgs//:default.nix"},
)

# https://github.com/andyscott/misc_rules
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

git_repository(
    name = "misc_rules",
    commit = "0bc662f896bf0e3841a13864482b6e4f4e6eca90",
    remote = "git://github.com/andyscott/misc_rules",
)

register_toolchains(
    "@misc_rules//toolchains/shellcheck:shellcheck_from_nixpkgs",
)

# TODO https://github.com/andyscott/misc_rules/issues/15
