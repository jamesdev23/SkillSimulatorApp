package com.example.for_testing.viewpager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.for_testing.databinding.FragmentABinding
import com.example.for_testing.adapter.GameAdapter
import com.example.for_testing.dao.GameDAO
import com.example.for_testing.dao.GameDAOSQLImpl
import com.example.for_testing.model.Game

class AFragment : Fragment() {

    private var _binding: FragmentABinding? = null
    private val binding get() = _binding!!

    private lateinit var dao: GameDAO
    private lateinit var games: ArrayList<Game>
    private lateinit var gameAdapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentABinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dao = GameDAOSQLImpl(requireContext())
        games = dao.getGames()
        gameAdapter = GameAdapter(games, requireActivity())
        Log.d("game list", games.toString())
        binding.gameList.layoutManager = LinearLayoutManager(requireContext())
        binding.gameList.adapter = gameAdapter

    }

    override fun onDestroy() {
        super.onDestroy()

    }
}