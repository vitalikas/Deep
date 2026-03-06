package lt.vitalijus.feature.scan.presentation.util

/**
 * Predefined depth colors for bathymetry visualization.
 */
enum class DepthColor(
    val minDepth: Double,
    val maxDepth: Double?,
    val hexColor: String
) {

    SHALLOW(0.0, 1.0, "#8B4513"),      // 0–1m: brown
    MEDIUM(1.0, 2.0, "#87CEEB"),       // 1–2m: light blue
    DEEP(2.0, 4.0, "#0000FF"),         // 2–4m: blue
    VERY_DEEP(4.0, null, "#00008B");   //  >4m: deep blue

    companion object {
        fun fromDepth(depth: Double): DepthColor {
            return entries.first { color ->
                depth >= color.minDepth &&
                        (color.maxDepth == null || depth <= color.maxDepth)
            }
        }
    }
}
