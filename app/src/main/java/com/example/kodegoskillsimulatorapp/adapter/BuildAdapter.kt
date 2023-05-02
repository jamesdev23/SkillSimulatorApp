package com.example.kodegoskillsimulatorapp.adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kodegoskillsimulatorapp.SkillListActivity
import com.example.kodegoskillsimulatorapp.dao.BuildDAO
import com.example.kodegoskillsimulatorapp.dao.BuildDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.ItemBuildBinding
import com.example.kodegoskillsimulatorapp.model.Build
import com.google.android.material.snackbar.Snackbar

class BuildAdapter (var builds: ArrayList<Build>, var activity: Activity)
    : RecyclerView.Adapter<BuildAdapter.BuildViewHolder>() {

    fun addBuild(build: Build){
        builds.add(0,build)
        notifyItemInserted(0)
    }

    fun removeBuild(position: Int){
        builds.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateBuild(newBuild: ArrayList<Build>){
        builds.clear()
        builds.addAll(newBuild)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return builds.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BuildViewHolder {

        val itemBinding = ItemBuildBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent, false)
        return BuildViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BuildViewHolder,
                                  position: Int) {
        holder.bindBuild(builds[position])

        holder.itemView.setOnClickListener {
            val intent = Intent(activity.applicationContext, SkillListActivity::class.java)

            val bundle = Bundle()
            bundle.putString("data2", builds[position].gameName)
            bundle.putString("data3", builds[position].name)
            bundle.putString("data4", builds[position].dataText)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }

    inner class BuildViewHolder(private val itemBinding: ItemBuildBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

        var build = Build()

        init{
            itemView.setOnClickListener(this)
        }

        fun bindBuild(build: Build) {
            this.build = build
            val buildId = "ID: ${build.id}"

            itemBinding.buildName.text = build.name
            itemBinding.buildId.text = buildId
            itemBinding.gameName.text = build.gameName
            itemBinding.jobclassName.text = build.jobClassName
            itemBinding.buildImage.setImageBitmap(build.img)

            itemBinding.btnOptionsRow.setOnClickListener {
                Snackbar.make(
                    itemBinding.root,
                    "Deleted ${build.name}",
                    Snackbar.LENGTH_SHORT
                ).show()

                val dao: BuildDAO = BuildDAOSQLImpl(it.context)
                bindBuild(build)
                dao.deleteBuild(build.id)
                removeBuild(adapterPosition)
            }
        }

        override fun onClick(v: View?) {
            // onClick code goes here...
        }
    }
}