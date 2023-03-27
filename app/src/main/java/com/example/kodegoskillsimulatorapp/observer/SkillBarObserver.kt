package com.example.kodegoskillsimulatorapp.observer

interface SkillBarObserver {
    fun getTotalProgress(list: MutableList<Int>)
    fun checkSkillPoints(list: MutableList<Int>)
}