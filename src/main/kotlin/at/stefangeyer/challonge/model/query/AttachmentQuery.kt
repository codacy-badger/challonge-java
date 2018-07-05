/*
 * Copyright 2017 Stefan Geyer <stefangeyer at outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.stefangeyer.challonge.model.query

import java.io.File
import java.io.IOException
import java.nio.file.Files

class AttachmentQuery(
        val asset: File? = null,
        val url: String? = null,
        val description: String? = null
) {

    /**
     * The asset's mime type or null if asset is null
     */
    val assetMimeType: String? by lazy {
        Files.probeContentType(asset?.toPath()) ?: null
    }
}
