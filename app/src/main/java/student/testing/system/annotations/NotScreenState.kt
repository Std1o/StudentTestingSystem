package student.testing.system.annotations

@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(
    "\"Success\" should never be the whole state of the screen.\n" +
            "This is either a successful operation completion after which " +
            "the ViewModel state can be reset if necessary, or a successful data download, " +
            "which MUST BE SAVED in ContentState."
)
annotation class NotScreenState
