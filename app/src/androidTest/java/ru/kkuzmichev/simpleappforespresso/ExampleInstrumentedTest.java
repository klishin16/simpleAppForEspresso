package ru.kkuzmichev.simpleappforespresso;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.anyOf;
import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.content.Context;
import android.os.Environment;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Allure;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


@RunWith(AllureAndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("ru.kkuzmichev.simpleappforespresso", appContext.getPackageName());
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule
            .grant(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            );

    private void takeScreenshot(String name) {
        File path = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/screenshots/");
        if (!path.exists()) {
            path.mkdirs();
        }
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        String filename = name + ".png";
        device.takeScreenshot(new File(path, filename));
        try {
            Allure.attachment(filename, new FileInputStream(new File(path, filename)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Rule
    public TestWatcher watcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, org.junit.runner.Description description) {
            String className = description.getClassName();
            className = className.substring(className.lastIndexOf('.') + 1);
            String methodName = description.getMethodName();
            takeScreenshot(className + "#" + methodName);
        }
    };


    @Test
    public void homeTest() {
        Allure.description("Home fragment test");
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
        Allure.description("Progress bar test");
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
        Allure.description("Intent test");
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
        Allure.description("List count test");
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_gallery)).perform(click());
        ViewInteraction recyclerView = onView(withId(R.id.recycle_view));
        recyclerView.check(matches(MyViewMatcher.itemsCount(10)));
    }

    @Test
    public void listElementsCountCheck2() {
        Allure.description("List count test 2");
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_gallery)).perform(click());
        ViewInteraction recyclerView = onView(withId(R.id.recycle_view));
        recyclerView.check(MyViewAssertion.hasItemCount(10));
    }
}