/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.fragment.app;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static androidx.fragment.app.FragmentTransaction.OP_ADD;
import static androidx.fragment.app.ReportFragment.REAL_FRAGMENT;

@Navigator.Name("ignore")
public class MyFragmentNavigator extends FragmentNavigator {
    private static final String TAG = "FragivityNavigator";
    private static final String KEY_BACK_STACK_IDS = "myFragmentNavigator:backStackIds";

    private ArrayDeque<Integer> mBackStack = new ArrayDeque<>();
    private boolean mIsPendingAddToBackStackOperation = false;
    private boolean mIsPendingPopBackStackOperation = false;

    private final Context mContext;
    private final FragmentManager mFragmentManager;
    private final int mContainerId;

    public MyFragmentNavigator(@NonNull Context context, @NonNull FragmentManager manager,
                               int containerId) {
        super(context, manager, containerId);
        mContext = context;
        mFragmentManager = manager;
        mContainerId = containerId;
        mFragmentManager.addOnBackStackChangedListener(mOnBackStackChangedListener);
    }

    @NonNull
    @Override
    public Fragment instantiateFragment(@NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull String className, @Nullable Bundle args) {
        Fragment fragment = super.instantiateFragment(context, fragmentManager, "androidx.fragment.app.ReportFragment", args);
        if (fragment instanceof ReportFragment) {
            Bundle bundle = new Bundle();
            bundle.putString(REAL_FRAGMENT, className);
            if (args != null) bundle.putAll(args);
            fragment.setArguments(bundle);
        } else {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Nullable
    @Override
    public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args,
                                   @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {
        if (mFragmentManager.isStateSaved()) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already"
                    + " saved its state");
            return null;
        }
        String className = destination.getClassName();
        if (className.charAt(0) == '.') {
            className = mContext.getPackageName() + className;
        }
        final Fragment frag = instantiateFragment(mContext, mFragmentManager,
                className, args);
//        frag.setArguments(args);
        final FragmentTransaction ft = mFragmentManager.beginTransaction();

        int enterAnim = navOptions != null ? navOptions.getEnterAnim() : -1;
        int exitAnim = navOptions != null ? navOptions.getExitAnim() : -1;
        int popEnterAnim = navOptions != null ? navOptions.getPopEnterAnim() : -1;
        int popExitAnim = navOptions != null ? navOptions.getPopExitAnim() : -1;
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = enterAnim != -1 ? enterAnim : 0;
            exitAnim = exitAnim != -1 ? exitAnim : 0;
            popEnterAnim = popEnterAnim != -1 ? popEnterAnim : 0;
            popExitAnim = popExitAnim != -1 ? popExitAnim : 0;
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
        }

//        ft.replace(mContainerId, frag);
        ft.add(mContainerId, frag, generateBackStackName(mBackStack.size(), destination.getId()));

        final Fragment preFrag = mFragmentManager.getPrimaryNavigationFragment();
        ft.setPrimaryNavigationFragment(frag);

        final @IdRes int destId = destination.getId();
        final boolean initialNavigation = mBackStack.isEmpty();
        // TODO Build first class singleTop behavior for fragments
        final boolean isSingleTopReplacement = navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.peekLast() == destId;

