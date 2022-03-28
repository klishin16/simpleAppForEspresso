package ru.kkuzmichev.simpleappforespresso;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anyOf;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("ru.kkuzmichev.simpleappforespresso", appContext.getPackageName());
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);



    @Test
    public void homeTest() {
        ViewInteraction settingsButton = onView(
                withId(R.id.text_home)
        );
        settingsButton.check(
                matches(
                        withText("This is home fragment")
                )
        );
    }

    @Test
    public void testProgressBar() {
        ViewInteraction progressBar = onView(
                withId(R.id.progress_bar)
        );
        ViewInteraction drawer = onView(
                withId(R.id.drawer_layout)
        );
        ViewInteraction navigation = onView(
                withId(R.id.nav_view)
        );

        drawer.perform(DrawerActions.open());
        navigation.perform(NavigationViewActions.navigateTo(R.id.nav_gallery));
        progressBar.check(doesNotExist());
    }

    @Test
    public void testYandexIntent() {
        Intents.init();
        try {
            openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
            onView(anyOf(withText(R.string.action_settings), withId(R.id.action_settings))).perform(click());
            Intents.intended(hasData("https://google.com"));
        } catch (Exception ignored) {}
    }

    @Before
    public void registerIdlingResources() { //Подключаемся к “счетчику”
        IdlingRegistry.getInstance().register(CounterResource.idlingResource);
    }

    @After
    public void unregisterIdlingResources() { //Отключаемся от “счетчика”
        IdlingRegistry.getInstance().unregister(CounterResource.idlingResource);
    }

    @Test
    public void listElementsCountCheck() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_gallery)).perform(click());
        ViewInteraction recyclerView = onView(withId(R.id.recycle_view));
        recyclerView.check(matches(MyViewMatcher.itemsCount(10)));
    }

    @Test
    public void listElementsCountCheck2() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_gallery)).perform(click());
        ViewInteraction recyclerView = onView(withId(R.id.recycle_view));
        recyclerView.check(MyViewAssertion.hasItemCount(10));
    }
}