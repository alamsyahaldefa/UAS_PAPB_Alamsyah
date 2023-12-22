package com.example.uas_papb.User

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uas_papb.data_class.Favorite
import com.example.uas_papb.databinding.ItemFavoriteBinding

class FavoriteAdapter (
    private val listMovie: List<Favorite>,  // Ubah ke MutableList agar dapat dihapus
    private val fragment: FavoriteFragment
    ) : RecyclerView.Adapter<FavoriteAdapter.ItemFavorite>() {

    inner class ItemFavorite(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Favorite) {
            with(binding) {
                movieTitle.text = movie.judul

                // Menggunakan Glide untuk memuat gambar dari alamat URL
                Glide.with(itemView.context)
                    .load(movie.gambar)
                    .into(imageFilm)

                btnDelete.setOnClickListener {
                    fragment.deleteFav(movie)
                    Toast.makeText(fragment.requireContext(), "Di Hapus dari Favorite", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFavorite {
        val binding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemFavorite(binding)
    }

    override fun onBindViewHolder(holder: ItemFavorite, position: Int) {
        val movie = listMovie[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }
}