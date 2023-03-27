package com.example.kodegoskillsimulatorapp.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.example.kodegoskillsimulatorapp.SkillListActivity
import com.example.kodegoskillsimulatorapp.databinding.SkillItemBinding
import com.example.kodegoskillsimulatorapp.model.Skill
//var observer: SkillBarObserver
class SkillAdapter(var skills: ArrayList<Skill>, var activity: Activity, )
    : RecyclerView.Adapter<SkillAdapter.SkillViewHolder>() {

    // Declare a list to hold the progress of each SeekBar
    private val skillPointsList: MutableList<Int> = MutableList(itemCount) { 0 }

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

            if(skill.skillType1.equals("Quest", ignoreCase = true)){
                itemBinding.skillBar.progress = skill.level
                itemBinding.skillBar.isClickable = false
                Log.d("value of quest skill", skill.skillType1.toString())
            }

            itemBinding.skillName.text = skill.name
            itemBinding.skillBar.progress = skill.currentLevel
            itemBinding.skillBar.max = skill.level
            itemBinding.skillIcon.setImageBitmap(skill.icon)
            itemBinding.skillValue.text = skill.currentLevel.toString()

            skillPointsList[adapterPosition] = itemBinding.skillBar.progress

            Log.i("skill 1 progress value", skillPointsList[0].toString())
            Log.i("skill 2 progress value", skillPointsList[1].toString())
            Log.i("sum of all progress right now", skillPointsList.sum().toString())

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
            Log.i("skill #${position} progress from onprogress", progress.toString())
            skillPointsList[position] = progress
//            observer.getTotalProgress(skillPointsList)
//            observer.checkSkillPoints(skillPointsList)
        }

        private fun setSeekbarWhenOutOfScreen(position: Int, progress: Int){
            if (adapterPosition != RecyclerView.NO_POSITION) {
                skillPointsList[adapterPosition] = progress
                Log.i("hidden view", skillPointsList[adapterPosition].toString())
            }
        }
    }




}