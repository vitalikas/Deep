package lt.vitalijus.feature.auth.data.repository2.fakes

import lt.vitalijus.core.domain.logging.DeepLogger

class FakeDeepLogger2 : DeepLogger {

    override fun debug(message: String) {}
    override fun warn(message: String) {}
    override fun error(message: String, throwable: Throwable?) {}
}
