#!/usr/bin/env bash
# SPDX-License-Identifier: Apache-2.0
#
# Copyright 2023-2025 The Enola <https://enola.dev> Authors
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

set -euox pipefail

tools/maven/install.bash

# TODO https://github.com/jbangdev/jbang/issues/1921, due to https://github.com/enola-dev/enola/issues/1040
rm -f ~/.jbang/bin/jbang.jar && JBANG_DOWNLOAD_VERSION=0.122.0 learn/jbang/jbang learn/jbang/hello.java
