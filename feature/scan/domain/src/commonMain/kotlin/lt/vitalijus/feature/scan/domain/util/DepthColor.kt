package lt.vitalijus.feature.scan.domain.util

/**
 * Predefined depth colors for bathymetry visualization.
 */
enum class DepthColor(
    val minDepth: Double,
    val maxDepth: Double?,
    val hexColor: String
) {

    SHALLOW(0.0, 1.0, "#FF0000"),      // 0–1m
    MEDIUM(1.0, 2.0, "#00FF00"),       // 1–2m
    DEEP(2.0, 4.0, "#0000FF"),         // 2–4m
    VERY_DEEP(4.0, null, "#800080");   // >4m

    companion object {
        fun fromDepth(depth: Double): DepthColor {
            return entries.first { color ->
                depth >= color.minDepth &&
                        (color.maxDepth == null || depth <= color.maxDepth)
            }
        }
    }
}