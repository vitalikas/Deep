package lt.vitalijus.core.presentation.util

import deep.core.presentation.generated.resources.Res
import deep.core.presentation.generated.resources.error_bad_request
import deep.core.presentation.generated.resources.error_conflict
import deep.core.presentation.generated.resources.error_disk_full
import deep.core.presentation.generated.resources.error_empty_fields
import deep.core.presentation.generated.resources.error_forbidden
import deep.core.presentation.generated.resources.error_invalid_email
import deep.core.presentation.generated.resources.error_invalid_password
import deep.core.presentation.generated.resources.error_local_not_found
import deep.core.presentation.generated.resources.error_local_unknown
import deep.core.presentation.generated.resources.error_no_internet
import deep.core.presentation.generated.resources.error_not_found
import deep.core.presentation.generated.resources.error_payload_too_large
import deep.core.presentation.generated.resources.error_request_timeout
import deep.core.presentation.generated.resources.error_serialization
import deep.core.presentation.generated.resources.error_server_error
import deep.core.presentation.generated.resources.error_service_unavailable
import deep.core.presentation.generated.resources.error_too_many_requests
import deep.core.presentation.generated.resources.error_unauthorized
import deep.core.presentation.generated.resources.error_unknown
import lt.vitalijus.core.domain.util.DataError
import org.jetbrains.compose.resources.StringResource

/**
 * Extension function to convert DataError to StringResource.
 *
 * This is defined in core:presentation because:
 * - DataError is from core:domain
 * - Res (Compose resources) is from core:presentation
 * - All CMP feature modules depend on core:presentation
 *
 * Usage in any presentation layer:
 * ```kotlin
 * val errorMessage = UiText.Resource(error.toStringResource())
 * ```
 */
fun DataError.toStringResource(): StringResource {
    return when (this) {
        is DataError.Remote -> when (this) {
            DataError.Remote.UNAUTHORIZED -> Res.string.error_unauthorized
            DataError.Remote.BAD_REQUEST -> Res.string.error_bad_request
            DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
            DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
            DataError.Remote.SERVER_ERROR -> Res.string.error_server_error
            DataError.Remote.FORBIDDEN -> Res.string.error_forbidden
            DataError.Remote.NOT_FOUND -> Res.string.error_not_found
            DataError.Remote.CONFLICT -> Res.string.error_conflict
            DataError.Remote.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
            DataError.Remote.PAYLOAD_TOO_LARGE -> Res.string.error_payload_too_large
            DataError.Remote.SERVICE_UNAVAILABLE -> Res.string.error_service_unavailable
            DataError.Remote.SERIALIZATION -> Res.string.error_serialization
            DataError.Remote.UNKNOWN -> Res.string.error_unknown
        }

        is DataError.Local -> when (this) {
            DataError.Local.DISK_FULL -> Res.string.error_disk_full
            DataError.Local.NOT_FOUND -> Res.string.error_local_not_found
            DataError.Local.UNKNOWN -> Res.string.error_local_unknown
        }

        is DataError.Validation -> when (this) {
            DataError.Validation.INVALID_EMAIL -> Res.string.error_invalid_email
            DataError.Validation.EMPTY_FIELDS -> Res.string.error_empty_fields
            DataError.Validation.INVALID_PASSWORD -> Res.string.error_invalid_password
        }
    }
}
