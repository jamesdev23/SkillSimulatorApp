package com.example.kodegoskillsimulatorapp.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.kodegoskillsimulatorapp.R
import com.example.kodegoskillsimulatorapp.SkillListActivity
import com.example.kodegoskillsimulatorapp.dao.JobClassDAO
import com.example.kodegoskillsimulatorapp.dao.JobClassDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.DialogEditJobClassBinding
import com.example.kodegoskillsimulatorapp.databinding.ItemJobClassGridBinding
import com.example.kodegoskillsimulatorapp.model.JobClass
import com.google.android.material.snackbar.Snackbar

class JobClassAdapter (var jobClasses: ArrayList<JobClass>, var context: Context)
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

        val itemBinding = ItemJobClassGridBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent, false)
        return JobClassViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: JobClassViewHolder,
                                  position: Int) {
        holder.bindJobClass(jobClasses[position])
    }

    inner class JobClassViewHolder(private val itemBinding: ItemJobClassGridBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

        var jobClass = JobClass()

        init {
            itemView.setOnClickListener(this)
        }

        fun bindJobClass(jobClass: JobClass) {
            this.jobClass = jobClass

            itemBinding.jobclassName.text = "${jobClass.name}"
            itemBinding.jobclassPicture.setImageBitmap(jobClass.icon)

            itemBinding.btnOptionsRow.setOnClickListener {
                popupMenu()
            }
        }

        override fun onClick(view: View?) {
            val intent = Intent(view?.context, SkillListActivity::class.java)

            val bundle = Bundle()
            bundle.putString("DATA_GAME_NAME", jobClasses[adapterPosition].gameName)
            bundle.putString("DATA_JOB_CLASS_NAME", jobClasses[adapterPosition].name)
            intent.putExtras(bundle)

            view?.context?.startActivity(intent)
        }

        private fun popupMenu() {
            val popupMenu = PopupMenu(context, itemBinding.btnOptionsRow)

            popupMenu.menuInflater.inflate(R.menu.item_popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.option_edit -> {
                        Snackbar.make(
                            itemBinding.root,
                            "Job Class name: ${jobClass.name}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        dialogEditJobClassInfo(context)
                        true
                    }
                    R.id.option_delete -> {
                        Snackbar.make(
                            itemBinding.root,
                            "Delete ${jobClass.name}",
                            Snackbar.LENGTH_SHORT
                        ).show()

                        var dao: JobClassDAO = JobClassDAOSQLImpl(itemView.context)
                        bindJobClass(jobClass)
                        dao.deleteJobClass(jobClass.id)
                        removeJobClass(adapterPosition)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        private fun dialogEditJobClassInfo(context: Context){
            context.let {
                val builder = android.app.AlertDialog.Builder(it)
                val dialogEditJobClassBinding: DialogEditJobClassBinding =
                    DialogEditJobClassBinding.inflate(LayoutInflater.from(it))

                with(dialogEditJobClassBinding) {
                    editJobClassName.setText(jobClass.name)
                    editJobClassType.setText(jobClass.jobClassType)
                    editJobClassMaxSkillPoints.setText(jobClass.maxSkillPoints.toString())
                    editJobClassDescription.setText(jobClass.description)
                }

                with(builder) {
                    setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->
                        val dao: JobClassDAO = JobClassDAOSQLImpl(it)
                        val editJobClassName = dialogEditJobClassBinding.editJobClassName.text.toString().trim()
                        val editJobClassType = dialogEditJobClassBinding.editJobClassType.text.toString().trim()
                        val editJobClassMaxSkillPoints = dialogEditJobClassBinding.editJobClassMaxSkillPoints.text.toString()
                        val editJobClassDescription = dialogEditJobClassBinding.editJobClassDescription.text.toString().trim()

                        val jobClassSearch = dao.getJobClassByName(editJobClassName)

                        when {
                            jobClassSearch.name.isNotEmpty() ->
                                toast("Error: Duplicate name.", it)
                            editJobClassName.isEmpty() ->
                                toast("Error: Class name is empty.", it)
                            editJobClassMaxSkillPoints.toInt() < 1 ->
                                toast("Error: Max Skill Points must be above 0.", it)
                            editJobClassName.length > 200 ->
                                toast("Error: Name exceeds 200 characters", it)
                            editJobClassDescription.length > 500 ->
                                toast("Error: Description exceeds 500 characters", it)
                            else -> {
                                jobClass.name = editJobClassName
                                jobClass.jobClassType = editJobClassType
                                jobClass.maxSkillPoints = editJobClassMaxSkillPoints.toInt()
                                jobClass.description = editJobClassDescription

                                dao.updateJobClass(jobClass.id, jobClass)
                                val newJobClassList = dao.getJobClassPerGame(jobClass.gameName)
                                updateJobClass(newJobClassList)
                                notifyItemChanged(adapterPosition)
                                toast("Updated ${jobClass.name}", it)
                            }
                        }

                    })
                    setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                        // Do something
                    })
                        .setView(dialogEditJobClassBinding.root)
                        .create()
                        .show()
                }
            }
        }

        private fun toast(message: String, context: Context){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }
}