package com.example.uas_papb.Admin

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uas_papb.data_class.Movie
import com.example.uas_papb.databinding.ItemMovieBinding

class MovieAdapter(
    private val listMovie: List<Movie>,  // Ubah ke MutableList agar dapat dihapus
    private val activity: AdminActivity
) : RecyclerView.Adapter<MovieAdapter.ItemMovie>() {

    inner class ItemMovie(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding) {
                textViewMovieTitle.text = movie.judul
                textViewMovieDescription.text = movie.deskripsi

                // Menggunakan Glide untuk memuat gambar dari alamat URL
                Glide.with(itemView.context)
                    .load(movie.gambar)
                    .into(imageViewMovie)

                imageViewEdit.setOnClickListener {
                    val intent = Intent(activity, EditMoviesActivity::class.java)
                    intent.putExtra("MOVIE_ID", movie.id)
                    activity.startActivity(intent)
                }

                imageViewDelete.setOnClickListener {
                    // Memanggil fungsi deleteMovie di AdminActivity
                    activity.deleteMovie(movie.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMovie {
        val binding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemMovie(binding)
    }

    override fun onBindViewHolder(holder: ItemMovie, position: Int) {
        val movie = listMovie[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }
}
