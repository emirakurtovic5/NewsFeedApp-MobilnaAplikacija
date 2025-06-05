package etf.ri.rma.newsfeedapp.dto

import com.google.gson.annotations.SerializedName

data class ImageTagResponse(
    @SerializedName("result") val result: ImageTagResultDTO
)

data class ImageTagResultDTO(
    @SerializedName("tags") val tags: List<ImageTagDTO>
)

data class ImageTagDTO(
    @SerializedName("tag") val tag: TagDetailDTO
)

data class TagDetailDTO(
    @SerializedName("en") val en: String
)

fun ImageTagResponse.toTagList(): List<String> {
    return this.result.tags.map { it.tag.en }
}