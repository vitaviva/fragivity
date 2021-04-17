package com.github.fragivity.java;

import static com.github.fragivity.Fragivity.Navigator.navOptionsBuilder;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.fragivity.Fragivity;
import com.github.fragivity.HomeFragmentKt;
import com.github.fragivity.LaunchMode;
import com.github.fragivity.swipeback.SwipeBackUtil;

public class HomeFragment extends Fragment {

    public Boolean testArgument() {
        return getArguments().get(null).equals(HomeFragmentKt.ARGUMENT);
    }

    public void testPush() {
        Fragivity.of(this).push(DestFragment.class);
    }

    public void testPushWithOptions() {
        Bundle bundle = new Bundle();
        bundle.putString(null, HomeFragmentKt.ARGUMENT);
        Fragivity.of(this).push(DestFragment.class,
                navOptionsBuilder()
                        .setArguments(bundle)
                        .setLaunchMode(LaunchMode.STANDARD)
                        .build());
    }

    public void testPushWithFactory() {
        Fragivity.of(this).push(DestFragment.class, (Bundle) -> new DestFragment()
                , navOptionsBuilder().build());
    }


    public void testShowDialog() {
        Bundle bundle = new Bundle();
        bundle.putString(null, HomeFragmentKt.ARGUMENT);
        Fragivity.of(this).showDialog(DialogFragment.class, bundle);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SwipeBackUtil.setEnableGesture(this, true);
    }
}
