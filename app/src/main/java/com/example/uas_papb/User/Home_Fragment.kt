package com.example.uas_papb.User

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.uas_papb.data_class.Movie
import com.example.uas_papb.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class home_Fragment : Fragment() {
    private val binding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private val firestore = FirebaseFirestore.getInstance()
    private val movies= firestore.collection("movies")
    val movieLiveData : MutableLiveData<List<Movie>> by lazy {
        MutableLiveData<List<Movie>>()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        observeMovies()
        getAllMovies()

        return binding.root
    }

    private fun observeMovies(){
        movieLiveData.observe(viewLifecycleOwner){
                listMovies->
            val listAdapter = FilmAdapter(listMovies, this)
            binding.recyclerViewFilm.apply {
                adapter = listAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }
    }

    private fun getAllMovies(){
        observeMoviesChanges()
    }

    private fun observeMoviesChanges() {
        movies.addSnapshotListener{ values, error ->
            if (error != null) {
                Log.d("public activity", "Error Listening $error")
            }
            val movie = values?.toObjects(Movie::class.java)
            if (movie != null) {
                movieLiveData.postValue(movie)
            }
        }
    }
}