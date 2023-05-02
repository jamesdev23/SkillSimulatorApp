package com.example.kodegoskillsimulatorapp.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.kodegoskillsimulatorapp.R
import com.example.kodegoskillsimulatorapp.SkillListActivity
import com.example.kodegoskillsimulatorapp.dao.JobClassDAO
import com.example.kodegoskillsimulatorapp.dao.JobClassDAOSQLImpl
import com.example.kodegoskillsimulatorapp.dao.SkillDAO
import com.example.kodegoskillsimulatorapp.dao.SkillDAOSQLImpl
import com.example.kodegoskillsimulatorapp.databinding.DialogEditSkillBinding
import com.example.kodegoskillsimulatorapp.databinding.DialogSkillDetailsBinding
import com.example.kodegoskillsimulatorapp.databinding.ItemSkillBinding
import com.example.kodegoskillsimulatorapp.model.Skill
import com.example.kodegoskillsimulatorapp.observer.SkillBarObserver
import com.example.kodegoskillsimulatorapp.observer.SkillDataObserver
import com.google.android.material.snackbar.Snackbar

class SkillAdapter(var skills: ArrayList<Skill>, var context: Context, var skillBarObserver: SkillBarObserver)
    : RecyclerView.Adapter<SkillAdapter.SkillViewHolder>() {

    // Declare a list to hold the progress of each SeekBar
    private val skillPointsList: MutableList<Int> = MutableList(100){ 0 }
    private var skillData: ArrayList<Skill> = ArrayList(100)
    fun addSkill(skill: Skill){
        skills.add(0,skill)
        notifyItemInserted(0)
    }

    fun removeSkill(position: Int){
        skills.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateSkill(newSkill: ArrayList<Skill>){
        skills.clear()
        skills.addAll(newSkill)
        notifyDataSetChanged()
    }



    override fun getItemCount(): Int {
        return skills.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SkillViewHolder {

        val itemBinding = ItemSkillBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent, false)
        return SkillViewHolder(itemBinding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(
        holder: SkillViewHolder,
        position: Int
    ) {
        holder.bindSkill(skills[position])
    }

    inner class SkillViewHolder(private val itemBinding: ItemSkillBinding) :
        RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

        var skill = Skill()

        init{
            itemView.setOnClickListener(this)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindSkill(skill: Skill) {
            this.skill = skill

            itemBinding.skillName.text = skill.name
            itemBinding.skillBar.progress = skill.currentLevel
            itemBinding.skillBar.min = skill.minLevel
            itemBinding.skillBar.max = skill.maxLevel
            itemBinding.skillIcon.setImageBitmap(skill.icon)
            itemBinding.skillValue.text = skill.currentLevel.toString()

            itemBinding.btnOptionsRow.setOnClickListener {
                popupMenu()
            }

            setSeekBarWidth()

            itemBinding.skillBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                    if (fromUser) {
                        saveSkillBarProgress(itemBinding, adapterPosition, progress)
                        skill.currentLevel = progress
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    setSeekbarWhenOutOfScreen(adapterPosition, seekBar!!.progress)
                }

            })

            // misc changes when skill is a quest skill
            if(skill.skillType == "Quest") {
                itemBinding.skillBar.progress = 1
                itemBinding.skillValue.text = "1"
                itemBinding.skillBar.isEnabled = false
                skill.currentLevel = itemBinding.skillBar.progress
            }

            setSkillPointsToObserver()

        }

        override fun onClick(v: View?) {
            dialogSkillDetails(v!!.context)
        }

        private fun setSeekBarWidth(){
            var maxValue = skill.maxLevel
            val params = itemBinding.skillBar.layoutParams as RelativeLayout.LayoutParams

            if(maxValue == 1 || maxValue == 5){
                params.width = (maxValue * context.resources.getDimension(R.dimen.seekbar_width_multiplier_below_ten)).toInt()
            }else {
                params.width = (maxValue * context.resources.getDimension(R.dimen.seekbar_width_multiplier)).toInt()
            }

            itemBinding.skillBar.layoutParams = params
        }

        private fun setSkillPointsToObserver(){
            skillPointsList[adapterPosition] = itemBinding.skillBar.progress
            skillData.add(skill)
        }


        private fun saveSkillBarProgress(itemBinding: ItemSkillBinding, position: Int, progress: Int){
            itemBinding.skillValue.text = progress.toString()
            skillPointsList[position] = progress
            skillBarObserver.getTotalProgress(skillPointsList)
            skillBarObserver.checkSkillPoints(skillPointsList)
        }

        private fun setSeekbarWhenOutOfScreen(position: Int, progress: Int){
            if (adapterPosition != RecyclerView.NO_POSITION) {
                skillPointsList[adapterPosition] = progress
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun popupMenu() {
            val popupMenu = PopupMenu(context, itemBinding.btnOptionsRow)

            popupMenu.menuInflater.inflate(R.menu.item_popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.option_edit -> {
                        Snackbar.make(
                            itemBinding.root,
                            "Skill Name: ${skill.name}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        dialogEditSkillInfo(context)
                        true
                    }
                    R.id.option_delete -> {
                        Snackbar.make(
                            itemBinding.root,
                            "Deleted ${skill.name}",
                            Snackbar.LENGTH_SHORT
                        ).show()

                        var dao: SkillDAO = SkillDAOSQLImpl(itemView.context)
                        bindSkill(skill)
                        dao.deleteSkill(skill.id)
                        removeSkill(adapterPosition)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        private fun dialogSkillDetails(context: Context){
            context.let {
                val builder = AlertDialog.Builder(it)
                val dialogSkillDetailsBinding: DialogSkillDetailsBinding =
                    DialogSkillDetailsBinding.inflate(LayoutInflater.from(it))

                with(dialogSkillDetailsBinding) {
                    skillName.text = skill.name
                    skillMaxLevel.text = skill.maxLevel.toString()
                    skillType.text = skill.skillType
                    skillDescription.text = skill.description
                }

                with(builder) {
                    setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                        // does nothing
                    })
                        .setView(dialogSkillDetailsBinding.root)
                        .create()
                        .show()
                }
            }
        }

        private fun dialogEditSkillInfo(context: Context){
            context.let {
                val builder = AlertDialog.Builder(it)
                val dialogEditSkillBinding: DialogEditSkillBinding =
                    DialogEditSkillBinding.inflate(LayoutInflater.from(it))

                with(dialogEditSkillBinding) {
                    editSkillName.setText(skill.name)
                    editSkillMaxLevel.setText(skill.maxLevel.toString())
                    editSkillType.setText(skill.skillType)
                    editSkillDescription.setText(skill.description)
                }

                with(builder) {
                    setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->
                        val editSkillName =
                            dialogEditSkillBinding.editSkillName.text.toString()
                        val editSkillMaxLevel =
                            dialogEditSkillBinding.editSkillMaxLevel.text.toString()
                        val editSkillType =
                            dialogEditSkillBinding.editSkillType.text.toString()
                        val editSkillDescription =
                            dialogEditSkillBinding.editSkillDescription.text.toString()

                        skill.name = editSkillName
                        skill.maxLevel = editSkillMaxLevel.toInt()
                        skill.skillType = editSkillType
                        skill.description = editSkillDescription

                        val dao: SkillDAO = SkillDAOSQLImpl(it)
                        dao.updateSkill(skill.id, skill)
                        updateSkill(dao.getSkillPerJob(skill.gameName, skill.jobClassName))
                        notifyItemChanged(adapterPosition)
                        Snackbar.make(itemBinding.root, "Updated ${skill.name}", Snackbar.LENGTH_SHORT).show()
                    })
                    setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                        // does nothing
                    })
                        .setView(dialogEditSkillBinding.root)
                        .create()
                        .show()
                }
            }
        }


   

    }




}