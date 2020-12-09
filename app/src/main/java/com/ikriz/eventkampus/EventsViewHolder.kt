package com.ikriz.eventkampus

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.items.view.*

class EventsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bindItem(events: Events) {
        view.apply {
            judul_event.text = events.judul
            deskripsi_event.text =
                if (events.deskripsi?.length!! > 200) events.deskripsi.substring(0, 200 - 1)+("…") else events.deskripsi
            Glide.with(context).load(events.logo).override(200, 200).centerCrop().into(logo_event)
        }
    }
}