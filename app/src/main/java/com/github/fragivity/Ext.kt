package com.github.fragivity

import com.my.example.R

fun NavOptions.applySlideInOut() {
    enterAnim = R.anim.slide_in
    exitAnim = R.anim.slide_out
    popEnterAnim = R.anim.slide_in_pop
    popExitAnim = R.anim.slide_out_pop
}


fun NavOptions.applyFadeInOut() {
    enterAnim = R.anim.nav_default_enter_anim
    exitAnim = R.anim.nav_default_exit_anim
    popEnterAnim = R.anim.nav_default_enter_anim
    popExitAnim = R.anim.nav_default_exit_anim
}