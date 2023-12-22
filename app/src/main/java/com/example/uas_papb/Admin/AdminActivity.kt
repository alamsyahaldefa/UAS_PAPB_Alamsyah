package com.example.uas_papb.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas_papb.data_class.Movie
import com.example.uas_papb.databinding.ActivityAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    private val movies= firestore.collection("movies")
    private val binding by lazy { ActivityAdminBinding.inflate(layoutInflater) }

    val movieLiveData : MutableLiveData<List<Movie>> by lazy {
        MutableLiveData<List<Movie>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeMovies()
        getAllMovies()

        with(binding) {
            buttonAdd.setOnClickListener{
                val intent= Intent(this@AdminActivity, EditMoviesActivity::class.java)
                intent.putExtra("MOVIE_ID", "")
                startActivity(intent)
            }
        }
    }

    private fun observeMovies(){
        movieLiveData.observe(this){
            listMovies->
            val listAdapter = MovieAdapter(listMovies, this)
            binding.recyclerViewMovies.apply {
                adapter = listAdapter
                layoutManager = LinearLayoutManager(this@AdminActivity)
            }
        }
    }

    private fun getAllMovies(){
        observeMoviesChanges()
    }

    private fun observeMoviesChanges() {
        movies.addSnapshotListener{ values, error ->
            if (error != null) {
                Log.d("admin activity", "Error Listening $error")
            }
            val movie = values?.toObjects(Movie::class.java)
            if (movie != null) {
                movieLiveData.postValue(movie)
            }
        }
    }


    fun deleteMovie(movieId: String) {
        movies.document(movieId).delete()
            .addOnSuccessListener {
                Toast.makeText(
                    this@AdminActivity,
                    "Film Berhasil Dihapus.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Log.e("AdminActivity", "Error deleting movie: $it")
                Toast.makeText(
                    this@AdminActivity,
                    "Gagal Mengahapus Film.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


}