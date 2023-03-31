package com.example.for_testing.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.for_testing.databinding.JobClassItemBinding
import com.example.for_testing.dao.JobClassDAO
import com.example.for_testing.dao.JobClassDAOSQLImpl
import com.example.for_testing.model.JobClass
import com.google.android.material.snackbar.Snackbar

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

//        holder.itemView.setOnClickListener {
//            val intent = Intent(activity.applicationContext, SkillListActivity::class.java)
//
//            val bundle = Bundle()
//            bundle.putInt("data_game1", jobClasses[position].gameId)
//            bundle.putInt("data_jobclass1", position)
//            bundle.putString("data_jobclass2", jobClasses[position].name)
//            intent.putExtras(bundle)
//            activity.startActivity(intent)
//        }
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
            itemBinding.jobclassPicture.setImageBitmap(jobClass.img)

            itemBinding.btnOptionsRow.setOnClickListener {
                Snackbar.make(
                    itemBinding.root,
                    "Delete by button",
                    Snackbar.LENGTH_SHORT
                ).show()

                var dao: JobClassDAO = JobClassDAOSQLImpl(it.context)
                bindJobClass(jobClass)
                dao.deleteJobClass(jobClass.id)
                removeJobClass(adapterPosition)
            }
        }

        override fun onClick(v: View?) {
            // onClick code goes here...
        }
    }
}