package com.example.for_testing.viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.example.for_testing.FragmentAdapter
import com.example.for_testing.R
import com.example.for_testing.databinding.ActivityViewPagerBinding
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Skill Simulator"
        supportActionBar?.displayOptions

        var fragmentAdapter = FragmentAdapter(supportFragmentManager, lifecycle)

        fragmentAdapter.addFragment(AFragment())
        fragmentAdapter.addFragment(BFragment())
        fragmentAdapter.addFragment(CFragment())

        with(binding.viewPager2){
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
//            setPageTransformer(ZoomOutPageTransformer())
            adapter = fragmentAdapter
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) {
            tab, position ->
            tab.text = "Chapter ${position + 1}"
        }.attach()

//        binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//        binding.viewPager2.adapter = fragmentAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_game -> {
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if(binding.viewPager2.currentItem == 0) {
            super.onBackPressed()
        }else{
            binding.viewPager2.currentItem = binding.viewPager2.currentItem - 1
        }
    }



}