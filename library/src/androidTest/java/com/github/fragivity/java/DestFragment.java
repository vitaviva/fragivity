package com.github.fragivity.java;

import androidx.fragment.app.Fragment;

import com.github.fragivity.HomeFragmentKt;

public class DestFragment extends Fragment {

    public Boolean testArguments()  {
        return getArguments().get(null).equals(HomeFragmentKt.ARGUMENT);
    }
}
