package com.example.kodegoskillsimulatorapp.adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kodegoskillsimulatorapp.SkillListActivity
import com.example.kodegoskillsimulatorapp.databinding.JobClassItemBinding
import com.example.kodegoskillsimulatorapp.model.JobClass

class JobClassAdapter (var jobClasses: ArrayList<JobClass>, var activity: Activity)
    : RecyclerView.Adapter<JobClassAdapter.JobClassViewHolder>() {

    fun addJobClass(jobClass: JobClass){
        jobClasses.add(0,jobClass)
        notifyItemInserted(0)
    }

    fun removeJobClass(position: Int){
        jobClasses.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateJobClass(newjobClass: ArrayList<JobClass>){
        jobClasses.clear()
        jobClasses.addAll(newjobClass)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return jobClasses.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JobClassViewHolder {

        val itemBinding = JobClassItemBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent, false)
        return JobClassViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: JobClassViewHolder,
                                  position: Int) {
        holder.bindJobClass(jobClasses[position])

        holder.itemView.setOnClickListener {
            val intent = Intent(activity.applicationContext, SkillListActivity::class.java)

            val bundle = Bundle()
            bundle.putInt("item_position", position)
            bundle.putString("jobclass_name", jobClasses[position].name)
            intent.putExtras(bundle)

            activity.startActivity(intent)
        }
    }

    inner class JobClassViewHolder(private val itemBinding: JobClassItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

        var jobClass = JobClass()

        init{
            itemView.setOnClickListener(this)
        }

        fun bindJobClass(jobClass: JobClass) {
            this.jobClass = jobClass

            itemBinding.jobclassName.text = "${jobClass.name}"
//            itemBinding.jobclassPicture.setImageBitmap(jobClass.img)
        }

        override fun onClick(v: View?) {
            // onClick code goes here...
        }
    }
}