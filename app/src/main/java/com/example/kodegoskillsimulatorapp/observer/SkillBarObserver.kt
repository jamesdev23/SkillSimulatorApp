package com.example.kodegoskillsimulatorapp.observer

import com.example.kodegoskillsimulatorapp.model.Skill

interface SkillBarObserver {
    fun getTotalProgress(list: MutableList<Int>)
    fun checkSkillPoints(list: MutableList<Int>)
}

interface SkillDataObserver {
    fun saveSkillData(skillData: ArrayList<Skill>)
    fun showSkillData(skillData: ArrayList<Skill>)
}