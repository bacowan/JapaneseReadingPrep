package PdfParsing

class ProgressReporter(val onProgressChange: (Double) -> Unit) {
    var progress: Progress = Progress()

    fun setAsRatio(numerator: Int, denominator: Int) {
        progress.numerator = numerator
        progress.denominator = denominator
        onProgressChange(progress.progress)
    }

    fun increment(count: Int = 1) {
        progress.numerator += count
        onProgressChange(progress.progress)
    }
}

class Progress {
    var numerator: Int = 0
    var denominator: Int = 1
    val progress: Double
        get() = if (denominator == 0) 100.toDouble()
                else 100 * numerator/denominator.toDouble()
}