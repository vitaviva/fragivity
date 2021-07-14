/*
 * Copyright 2019, The Android Open Source Project
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

package com.github.fragivity.example.multiback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.github.fragivity.applyArguments
import com.github.fragivity.example.R
import com.github.fragivity.navigator
import com.github.fragivity.push

/**
 * Shows a static leaderboard with multiple users.
 */
class Leaderboard : Fragment(R.layout.multi_back_fragment_leaderboard) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.leaderboard_list).run {
            setHasFixedSize(true)
            adapter = MyAdapter(Array(10) { "Person ${it + 1}" })
        }
    }
}

class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    val name: TextView = item.findViewById(R.id.user_name_text)
    val logo: ImageView = item.findViewById(R.id.user_avatar_image)
}

class MyAdapter(private val myDataset: Array<String>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.multi_back_list_view_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            name.text = myDataset[position]
            logo.setImageResource(listOfAvatars[position % listOfAvatars.size])
            itemView.setOnClickListener {
                itemView.navigator.push({
                    applyArguments(USERNAME_KEY to myDataset[position])
                }) { UserProfile() }
            }
        }
    }

    override fun getItemCount() = myDataset.size

    companion object {
        const val USERNAME_KEY = "userName"
    }
}

private val listOfAvatars = listOf(
    R.drawable.avatar_1_raster,
    R.drawable.avatar_2_raster,
    R.drawable.avatar_3_raster,
    R.drawable.avatar_4_raster,
    R.drawable.avatar_5_raster,
    R.drawable.avatar_6_raster
)

class UserProfile : Fragment(R.layout.multi_back_fragment_user_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString(MyAdapter.USERNAME_KEY) ?: "Ali Connors"
        view.findViewById<TextView>(R.id.profile_user_name).text = name
    }
}
