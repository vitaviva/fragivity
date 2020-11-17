package com.github.fragivity

import androidx.navigation.NavOptions
import com.my.example.R

fun NavOptions.Builder.applySlideInOut() {
    setEnterAnim(R.anim.slide_in)
    setExitAnim(R.anim.slide_out)
    setPopEnterAnim(R.anim.slide_in_pop)
    setPopExitAnim(R.anim.slide_out_pop)
}


fun NavOptions.Builder.applyFadeInOut() {
    setEnterAnim(R.anim.nav_default_enter_anim)
    setExitAnim(R.anim.nav_default_exit_anim)
    setPopEnterAnim(R.anim.nav_default_enter_anim)
    setPopExitAnim(R.anim.nav_default_exit_anim)
}