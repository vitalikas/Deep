package lt.vitalijus.core.designsystem.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import deep.core.designsystem.generated.resources.Res
import deep.core.designsystem.generated.resources.arrow_left_icon
import deep.core.designsystem.generated.resources.check_icon
import deep.core.designsystem.generated.resources.cloud_off_icon
import deep.core.designsystem.generated.resources.dots_icon
import deep.core.designsystem.generated.resources.eye_icon
import deep.core.designsystem.generated.resources.eye_off_icon
import deep.core.designsystem.generated.resources.loading_icon
import deep.core.designsystem.generated.resources.log_out_icon
import deep.core.designsystem.generated.resources.plus_icon
import deep.core.designsystem.generated.resources.reload_icon
import deep.core.designsystem.generated.resources.settings_icon
import deep.core.designsystem.generated.resources.success_checkmark
import deep.core.designsystem.generated.resources.upload_icon
import deep.core.designsystem.generated.resources.users_icon
import org.jetbrains.compose.resources.painterResource

object DeepIcons {
    val eyeIcon: Painter
        @Composable
        get() = painterResource(Res.drawable.eye_icon)

    val eyeOffIcon: Painter
        @Composable
        get() = painterResource(Res.drawable.eye_off_icon)

    val arrowLeftIcon: Painter
        @Composable
        get() = painterResource(Res.drawable.arrow_left_icon)

    val checkIcon: Painter
        @Composable
        get() = painterResource(Res.drawable.check_icon)

    val cloudOffIcon: Painter
        @Composable
        get() = painterResource(Res.drawable.cloud_off_icon)

    val dotsIcon: Painter
        @Composable
        get() = painterResource(Res.drawable.dots_icon)

    val loadingIcon: Painter
        @Composable
        get() = painterResource(Res.drawable.loading_icon)

    val logOutIcon: Painter
        @Composable
        get() = painterResource(Res.drawable.log_out_icon)

    val plusIcon: Painter
        @Composable
        get() = painterResource(Res.drawable.plus_icon)

    val reloadIcon: Painter
        @Composable
        get() = painterResource(Res.drawable.reload_icon)

    val settingsIcon: Painter
        @Composable
        get() = painterResource(Res.drawable.settings_icon)

    val successCheckmark: Painter
        @Composable
        get() = painterResource(Res.drawable.success_checkmark)

    val uploadIcon: Painter
        @Composable
        get() = painterResource(Res.drawable.upload_icon)

    val usersIcon: Painter
        @Composable
        get() = painterResource(Res.drawable.users_icon)
}
