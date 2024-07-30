package com.pproject.sharednotes.data.test

import com.pproject.sharednotes.data.entity.Note

var testnotes: List<Note> = listOf(
    Note(0, "Prueba0 de nombre largo para ver cómo se gestiona la movida", "Esto es la prueba 0", false, Note.Situation.ON_USE, 1, listOf<String>("pablo")),
    Note(1, "Prueba1", "Esto es la prueba 1", false, Note.Situation.ON_USE, 1,listOf<String>("pedro", "pablo")),
    Note(2, "Prueba2", "Esto es la prueba 2, especialmente larga para probar como queda así una grandecita", true, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(3, "Prueba3", "Esto es la prueba 3", true, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(4, "Prueba4", "Esto es la prueba 4", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(5, "Prueba5", "Esto es la prueba 5, enumeracion\n\n- Cosa 1\n- Cosa 2\n- Cosa 3\n -Cosa 4", true, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(6, "Prueba6", "Esto es la prueba 6", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(7, "Prueba7", "Esto es la prueba 7 bueno otra grande porque no se como quedará si es demasiado grande que viva vox y la clase obrera", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(8, "Prueba8", "Esto es la prueba 8", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(9, "Prueba9", "Esto es la prueba 9", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(10, "Prueba10", "Esto es la prueba 10", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(11, "Prueba11", "Esto es la prueba 11", true, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(12, "Prueba12", "Esto es la prueba 12", true, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(13, "Prueba13", "Esto es la prueba 13", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(14, "Prueba14", "Esto es la prueba 14", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(15, "Prueba15", "Esto es la prueba 15", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(16, "Prueba16", "Esto es la prueba 16", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(17, "Prueba17 de titulo medio largo a ver", "Esto es la prueba 17", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(18, "Prueba18", "Esto es la prueba 18", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(19, "Prueba19", "Esto es la prueba 19", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(20, "Prueba20", "Esto es la prueba 20", true, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(21, "Prueba21 largo y pinned para ver cómo queda", "Esto es la prueba 21", true, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(22, "Prueba22", "Esto es la prueba 22", true, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(23, "Prueba23", "Esto es la prueba 23", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(24, "Prueba24", "Esto es la prueba 24", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(25, "Prueba25", "Esto es la prueba 25", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(26, "Prueba26 titulo medio", "Esto es la prueba 26", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(27, "Prueba27", "Esto es la prueba 27", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(28, "Prueba28", "Esto es la prueba 28", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(29, "Prueba29", "Esto es la prueba 29", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(30, "Prueba30", "Esto es la prueba 30", true, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(31, "Prueba31", "Esto es la prueba 31", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(32, "Prueba32", "Esto es la prueba 32", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(33, "Prueba33", "Esto es la prueba 33", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(34, "Prueba34", "Esto es la prueba 34", true, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(35, "Prueba35", "Esto es la prueba 35", true, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(36, "Prueba36 titulo extremadamente largo para ver como funciona llegando al límite, espero que de verdad permita que sea muy largo porque la vdd me hace falta", "Esto es la prueba 36", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(37, "Prueba37", "Esto es la prueba 37", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(38, "Prueba38", "Esto es la prueba 38", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(39, "Prueba39", "Esto es la prueba 39", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
)

fun getAllNotes(): List<Note> {
    return testnotes
}

fun addNote(newNote: Note) {
    testnotes = testnotes.plus(newNote)
}

fun getNote(id: Int): Note? {
    for (note in getAllNotes()) {
        if (note.id == id) {
            return note
        }
    }
    return null
}

fun getAllNoteIds(): List<Int> {
    val result: MutableList<Int> = mutableListOf()
    for (note in getAllNotes()) {
        result.add(note.id)
    }
    return result.toList()
}