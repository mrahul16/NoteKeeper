package com.mrahul.notekeeper;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.support.test.espresso.Espresso;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {
    public static DataManager sDataManager;

    @BeforeClass
    public static void classSetUp() {
        sDataManager = DataManager.getInstance();
    }

    @Rule
    public ActivityTestRule<NoteListActivity> mNoteListActivityActivityTestRule =
            new ActivityTestRule<>(NoteListActivity.class);

    @Test
    public void createNewNote() {
        CourseInfo course = sDataManager.getCourse("java_lang");
        final String noteTitle = "Note Title";
        final String noteText = "This is note text";

        ViewInteraction fabNewNote = Espresso.onView(ViewMatchers.withId(R.id.fab));
        fabNewNote.perform(ViewActions.click());

        ViewInteraction spinnerCourses = Espresso.onView(ViewMatchers.withId(R.id.spinner_courses));
        spinnerCourses.perform(ViewActions.click());

        DataInteraction spinnerCoursesItem = Espresso.onData(
                Matchers.allOf(Matchers.instanceOf(CourseInfo.class), Matchers.equalTo(course)));
        spinnerCoursesItem.perform(ViewActions.click());

        spinnerCourses.check(ViewAssertions.matches(
                ViewMatchers.withSpinnerText(Matchers.containsString(course.getTitle()))));

        ViewInteraction textNoteTitle = Espresso.onView(ViewMatchers.withId(R.id.text_note_title));
        textNoteTitle.perform(ViewActions.typeText(noteTitle));
        // Don't do this, trust typeText()
        textNoteTitle.check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString(noteTitle))));

        ViewInteraction textNoteText = Espresso.onView(ViewMatchers.withId(R.id.text_note_text));
        textNoteText.perform(ViewActions.typeText(noteText),
                ViewActions.closeSoftKeyboard());
        // Don't do this, trust typeText()
        textNoteText.check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString(noteText))));

        Espresso.pressBack();

        List<NoteInfo> notes = sDataManager.getNotes();
        int noteIndex = notes.size() - 1;
        NoteInfo note = notes.get(noteIndex);
        assertEquals(course, note.getCourse());
        assertEquals(noteTitle, note.getTitle());
        assertEquals(noteText, note.getText());
    }
}