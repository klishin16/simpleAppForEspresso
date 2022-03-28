package ru.kkuzmichev.simpleappforespresso;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.CoreMatchers;

public class MyViewAssertion {
    public static ViewAssertion hasItemCount(int count) {
        return (view, noViewFoundException) -> {
            if (noViewFoundException == null) {
                if (view instanceof RecyclerView) {
                    try {
                        RecyclerView recyclerView = (RecyclerView) view;
                        if (recyclerView.getAdapter() != null) {
                            ViewMatchers.assertThat("Items count", recyclerView.getAdapter().getItemCount(), CoreMatchers.equalTo(count));
                        }
                    } catch (ClassCastException cce) {
                        throw new IllegalStateException("This is not a RecyclerView");
                    }

                }
            }
        };
    }
}
