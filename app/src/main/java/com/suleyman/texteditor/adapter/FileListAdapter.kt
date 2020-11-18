package com.suleyman.texteditor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.suleyman.texteditor.R
import com.suleyman.texteditor.model.FileItem

class FileListAdapter(val files: ArrayList<FileItem>, var listener: FileClickListener?): Adapter<FileListAdapter.FileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.file_item, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]

        holder.fileName.text = file.name
        holder.fileSize.text = file.size.toString()
        holder.itemView.setOnClickListener {
            listener?.onClick(file, position)
        }
    }

    override fun getItemCount(): Int = files.size

    override fun getItemId(position: Int): Long = position.toLong()

    class FileViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var fileName: TextView = view.findViewById(R.id.fileName)
        var fileSize: TextView = view.findViewById(R.id.fileSize)
    }

    interface FileClickListener {
        fun onClick(file : FileItem, pos: Int)
    }
}
