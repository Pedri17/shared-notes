package com.pproject.sharednotes.data.test

import com.pproject.sharednotes.data.entity.Note

var testnotes: List<Note> = listOf(
    Note(0, "Prueba0", "Esto es la prueba 0", false, Note.Situation.ON_USE, 1, listOf<String>("pablo")),
    Note(1, "Prueba1", "Esto es la prueba 1", false, Note.Situation.ON_USE, 1,listOf<String>("pedro", "pablo")),
    Note(2, "Prueba2", "Esto es la prueba 2, especialmente larga para probar como queda así una grandecita", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(3, "Prueba3", "Esto es la prueba 3", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(4, "Prueba4", "Esto es la prueba 4", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(5, "Prueba5", "Esto es la prueba 5, enumeracion\n\n- Cosa 1\n- Cosa 2\n- Cosa 3\n -Cosa 4", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(6, "Prueba6", "Esto es la prueba 6", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(7, "Prueba7", "Esto es la prueba 7 bueno otra grande porque no se como quedará si es demasiado grande que viva vox y la clase obrera", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(8, "Prueba8", "Esto es la prueba 8", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(9, "Prueba9", "Esto es la prueba 9", false, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(10, "Prueba10", "Esto es la prueba 10", true, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(11, "Prueba11", "Esto es la prueba 11", true, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(12, "Prueba12", "Esto es la prueba 12", true, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
    Note(13, "Prueba13", "Esto es la prueba 13", true, Note.Situation.ON_USE, 1, listOf<String>("pedro", "pablo")),
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
    var result: List<Int> = emptyList()
    for (note in getAllNotes()) {
        result = result.plus(note.id)
    }
    return result
}