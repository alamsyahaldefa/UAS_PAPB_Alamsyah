package com.example.uas_papb.User

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas_papb.PrefManager
import com.example.uas_papb.data_class.Favorite
import com.example.uas_papb.databinding.FragmentFavoriteBinding
import com.example.uas_papb.room.FavoriteDao
import com.example.uas_papb.room.FilmRoom
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteFragment : Fragment() {
    private val  binding by lazy {
        FragmentFavoriteBinding.inflate(layoutInflater)
    }
    private lateinit var executorService: ExecutorService
    private lateinit var mFavoriteDao: FavoriteDao
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        executorService = Executors.newSingleThreadExecutor()
        val db = FilmRoom.getDatabase(requireContext())
        mFavoriteDao = db!!.FavoriteDao()

        val prefManager = PrefManager.getInstance(requireContext())
        username = prefManager.getUsername()

        getAllFavorite()

        return binding.root
    }

    fun getAllFavorite () {
        mFavoriteDao.getUserFavorite(username).observe(viewLifecycleOwner){
            listFav->
            val listAdapter = FavoriteAdapter(listFav,this)
            binding.recyclerFavorite.apply {
                adapter=listAdapter
                layoutManager=LinearLayoutManager(requireContext())
            }
        }
    }

    fun deleteFav (favorite: Favorite){
        executorService.execute{
            mFavoriteDao.Delete(favorite)
        }
    }

}