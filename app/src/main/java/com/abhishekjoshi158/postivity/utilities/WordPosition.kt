package com.abhishekjoshi158.postivity.utilities


fun wordsToColor(sentence: String) : String {
    val words : MutableList<String> = getArrayOfWords(sentence).toMutableList()
    val requiredWords = getNoOFWordsToColor(words)
    val wordsPosition = getWordsPosition(words,requiredWords)
    wordsPosition.forEach{position ->
        words[position] = "<font color='#F2AA4C'>${words[position]}</font>";
    }

    return words.joinToString(" ")
}

fun getWordsPosition(words: List<String>, requiredWords: Int): ArrayList<Int>{
    val ignorePosition : ArrayList<Int> = arrayListOf()
    val preciousPosition : ArrayList<Int> = arrayListOf()

    repeat(requiredWords) {
        var bigLength = -1 ; var position = 0
        words.forEachIndexed { index, word ->
            val wLength = word.length
            if(wLength >= bigLength && !ignorePosition.contains(index)){
                bigLength = wLength
                position = index
            }
        }
        ignorePosition.add(position)
        ignorePosition.add(position+1)
        ignorePosition.add(position-1)
        preciousPosition.add(position)
    }

    return preciousPosition
}

fun getArrayOfWords( sentence: String ) : List<String>{
    val s  = sentence.trim()
    return s.split("\\s+".toRegex()).map { word ->
        word.replace("""^[,\.]|[,\.]$""".toRegex(), "")
    }
}

fun getNoOFWordsToColor(words: List<String>): Int{

    return when(words.size){
        in 0..5 -> 1
        in 6..10 -> 3
        in 11..18 -> 4
        in 19..30 -> 5
        in 31..45 -> 6
        else -> 7
    }

}