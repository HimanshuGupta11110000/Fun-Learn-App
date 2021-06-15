package com.example.android.roomwordssample

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class WordDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var noteDao: NoteDao
    private lateinit var db: NoteDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
   
        db = Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java)

            .allowMainThreadQueries()
            .build()
        noteDao = db.getNoteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetWord() = runBlocking {
        val word = Note("word")
        noteDao.insert(word)
        val allWords = noteDao.getAlphabetizedWords().first()
        assertEquals(allWords[0].word, word.word)
    }

    @Test
    @Throws(Exception::class)
    fun getAllWords() = runBlocking {
        val word = Note("aaa")
        noteDao.insert(word)
        val word2 = Note("bbb")
        noteDao.insert(word2)
        val allWords = noteDao.getAlphabetizedWords().first()
        assertEquals(allWords[0].word, word.word)
        assertEquals(allWords[1].word, word2.word)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() = runBlocking {
        val word = Note("word")
        noteDao.insert(word)
        val word2 = Note("word2")
        noteDao.insert(word2)
        noteDao.deleteAll()
        val allWords = noteDao.getAlphabetizedWords().first()
        assertTrue(allWords.isEmpty())
    }
}
