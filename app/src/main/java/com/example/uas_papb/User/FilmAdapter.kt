package com.example.uas_papb.User

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uas_papb.data_class.Movie
import com.example.uas_papb.databinding.ItemFilmBinding

class FilmAdapter(
    private val listMovie: List<Movie>,  // Ubah ke MutableList agar dapat dihapus
    private val fragment: home_Fragment
) : RecyclerView.Adapter<FilmAdapter.ItemFilm>() {

    inner class ItemFilm(private val binding: ItemFilmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding) {
                movieTitle.text = movie.judul

                // Menggunakan Glide untuk memuat gambar dari alamat URL
                Glide.with(itemView.context)
                    .load(movie.gambar)
                    .into(imageFilm)

                imageFilm.setOnClickListener {
                    val action = home_FragmentDirections.actionHomeFragmentToDetailsActivity(movie.id)
                    fragment.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFilm {
        val binding =
            ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemFilm(binding)
    }

    override fun onBindViewHolder(holder: ItemFilm, position: Int) {
        val movie = listMovie[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }
}