        boolean isAdded;
        if (initialNavigation) {
            isAdded = true;
        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (mBackStack.size() > 1) {
//                // If the Fragment to be replaced is on the FragmentManager's
//                // back stack, a simple replace() isn't enough so we
//                // remove it from the back stack and put our replacement
//                // on the back stack in its place
//                mFragmentManager.popBackStack(
//                        generateBackStackName(mBackStack.size(), mBackStack.peekLast()),
//                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                ft.addToBackStack(generateBackStackName(mBackStack.size(), destId));


                // Don't use mFragmentManager.removeFragment(fragment) because of animation
//                mFragmentManager.removeFragment(preFrag);
                ft.remove(preFrag);
                frag.mTag = generateBackStackName(mBackStack.size() - 1, destination.getId());
                if (mFragmentManager.mBackStack.size() > 0) {
                    List<FragmentTransaction.Op> ops =
                            mFragmentManager.mBackStack.get(mFragmentManager.mBackStack.size() - 1).mOps;
                    for (FragmentTransaction.Op op : ops) {
                        if (op.mCmd == OP_ADD && op.mFragment == preFrag) {
                            op.mFragment = frag;
                        }
                    }
                }
            }
            isAdded = false;
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size() + 1, destId));
            mIsPendingAddToBackStackOperation = true;
            isAdded = true;
        }

        if (isAdded) {
            if (preFrag != null) {
                ft.hide(preFrag);
            }
        }

        if (navigatorExtras instanceof Extras) {
            Extras extras = (Extras) navigatorExtras;
            for (Map.Entry<View, String> sharedElement : extras.getSharedElements().entrySet()) {
                ft.addSharedElement(sharedElement.getKey(), sharedElement.getValue());
            }
        }
        ft.setReorderingAllowed(true);
        ft.commit();

        // The commit succeeded, update our view of the world
        if (isAdded) {
            mBackStack.add(destId);
            return destination;
        } else {
            return null;
        }
    }


    @NonNull
    private String generateBackStackName(int backStackIndex, int destId) {
        return backStackIndex + "-" + destId;
    }

    @Override
    public boolean popBackStack() {
        if (mBackStack.isEmpty()) {
            return false;
        }
        if (mFragmentManager.isStateSaved()) {
            Log.i(TAG, "Ignoring popBackStack() call: FragmentManager has already"
                    + " saved its state");
            return false;
        }

        if (mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStack(
                    generateBackStackName(mBackStack.size(), mBackStack.peekLast()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
            mIsPendingPopBackStackOperation = true;
        } // else, we're on the first Fragment, so there's nothing to pop from FragmentManager
        mBackStack.removeLast();

        return true;

    }


    private final FragmentManager.OnBackStackChangedListener mOnBackStackChangedListener =
            new FragmentManager.OnBackStackChangedListener() {

                @Override
                public void onBackStackChanged() {
                    // If we have pending operations made by us then consume this change, otherwise
                    // detect a pop in the back stack to dispatch callback.
                    if (mIsPendingAddToBackStackOperation) {
                        mIsPendingAddToBackStackOperation = !isBackStackEqual();

                        if (mFragmentManager.getFragments().size() > 1) {
                            // 切到后台时的生命周期
                            Fragment fragment = mFragmentManager.getFragments().get(mFragmentManager.getFragments().size() - 2);
                            if (fragment instanceof ReportFragment) {
                                fragment.performStop();
                                ((ReportFragment) fragment).setInForeground(false);
                            }
                        }
                    } else if (mIsPendingPopBackStackOperation) {
                        mIsPendingPopBackStackOperation = !isBackStackEqual();
                        // 回到前台时的生命周期
                        Fragment fragment = mFragmentManager.getPrimaryNavigationFragment();
                        if (fragment instanceof ReportFragment) {
                            ((ReportFragment) fragment).setInForeground(true);
                            fragment.performResume();
                        }
                    }
                }
            };


    /**
     * Checks if this FragmentNavigator's back stack is equal to the FragmentManager's back stack.
     */
    @SuppressWarnings("WeakerAccess") /* synthetic access */
    boolean isBackStackEqual() {
        int fragmentBackStackCount = mFragmentManager.getBackStackEntryCount();
        // Initial fragment won't be on the FragmentManager's back stack so +1 its count.
        if (mBackStack.size() != fragmentBackStackCount + 1) {
            return false;
        }

        // From top to bottom verify destination ids match in both back stacks/
        Iterator<Integer> backStackIterator = mBackStack.descendingIterator();
        int fragmentBackStackIndex = fragmentBackStackCount - 1;
        while (backStackIterator.hasNext() && fragmentBackStackIndex >= 0) {
            int destId = backStackIterator.next();
            try {
                int fragmentDestId = getDestId(mFragmentManager
                        .getBackStackEntryAt(fragmentBackStackIndex--)
                        .getName());
                if (destId != fragmentDestId) {
                    return false;
                }
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid back stack entry on the "
                        + "NavHostFragment's back stack - use getChildFragmentManager() "
                        + "if you need to do custom FragmentTransactions from within "
                        + "Fragments created via your navigation graph.");
            }
        }

        return true;
    }


    private int getDestId(@Nullable String backStackName) {
        String[] split = backStackName != null ? backStackName.split("-") : new String[0];
        if (split.length != 2) {
            throw new IllegalStateException("Invalid back stack entry on the "
                    + "NavHostFragment's back stack - use getChildFragmentManager() "
                    + "if you need to do custom FragmentTransactions from within "
                    + "Fragments created via your navigation graph.");
        }
        try {
            // Just make sure the backStackIndex is correctly formatted
            Integer.parseInt(split[0]);
            return Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid back stack entry on the "
                    + "NavHostFragment's back stack - use getChildFragmentManager() "
                    + "if you need to do custom FragmentTransactions from within "
                    + "Fragments created via your navigation graph.");
        }
    }

    @Override
    @Nullable
    public Bundle onSaveState() {
        Bundle b = new Bundle();
        int[] backStack = new int[mBackStack.size()];
        int index = 0;
        for (Integer id : mBackStack) {
            backStack[index++] = id;
        }
        b.putIntArray(KEY_BACK_STACK_IDS, backStack);
        return b;
    }

    @Override
    public void onRestoreState(@Nullable Bundle savedState) {
        if (savedState != null) {
            int[] backStack = savedState.getIntArray(KEY_BACK_STACK_IDS);
            if (backStack != null) {
                mBackStack.clear();
                for (int destId : backStack) {
                    mBackStack.add(destId);
                }
            }
        }
    }
}
