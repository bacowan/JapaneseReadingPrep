package Server

class WordFilter {
    fun Include(word: Word): Boolean {
        return !word.partsOfSpeech.contains(SYMBOL)
    }
}