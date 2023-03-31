package com.example.for_testing.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.example.for_testing.databinding.SkillItemBinding
import com.example.for_testing.dao.SkillDAO
import com.example.for_testing.dao.SkillDAOSQLImpl
import com.example.for_testing.model.Skill
import com.google.android.material.snackbar.Snackbar

//var observer: SkillBarObserver
class SkillAdapter(var skills: ArrayList<Skill>, var activity: Activity, )
    : RecyclerView.Adapter<SkillAdapter.SkillViewHolder>() {

    // Declare a list to hold the progress of each SeekBar
    private val skillPointsList: MutableList<Int> = MutableList(itemCount+1){ 0 }
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

        val itemBinding = SkillItemBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent, false)
        return SkillViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SkillViewHolder,
                                  position: Int) {
        holder.bindSkill(skills[position])
    }

    inner class SkillViewHolder(private val itemBinding: SkillItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

        private var skill = Skill()

        init{
            itemView.setOnClickListener(this)
        }

        fun bindSkill(skill: Skill) {
            this.skill = skill

            itemBinding.skillName.text = skill.name
            itemBinding.skillBar.progress = skill.currentLevel
            itemBinding.skillBar.max = skill.maxLevel
            itemBinding.skillIcon.setImageBitmap(skill.icon)
            itemBinding.skillValue.text = skill.currentLevel.toString()

            itemBinding.btnOptionsRow.setOnClickListener {
                Snackbar.make(
                    itemBinding.root,
                    "Delete by button",
                    Snackbar.LENGTH_SHORT
                ).show()

                var dao: SkillDAO = SkillDAOSQLImpl(it.context)
                bindSkill(skill)
                dao.deleteSkill(skill.id)
                removeSkill(adapterPosition)
            }

            skillPointsList[adapterPosition] = itemBinding.skillBar.progress

            itemBinding.skillBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                    if (fromUser) {
                        saveSkillBarProgress(itemBinding, adapterPosition, progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    setSeekbarWhenOutOfScreen(adapterPosition, seekBar!!.progress)
                }



            })


        }
        override fun onClick(v: View?) {}

        private fun saveSkillBarProgress(itemBinding: SkillItemBinding, position: Int, progress: Int){
            itemBinding.skillValue.text = progress.toString()
            skillPointsList[position] = progress
//            observer.getTotalProgress(skillPointsList)
//            observer.checkSkillPoints(skillPointsList)
        }

        private fun setSeekbarWhenOutOfScreen(position: Int, progress: Int){
            if (adapterPosition != RecyclerView.NO_POSITION) {
                skillPointsList[adapterPosition] = progress
            }
        }
    }




}