package ru.kkuzmichev.simpleappforespresso;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class MyViewMatcher {
    public static Matcher<View> itemsCount(int count) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("RecyclerView with item count: $count");
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                if (view.getAdapter() != null) {
                    return view.getAdapter().getItemCount() == count;
                }

                return false;
            }
        };
    }
}
