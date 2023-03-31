package com.example.kodegoskillsimulatorapp.observer

import com.example.kodegoskillsimulatorapp.model.Skill

interface SkillDataObserver {
    fun saveSkillData(skillData: ArrayList<Skill>)
    fun showSkillData(skillData: ArrayList<Skill>)
}