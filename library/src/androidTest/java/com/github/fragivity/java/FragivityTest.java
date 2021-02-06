package com.github.fragivity.java;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.fragment.app.ReportFragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.github.fragivity.Fragivity;
import com.github.fragivity.FragivityUtil;
import com.github.fragivity.R;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import static com.github.fragivity.HomeFragmentKt.ARGUMENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class FragivityTest {

    static Context appContext;
    FragmentScenario<NavHostFragment> scenario;

    @BeforeClass
    public static void setUpOnce() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.github.fragivity.test", appContext.getPackageName());
    }

    @Before
    public void setUp() {
        scenario = FragmentScenario.launchInContainer(NavHostFragment.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void loadRoot() {
        scenario.onFragment(fragment -> {
            Fragivity.loadRoot(fragment, HomeFragment.class);
            assertEquals(Objects.requireNonNull(fragment.getNavController().getCurrentDestination()).getId(),
                    HomeFragment.class.hashCode());
        });

    }

    @Test
    public void loadRootWithFactory() {

        scenario.onFragment(host ->
                Fragivity.loadRoot(host, HomeFragment.class, () -> {
                    HomeFragment fragment = new HomeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(null, ARGUMENT);
                    fragment.setArguments(bundle);
                    fragment.getViewLifecycleOwnerLiveData().observeForever(lifecycleOwner -> {
                        assertEquals(
                                host.getChildFragmentManager().getFragments().get(0).//ReportFragment
                                        getChildFragmentManager().getFragments().get(0),//real
                                fragment);
                        assertTrue(fragment.testArgument());
                    });
                    return fragment;
                }));

    }


    @Test
    public void push() {
        scenario.onFragment(host -> {
            Fragivity.loadRoot(host, HomeFragment.class, () -> {
                HomeFragment fragment = new HomeFragment();
                fragment.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
                    if (event == Lifecycle.Event.ON_START) {
                        fragment.testPush();
                        assertEquals(host.getNavController().getCurrentDestination().getId(),
                                DestFragment.class.hashCode());
                    }
                });
                return fragment;
            });
        });
    }


    @Test
    public void pushWithFactory() {
        scenario.onFragment(host -> {
            Fragivity.loadRoot(host, HomeFragment.class, () -> {
                HomeFragment home = new HomeFragment();
                home.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
                    if (event == Lifecycle.Event.ON_START) {
                        home.testPushWithFactory();
                        assertEquals(host.getNavController().getCurrentDestination().getId(),
                                DestFragment.class.hashCode());
                    }
                });
                return home;
            });
        });
    }

    @Test
    public void pushWithOptions() {
        scenario.onFragment(host -> {
            Fragivity.loadRoot(host, HomeFragment.class, () -> {
                HomeFragment home = new HomeFragment();
                home.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
                    if (event == Lifecycle.Event.ON_START) {
                        home.testPushWithOptions();
                        new Handler().post(() -> {
                            DestFragment fragment =
                                    ((DestFragment) host.getChildFragmentManager().getFragments().get(1)
                                            .getChildFragmentManager().getFragments().get(0));
                            assertTrue(fragment.testArguments());
                        });
                    }
                });
                return home;
            });
        });
    }


    @Test
    public void pop() {
        scenario.onFragment(host -> {
            Fragivity.loadRoot(host, HomeFragment.class, () -> {
                HomeFragment home = new HomeFragment();
                home.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
                    if (event == Lifecycle.Event.ON_START) {
                        home.testPush();
                    }
                });
                return home;

            });

            host.getChildFragmentManager().addFragmentOnAttachListener((fragmentManager, fragment) -> {
                if (host.getChildFragmentManager().getFragments().size() == 2) {
                    fragment.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
                        if (event == Lifecycle.Event.ON_START) {
                            Fragivity.of(fragment.getChildFragmentManager().getFragments().get(0)).pop();
                            assertEquals(host.getNavController().getCurrentDestination().getId(), HomeFragment.class.hashCode());
                        }
                    });
                }
            });

        });
    }

    @Test
    public void showDialog() {
        scenario.onFragment(host -> {
            Fragivity.loadRoot(host, HomeFragment.class, () -> {
                HomeFragment home = new HomeFragment();
                home.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
                    if (event == Lifecycle.Event.ON_RESUME) {
                        home.testShowDialog();
                        assertNull(host.requireActivity().findViewById(R.id.dialog));
                    }
                });
                return home;
            });
        });
    }
}



